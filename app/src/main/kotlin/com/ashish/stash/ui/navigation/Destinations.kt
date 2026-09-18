package com.ashish.stash.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {
    @Serializable
    data object Splash : Destination

    @Serializable
    data object Onboarding : Destination

    @Serializable
    data object Lock : Destination

    @Serializable
    data object Home : Destination

    @Serializable
    data object Search : Destination

    @Serializable
    data object Priority : Destination

    @Serializable
    data object Settings : Destination

    @Serializable
    data object About : Destination

    @Serializable
    data object Privacy : Destination

    @Serializable
    data object Help : Destination

    @Serializable
    data object Licenses : Destination

    @Serializable
    data class Viewer(val documentId: Long) : Destination

    @Serializable
    data class DocumentDetail(val documentId: Long) : Destination
}
