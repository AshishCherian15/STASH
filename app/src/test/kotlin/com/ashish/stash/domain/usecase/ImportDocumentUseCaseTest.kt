package com.ashish.stash.domain.usecase

import android.content.Context
import android.net.Uri
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.hash.HashService
import com.ashish.stash.core.saf.SafUriManager
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ImportDocumentUseCaseTest {
    private lateinit var useCase: ImportDocumentUseCase
    private val context = mockk<Context>(relaxed = true)
    private val repository = mockk<DocumentRepository>(relaxed = true)
    private val safUriManager = mockk<SafUriManager>(relaxed = true)
    private val hashService = mockk<HashService>()

    @Before
    fun setup() {
        mockkStatic(Uri::class)
        val mockUri = mockk<Uri>(relaxed = true)
        every { Uri.parse(any()) } returns mockUri
        
        useCase = ImportDocumentUseCase(context, repository, safUriManager, hashService)
    }

    @Test
    fun `invoke returns -2 when document is duplicate`() = runTest {
        val uri = Uri.parse("content://file")
        val hash = "hash123"
        val metadata = SafUriManager.SafMetadata("file.pdf", 1024L, "application/pdf")
        
        every { safUriManager.queryMetadata(any()) } returns metadata
        coEvery { hashService.calculateHash(any()) } returns hash
        coEvery { repository.countDocumentsByHashAndSize(hash, 1024L) } returns 1

        val result = useCase(uri)

        assertEquals(-2L, result)
        coVerify(exactly = 0) { repository.insertDocument(any()) }
    }

    @Test
    fun `invoke inserts document when not duplicate`() = runTest {
        val uri = Uri.parse("content://file")
        val hash = "hash123"
        val metadata = SafUriManager.SafMetadata("file.pdf", 1024L, "application/pdf")
        
        every { safUriManager.queryMetadata(any()) } returns metadata
        every { safUriManager.takePersistablePermission(any()) } returns Unit
        coEvery { hashService.calculateHash(any()) } returns hash
        coEvery { repository.countDocumentsByHashAndSize(hash, 1024L) } returns 0
        coEvery { repository.insertDocument(any()) } returns 100L

        val result = useCase(uri)

        assertEquals(100L, result)
        coVerify(exactly = 1) { repository.insertDocument(any()) }
    }
}
