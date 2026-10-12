package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldDark

@Composable
fun AlgiersMonumentSkyline(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val goldBrush = Brush.horizontalGradient(
            colors = listOf(
                GoldDark.copy(alpha = 0.2f),
                GoldAccent.copy(alpha = 0.7f),
                GoldAccent.copy(alpha = 0.9f),
                GoldAccent.copy(alpha = 0.7f),
                GoldDark.copy(alpha = 0.2f)
            )
        )

        val groundPath = Path().apply {
            moveTo(0f, h - 12f)
            lineTo(w, h - 12f)
        }
        drawPath(groundPath, brush = goldBrush, style = Stroke(width = 1.5f))

        // Center Monument (Maqam Echahid - 3 parabolic soaring leaves)
        val cx = w / 2f
        val monumentBaseY = h - 12f
        val monumentTopY = 8f

        // Left fin
        val leftFin = Path().apply {
            moveTo(cx - 36f, monumentBaseY)
            quadraticTo(cx - 24f, monumentBaseY - 30f, cx - 8f, monumentTopY)
            quadraticTo(cx - 16f, monumentBaseY - 24f, cx - 22f, monumentBaseY)
        }
        drawPath(leftFin, brush = goldBrush, style = Stroke(width = 1.8f))

        // Right fin
        val rightFin = Path().apply {
            moveTo(cx + 36f, monumentBaseY)
            quadraticTo(cx + 24f, monumentBaseY - 30f, cx + 8f, monumentTopY)
            quadraticTo(cx + 16f, monumentBaseY - 24f, cx + 22f, monumentBaseY)
        }
        drawPath(rightFin, brush = goldBrush, style = Stroke(width = 1.8f))

        // Center fin & turret
        val centerFin = Path().apply {
            moveTo(cx - 8f, monumentBaseY)
            lineTo(cx - 4f, monumentTopY + 4f)
            lineTo(cx + 4f, monumentTopY + 4f)
            lineTo(cx + 8f, monumentBaseY)
        }
        drawPath(centerFin, brush = goldBrush, style = Stroke(width = 1.2f))

        // Eternal flame dome
        drawCircle(
            color = GoldAccent.copy(alpha = 0.8f),
            radius = 3.5f,
            center = Offset(cx, monumentTopY - 2f)
        )

        // Palm trees on left and right
        val palmPositions = listOf(
            Offset(cx - 70f, monumentBaseY),
            Offset(cx - 110f, monumentBaseY),
            Offset(cx + 70f, monumentBaseY),
            Offset(cx + 110f, monumentBaseY)
        )
        palmPositions.forEach { pos ->
            val trunk = Path().apply {
                moveTo(pos.x, pos.y)
                quadraticTo(pos.x - 2f, pos.y - 12f, pos.x - 1f, pos.y - 22f)
            }
            drawPath(trunk, brush = goldBrush, style = Stroke(width = 1.5f))

            // Fronds
            val fronds = Path().apply {
                moveTo(pos.x - 1f, pos.y - 22f)
                quadraticTo(pos.x - 12f, pos.y - 26f, pos.x - 16f, pos.y - 20f)
                moveTo(pos.x - 1f, pos.y - 22f)
                quadraticTo(pos.x + 10f, pos.y - 26f, pos.x + 14f, pos.y - 20f)
                moveTo(pos.x - 1f, pos.y - 22f)
                quadraticTo(pos.x - 8f, pos.y - 30f, pos.x - 12f, pos.y - 28f)
                moveTo(pos.x - 1f, pos.y - 22f)
                quadraticTo(pos.x + 8f, pos.y - 30f, pos.x + 12f, pos.y - 28f)
            }
            drawPath(fronds, brush = goldBrush, style = Stroke(width = 1.2f))
        }

        // City skyline buildings left & right
        val cityLeft = Path().apply {
            moveTo(10f, monumentBaseY)
            lineTo(10f, monumentBaseY - 14f)
            lineTo(24f, monumentBaseY - 14f)
            lineTo(24f, monumentBaseY - 20f)
            lineTo(38f, monumentBaseY - 20f)
            lineTo(38f, monumentBaseY)
        }
        drawPath(cityLeft, brush = goldBrush, style = Stroke(width = 1.0f))

        val cityRight = Path().apply {
            moveTo(w - 10f, monumentBaseY)
            lineTo(w - 10f, monumentBaseY - 16f)
            lineTo(w - 26f, monumentBaseY - 16f)
            lineTo(w - 26f, monumentBaseY - 22f)
            lineTo(w - 42f, monumentBaseY - 22f)
            lineTo(w - 42f, monumentBaseY)
        }
        drawPath(cityRight, brush = goldBrush, style = Stroke(width = 1.0f))
    }
}
