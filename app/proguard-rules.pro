# 1. Native C++ JNI Audio Engine
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class com.lastwave.app.playback.NativeAudioEngine { *; }

# 2. JSON & Data Serialization Models
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}
-keep @kotlinx.serialization.Serializable class * { *; }
-keep class com.lastwave.app.data.** { *; }

# 3. AndroidX Media3 / ExoPlayer
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# 4. Coil & Image Loading
-keep class coil.** { *; }

# 5. OkHttp / Retrofit / Room / Hilt / NewPipe / InnerTubeX
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class *

-keep class dagger.hilt.internal.aggregatedroot.codegen.** { *; }
-keep class hilt_aggregated_deps.** { *; }

-keep class org.schabi.newpipe.extractor.** { *; }
-dontwarn org.schabi.newpipe.extractor.**
-keep class org.jsoup.** { *; }
-dontwarn org.jsoup.**
-dontwarn java.beans.**
-dontwarn org.mozilla.javascript.**
-dontwarn javax.script.**

-keep class com.metrolist.innertubex.** { *; }
-keep class io.ktor.client.HttpClientJvmKt { *; }
-keep class io.ktor.client.HttpClientKt { *; }
-keep class io.ktor.client.engine.cio.** { *; }

-dontwarn kotlin.**
-dontwarn kotlinx.io.**
-dontwarn kotlinx.coroutines.**
-dontwarn kotlinx.serialization.**
-dontwarn com.dokar.quickjs.**
-dontwarn com.metrolist.innertubex.**
-dontwarn io.ktor.**
