package com.app.bmicalculator.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import com.app.bmicalculator.R
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class HoloCircularProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = R.attr.circularProgressBarStyle
) : View(context, attrs, defStyle) {
    /**
     * The rectangle enclosing the circle.
     */
    private val mCircleBounds = RectF()

    /**
     * the rect for the thumb square
     */
    private val mSquareRect = RectF()

    /**
     * the paint for the background.
     */
    private var mBackgroundColorPaint = Paint()

    /**
     * The stroke width used to paint the circle.
     */
    var circleStrokeWidth: Int = 10
        private set

    /**
     * The gravity of the view. Where should the Circle be drawn within the given bounds
     *
     * [.computeInsets]
     */
    private var mGravity = Gravity.CENTER

    /**
     * The Horizontal inset calcualted in [.computeInsets] depends on [ ][.mGravity].
     */
    private var mHorizontalInset = 0

    /**
     * true if not all properties are set. then the view isn't drawn and there are no errors in the
     * LayoutEditor
     */
    private var mIsInitializing = true

    var isMarkerEnabled: Boolean = false

    var isThumbEnabled: Boolean = true

    /**
     * The Marker color paint.
     */
    private var mMarkerColorPaint: Paint? = null

    /**
     * The Marker progress.
     */
    private var mMarkerProgress = 0.0f

    /**
     * the overdraw is true if the progress is over 1.0.
     */
    private var mOverrdraw = false

    /**
     * The current progress.
     */
    private var mProgress = 0.3f

    /**
     * The color of the progress background.
     */
    private var mProgressBackgroundColor = 0

    /**
     * the color of the progress.
     */
    private var mProgressColor = 0

    /**
     * paint for the progress.
     */
    private var mProgressColorPaint: Paint? = null

    /**
     * Radius of the circle
     *
     *
     *  Note: (Re)calculated in [.onMeasure].
     */
    private var mRadius = 0f

    /**
     * The Thumb color paint.
     */
    private var mThumbColorPaint = Paint()

    /**
     * The Thumb pos x.
     *
     * Care. the position is not the position of the rotated thumb. The position is only calculated
     * in [.onMeasure]
     */
    private var mThumbPosX = 0f

    /**
     * The Thumb pos y.
     *
     * Care. the position is not the position of the rotated thumb. The position is only calculated
     * in [.onMeasure]
     */
    private var mThumbPosY = 0f

    /**
     * The pointer width (in pixels).
     */
    private var mThumbRadius = 20

    /**
     * The Translation offset x which gives us the ability to use our own coordinates system.
     */
    private var mTranslationOffsetX = 0f

    /**
     * The Translation offset y which gives us the ability to use our own coordinates system.
     */
    private var mTranslationOffsetY = 0f


    private var mVerticalInset = 0

    var markerProgress: Float
        get() = mMarkerProgress
        set(progress) {
            isMarkerEnabled = true
            mMarkerProgress = progress
        }

    var progress: Float
        get() = mProgress
        /**
         * Sets the progress.
         *
         * @param progress the new progress
         */
        set(progress) {
            if (progress == mProgress) {
                return
            }

            if (progress == 1f) {
                mOverrdraw = false
                mProgress = 1f
            } else {
                mOverrdraw = if (progress >= 1) {
                    true
                } else {
                    false
                }

                mProgress = progress % 1.0f
            }

            if (!mIsInitializing) {
                invalidate()
            }
        }

    var progressColor: Int
        /**
         * Gets the progress color.
         *
         * @return the progress color
         */
        get() = mProgressColor
        /**
         * Sets the progress color.
         *
         * @param color the new progress color
         */
        set(color) {
            mProgressColor = color

            updateProgressColor()
        }


    init {
        // load the styled attributes and set their properties
        val attributes = context.obtainStyledAttributes(
            attrs, R.styleable.HoloCircularProgressBar, defStyle, 0
        )
        try {
            progressColor = attributes.getColor(
                R.styleable.HoloCircularProgressBar_progress_color, Color.CYAN
            )
            setProgressBackgroundColor(
                attributes.getColor(
                    R.styleable.HoloCircularProgressBar_progress_background_color, Color.GREEN
                )
            )
            progress = attributes.getFloat(R.styleable.HoloCircularProgressBar_progress, 0.0f)
            markerProgress = attributes.getFloat(
                R.styleable.HoloCircularProgressBar_marker_progress, 0.0f
            )
            setWheelSize(
                attributes.getDimension(R.styleable.HoloCircularProgressBar_stroke_width, 10f)
                    .toInt()
            )
            isThumbEnabled =
                attributes.getBoolean(R.styleable.HoloCircularProgressBar_thumb_visible, true)
            isMarkerEnabled =
                attributes.getBoolean(R.styleable.HoloCircularProgressBar_marker_visible, true)

            mGravity = attributes.getInt(
                R.styleable.HoloCircularProgressBar_android_gravity, Gravity.CENTER
            )
        } finally {
            // make sure recycle is always called.
            attributes.recycle()
        }

        mThumbRadius = circleStrokeWidth * 2

        updateBackgroundColor()

        updateMarkerColor()

        updateProgressColor()

        // the view has now all properties and can be drawn
        mIsInitializing = false
    }

    override fun onDraw(canvas: Canvas) {
        // All of our positions are using our internal coordinate system.
        // Instead of translating
        // them we let Canvas do the work for us.

        canvas.translate(mTranslationOffsetX, mTranslationOffsetY)

        val progressRotation = currentRotation

        // draw the background
        if (!mOverrdraw) {
            canvas.drawArc(
                mCircleBounds, 270f, -(360 - progressRotation), false, mBackgroundColorPaint
            )
        }

        // draw the progress or a full circle if overdraw is true
        canvas.drawArc(
            mCircleBounds,
            270f,
            if (mOverrdraw) 360f else progressRotation,
            false,
            mProgressColorPaint!!
        )

        // draw the marker at the correct rotated position
        if (isMarkerEnabled) {
            val markerRotation = markerRotation

            canvas.save()
            canvas.rotate(markerRotation - 90)
            canvas.drawLine(
                (mThumbPosX + mThumbRadius / 2 * 1.4).toFloat(),
                mThumbPosY,
                (mThumbPosX - mThumbRadius / 2 * 1.4).toFloat(),
                mThumbPosY,
                mMarkerColorPaint!!
            )
            canvas.restore()
        }

        if (isThumbEnabled) {
            // draw the thumb square at the correct rotated position
            canvas.save()
            canvas.rotate(progressRotation - 90)
            // rotate the square by 45 degrees
            canvas.rotate(45f, mThumbPosX, mThumbPosY)
            mSquareRect.left = mThumbPosX - mThumbRadius / 3
            mSquareRect.right = mThumbPosX + mThumbRadius / 3
            mSquareRect.top = mThumbPosY - mThumbRadius / 3
            mSquareRect.bottom = mThumbPosY + mThumbRadius / 3
            canvas.drawRect(mSquareRect, mThumbColorPaint)
            canvas.restore()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = getDefaultSize(
            suggestedMinimumHeight + paddingTop + paddingBottom, heightMeasureSpec
        )
        val width = getDefaultSize(
            suggestedMinimumWidth + paddingLeft + paddingRight, widthMeasureSpec
        )

        val diameter: Int
        if (heightMeasureSpec == MeasureSpec.UNSPECIFIED) {
            // ScrollView
            diameter = width
            computeInsets(0, 0)
        } else if (widthMeasureSpec == MeasureSpec.UNSPECIFIED) {
            // HorizontalScrollView
            diameter = height
            computeInsets(0, 0)
        } else {
            // Default
            diameter = min(width.toDouble(), height.toDouble()).toInt()
            computeInsets(width - diameter, height - diameter)
        }

        setMeasuredDimension(diameter, diameter)

        val halfWidth = diameter * 0.5f

        // width of the drawed circle (+ the drawedThumb)
        val drawedWith = if (isThumbEnabled) {
            mThumbRadius * (5f / 6f)
        } else if (isMarkerEnabled) {
            circleStrokeWidth * 1.4f
        } else {
            circleStrokeWidth / 2f
        }

        // -0.5f for pixel perfect fit inside the viewbounds
        mRadius = halfWidth - drawedWith - 0.5f

        mCircleBounds[-mRadius, -mRadius, mRadius] = mRadius

        mThumbPosX = (mRadius * cos(0.0)).toFloat()
        mThumbPosY = (mRadius * sin(0.0)).toFloat()

        mTranslationOffsetX = halfWidth + mHorizontalInset
        mTranslationOffsetY = halfWidth + mVerticalInset
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            val bundle = state
            progress = bundle.getFloat(INSTANCE_STATE_PROGRESS)
            markerProgress = bundle.getFloat(INSTANCE_STATE_MARKER_PROGRESS)

            val progressColor = bundle.getInt(INSTANCE_STATE_PROGRESS_COLOR)
            if (progressColor != mProgressColor) {
                mProgressColor = progressColor
                updateProgressColor()
            }

            val progressBackgroundColor = bundle.getInt(INSTANCE_STATE_PROGRESS_BACKGROUND_COLOR)
            if (progressBackgroundColor != mProgressBackgroundColor) {
                mProgressBackgroundColor = progressBackgroundColor
                updateBackgroundColor()
            }

            isThumbEnabled = bundle.getBoolean(INSTANCE_STATE_THUMB_VISIBLE)

            isMarkerEnabled = bundle.getBoolean(INSTANCE_STATE_MARKER_VISIBLE)


            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE_SAVEDSTATE))
            return
        }

        super.onRestoreInstanceState(state)
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE_STATE_SAVEDSTATE, super.onSaveInstanceState())
        bundle.putFloat(INSTANCE_STATE_PROGRESS, mProgress)
        bundle.putFloat(INSTANCE_STATE_MARKER_PROGRESS, mMarkerProgress)
        bundle.putInt(INSTANCE_STATE_PROGRESS_COLOR, mProgressColor)
        bundle.putInt(INSTANCE_STATE_PROGRESS_BACKGROUND_COLOR, mProgressBackgroundColor)
        bundle.putBoolean(INSTANCE_STATE_THUMB_VISIBLE, isThumbEnabled)
        bundle.putBoolean(INSTANCE_STATE_MARKER_VISIBLE, isMarkerEnabled)
        return bundle
    }

    fun setProgressBackgroundColor(color: Int) {
        mProgressBackgroundColor = color

        updateMarkerColor()
        updateBackgroundColor()
    }

    /**
     * Sets the wheel size.
     *
     * @param dimension the new wheel size
     */
    fun setWheelSize(dimension: Int) {
        circleStrokeWidth = dimension

        // update the paints
        updateBackgroundColor()
        updateMarkerColor()
        updateProgressColor()
    }

    /**
     * Compute insets.
     *
     * <pre>
     * ______________________
     * |_________dx/2_________|
     * |......| /'''''\|......|
     * |-dx/2-|| View ||-dx/2-|
     * |______| \_____/|______|
     * |________ dx/2_________|
    </pre> *
     *
     * @param dx the dx the horizontal unfilled space
     * @param dy the dy the horizontal unfilled space
     */
    @SuppressLint("NewApi")
    private fun computeInsets(dx: Int, dy: Int) {
        val absoluteGravity: Int = Gravity.getAbsoluteGravity(mGravity, layoutDirection)

        mHorizontalInset = when (absoluteGravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
            Gravity.LEFT -> 0
            Gravity.RIGHT -> dx
            Gravity.CENTER_HORIZONTAL -> dx / 2
            else -> dx / 2
        }
        mVerticalInset = when (absoluteGravity and Gravity.VERTICAL_GRAVITY_MASK) {
            Gravity.TOP -> 0
            Gravity.BOTTOM -> dy
            Gravity.CENTER_VERTICAL -> dy / 2
            else -> dy / 2
        }
    }

    private val currentRotation: Float
        /**
         * Gets the current rotation.
         *
         * @return the current rotation
         */
        get() = 360 * mProgress

    private val markerRotation: Float
        /**
         * Gets the marker rotation.
         *
         * @return the marker rotation
         */
        get() = 360 * mMarkerProgress

    /**
     * updates the paint of the background
     */
    private fun updateBackgroundColor() {
        mBackgroundColorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBackgroundColorPaint.color = mProgressBackgroundColor
        mBackgroundColorPaint.style = Paint.Style.STROKE
        mBackgroundColorPaint.strokeWidth = circleStrokeWidth.toFloat()

        invalidate()
    }

    /**
     * updates the paint of the marker
     */
    private fun updateMarkerColor() {
        mMarkerColorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mMarkerColorPaint!!.color = mProgressBackgroundColor
        mMarkerColorPaint!!.style = Paint.Style.STROKE
        mMarkerColorPaint!!.strokeWidth = (circleStrokeWidth / 2).toFloat()

        invalidate()
    }

    /**
     * updates the paint of the progress and the thumb to give them a new visual style
     */
    private fun updateProgressColor() {
        mProgressColorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mProgressColorPaint!!.color = mProgressColor
        mProgressColorPaint!!.style = Paint.Style.STROKE
        mProgressColorPaint!!.strokeWidth = circleStrokeWidth.toFloat()

        mThumbColorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mThumbColorPaint.color = mProgressColor
        mThumbColorPaint.style = Paint.Style.FILL_AND_STROKE
        mThumbColorPaint.strokeWidth = circleStrokeWidth.toFloat()

        invalidate()
    }

    companion object {
        /**
         * used to save the super state on configuration change
         */
        private const val INSTANCE_STATE_SAVEDSTATE = "saved_state"

        /**
         * used to save the progress on configuration changes
         */
        private const val INSTANCE_STATE_PROGRESS = "progress"

        /**
         * used to save the marker progress on configuration changes
         */
        private const val INSTANCE_STATE_MARKER_PROGRESS = "marker_progress"

        /**
         * used to save the background color of the progress
         */
        private const val INSTANCE_STATE_PROGRESS_BACKGROUND_COLOR = "progress_background_color"

        /**
         * used to save the color of the progress
         */
        private const val INSTANCE_STATE_PROGRESS_COLOR = "progress_color"

        /**
         * used to save and restore the visibility of the thumb in this instance
         */
        private const val INSTANCE_STATE_THUMB_VISIBLE = "thumb_visible"

        /**
         * used to save and restore the visibility of the marker in this instance
         */
        private const val INSTANCE_STATE_MARKER_VISIBLE = "marker_visible"
    }
}
