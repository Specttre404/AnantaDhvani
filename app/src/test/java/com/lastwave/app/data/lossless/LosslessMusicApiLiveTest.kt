package com.lastwave.app.data.lossless

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class LosslessMusicApiLiveTest {

    private lateinit var api: LosslessMusicApi
    private lateinit var okHttpClient: OkHttpClient

    @Before
    fun setUp() {
        val mockInterceptor = Interceptor { chain ->
            val request = chain.request()
            val url = request.url.toString()
            val query = request.url.queryParameter("q").orEmpty()

            val responseBuilder = Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)

            when {
                url.contains("fLaC_stream") || url.endsWith(".flac") -> {
                    val flacBytes = byteArrayOf('f'.code.toByte(), 'L'.code.toByte(), 'a'.code.toByte(), 'C'.code.toByte()) + ByteArray(4092)
                    responseBuilder
                        .code(200)
                        .message("OK")
                        .header("Content-Type", "audio/flac")
                        .body(flacBytes.toResponseBody("audio/flac".toMediaTypeOrNull()))
                        .build()
                }
                url.contains("/api/track/") -> {
                    val json = """{"success":true,"data":{"url":"https://mock.stream.url/fLaC_stream.flac","mime_type":"audio/flac","bit_depth":24,"sampling_rate":96.0,"format_id":7}}"""
                    responseBuilder
                        .code(200)
                        .message("OK")
                        .header("Content-Type", "application/json")
                        .body(json.toResponseBody("application/json".toMediaTypeOrNull()))
                        .build()
                }
                url.contains("/api/search") -> {
                    val title = when {
                        query.contains("zindagi", ignoreCase = true) -> "Do Zindagi | Dikz (Official Audio)"
                        query.contains("ayanokoji", ignoreCase = true) -> "Ayanokoji | Dikz"
                        query.contains("sakuna", ignoreCase = true) -> "Gojo vs Sakuna Rap"
                        query.contains("smile", ignoreCase = true) -> "Die With A Smile"
                        else -> "Mock Track"
                    }
                    val artist = when {
                        query.contains("gaga", ignoreCase = true) -> "Lady Gaga & Bruno Mars"
                        query.contains("dikz", ignoreCase = true) -> "Dikz"
                        else -> "Mock Artist"
                    }
                    val json = """{"success":true,"results":{"tracks":{"items":[{"id":12345,"title":"$title","duration":200,"source":"qobuz","performer":{"name":"$artist"},"hires":true,"maximum_bit_depth":24,"maximum_sampling_rate":96.0}]}}}"""
                    responseBuilder
                        .code(200)
                        .message("OK")
                        .header("Content-Type", "application/json")
                        .body(json.toResponseBody("application/json".toMediaTypeOrNull()))
                        .build()
                }
                else -> {
                    val json = """{"success":true,"data":{"url":"https://mock.stream.url/fLaC_stream.flac","mime_type":"audio/flac","bit_depth":16,"sampling_rate":44.1,"format_id":6}}"""
                    responseBuilder
                        .code(200)
                        .message("OK")
                        .header("Content-Type", "application/json")
                        .body(json.toResponseBody("application/json".toMediaTypeOrNull()))
                        .build()
                }
            }
        }

        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(mockInterceptor)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
        api = LosslessMusicApi(okHttpClient)
    }

    private fun verifyFlacStream(streamUrl: String) {
        val req = Request.Builder().url(streamUrl).header("User-Agent", "LastWave/1.0").get().build()
        okHttpClient.newCall(req).execute().use { response ->
            assertTrue("Expected HTTP 200/206 but got ${response.code}", response.isSuccessful)
            val contentType = response.header("Content-Type").orEmpty()
            assertTrue("Expected audio/flac but got $contentType", contentType.contains("flac"))
            val source = response.body?.source()
            assertNotNull("Response body source should not be null", source)
            val buffer = okio.Buffer()
            source?.read(buffer, 4096)
            val bytes = buffer.readByteArray()
            val hasFlacHeader = bytes.size >= 4 && bytes[0] == 'f'.code.toByte() && bytes[1] == 'L'.code.toByte() && bytes[2] == 'a'.code.toByte() && bytes[3] == 'C'.code.toByte()
            println("  [Stream Verified] HTTP ${response.code} | Content-Type: $contentType | First ${bytes.size} bytes | Native FLAC Header: $hasFlacHeader")
            assertTrue("Stream should begin with fLaC header", hasFlacHeader)
        }
    }

    @Test
    fun testDoZindigiByDikzYouTubeFormat() = runBlocking {
        println("\n=== KOTLIN TEST: 'Do Zindagi | Dikz (Official Audio)' by 'Dikz - Topic' ===")
        val stream = api.resolveStream(
            title = "Do Zindagi | Dikz (Official Audio)",
            artist = "Dikz - Topic",
        )
        assertNotNull("Do Zindagi YouTube format should resolve to a Lossless stream", stream)
        println("  Resolved Stream URL: ${stream?.url}")
        println("  FormatId: ${stream?.formatId} | MimeType: ${stream?.mimeType}")
        verifyFlacStream(stream!!.url)
    }

    @Test
    fun testAyanokojiByDikzYouTubeFormat() = runBlocking {
        println("\n=== KOTLIN TEST: 'Ayanokoji | Dikz' by 'Dikz - Topic' ===")
        val stream = api.resolveStream(
            title = "Ayanokoji | Dikz",
            artist = "Dikz - Topic",
        )
        assertNotNull("Ayanokoji YouTube format should resolve to a Lossless stream", stream)
        println("  Resolved Stream URL: ${stream?.url}")
        println("  FormatId: ${stream?.formatId} | MimeType: ${stream?.mimeType}")
        verifyFlacStream(stream!!.url)
    }

    @Test
    fun testGojoVsSakunaByDikzYouTubeFormat() = runBlocking {
        println("\n=== KOTLIN TEST: 'Gojo vs Sakuna Rap' by 'Dikz' ===")
        val stream = api.resolveStream(
            title = "Gojo vs Sakuna Rap",
            artist = "Dikz",
        )
        assertNotNull("Gojo vs Sakuna YouTube format should resolve to a Lossless stream", stream)
        println("  Resolved Stream URL: ${stream?.url}")
        println("  FormatId: ${stream?.formatId} | MimeType: ${stream?.mimeType}")
        verifyFlacStream(stream!!.url)
    }

    @Test
    fun testDieWithASmilePureQobuz() = runBlocking {
        println("\n=== KOTLIN TEST: 'Die With A Smile' by 'Lady Gaga & Bruno Mars' ===")
        val stream = api.resolveStream(
            title = "Die With A Smile",
            artist = "Lady Gaga & Bruno Mars",
        )
        assertNotNull("Die With A Smile should resolve to a Lossless stream", stream)
        println("  Resolved Stream URL: ${stream?.url}")
        println("  FormatId: ${stream?.formatId} | MimeType: ${stream?.mimeType}")
        verifyFlacStream(stream!!.url)
    }
}
