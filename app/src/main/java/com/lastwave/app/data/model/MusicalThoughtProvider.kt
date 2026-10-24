package com.lastwave.app.data.model

import java.util.Calendar

data class MusicalThought(
    val quote: String,
    val author: String,
    val category: String = "Acoustic Philosophy",
)

object MusicalThoughtProvider {

    val THOUGHTS: List<MusicalThought> = listOf(
        MusicalThought("Music is the silence between the notes.", "Claude Debussy", "Acoustic Philosophy"),
        MusicalThought("Where words fail, music speaks.", "Hans Christian Andersen", "Expressive Purity"),
        MusicalThought("Soundstage purity reveals the spatial dimension of acoustic vibration.", "Ananta Dhvani", "Audiophile Fidelity"),
        MusicalThought("Dynamic range is the breath and heartbeat of uncompressed sound.", "Acoustic Principles", "Acoustic Fidelity"),
        MusicalThought("Without music, life would be a mistake.", "Friedrich Nietzsche", "Philosophical Thought"),
        MusicalThought("Music produces a kind of pleasure which human nature cannot do without.", "Confucius", "Timeless Vibration"),
        MusicalThought("High-fidelity audio is an unbroken continuum from transducer to emotion.", "Sound Engineering", "Signal Integrity"),
        MusicalThought("The goal of acoustics is to make the equipment invisible to the performance.", "Audio Philosophy", "Soundstage Purity"),
        MusicalThought("Music is the shorthand of emotion.", "Leo Tolstoy", "Expressive Purity"),
        MusicalThought("Acoustic resonance translates raw frequencies into physical sensation.", "Acoustic Science", "Sonic Texture"),
        MusicalThought("Dynamic headroom preserves the delicate nuances of transient impact.", "Audiophile Engineering", "Dynamic Range"),
        MusicalThought("Music expresses that which cannot be put into words and that which cannot remain silent.", "Victor Hugo", "Acoustic Philosophy"),
        MusicalThought("True fidelity is rendering the reverberant decay of the recording room.", "Listening Artistry", "Soundstage Purity"),
        MusicalThought("The universe has rhythm; music is human harmony aligning with cosmos.", "Pythagorean Harmonia", "Universal Waves"),
        MusicalThought("Linear phase response guarantees that every harmonic reaches the ear in unison.", "DSP Science", "Phase Coherence"),
        MusicalThought("Music is a higher revelation than all wisdom and philosophy.", "Ludwig van Beethoven", "Expressive Purity"),
        MusicalThought("A clean noise floor unveils micro-details previously lost in digital shadow.", "Audiophile Engineering", "Signal Integrity"),
        MusicalThought("Music cleanses the understanding; inspires it, and lifts it into a realm which it would not otherwise reach.", "Henry Ward Beecher", "Acoustic Philosophy"),
        MusicalThought("Direct bitstream transport ensures bit-perfect playback without OS resampling.", "Digital Audio", "Bit-Perfect Fidelity"),
        MusicalThought("Sound is the vocabulary of the soul.", "Acoustic Thought", "Sonic Texture"),
        MusicalThought("Timbre is the unique fingerprint of every instrument vibrating in physical space.", "Acoustic Principles", "Acoustic Fidelity"),
        MusicalThought("When the acoustic soundstage opens, the physical walls of the room vanish.", "Listening Artistry", "Soundstage Purity"),
        MusicalThought("Music gives a soul to the universe, wings to the mind, flight to the imagination and life to everything.", "Plato", "Timeless Vibration"),
        MusicalThought("The purity of a harmonic triad is rooted in mathematical resonance.", "Acoustic Science", "Phase Coherence"),
        MusicalThought("In high resolution audio, silence is as deliberate as sound.", "Ananta Dhvani", "Audiophile Fidelity"),
        MusicalThought("Acoustic vibration connects the listener directly to the artist's original intent.", "Sound Engineering", "Expressive Purity"),
    )

    fun getDailyThought(): MusicalThought {
        val calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val year = calendar.get(Calendar.YEAR)
        val seed = dayOfYear + year * 365
        val index = (seed % THOUGHTS.size + THOUGHTS.size) % THOUGHTS.size
        return THOUGHTS[index]
    }
}
