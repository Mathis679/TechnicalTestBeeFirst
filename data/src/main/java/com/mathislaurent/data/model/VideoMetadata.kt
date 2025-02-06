package com.mathislaurent.data.model

import com.google.gson.annotations.SerializedName

data class VideoMetadata(
    @SerializedName("url")
    val url: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("qualities")
    val qualities: VideoMetadataQuality
)
