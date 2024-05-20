package com.example.zubrilkaenglish.utils.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.example.zubrilkaenglish.R
import kotlin.math.min

class CustButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var backgroundColor: Int

    private var borderColor: Int
    private var cornerRadius: Float
    private var borderWidth: Float

    private var textColor: Int
    private var textSize: Float
    private var text: String
    private var textStyle: Int
    private var fontFamily: String?
    // Переменные для измерения текста
    private val textBounds = Rect()
    private val textPaint = Paint()

    private var reduction: Float = 1f
    private var reductionEnd: Float = 0.9f


    init {
        context
            .theme
            .obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0)
            .apply {
                try {
                    borderColor = getColor(R.styleable.CustomView_borderColor, Color.BLACK)
                    cornerRadius = getDimension(R.styleable.CustomView_cornerRadius, 20f)
                    borderWidth = getDimension(R.styleable.CustomView_borderWidth, 10f)
                    backgroundColor = getColor(R.styleable.CustomView_backgroundColor, Color.GREEN)
                    textColor = getColor(R.styleable.CustomView_textColor, Color.BLACK)
                    textSize = getDimension(R.styleable.CustomView_textSize, 40f)
                    text = getString(R.styleable.CustomView_text) ?: "BUTTON"
                    textStyle = getInt(R.styleable.CustomView_textStyle, Typeface.NORMAL)
                    fontFamily = getString(R.styleable.CustomView_fontFamily)

                    // Настройка Paint для измерения текста
                    textPaint.color = textColor
                    textPaint.textSize = textSize
                    textPaint.typeface = Typeface.create(fontFamily, textStyle)
                } finally {
                    recycle()
                }
                // Измеряем размер текста
                textPaint.getTextBounds(text, 0, text.length, textBounds)
            }
            setOnClickListener {
                startAnimation()
            }
    }

    private fun startAnimation() {
        val shrinkAnimator = ValueAnimator.ofFloat(reduction, reductionEnd).apply {
            duration = 100
            interpolator = AccelerateDecelerateInterpolator()
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 1
            addUpdateListener { animation ->
                reduction = animation.animatedValue as Float
                invalidate() // Перерисовываем View с новым радиусом
            }
            start()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val desiredWidth = textBounds.width() + paddingLeft + paddingRight
        val desiredHeight = textBounds.height() + paddingTop + paddingBottom

        val measuredWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec)
            MeasureSpec.AT_MOST -> min(desiredWidth, MeasureSpec.getSize(widthMeasureSpec))
            else -> desiredWidth
        }

        val measuredHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(heightMeasureSpec)
            MeasureSpec.AT_MOST -> min(desiredHeight, MeasureSpec.getSize(heightMeasureSpec))
            else -> desiredHeight
        }

        setMeasuredDimension(measuredWidth, measuredHeight)
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        //значения зазора между фигурой и краем канваса
        val widthGap = (width-(width-borderWidth)*reduction)/2
        val heightGap = (height-(height-borderWidth)*reduction)/2

        drawBackground(canvas,widthGap,heightGap)
        drawBorder(canvas,widthGap,heightGap)
        drawText(canvas)
    }
    private fun drawText(canvas: Canvas) {
        val paint = Paint().apply {
            color = textColor
            textSize = this@CustButton.textSize*reduction
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            typeface = Typeface.create(fontFamily, textStyle)
        }

        // Compute the position to draw the text
        val xPos = width / 2f
        val yPos = (height / 2f - (paint.descent() + paint.ascent()) / 2)

        canvas.drawText(text, xPos, yPos, paint)
    }

    private fun drawBackground(canvas: Canvas, widthGap: Float, heightGap: Float) {
        val paint = Paint().apply {
            color = backgroundColor
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        // Задаем размеры и координаты прямоугольника
        val left = 0f+widthGap
        val top = 0f+heightGap
        val right = width.toFloat()-widthGap
        val bottom = height.toFloat()-heightGap
        val rectF = RectF(left, top, right, bottom)


        // Рисуем прямоугольник с закругленными краями
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)
    }

    private fun drawBorder(canvas: Canvas, widthGap: Float, heightGap: Float) {
        // Создаем объект Paint для настройки стиля рисования
        val paint = Paint().apply {
            color = borderColor // Устанавливаем цвет
            style = Paint.Style.STROKE // Устанавливаем стиль рисования
            isAntiAlias = true // Включаем сглаживание
            strokeWidth = borderWidth// Устанавливаем ширину линии
        }

        // Задаем размеры и координаты прямоугольника
        val left = 0f+widthGap
        val top = 0f+heightGap
        val right = width.toFloat()-widthGap
        val bottom = height.toFloat()-heightGap
        val rectF = RectF(left, top, right, bottom)


        // Рисуем прямоугольник с закругленными краями
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)
    }
}