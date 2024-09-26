package  com.app.bmicalculator.utils.snackBar

import android.app.Activity
import android.graphics.Point
import android.view.Display

internal object DisplayCompat {

    private val IMPL: Impl

    internal abstract class Impl {
        internal abstract fun getSize(display: Display, outSize: Point)

        internal abstract fun getRealSize(display: Display, outSize: Point)
    }

    init {
        IMPL = DisplayCompatImplJBMR1()
    }

    fun getSize(display: Display, outSize: Point) {
        IMPL.getSize(display, outSize)
    }

    fun getRealSize(display: Display, outSize: Point) {
        IMPL.getRealSize(display, outSize)
    }

    fun getWidthFromPercentage(targetActivity: Activity, mMaxWidthPercentage: Float?): Int {
        val display = targetActivity.windowManager.defaultDisplay
        val dispSize = Point()
        getRealSize(display, dispSize)

        return (dispSize.x * mMaxWidthPercentage!!).toInt()
    }
}
