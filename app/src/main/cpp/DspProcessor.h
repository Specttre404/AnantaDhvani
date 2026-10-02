#pragma once

#include <algorithm>
#include <array>
#include <atomic>
#include <cmath>
#include <cstddef>
#include <cstdint>

namespace lastwave::audio {

class DspProcessor final {
public:
    static constexpr std::size_t kEqualizerBandCount = 31;

    DspProcessor() noexcept;

    void configure(double sampleRate) noexcept;
    void reset() noexcept;
    void setStudioMasterClarity(bool enabled) noexcept;
    void setBitPerfect(bool enabled) noexcept;
    void setPeakProtectionEnabled(bool enabled) noexcept;
    void setEqualizer(
        bool enabled,
        float preampDb,
        const float* gainsDb,
        std::size_t gainCount) noexcept;
    void setCrossfeed(bool enabled, float levelDb, float cutoffHz) noexcept;
    void setEqualizerQ(float q) noexcept;
    void setSpatialAudio(bool enabled, float roomSize, float damping, float haasDelayMs, float widthRatio) noexcept;
    void setBitcrusher(bool enabled, int bits, int downsampleFactor) noexcept;
    void process(
        float* interleaved,
        std::int32_t frameCount,
        std::int32_t channelCount = 2) noexcept;

    [[nodiscard]] bool isStudioMasterClarityEnabled() const noexcept {
        return targetEnabled_.load(std::memory_order_acquire);
    }

    [[nodiscard]] bool isBitPerfectEnabled() const noexcept {
        return bitPerfectEnabled_.load(std::memory_order_acquire);
    }

private:
    struct Biquad final {
        double b0{1.0};
        double b1{0.0};
        double b2{0.0};
        double a1{0.0};
        double a2{0.0};
        std::array<double, 2> z1{};
        std::array<double, 2> z2{};

        static Biquad highPass(double sampleRate, double frequency, double q) noexcept;
        static Biquad peaking(
            double sampleRate,
            double frequency,
            double q,
            double gainDb) noexcept;
        static Biquad highShelf(
            double sampleRate,
            double frequency,
            double slope,
            double gainDb) noexcept;
        void setPeaking(
            double sampleRate,
            double frequency,
            double q,
            double gainDb) noexcept;

        [[nodiscard]] inline float tick(float input, std::size_t channel) noexcept {
            const double value = static_cast<double>(input);
            const double output = b0 * value + z1[channel];
            z1[channel] = b1 * value - a1 * output + z2[channel];
            z2[channel] = b2 * value - a2 * output;
            // Anti-denormal guard: protects against CPU penalty on budget ARM cores
            if (std::abs(z1[channel]) < 1.0e-20) z1[channel] = 0.0;
            if (std::abs(z2[channel]) < 1.0e-20) z2[channel] = 0.0;
            if (!std::isfinite(output) || !std::isfinite(z1[channel]) || !std::isfinite(z2[channel])) {
                z1[channel] = 0.0;
                z2[channel] = 0.0;
                return 0.0F;
            }
            return static_cast<float>(output);
        }

        [[nodiscard]] double magnitude(double sampleRate, double frequency) const noexcept;

        inline void clear() noexcept {
            z1.fill(0.0);
            z2.fill(0.0);
        }
    };

    struct Crossfeed final {
        double a0Low{0.0};
        double b1Low{0.0};
        double a0High{1.0};
        double a1High{0.0};
        double b1High{0.0};
        double gain{1.0};
        std::array<double, 2> low{};
        std::array<double, 2> high{};
        std::array<double, 2> previousInput{};

        void configure(double sampleRate, double cutoffHz, double levelDb) noexcept;

        inline void process(float& left, float& right) noexcept {
            const double inputLeft = left;
            const double inputRight = right;
            low[0] = a0Low * inputLeft + b1Low * low[0];
            low[1] = a0Low * inputRight + b1Low * low[1];
            high[0] = a0High * inputLeft + a1High * previousInput[0] + b1High * high[0];
            high[1] = a0High * inputRight + a1High * previousInput[1] + b1High * high[1];
            previousInput[0] = inputLeft;
            previousInput[1] = inputRight;
            left = static_cast<float>((high[0] + low[1]) * gain);
            right = static_cast<float>((high[1] + low[0]) * gain);
            if (!std::isfinite(left)) left = 0.0F;
            if (!std::isfinite(right)) right = 0.0F;
        }

        inline void clear() noexcept {
            low.fill(0.0);
            high.fill(0.0);
            previousInput.fill(0.0);
        }
    };

    double sampleRate_{48000.0};
    float currentWet_{0.0F};
    float rampPerFrame_{1.0F / 2400.0F};
    std::atomic<bool> targetEnabled_{false};
    std::atomic<bool> bitPerfectEnabled_{false};
    std::atomic<bool> peakProtectionEnabled_{false};
    std::atomic<bool> targetEqualizerEnabled_{false};
    std::atomic<bool> targetCrossfeedEnabled_{false};
    std::atomic<float> targetCrossfeedLevelDb_{4.5F};
    std::atomic<float> targetCrossfeedCutoffHz_{700.0F};
    std::atomic<float> targetEqualizerQ_{1.414F};
    std::atomic<float> targetPreampDb_{0.0F};
    std::atomic<bool> targetSpatialEnabled_{false};
    std::atomic<float> targetSpatialRoomSize_{0.5F};
    std::atomic<float> targetSpatialDamping_{0.5F};
    std::atomic<float> targetSpatialHaasDelayMs_{15.0F};
    std::atomic<float> targetSpatialWidthRatio_{1.2F};
    std::atomic<bool> targetBitcrusherEnabled_{false};
    std::atomic<int> targetBitcrusherBits_{10};
    std::atomic<int> targetBitcrusherDownsample_{2};

    struct SpatialReverb final {
        std::array<float, 4096> delayBufferLeft{};
        std::array<float, 4096> delayBufferRight{};
        std::size_t writeIdx{0};

        inline void process(float& left, float& right, bool enabled, float roomSize, float damping, float haasDelayMs, float widthRatio, double sampleRate) noexcept {
            if (!enabled) return;
            std::size_t haasSamples = static_cast<std::size_t>((haasDelayMs * sampleRate) / 1000.0);
            if (haasSamples >= 4090) haasSamples = 4090;

            delayBufferLeft[writeIdx] = left;
            delayBufferRight[writeIdx] = right;

            std::size_t readIdx = (writeIdx + 4096 - haasSamples) % 4096;
            float delayedRight = delayBufferRight[readIdx];

            float mid = (left + right) * 0.5f;
            float side = (left - right) * 0.5f * widthRatio;
            left = mid + side;
            right = mid - side + (delayedRight * roomSize * 0.35f);

            writeIdx = (writeIdx + 1) % 4096;
        }
    } spatialReverb_{};

    struct Bitcrusher final {
        int counter{0};
        float lastLeft{0.0f};
        float lastRight{0.0f};

        inline void process(float& left, float& right, bool enabled, int bits, int downsampleFactor) noexcept {
            if (!enabled) return;
            counter++;
            if (counter >= downsampleFactor) {
                counter = 0;
                float step = std::pow(2.0f, static_cast<float>(bits - 1));
                lastLeft = std::round(left * step) / step;
                lastRight = std::round(right * step) / step;
            }
            left = lastLeft;
            right = lastRight;
        }
    } bitcrusher_{};
    std::atomic<std::uint32_t> targetEqualizerRevision_{0};
    std::array<std::atomic<float>, kEqualizerBandCount> targetEqGainsDb_{};
    std::array<float, kEqualizerBandCount> currentEqGainsDb_{};
    std::array<Biquad, kEqualizerBandCount> equalizerBands_{};
    std::int32_t equalizerUpdateCountdown_{0};
    std::int32_t equalizerHeadroomCountdown_{0};
    std::uint32_t appliedEqualizerRevision_{0};
    std::uint32_t activeEqualizerBands_{0};
    float currentPreampDb_{0.0F};
    float currentPreampGain_{1.0F};
    float equalizerMaximumBoostDb_{0.0F};
    float limiterGain_{1.0F};
    float equalizerGainSmoothing_{0.1F};
    float limiterRelease_{0.001F};
    // One-pole DC blocker (10 Hz). Removes stream DC offset so peaks keep the
    // full symmetric headroom; transparent for DC-free program material.
    float dcBlockerR_{0.999F};
    std::array<double, 2> dcXPrev_{};
    std::array<double, 2> dcYPrev_{};
    std::int32_t microFadeFrameCount_{96};
    std::int32_t microFadePosition_{0};
    bool clarityChainActive_{false};
    Biquad subBassHighPass_{};
    Biquad bassFoundation_{};
    Biquad lowMidSeparation_{};
    Biquad boxinessControl_{};
    Biquad presenceDetail_{};
    Biquad airDetail_{};
    Biquad monoBassFilter_{};
    Biquad airExciterFilter_{};
    Crossfeed crossfeed_{};
};

}  // namespace lastwave::audio
