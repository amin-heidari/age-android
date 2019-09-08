package com.aminheidari.age.widgets

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.aminheidari.age.R
import kotlin.properties.Delegates

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

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    init {
//        context.theme.obtainStyledAttributes(
//            attrs,
//            R.styleable.RingView,
//            0, 0).apply {
//
//            try {
//                strokeColor2 = getColor(R.styleable.RingView_strokeColor, 0)
////                val strokeWidthh = getDimension(R.styleable.RingView_strokeWidth, 0f)
////                val segmentsCountd = getInt(R.styleable.RingView_segmentsCount, 0)
//            } finally {
//                recycle()
//            }
//        }
    }

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
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            strokePaint.color = strokeColor//resources.getColor(R.color.accent)
            strokePaint.strokeWidth = strokeWidth

            drawOval(0f, 0f, 200f, 200f, strokePaint)
        }
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private var strokeColor2: Int = 174

    private var strokeColor: Int by Delegates.observable(0, { _, _, newValue ->
        invalidate()
        requestLayout()
    })

    private var strokeWidth: Float by Delegates.observable(0f, { _, _, newValue ->
        invalidate()
        requestLayout()
    })

    private var segmentsCount: Int by Delegates.observable(0, { _, _, newValue ->
        invalidate()
        requestLayout()
    })

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