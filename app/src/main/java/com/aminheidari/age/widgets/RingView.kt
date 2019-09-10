package com.aminheidari.age.widgets

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.aminheidari.age.R
import com.aminheidari.age.utils.dpToPx
import com.aminheidari.age.utils.pxToDp
import kotlin.math.PI
import kotlin.math.min
import kotlin.properties.Delegates

/**
 * This custom view is only to be used with a 1:1 aspect ratio.
 * Other aspect ratios are not supported.
 */
class RingView : View {

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region API
    // ====================================================================================================

    var strokeColor: Int by Delegates.observable(0, { _, _, newValue ->
        invalidate()
        requestLayout()
    })

    var strokeWidth: Float by Delegates.observable(0f, { _, _, newValue ->
        invalidate()
        requestLayout()
    })

    var segmentsCount: Int by Delegates.observable(0, { _, _, newValue ->
        invalidate()
        requestLayout()
    })

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    constructor(context: Context) : super(context, null) {
        sharedInit()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        attrs?.let { attrSet ->
            sharedInit(context.theme.obtainStyledAttributes(attrSet, R.styleable.RingView, 0, 0))
        }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrs?.let { attrSet ->
            sharedInit(context.theme.obtainStyledAttributes(attrSet, R.styleable.RingView, defStyleAttr, 0))
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Paddings
        ovalRect = RectF(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            (width - paddingRight).toFloat(),
            (height- paddingBottom).toFloat()
        )

        // Stroke width inset.
        ovalRect.inset(strokeWidth / 2f, strokeWidth / 2f)

        // Paint.
        strokePaint.color = strokeColor
        strokePaint.strokeWidth = strokeWidth

        if (segmentsCount > 1) {
            val ellipseRadius = min(ovalRect.width(), ovalRect.height())
            val ellpiseCircumference = 2f * PI * ellipseRadius
            val onOffInterval = ellpiseCircumference.toFloat() / (2f * segmentsCount.toFloat()) / 2f
            strokePaint.pathEffect = DashPathEffect(floatArrayOf(onOffInterval, onOffInterval), 0f)
        } else {
            strokePaint.pathEffect = null
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawOval(ovalRect, strokePaint)
        }
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private var ovalRect: RectF = RectF(0f, 0f, 0f, 0f)

    // endregion

    // ====================================================================================================
    // region Methods
    // ====================================================================================================

    private fun sharedInit(typedArray: TypedArray? = null) {
        typedArray?.apply {
            strokeColor = getColor(R.styleable.RingView_strokeColor, 0)
            strokeWidth = getDimension(R.styleable.RingView_strokeWidth, 0f)
            segmentsCount = getInt(R.styleable.RingView_segmentsCount, 0)
        }
    }

    // endregion

    // ====================================================================================================
    // region Actions
    // ====================================================================================================

    // endregion

}