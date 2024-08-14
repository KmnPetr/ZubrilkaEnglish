package com.zubrilka.zubrilkaenglish.utils.ui

import android.animation.AnimatorSet
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
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.zubrilka.zubrilkaenglish.R
import kotlin.math.min

open class HealthStrip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var health = 100
    private var addBandHealth = 100 //здоровье для дополнительной полосы используемой в анимации
    private var reflect:Boolean = false

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

    private var animator: ValueAnimator? = null
    private var addAnimator: ValueAnimator? = null

    private var paintBorder:Paint
    private var paintBackground:Paint
    private var paintAddBandBack:Paint
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
                    reflect = getBoolean(R.styleable.CustomView_reflect, false)

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
                paintAddBandBack = Paint().apply {
                    color = Color.parseColor("#AAAAAA")
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }
                paintText = TextPaint().apply {//при переиспользовании данного вью возникают проблемы из-за того что эта вещь сохраняет старые настройки
                    color = textColor
                    textSize = this@HealthStrip.textSize
//                    textAlign = Paint.Align.CENTER //используется отрисовке текста без разбиения на строки с использованием StaticLayout
                    isAntiAlias = true
                    typeface = Typeface.create(fontFamily, textStyle)
                    getTextBounds(text, 0, text.length, textBounds)
                    textAlign = Paint.Align.LEFT  // Используем Align.LEFT для многострочного текста
                }
            }
    }

    fun setHealth(newHealth: Int) {
        if ((newHealth-health)>0){
            startAnimation(newHealth,"up")
        } else if ((newHealth-health)<0){
            startAnimation(newHealth,"down")
        }

    }
    private fun startAnimation(newHealth:Int,direction:String) {
        if (true/*animator==null|| animator?.isStarted == false*/){

            animator = ValueAnimator.ofInt(health, newHealth).apply {
                duration = 300
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener { animation ->
                    health = animation.animatedValue as Int
                    invalidate()
                }
            }
            addAnimator = ValueAnimator.ofInt(addBandHealth, newHealth).apply {
                startDelay = 100
                duration = 400
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener { animation ->
                    addBandHealth = animation.animatedValue as Int
                    invalidate()
                }
            }

            if (direction=="down"){ //по направлению вниз
                animator = ValueAnimator.ofInt(health, newHealth).apply {
                    duration = 300
                    interpolator = AccelerateDecelerateInterpolator()
                    addUpdateListener { animation ->
                        health = animation.animatedValue as Int
                        invalidate()
                    }
                }
                addAnimator = ValueAnimator.ofInt(addBandHealth, newHealth).apply {
                    startDelay = 100
                    duration = 400
                    interpolator = AccelerateDecelerateInterpolator()
                    addUpdateListener { animation ->
                        addBandHealth = animation.animatedValue as Int
                        invalidate()
                    }
                }
                paintAddBandBack.color = Color.parseColor("#AAAAAA")


                val animatorSet = AnimatorSet()
                animatorSet.playTogether(animator, addAnimator)
                animatorSet.start()
//                health = newHealth
            }else if (direction=="up"){ //по направлению нарастания
                addAnimator = ValueAnimator.ofInt(addBandHealth, newHealth).apply {
                    duration = 300
                    interpolator = AccelerateDecelerateInterpolator()
                    addUpdateListener { animation ->
                        addBandHealth = animation.animatedValue as Int
                        invalidate()
                    }
                }
                animator = ValueAnimator.ofInt(health, newHealth).apply {
                    startDelay = 100
                    duration = 400
                    interpolator = AccelerateDecelerateInterpolator()
                    addUpdateListener { animation ->
                        health = animation.animatedValue as Int
                        invalidate()
                    }
                }
                paintAddBandBack.color = Color.parseColor("#AAAAAA")


                val animatorSet = AnimatorSet()
                animatorSet.playTogether(addAnimator, animator)
                animatorSet.start()
                paintAddBandBack.color = Color.GREEN

//                addBandHealth = newHealth
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
        val widthGap = (width-(width-borderWidth))/2
        val heightGap = (height-(height-borderWidth))/2

        drawAddBand(canvas,widthGap,heightGap)
        drawHealthStrip(canvas,widthGap,heightGap)
        drawBorder(canvas,widthGap,heightGap)
//        drawText(canvas)
    }

    /**
     * отрисует дополнительную полосу при изменении значения здоровья
     */
    private fun drawAddBand(canvas: Canvas, widthGap: Float, heightGap: Float) {
        // Задаем размеры и координаты прямоугольника
        var left = 0f+widthGap
        val top = height/2+heightGap
        var right = width.toFloat()*addBandHealth/100-widthGap
        val bottom = height.toFloat()-heightGap

        if (reflect){ //отражение слева направо
            left = width-left
            right = width-right
        }

        val rectF = RectF(left, top, right, bottom)

        // Рисуем прямоугольник с закругленными краями
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paintAddBandBack)

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

    /**
     * отрисует фон полоски здоровья
     */
    private fun drawHealthStrip(canvas: Canvas, widthGap: Float, heightGap: Float) {

        // Задаем размеры и координаты прямоугольника
        var left = 0f+widthGap
        val top = height/2+heightGap
        var right = width.toFloat()*health/100-widthGap
        val bottom = height.toFloat()-heightGap

        if (reflect){ //отражение слева направо
            left = width-left
            right = width-right
        }

        val rectF = RectF(left, top, right, bottom)

        paintBackground.color = getHealthColor(health)

        // Рисуем прямоугольник с закругленными краями
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paintBackground)
    }

    private fun drawBorder(canvas: Canvas, widthGap: Float, heightGap: Float) {

        // Задаем размеры и координаты прямоугольника
        val left = 0f+widthGap
        val top = height/2+heightGap
        val right = width.toFloat()-widthGap
        val bottom = height.toFloat()-heightGap
        val rectF = RectF(left, top, right, bottom)


        // Рисуем прямоугольник с закругленными краями
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paintBorder)
    }
    /*override fun setBackgroundColor(color: Int) {
//        super.setBackgroundColor(color)
        backgroundColor = color
        paintBackground.color = backgroundColor
        invalidate()
    }*/


    fun interpolateColor(colorStart: Int, colorEnd: Int, factor: Float): Int {
        val startA = Color.alpha(colorStart)
        val startR = Color.red(colorStart)
        val startG = Color.green(colorStart)
        val startB = Color.blue(colorStart)

        val endA = Color.alpha(colorEnd)
        val endR = Color.red(colorEnd)
        val endG = Color.green(colorEnd)
        val endB = Color.blue(colorEnd)

        val a = (startA + factor * (endA - startA)).toInt()
        val r = (startR + factor * (endR - startR)).toInt()
        val g = (startG + factor * (endG - startG)).toInt()
        val b = (startB + factor * (endB - startB)).toInt()

        return Color.argb(a, r, g, b)
    }

    fun getHealthColor(health: Int): Int {
        return when {
            health <= 50 -> {
                // Interpolate between red and yellow
                val factor = health / 50f
                interpolateColor(Color.RED, Color.YELLOW, factor)
            }
            else -> {
                // Interpolate between yellow and green
                val factor = (health - 50) / 50f
                interpolateColor(Color.YELLOW, Color.GREEN, factor)
            }
        }
    }

}