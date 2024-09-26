package  com.app.bmicalculator.utils.snackBar

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

open class SnackbarLayout : LinearLayout {
    private var mMaxWidth = Integer.MAX_VALUE
    private var mMaxHeight = Integer.MAX_VALUE

    constructor(context: Context) : super(context)

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : super(
        context,
        attrs,
        defStyle
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        // Adjust width as necessary
        val width = MeasureSpec.getSize(widthMeasureSpec)
        if (mMaxWidth < width) {
            val mode = MeasureSpec.getMode(widthMeasureSpec)
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, mode)
        }
        // Adjust height as necessary
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (mMaxHeight < height) {
            val mode = MeasureSpec.getMode(heightMeasureSpec)
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, mode)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun setMaxWidth(maxWidth: Int) {
        mMaxWidth = maxWidth
        requestLayout()
    }

    fun setMaxHeight(maxHeight: Int) {
        mMaxHeight = maxHeight
        requestLayout()
    }

}
