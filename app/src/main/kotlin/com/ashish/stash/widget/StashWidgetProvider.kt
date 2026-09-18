package com.ashish.stash.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.ashish.stash.R
import com.ashish.stash.core.database.repository.DocumentRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class StashWidgetProvider : AppWidgetProvider() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetEntryPoint {
        fun documentRepository(): DocumentRepository
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java
        )
        val repository = entryPoint.documentRepository()

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {
                val totalDocs = repository.observeAllDocuments(showLocked = true).first().size
                val unlockedDocs = repository.observeAllDocuments(showLocked = false).first().size
                
                for (appWidgetId in appWidgetIds) {
                    val views = RemoteViews(context.packageName, R.layout.stash_widget)
                    views.setTextViewText(
                        R.id.widget_document_count,
                        context.getString(R.string.widget_documents, totalDocs)
                    )
                    views.setTextViewText(
                        R.id.widget_storage,
                        "$unlockedDocs unlocked"
                    )
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }
}
