package com.nuvio.app.features.trakt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TraktCalendarItemDto(
    @SerialName("first_aired") val firstAired: String? = null,
    @SerialName("episode") val episode: TraktCalendarEpisodeDto? = null,
    @SerialName("show") val show: TraktCalendarShowDto? = null,
)

@Serializable
internal data class TraktCalendarEpisodeDto(
    @SerialName("season") val season: Int? = null,
    @SerialName("number") val number: Int? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("ids") val ids: TraktCalendarIdsDto? = null,
)

@Serializable
internal data class TraktCalendarShowDto(
    @SerialName("title") val title: String? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("ids") val ids: TraktCalendarIdsDto? = null,
)

@Serializable
internal data class TraktCalendarIdsDto(
    @SerialName("trakt") val trakt: Long? = null,
    @SerialName("tvdb") val tvdb: Long? = null,
    @SerialName("imdb") val imdb: String? = null,
    @SerialName("tmdb") val tmdb: Long? = null,
)

data class TraktCalendarEntry(
    val showTitle: String,
    val episodeTitle: String,
    val season: Int,
    val number: Int,
    val firstAired: String,
    val tmdbId: Long?,
    val imdbId: String?
)
