package com.ashish.stash.core.backup

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.ashish.stash.core.database.entity.CategoryEntity
import com.ashish.stash.core.database.entity.FolderEntity
import com.ashish.stash.core.database.entity.LabelEntity
import com.ashish.stash.core.database.repository.DocumentRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class StashBackup(
    val version: Int = 1,
    val categories: List<CategoryEntity>,
    val labels: List<LabelEntity>,
    val folders: List<FolderEntity>
)

@Singleton
class BackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: DocumentRepository
) {
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    suspend fun exportBackup(): String = withContext(Dispatchers.IO) {
        try {
            val categories = repository.observeAllCategories().first()
            val labels = repository.observeAllLabels().first()
            val folders = repository.observeAllFolders(showLocked = true).first()

            val backup = StashBackup(
                categories = categories,
                labels = labels,
                folders = folders
            )

            val jsonString = json.encodeToString(backup)
            val fileName = "stash_backup_${System.currentTimeMillis()}.json"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/Stash")
                }
                val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    context.contentResolver.openOutputStream(uri)?.use { 
                        it.write(jsonString.toByteArray())
                    }
                    "Backup saved to Downloads/Stash/$fileName"
                } else {
                    "Failed to create file in MediaStore"
                }
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val stashDir = File(downloadsDir, "Stash")
                if (!stashDir.exists()) stashDir.mkdirs()
                val file = File(stashDir, fileName)
                file.writeText(jsonString)
                "Backup saved to Downloads/Stash/$fileName"
            }
        } catch (e: Exception) {
            "Export failed: ${e.message}"
        }
    }

    suspend fun importBackup(jsonString: String): String = withContext(Dispatchers.IO) {
        try {
            val backup = json.decodeFromString<StashBackup>(jsonString)
            
            backup.categories.forEach { repository.insertCategory(it) }
            backup.labels.forEach { repository.insertLabel(it) }
            backup.folders.forEach { repository.insertFolder(it) }
            
            "Import complete: ${backup.categories.size} categories, ${backup.labels.size} labels."
        } catch (e: Exception) {
            "Import failed: ${e.message}"
        }
    }
}
