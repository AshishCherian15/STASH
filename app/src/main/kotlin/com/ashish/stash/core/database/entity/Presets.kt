package com.ashish.stash.core.database.entity

/**
 * Professional preset palette for Legal, Financial, and Medical documents.
 * design.md §1: premium leather and paper tones.
 */
object StashPalette {
    const val ACTIVE_CASE = "#E6D2B5"
    const val ARCHIVED = "#BCAAA4"
    const val PENDING = "#DBBDAA"
    const val EXTERNAL = "#DFCCBE"
    const val INTERNAL = "#EBDED3"
    const val FINANCIAL = "#4A90E2"
    const val LEGAL = "#D9534F"
    const val PERSONNEL = "#5CB85C"
    const val MEDICAL = "#F0E68C"
    const val MISC = "#9370DB"
}

/**
 * Default organizational data injected on first launch.
 */
object StashDefaults {
    val Categories = listOf(
        CategoryEntity(name = "Active Cases", color = StashPalette.ACTIVE_CASE),
        CategoryEntity(name = "Archived", color = StashPalette.ARCHIVED),
        CategoryEntity(name = "Financial", color = StashPalette.FINANCIAL),
        CategoryEntity(name = "Legal", color = StashPalette.LEGAL),
        CategoryEntity(name = "Medical", color = StashPalette.MEDICAL),
        CategoryEntity(name = "Personnel", color = StashPalette.PERSONNEL)
    )
    
    val Folders = listOf(
        FolderEntity(name = "General Vault", isLocked = 1),
        FolderEntity(name = "Priority Inbox", isLocked = 0)
    )
    
    val Labels = listOf(
        LabelEntity(name = "Urgent"),
        LabelEntity(name = "To Review"),
        LabelEntity(name = "Personal")
    )
}
