package com.lastwave.app.data.backup

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebDavSyncManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient,
) {
    suspend fun backupToWebDav(serverUrl: String, user: String, pass: String, secretKey: String): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val dbFile = context.getDatabasePath("lastwave.db")
            if (!dbFile.exists()) return@withContext false
            val dbBytes = dbFile.readBytes()

            // Encrypt using AES-256-GCM
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val keySpec = SecretKeySpec(secretKey.padEnd(32, '0').take(32).toByteArray(), "AES")
            val iv = ByteArray(12).also { java.security.SecureRandom().nextBytes(it) }
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, GCMParameterSpec(128, iv))
            val encrypted = iv + cipher.doFinal(dbBytes)

            val targetUrl = if (serverUrl.endsWith("/")) "${serverUrl}ananta_dhvani_backup.enc" else "$serverUrl/ananta_dhvani_backup.enc"
            val request = Request.Builder()
                .url(targetUrl)
                .addHeader("Authorization", Credentials.basic(user, pass))
                .put(encrypted.toRequestBody("application/octet-stream".toMediaType()))
                .build()

            okHttpClient.newCall(request).execute().use { it.isSuccessful }
        }.getOrDefault(false)
    }

    suspend fun restoreFromWebDav(serverUrl: String, user: String, pass: String, secretKey: String): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val targetUrl = if (serverUrl.endsWith("/")) "${serverUrl}ananta_dhvani_backup.enc" else "$serverUrl/ananta_dhvani_backup.enc"
            val request = Request.Builder()
                .url(targetUrl)
                .addHeader("Authorization", Credentials.basic(user, pass))
                .get()
                .build()

            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext false
                val encryptedBytes = response.body?.bytes() ?: return@withContext false
                if (encryptedBytes.size < 13) return@withContext false

                val iv = encryptedBytes.copyOfRange(0, 12)
                val payload = encryptedBytes.copyOfRange(12, encryptedBytes.size)

                val cipher = Cipher.getInstance("AES/GCM/NoPadding")
                val keySpec = SecretKeySpec(secretKey.padEnd(32, '0').take(32).toByteArray(), "AES")
                cipher.init(Cipher.DECRYPT_MODE, keySpec, GCMParameterSpec(128, iv))
                val decrypted = cipher.doFinal(payload)

                val dbFile = context.getDatabasePath("lastwave.db")
                dbFile.parentFile?.mkdirs()
                dbFile.writeBytes(decrypted)
                true
            }
        }.getOrDefault(false)
    }
}
