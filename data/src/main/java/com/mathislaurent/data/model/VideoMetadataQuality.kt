package com.mathislaurent.data.model

import com.google.gson.annotations.SerializedName

data class VideoMetadataQuality(
    @SerializedName("auto")
    val autoList: List<VideoMetadataQualityAuto>
)

data class VideoMetadataQualityAuto(
    @SerializedName("url")
    val url: String,

    @SerializedName("type")
    val type: String
)
