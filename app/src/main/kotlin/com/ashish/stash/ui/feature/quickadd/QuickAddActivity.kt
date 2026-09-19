package com.ashish.stash.ui.feature.quickadd

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ashish.stash.domain.usecase.ImportDocumentUseCase
import com.ashish.stash.ui.theme.StashTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class QuickAddActivity : ComponentActivity() {

    @Inject
    lateinit var importDocumentUseCase: ImportDocumentUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val action = intent.action

        val uris = when (action) {
            Intent.ACTION_SEND -> {
                val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                if (uri != null) listOf(uri) else emptyList()
            }
            Intent.ACTION_SEND_MULTIPLE -> {
                intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM) ?: emptyList()
            }
            else -> emptyList()
        }

        if (uris.isEmpty()) {
            finish()
            return
        }

        setContent {
            StashTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black.copy(alpha = 0.4f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            }
        }

        importUris(uris)
    }

    private fun importUris(uris: List<Uri>) {
        CoroutineScope(Dispatchers.Main).launch {
            var successCount = 0
            withContext(Dispatchers.IO) {
                uris.forEach { uri ->
                    val result = importDocumentUseCase(uri, source = "SHARE")
                    if (result >= 0) successCount++
                }
            }
            
            if (successCount > 0) {
                Toast.makeText(this@QuickAddActivity, "Stashed $successCount documents", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@QuickAddActivity, "Import failed or duplicates skipped", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}
