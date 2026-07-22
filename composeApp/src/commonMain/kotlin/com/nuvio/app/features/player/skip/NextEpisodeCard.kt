package com.nuvio.app.features.player.skip

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import nuvio.composeapp.generated.resources.Res
import nuvio.composeapp.generated.resources.compose_player_episode_title_format
import nuvio.composeapp.generated.resources.detail_btn_play
import nuvio.composeapp.generated.resources.player_next_episode
import nuvio.composeapp.generated.resources.player_next_episode_finding_source
import nuvio.composeapp.generated.resources.player_next_episode_playing_via_countdown
import nuvio.composeapp.generated.resources.player_next_episode_thumbnail
import nuvio.composeapp.generated.resources.player_next_episode_unaired
import org.jetbrains.compose.resources.stringResource

@Composable
fun NextEpisodeCard(
    nextEpisode: NextEpisodeInfo?,
    visible: Boolean,
    isAutoPlaySearching: Boolean,
    autoPlaySourceName: String?,
    autoPlayCountdownSec: Int?,
    onPlayNext: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (nextEpisode == null) return

    val isPlayable = nextEpisode.hasAired

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(animationSpec = tween(260), initialOffsetX = { it / 2 }) +
            fadeIn(animationSpec = tween(220)),
        exit = slideOutHorizontally(animationSpec = tween(200), targetOffsetX = { it / 2 }) +
            fadeOut(animationSpec = tween(160)),
        modifier = modifier,
    ) {
        val shape = RoundedCornerShape(12.dp)
        Column(
            modifier = Modifier
                .width(280.dp)
                .clip(shape)
                .background(Color(0xFF141414).copy(alpha = 0.95f))
                .clickable { if (isPlayable) onPlayNext() }
        ) {
            // Thumbnail area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(157.dp)
            ) {
                AsyncImage(
                    model = nextEpisode.thumbnail,
                    contentDescription = stringResource(Res.string.player_next_episode_thumbnail),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                // Bottom to top gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xFF141414).copy(alpha = 0.95f),
                                ),
                                startY = 50f
                            ),
                        ),
                )
                
                // "Up Next" Label at Top Left
                Text(
                    text = stringResource(Res.string.player_next_episode).uppercase(),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )

                // Play Icon overlay
                if (isPlayable) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(48.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            // Info area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
            ) {
                Text(
                    text = stringResource(
                        Res.string.compose_player_episode_title_format,
                        nextEpisode.season,
                        nextEpisode.episode,
                        nextEpisode.title,
                    ),
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                
                val autoPlayStatus = when {
                    !isPlayable && !nextEpisode.unairedMessage.isNullOrBlank() -> nextEpisode.unairedMessage
                    isAutoPlaySearching -> stringResource(Res.string.player_next_episode_finding_source)
                    !autoPlaySourceName.isNullOrBlank() && autoPlayCountdownSec != null ->
                        stringResource(
                            Res.string.player_next_episode_playing_via_countdown,
                            autoPlaySourceName,
                            autoPlayCountdownSec,
                        )
                    else -> null
                }
                
                if (autoPlayStatus != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = autoPlayStatus,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}
