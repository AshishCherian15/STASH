package com.ashish.stash.core.database

import androidx.room.TypeConverter
import com.ashish.stash.core.database.entity.Importance
import com.ashish.stash.core.database.entity.OcrStatus
import com.ashish.stash.core.database.entity.SourceKind

class StashTypeConverters {
    @TypeConverter
    fun fromImportance(value: Importance): String = value.name

    @TypeConverter
    fun toImportance(value: String): Importance = try {
        Importance.valueOf(value)
    } catch (e: Exception) {
        Importance.MEDIUM
    }

    @TypeConverter
    fun fromOcrStatus(value: OcrStatus): String = value.name

    @TypeConverter
    fun toOcrStatus(value: String): OcrStatus = try {
        OcrStatus.valueOf(value)
    } catch (e: Exception) {
        OcrStatus.NONE
    }

    @TypeConverter
    fun fromSourceKind(value: SourceKind): String = value.name

    @TypeConverter
    fun toSourceKind(value: String): SourceKind = try {
        SourceKind.valueOf(value)
    } catch (e: Exception) {
        SourceKind.SAF_REFERENCE
    }
}
