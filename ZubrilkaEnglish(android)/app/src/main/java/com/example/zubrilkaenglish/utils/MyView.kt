package com.example.zubrilkaenglish.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.zubrilkaenglish.R

class MyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var borderColor: Int = Color.BLACK
    private var cornerRadius: Float = 20f
    private var borderWidth: Float = 10f

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0)
            .apply {
            try {
                borderColor = getColor(R.styleable.CustomView_borderColor, Color.BLACK)
                cornerRadius = getDimension(R.styleable.CustomView_cornerRadius, 20f)
                borderWidth = getDimension(R.styleable.CustomView_borderWidth, 10f)
            } finally {
                recycle()
            }
        }
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // Проверяем, что canvas не null
        if (canvas == null) return

        drawBorder(canvas)
        drawBackground(canvas)
    }

    private fun drawBackground(canvas: Canvas) {

    }

    private fun drawBorder(canvas: Canvas) {
        // Создаем объект Paint для настройки стиля рисования
        val paint = Paint().apply {
            color = Color.GRAY // Устанавливаем цвет
            style = Paint.Style.STROKE // Устанавливаем стиль рисования
            isAntiAlias = true // Включаем сглаживание
            strokeWidth = borderWidth// Устанавливаем ширину линии
        }

        // Задаем размеры и координаты прямоугольника
        val left = 0f+borderWidth/2
        val top = 0f+borderWidth/2
        val right = width.toFloat()-borderWidth/2
        val bottom = height.toFloat()-borderWidth/2
        val rectF = RectF(left, top, right, bottom)

        // Задаем радиус закругления углов
        val cornerRadius = 20f

        // Рисуем прямоугольник с закругленными краями
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)
    }
}