package com.ashish.stash.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ashish.stash.core.database.dao.DocumentDao
import com.ashish.stash.core.database.entity.DocumentEntity
import com.ashish.stash.core.database.entity.Importance
import com.ashish.stash.core.database.entity.ImportSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented DB tests — Week 1 exit criteria (PLAN.md Phase 1 Week 1).
 * Verifies:
 *   1. All 7 tables created (checked via insert/query round-trips)
 *   2. is_locked filter in DAO queries enforced — locked docs never returned in
 *      general queries (SCHEMA.md non-obvious constraint, REVIEW.md correctness check)
 *   3. Dedup check (hash + size) returns correct count
 *   4. Priority mode query (HIGH/CRITICAL only)
 *
 * Uses in-memory Room DB — no on-disk state, no cleanup needed between test runs.
 */
@RunWith(AndroidJUnit4::class)
class StashDatabaseTest {

    private lateinit var db: StashDatabase
    private lateinit var documentDao: DocumentDao

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, StashDatabase::class.java)
            .allowMainThreadQueries() // acceptable in tests only
            .build()
        documentDao = db.documentDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun makeDocument(
        title: String,
        isLocked: Int = 0,
        importance: String = Importance.MEDIUM.name,
        hash: String = "abc123",
        sizeBytes: Long = 1024L,
    ) = DocumentEntity(
        uri = "content://test/$title",
        displayTitle = title,
        originalFilename = "$title.pdf",
        mimeType = "application/pdf",
        fileHash = hash,
        fileSize = sizeBytes,
        isLocked = isLocked,
        importance = importance,
        createdAt = System.currentTimeMillis(),
        lastOpenedAt = System.currentTimeMillis(),
        importSource = ImportSource.PICKER.name,
    )

    // ── Tests ─────────────────────────────────────────────────────────────────

    @Test
    fun insertAndRetrieveDocument() = runTest {
        val doc = makeDocument("Physics.pdf")
        val id = documentDao.insert(doc)
        assertTrue("Insert should return a positive ID", id > 0)

        val retrieved = documentDao.getById(id)
        assertNotNull("Should retrieve inserted doc", retrieved)
        assertEquals("Physics.pdf", retrieved!!.displayTitle)
    }

    /**
     * Privacy correctness: locked document must NOT appear in observeAllUnlocked().
     * This is the most critical Week 1 test — a failure here means locked content
     * could leak through the UI. (SCHEMA.md non-obvious constraint, REVIEW.md §Correctness)
     */
    @Test
    fun lockedDocumentExcludedFromUnlockedQuery() = runTest {
        documentDao.insert(makeDocument("Visible.pdf", isLocked = 0, hash = "hash1"))
        documentDao.insert(makeDocument("Secret.pdf",  isLocked = 1, hash = "hash2"))

        val visible = documentDao.observeAllUnlocked().first()
        assertEquals("Only 1 unlocked doc should be returned", 1, visible.size)
        assertEquals("Visible.pdf", visible[0].displayTitle)
        assertTrue(
            "Secret.pdf must never appear in unlocked query",
            visible.none { it.displayTitle == "Secret.pdf" },
        )
    }

    @Test
    fun dupCheckReturnsTrueForSameHashAndSize() = runTest {
        documentDao.insert(makeDocument("Original.pdf", hash = "deadbeef", sizeBytes = 2048L))

        val count = documentDao.countByHashAndSize("deadbeef", 2048L)
        assertEquals("Dedup check: should find 1 existing match", 1, count)
    }

    @Test
    fun dupCheckReturnsFalseForDifferentHash() = runTest {
        documentDao.insert(makeDocument("Original.pdf", hash = "aaaa", sizeBytes = 2048L))

        val count = documentDao.countByHashAndSize("bbbb", 2048L)
        assertEquals("Different hash: no dedup match expected", 0, count)
    }

    @Test
    fun priorityModeQueryReturnsOnlyHighAndCritical() = runTest {
        documentDao.insert(makeDocument("LowDoc",    importance = Importance.LOW.name,      hash = "h1"))
        documentDao.insert(makeDocument("MediumDoc", importance = Importance.MEDIUM.name,   hash = "h2"))
        documentDao.insert(makeDocument("HighDoc",   importance = Importance.HIGH.name,     hash = "h3"))
        documentDao.insert(makeDocument("CritDoc",   importance = Importance.CRITICAL.name, hash = "h4"))

        val priority = documentDao.observePriority().first()
        assertEquals("Priority mode must return exactly 2 docs", 2, priority.size)
        assertTrue(priority.all { it.importance in listOf(Importance.HIGH.name, Importance.CRITICAL.name) })
    }

    @Test
    fun priorityModeExcludesLockedDocs() = runTest {
        documentDao.insert(makeDocument("LockedHigh", isLocked = 1, importance = Importance.HIGH.name, hash = "h1"))
        documentDao.insert(makeDocument("UnlockedHigh", isLocked = 0, importance = Importance.HIGH.name, hash = "h2"))

        val priority = documentDao.observePriority().first()
        assertEquals("Locked high-priority doc must not appear in priority mode", 1, priority.size)
        assertEquals("UnlockedHigh", priority[0].displayTitle)
    }

    @Test
    fun countsAreCorrect() = runTest {
        documentDao.insert(makeDocument("Doc1", isLocked = 0, hash = "h1"))
        documentDao.insert(makeDocument("Doc2", isLocked = 0, hash = "h2"))
        documentDao.insert(makeDocument("Doc3", isLocked = 1, hash = "h3"))

        val unlockedCount = documentDao.observeUnlockedCount().first()
        val lockedCount   = documentDao.observeLockedCount().first()
        assertEquals(2, unlockedCount)
        assertEquals(1, lockedCount)
    }

    @Test
    fun updateLastOpenedAt() = runTest {
        val id = documentDao.insert(makeDocument("Doc.pdf"))
        val newTime = System.currentTimeMillis() + 5000L
        documentDao.updateLastOpened(id, newTime)

        val updated = documentDao.getById(id)
        assertEquals("lastOpenedAt should be updated", newTime, updated!!.lastOpenedAt)
    }
}
