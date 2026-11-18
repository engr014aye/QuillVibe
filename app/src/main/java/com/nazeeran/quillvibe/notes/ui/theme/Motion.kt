package com.nazeeran.quillvibe.notes.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring

object QuillMotion {
    /**
     * Smooth spring-physics animation spec mandated for adding, pinning, reordering, and deleting items.
     */
    val springPhysics: SpringSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    fun <T> springPhysicsSpec(): SpringSpec<T> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
}
