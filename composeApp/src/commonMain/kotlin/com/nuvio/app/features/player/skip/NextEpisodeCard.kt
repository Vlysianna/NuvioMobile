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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
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
        val maxCountdown = androidx.compose.runtime.remember { 
            androidx.compose.runtime.mutableStateOf(autoPlayCountdownSec?.toFloat() ?: 10f) 
        }
        androidx.compose.runtime.LaunchedEffect(autoPlayCountdownSec) {
            if (autoPlayCountdownSec != null && autoPlayCountdownSec > maxCountdown.value) {
                maxCountdown.value = autoPlayCountdownSec.toFloat()
            }
        }
        
        val targetProgress = if (autoPlayCountdownSec != null && maxCountdown.value > 0) {
            (1f - (autoPlayCountdownSec.toFloat() / maxCountdown.value)).coerceIn(0f, 1f)
        } else {
            if (isAutoPlaySearching) 1f else 0f
        }
        
        val animatedProgress by androidx.compose.animation.core.animateFloatAsState(
            targetValue = targetProgress,
            animationSpec = tween(1000, easing = androidx.compose.animation.core.LinearEasing)
        )

        val shape = RoundedCornerShape(24.dp)
        
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp, min = 200.dp)
                .clip(shape)
                .background(Color.White)
                .clickable { if (isPlayable) onPlayNext() }
        ) {
            // Progress Bar Background
            Box(modifier = Modifier.matchParentSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress)
                        .background(Color.Gray.copy(alpha = 0.25f))
                )
            }
            
            // Content
            Row(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(Res.string.player_next_episode).uppercase(),
                        color = Color.Black.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(
                            Res.string.compose_player_episode_title_format,
                            nextEpisode.season,
                            nextEpisode.episode,
                            nextEpisode.title,
                        ),
                        color = Color.Black,
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
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = autoPlayStatus,
                            color = Color.Black.copy(alpha = 0.7f),
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Play Icon
                if (isPlayable) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
