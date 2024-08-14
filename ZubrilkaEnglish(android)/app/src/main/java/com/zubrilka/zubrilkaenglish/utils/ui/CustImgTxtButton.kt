package com.zubrilka.zubrilkaenglish.utils.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.zubrilka.zubrilkaenglish.R
import kotlin.math.max
import kotlin.math.min

class CustImgTxtButton @JvmOverloads constructor(
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
    // Переменная для измерения текста
    private val textBounds = Rect()

    private var reduction: Float = 1f
    private var reductionEnd: Float = 0.95f

    private var customClickListener: OnClickListener? = null
    private var animator: ValueAnimator? = null

    var drawable: Int = R.drawable.book_with_light_bulb_small
    private var bitmap: Bitmap? = null
    var imageSize: Int = 80 //предполагается что картинка квадратная
    var indentationBetweenEl: Int = 20
    var imageOrientation: Int = ImageOrientation.LEFT.ordinal

    private var paintBorder:Paint
    private var paintBackground:Paint
    private var paintText:Paint

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
                    drawable = getResourceId(R.styleable.CustomView_drawable, R.drawable.book_with_light_bulb_small)
                    imageSize = getDimensionPixelSize(R.styleable.CustomView_imageSize, 80)
                    indentationBetweenEl = getDimensionPixelSize(R.styleable.CustomView_indentationBetweenEl, 20)
                    imageOrientation = getInt(R.styleable.CustomView_imageOrientation, ImageOrientation.LEFT.ordinal)

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
                paintText = Paint().apply {
                    color = textColor
                    textSize = this@CustImgTxtButton.textSize
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                    typeface = Typeface.create(fontFamily, textStyle)
                    getTextBounds(text, 0, text.length, textBounds)
                }
            }

        // Установка собственного OnClickListener
        super.setOnClickListener {
            startAnimation()
        }

        if (drawable!=0) bitmap = BitmapFactory.decodeResource(resources, drawable)
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        //значения зазора между фигурой и краем канваса
        val widthGap = (width-(width-borderWidth)*reduction)/2
        val heightGap = (height-(height-borderWidth)*reduction)/2

        drawBackground(canvas,widthGap,heightGap)
        drawBorder(canvas,widthGap,heightGap)

        //вычислим общую длину двух элементов картинки и текста
        val imageSizeEnd:Int = (imageSize*reduction).toInt()
        val textSiseEnd:Int = (textBounds.width()*reduction).toInt()
        val fullSize = imageSizeEnd+indentationBetweenEl+textSiseEnd
        val imageOffset = fullSize/2 - imageSizeEnd/2
        val textOffset = fullSize/2 - textSiseEnd/2

        drawImage(canvas,imageOffset,imageSizeEnd)
        drawText(canvas,textOffset)

    }

    private fun drawImage(canvas: Canvas, imageOffset: Int, imageSizeEnd: Int) {
        var left:Int = 0
        var right:Int = 0
        //определим положение картинки относительно текста
        if (imageOrientation==ImageOrientation.LEFT.ordinal){
            left = (width/2-imageSizeEnd/2)-imageOffset
            right = (width/2-imageSizeEnd/2)+imageSizeEnd-imageOffset
        } else if (imageOrientation==ImageOrientation.RIGHT.ordinal){
            left = (width/2-imageSizeEnd/2)+imageOffset
            right = (width/2-imageSizeEnd/2)+imageSizeEnd+imageOffset
        }

        val destRect = Rect(
            left,
            (height/2-imageSizeEnd/2),
            right,
            (height/2-imageSizeEnd/2)+imageSizeEnd)
        if (bitmap!=null) canvas.drawBitmap(bitmap!!, null, destRect, null)
    }
    private fun drawText(canvas: Canvas, textOffset: Int) {

        paintText.textSize = this@CustImgTxtButton.textSize*reduction
        // Compute the position to draw the text
        var xPos:Float = 0f
        //определим положение картинки относительно текста
        if (imageOrientation==ImageOrientation.LEFT.ordinal){
            xPos = width / 2f+textOffset
        }else if (imageOrientation==ImageOrientation.RIGHT.ordinal){
            xPos = width / 2f-textOffset
        }

        val yPos = (height / 2f - (paintText.descent() + paintText.ascent()) / 2)

        canvas.drawText(text, xPos, yPos, paintText)
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
                        customClickListener?.onClick(this@CustImgTxtButton)
                    }
                })
                start()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode: Int = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode: Int = MeasureSpec.getMode(heightMeasureSpec)


        val desiredWidth: Int = textBounds.width() + paddingLeft + paddingRight + imageSize
        val desiredImageHeight: Int = imageSize + paddingTop + paddingBottom
        val desiredTextHeight: Int = textBounds.height() + paddingTop + paddingBottom

        val measuredWidth: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec)//точное задание размера из xml
            MeasureSpec.AT_MOST -> min(desiredWidth, MeasureSpec.getSize(widthMeasureSpec))//при wrap_content
            else -> desiredWidth
        }

        val measuredHeight: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(heightMeasureSpec)//точное задание размера из xml
            MeasureSpec.AT_MOST -> min(max(desiredImageHeight,desiredTextHeight), MeasureSpec.getSize(heightMeasureSpec))//при wrap_content
            else -> max(desiredImageHeight,desiredTextHeight)
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

    /**
     * показывает положение картинки относительно текста
     */
    enum class ImageOrientation{
        LEFT,
        RIGHT;
    }
}
