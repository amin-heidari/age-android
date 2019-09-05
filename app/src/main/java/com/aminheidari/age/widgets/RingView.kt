package com.aminheidari.age.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.aminheidari.age.R
import kotlin.properties.Delegates

class RingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

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
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RingView,
            0, 0).apply {

            try {
                strokeColor = getColor(R.styleable.RingView_strokeColor, 0)
                strokeWidth = getDimension(R.styleable.RingView_strokeColor, 0f)
                segmentsCount = getColor(R.styleable.RingView_strokeColor, 0)
            } finally {
                recycle()
            }
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

        canvas?.let { c ->
            strokePaint.color = strokeColor
            strokePaint.strokeWidth = strokeWidth
        }
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

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

    // endregion

    // ====================================================================================================
    // region Actions
    // ====================================================================================================

    // endregion

}