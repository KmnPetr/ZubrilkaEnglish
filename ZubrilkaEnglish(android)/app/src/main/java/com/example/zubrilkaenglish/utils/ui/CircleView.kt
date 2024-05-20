package com.example.zubrilkaenglish.utils.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class CircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
    }

    private var radius: Float = 0f
    private var initialRadius: Float = 0f

    init {
        setOnClickListener {
            startShrinkAndExpandAnimation()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initialRadius = Math.min(w, h) / 2f
        radius = initialRadius
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }

    private fun startShrinkAndExpandAnimation() {
        val shrinkAnimator = ValueAnimator.ofFloat(initialRadius, initialRadius / 2).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 1
            addUpdateListener { animation ->
                radius = animation.animatedValue as Float
                invalidate() // Перерисовываем View с новым радиусом
            }
            start()
        }
    }
}