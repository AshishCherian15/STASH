package com.ashish.stash.domain.usecase

import android.net.Uri
import com.ashish.stash.core.preferences.PreferencesManager
import com.ashish.stash.core.saf.SafUriManager
import javax.inject.Inject

class UpdateVaultFolderUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val safUriManager: SafUriManager
) {
    suspend operator fun invoke(uri: Uri) {
        safUriManager.takePersistablePermission(uri)
        preferencesManager.setVaultRootUri(uri.toString())
    }
}
