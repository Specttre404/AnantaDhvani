package com.lastwave.app.ui.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.lastwave.app.ui.theme.LocalLiquidGlass
import com.lastwave.app.ui.theme.liquidGlassChrome
import com.lastwave.app.ui.theme.liquidGlassContainerColor

private val HeaderShape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)

/**
 * The one container every major screen's header sits inside. Full-bleed
 * width with no side margins and no gap above it — it IS the top of the
 * screen, edge to edge, with only its bottom edge rounded.
 *
 * Feature enhancements:
 * - Animated HDR Ambient Bokeh Glow Orbs (Cyan + Magenta shifting endlessly).
 * - Multi-stop Gradient Title Brush (Cyan -> Rose -> Solar Gold) with Serif styling.
 * - Window-inset aware and liquid-glass container compatible.
 */
@Composable
fun ExpressiveHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bokeh_glow")
    val bokehShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "bokeh_shift",
    )

    val liquidGlass = LocalLiquidGlass.current
    Box(modifier = modifier.fillMaxWidth().zIndex(1f)) {
        Surface(
            shape = HeaderShape,
            color = liquidGlassContainerColor(MaterialTheme.colorScheme.surfaceContainer),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth().liquidGlassChrome(HeaderShape, liquidGlass),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp),
            ) {
                // Ambient HDR Bokeh Glow Orbs
                Canvas(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(HeaderShape),
                ) {
                    drawCircle(
                        color = Color(0xFF00F0FF).copy(alpha = 0.14f),
                        radius = size.minDimension * 0.85f,
                        center = Offset(
                            x = size.width * (0.15f + 0.2f * bokehShift),
                            y = size.height * 0.3f,
                        ),
                    )
                    drawCircle(
                        color = Color(0xFFFF2E93).copy(alpha = 0.12f),
                        radius = size.minDimension * 0.75f,
                        center = Offset(
                            x = size.width * (0.85f - 0.2f * bokehShift),
                            y = size.height * 0.7f,
                        ),
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (onBack != null) {
                            FilledTonalIconButton(
                                onClick = onBack,
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                ),
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                            Spacer(Modifier.width(12.dp))
                        }

                        Column(Modifier.weight(1f)) {
                            Text(
                                text = title,
                                style = (if (onBack != null) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineMedium).copy(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.2.sp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF00F0FF), // Vivid HDR Cyan
                                            Color(0xFFFF2E93), // Neon Rose Magenta
                                            Color(0xFFFFC700), // Radiant Solar Gold
                                        ),
                                    ),
                                ),
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis,
                            )
                            subtitle?.let {
                                Text(
                                    it,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        content = actions,
                    )
                }
            }
        }
    }
}

/** A trailing action icon for [ExpressiveHeader] — a glowing liquid-glass surface
 *  with gradient border and vivid cyan icon tinting. */
@Composable
fun HeaderActionIcon(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF101626).copy(alpha = 0.85f),
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFF00F0FF).copy(alpha = 0.55f), Color(0xFFFF2E93).copy(alpha = 0.45f)),
            ),
        ),
        modifier = Modifier.size(42.dp),
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color(0xFF00F0FF),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
