package com.ashish.stash.core.backup

import android.content.Context
import android.os.Environment
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
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val stashDir = File(downloadsDir, "Stash")
            if (!stashDir.exists()) stashDir.mkdirs()
            
            val file = File(stashDir, fileName)
            file.writeText(jsonString)
            
            "Backup exported to: Download/Stash/$fileName"
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
