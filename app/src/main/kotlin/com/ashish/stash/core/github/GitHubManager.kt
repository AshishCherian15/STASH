package com.ashish.stash.core.github

import android.content.Context
import com.ashish.stash.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val repoOwner = "AshishCherian15"
    private val repoName = "STASH"
    private val baseUrl = "https://api.github.com/repos/$repoOwner/$repoName"

    data class ReleaseInfo(
        val tagName: String,
        val downloadUrl: String,
        val downloadCount: Int,
        val isNewer: Boolean
    )

    suspend fun getLatestRelease(): ReleaseInfo? = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/releases/latest")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json")

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(response)
                val tagName = json.getString("tag_name")
                val assets = json.getJSONArray("assets")
                var downloadCount = 0
                var downloadUrl = ""

                if (assets.length() > 0) {
                    val asset = assets.getJSONObject(0)
                    downloadCount = asset.getInt("download_count")
                    downloadUrl = asset.getString("browser_download_url")
                }

                ReleaseInfo(
                    tagName = tagName,
                    downloadUrl = downloadUrl,
                    downloadCount = downloadCount,
                    isNewer = isNewerThanCurrent(tagName)
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun submitFeedback(title: String, body: String, token: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/issues")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Authorization", "token $token")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val payload = JSONObject().apply {
                put("title", title)
                put("body", body)
            }

            connection.outputStream.use { it.write(payload.toString().toByteArray()) }
            connection.responseCode == 201
        } catch (e: Exception) {
            false
        }
    }

    private fun isNewerThanCurrent(remoteTag: String): Boolean {
        // Simple string comparison for demo. In production, use Semantic Versioning logic.
        val current = "1.0.0" // BuildConfig.VERSION_NAME - assuming 1.0.0 for now
        return remoteTag.trimStart('v') > current
    }
}
