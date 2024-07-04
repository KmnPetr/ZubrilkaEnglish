package com.example.zubrilkaenglish.utils.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.utils.LOG
import kotlin.math.min

open class CustButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var backgroundColor: Int

    private var borderColor: Int
    private var cornerRadius: Float
    private var borderWidth: Float

    private var paintText:TextPaint
    private var textColor: Int
    private var textSize: Float
    var text: String
    private lateinit var textView:TextView
    private var textStyle: Int
    private var fontFamily: String?
    // Переменные для измерения текста
    private val textBounds = Rect()

    private var reduction: Float = 1f
    private var reductionEnd: Float = 0.95f

    private var customClickListener: OnClickListener? = null
    private var animator: ValueAnimator? = null

    private var paintBorder:Paint
    private var paintBackground:Paint
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

                } finally {
                    recycle()
                }
                // Создаем объект Paint для настройки стиля рисования
                paintBorder = Paint().apply {
                    color = borderColor // Устанавливаем цвет
                    style = Paint.Style.STROKE // Устанавливаем стиль рисования
                    isAntiAlias = true // Включаем сглаживание
                    strokeWidth = borderWidth// Устанавливаем ширину линии
                }
                paintBackground = Paint().apply {
                    color = backgroundColor
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }
                paintText = TextPaint().apply {//при переиспользовании данного вью возникают проблемы из-за того что эта вещь сохраняет старые настройки
                    color = textColor
                    textSize = this@CustButton.textSize
//                    textAlign = Paint.Align.CENTER //используется отрисовке текста без разбиения на строки с использованием StaticLayout
                    isAntiAlias = true
                    typeface = Typeface.create(fontFamily, textStyle)
                    getTextBounds(text, 0, text.length, textBounds)
                    textAlign = Paint.Align.LEFT  // Используем Align.LEFT для многострочного текста
                }
            }

        // Установка собственного OnClickListener
        super.setOnClickListener {
            startAnimation()
        }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        customClickListener = listener
    }
    private fun startAnimation() {
        if (animator==null|| animator?.isStarted == false){
            animator = ValueAnimator.ofFloat(reduction, reductionEnd).apply {
                duration = 25
                interpolator = AccelerateDecelerateInterpolator()
                repeatMode = ValueAnimator.REVERSE
                repeatCount = 1
                addUpdateListener { animation ->
                    reduction = animation.animatedValue as Float
                    invalidate()
                }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        customClickListener?.onClick(this@CustButton)
                    }
                })
                start()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        paintText.getTextBounds(text, 0, text.length, textBounds)

        val textWidth0:Int = textBounds.width()// Определение ширины текста если бы он не делился на несколько строк
        val desiredWidth0:Int = textWidth0 + paddingLeft + paddingRight

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        // Определение доступной ширины
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        if (desiredWidth0<=widthSize){
            //если данной максимальной ширины достаточно на на написание этой строки,
            // то измеряем размеры view без разбиения текста на строки

            val desiredHeight0:Int = textBounds.height() + paddingTop + paddingBottom

            val measuredWidth = when (widthMode) {
                MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec)
                MeasureSpec.AT_MOST -> min(desiredWidth0, MeasureSpec.getSize(widthMeasureSpec))
                else -> desiredWidth0
            }

            val measuredHeight = when (heightMode) {
                MeasureSpec.EXACTLY -> MeasureSpec.getSize(heightMeasureSpec)
                MeasureSpec.AT_MOST -> min(desiredHeight0, MeasureSpec.getSize(heightMeasureSpec))
                else -> desiredHeight0
            }

            setMeasuredDimension(measuredWidth, measuredHeight)
        } else {
            //если максимальной ширины не достаточно то измеряем с учетом разбиения текста на строки

            // Создание StaticLayout для измерения текста
            val staticLayout = StaticLayout(text, paintText, widthSize, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false)

            // Учитывание padding
            val desiredWidth:Int = staticLayout.width + paddingLeft + paddingRight
            val desiredHeight:Int = staticLayout.height + paddingTop + paddingBottom

            val measuredWidth = when (widthMode) {
                MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec)
                MeasureSpec.AT_MOST -> minOf(desiredWidth,textWidth0, MeasureSpec.getSize(widthMeasureSpec))
                else -> desiredWidth
            }

            val measuredHeight = when (heightMode) {
                MeasureSpec.EXACTLY -> MeasureSpec.getSize(heightMeasureSpec)
                MeasureSpec.AT_MOST -> min(desiredHeight, MeasureSpec.getSize(heightMeasureSpec))
                else -> desiredHeight
            }

            setMeasuredDimension(measuredWidth, measuredHeight)
        }
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
        paintText.getTextBounds(text, 0, text.length, textBounds)

        if ((textBounds.width() + paddingLeft + paddingRight)<=canvas.width){
        //если бы текст умещался в предоставленную ширину канваса то рисуем его без попыток деления на строки


            paintText.textAlign = Paint.Align.CENTER
            // Compute the position to draw the text
            val xPos = width / 2f
            val yPos = (height / 2f - (paintText.descent() + paintText.ascent()) / 2)
            canvas.drawText(text, xPos, yPos, paintText)

        }else{
            paintText.textAlign = Paint.Align.LEFT
            // Ограничение ширины текста
            val textWidth = canvas.width - paddingLeft - paddingRight

            // Создание StaticLayout для многострочного текста
            val staticLayout = StaticLayout(text, paintText, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false)

            // Позиционирование текста
            val xPos = paddingLeft.toFloat()
            val yPos = (canvas.height/ 2 - (staticLayout.height / 2)).toFloat()


            canvas.save()// Сохранение состояния канвы
            canvas.translate(xPos, yPos)// Позиционирование канвы
            staticLayout.draw(canvas)// Рисование текста
            canvas.restore()// Восстановление состояния канвы
        }
    }

    private fun drawBackground(canvas: Canvas, widthGap: Float, heightGap: Float) {

        // Задаем размеры и координаты прямоугольника
        val left = 0f+widthGap
        val top = 0f+heightGap
        val right = width.toFloat()-widthGap
        val bottom = height.toFloat()-heightGap
        val rectF = RectF(left, top, right, bottom)


        // Рисуем прямоугольник с закругленными краями
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paintBackground)
    }

    private fun drawBorder(canvas: Canvas, widthGap: Float, heightGap: Float) {

        // Задаем размеры и координаты прямоугольника
        val left = 0f+widthGap
        val top = 0f+heightGap
        val right = width.toFloat()-widthGap
        val bottom = height.toFloat()-heightGap
        val rectF = RectF(left, top, right, bottom)


        // Рисуем прямоугольник с закругленными краями
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paintBorder)
    }
    override fun setBackgroundColor(color: Int) {
//        super.setBackgroundColor(color)
        backgroundColor = color
        paintBackground.color = backgroundColor
        invalidate()
    }
}