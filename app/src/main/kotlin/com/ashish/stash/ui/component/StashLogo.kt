package com.ashish.stash.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.ashish.stash.R

@Composable
fun StashLogo(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.stash_logo),
            contentDescription = "Stash Logo",
            modifier = Modifier.fillMaxSize()
        )
    }
}
