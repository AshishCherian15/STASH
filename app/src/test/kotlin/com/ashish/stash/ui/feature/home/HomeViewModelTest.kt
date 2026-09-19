package com.ashish.stash.ui.feature.home

import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.saf.SafUriManager
import com.ashish.stash.core.security.LockState
import com.ashish.stash.core.security.SecuritySessionManager
import com.ashish.stash.domain.usecase.ImportDocumentUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel
    private val repository = mockk<DocumentRepository>(relaxed = true)
    private val importUseCase = mockk<ImportDocumentUseCase>()
    private val safUriManager = mockk<SafUriManager>()
    private val securitySessionManager = mockk<SecuritySessionManager>(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        every { securitySessionManager.lockState } returns MutableStateFlow(LockState.Unlocked)
        every { securitySessionManager.itemsUnlocked } returns MutableStateFlow(true)
        every { repository.observeAllDocuments(any()) } returns flowOf(emptyList())
        every { repository.observeUnlockedDocumentsCount() } returns flowOf(0)
        every { repository.observeLockedDocumentsCount() } returns flowOf(0)
        every { repository.observeTotalSizeBytes(any()) } returns flowOf(0L)

        viewModel = HomeViewModel(repository, importUseCase, safUriManager, securitySessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() {
        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertEquals(emptyList<DocumentUiModel>(), state.documents)
    }
}
