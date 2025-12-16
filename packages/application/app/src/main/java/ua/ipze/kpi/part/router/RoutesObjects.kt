package ua.ipze.kpi.part.router

import kotlinx.serialization.Serializable

@Serializable
data object LoginPageData

@Serializable
data object CreateArtPageData

@Serializable
data object GalleryPageData

@Serializable
data class EditorPageData(
    val historyLength: Int,
    val id: Long
)