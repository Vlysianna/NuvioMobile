package com.nuvio.app.features.trakt

import co.touchlab.kermit.Logger
import com.nuvio.app.features.addons.httpRequestRaw
import kotlinx.serialization.json.Json

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object TraktCalendarRepository {
    private val syncScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val log = Logger.withTag("TraktCalendar")
    private val json = Json { ignoreUnknownKeys = true }

    private val _upcomingShows = MutableStateFlow<List<TraktCalendarEntry>>(emptyList())
    val upcomingShows: StateFlow<List<TraktCalendarEntry>> = _upcomingShows.asStateFlow()

    init {
        syncScope.launch {
            TraktAuthRepository.isAuthenticated.collect { authenticated ->
                if (authenticated) {
                    refresh()
                } else {
                    _upcomingShows.value = emptyList()
                }
            }
        }
    }

    suspend fun refresh() {
        _upcomingShows.value = getUpcomingShows()
    }

    suspend fun getUpcomingShows(): List<TraktCalendarEntry> {
        val headers = TraktAuthRepository.authorizedHeaders() ?: return emptyList()
        val url = "https://api.trakt.tv/calendars/my/shows"

        return try {
            val response = httpRequestRaw(
                method = "GET",
                url = url,
                headers = headers,
                body = "",
            )

            if (response.status !in 200..299) {
                log.w { "Trakt calendar fetch failed with status ${response.status}" }
                return emptyList()
            }

            val dtos = json.decodeFromString<List<TraktCalendarItemDto>>(response.body)
            dtos.mapNotNull { dto ->
                if (dto.firstAired != null && dto.episode != null && dto.show != null) {
                    TraktCalendarEntry(
                        showTitle = dto.show.title.orEmpty(),
                        episodeTitle = dto.episode.title.orEmpty(),
                        season = dto.episode.season ?: 0,
                        number = dto.episode.number ?: 0,
                        firstAired = dto.firstAired,
                        tmdbId = dto.show.ids?.tmdb,
                        imdbId = dto.show.ids?.imdb,
                    )
                } else null
            }
        } catch (e: Exception) {
            log.e(e) { "Failed to load trakt calendar from $url" }
            emptyList()
        }
    }
}
