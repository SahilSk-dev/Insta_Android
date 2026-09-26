package com.example.ui.components

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * Custom 8-sided rounded polygon (octagon badge) matching the gaming profile avatar in the screenshot.
 */
val OctagonBadgeShape: Shape = GenericShape { size, _ ->
    val w = size.width
    val h = size.height
    val cut = w * 0.28f
    val radius = w * 0.08f

    // 8 sides with smooth rounded corners
    moveTo(cut + radius, 0f)
    lineTo(w - cut - radius, 0f)
    quadraticBezierTo(w - cut, 0f, w - cut * 0.7f, cut * 0.3f)
    lineTo(w - cut * 0.3f, cut * 0.7f)
    quadraticBezierTo(w, cut, w, cut + radius)
    lineTo(w, h - cut - radius)
    quadraticBezierTo(w, h - cut, w - cut * 0.3f, h - cut * 0.7f)
    lineTo(w - cut * 0.7f, h - cut * 0.3f)
    quadraticBezierTo(w - cut, h, w - cut - radius, h)
    lineTo(cut + radius, h)
    quadraticBezierTo(cut, h, cut * 0.7f, h - cut * 0.3f)
    lineTo(cut * 0.3f, h - cut * 0.7f)
    quadraticBezierTo(0f, h - cut, 0f, h - cut - radius)
    lineTo(0f, cut + radius)
    quadraticBezierTo(0f, cut, cut * 0.3f, cut * 0.7f)
    lineTo(cut * 0.7f, cut * 0.3f)
    quadraticBezierTo(cut, 0f, cut + radius, 0f)
    close()
}
