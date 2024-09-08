package com.zubrilka.zubrilkaenglish.utils.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.zubrilka.zubrilkaenglish.R
import com.zubrilka.zubrilkaenglish.utils.LOG
import kotlin.math.min

/**
 * кнопка для отображения значка рекламы содержит особую анимацию появления
 */
class CustAdButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var backgroundColor: Int

    private var borderColor: Int
    private var cornerRadius: Float
    private var borderWidth: Float

    private var reduction: Float = 1f
    private var reductionEnd: Float = 0.95f
    private val reduction3: Float = 1.2f

    private var customClickListener: OnClickListener? = null
    private var animator: ValueAnimator? = null
    private var appearanceAnimator: ValueAnimator? = null

    var drawable: Int = R.drawable.book_with_light_bulb_small
    private var bitmap: Bitmap? = null
    var imageSize: Int = 80 //предполагается что картинка квадратная
    var indentationBetweenEl: Int = 20

    private var paintBorder: Paint
    private var paintBackground: Paint
    private val paintImage = Paint()



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
                    drawable = getResourceId(R.styleable.CustomView_drawable, R.drawable.book_with_light_bulb_small)
                    imageSize = getDimensionPixelSize(R.styleable.CustomView_imageSize, 80)
                    indentationBetweenEl = getDimensionPixelSize(R.styleable.CustomView_indentationBetweenEl, 20)

                } finally {
                    recycle()
                }
            }

        // Установка собственного OnClickListener
        super.setOnClickListener {
            clickAnimation()
        }

        if (drawable!=0) bitmap = BitmapFactory.decodeResource(resources, drawable)


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
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //значения зазора между фигурой и краем канваса
        val widthGap = (width-(width-borderWidth)*reduction)/2
        val heightGap = (height-(height-borderWidth)*reduction)/2

        drawBackground(canvas,widthGap,heightGap)
        drawBorder(canvas,widthGap,heightGap)
        drawImage(canvas)
    }
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        appearanceAnimation()
    }

    private fun appearanceAnimation() {
        if (appearanceAnimator==null|| appearanceAnimator?.isStarted == false){
            appearanceAnimator = ValueAnimator.ofFloat(reduction, reduction3).apply {
                duration = 300
                interpolator = AccelerateDecelerateInterpolator()
                repeatMode = ValueAnimator.REVERSE
                repeatCount = 3
                addUpdateListener { animation ->
                    reduction = animation.animatedValue as Float
                    invalidate()
                }
                start()
            }
        }
    }

    private fun drawImage(canvas: Canvas) {
        if (isEnabled) paintImage.colorFilter = null
        else paintImage.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })

        val imageSizeEnd:Int = (imageSize*reduction).toInt()
        val destRect = Rect(
            (width/2-imageSizeEnd/2),
            (height/2-imageSizeEnd/2),
            (width/2-imageSizeEnd/2)+imageSizeEnd,
            (height/2-imageSizeEnd/2)+imageSizeEnd)
        if (bitmap!=null) canvas.drawBitmap(bitmap!!, null, destRect, paintImage)
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        customClickListener = listener
    }
    private fun clickAnimation() {
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
                        customClickListener?.onClick(this@CustAdButton)
                    }
                })
                start()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode: Int = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode: Int = MeasureSpec.getMode(heightMeasureSpec)


        val desiredWidth: Int = imageSize + paddingLeft + paddingRight
        val desiredHeight: Int = imageSize + paddingTop + paddingBottom

        val measuredWidth: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec)//точное задание размера из xml
            MeasureSpec.AT_MOST -> min((desiredWidth*reduction3).toInt(), MeasureSpec.getSize(widthMeasureSpec))//при wrap_content
            else -> desiredWidth
        }

        val measuredHeight: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(heightMeasureSpec)//точное задание размера из xml
            MeasureSpec.AT_MOST -> min((desiredHeight*reduction3).toInt(), MeasureSpec.getSize(heightMeasureSpec))//при wrap_content
            else -> desiredHeight
        }

        setMeasuredDimension(measuredWidth, measuredHeight)
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
}