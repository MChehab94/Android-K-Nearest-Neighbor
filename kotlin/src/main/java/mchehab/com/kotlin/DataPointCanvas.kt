package mchehab.com.kotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DataPointCanvas(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var paint: Paint? = null
    private var listDataPoints: List<DataPoint> = ArrayList()

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        setupPaint()
    }

    private fun setupPaint() {
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.strokeWidth = 30f
        paint!!.style = Paint.Style.STROKE
        paint!!.strokeCap = Paint.Cap.SQUARE
    }

    public fun setListDataPoints(list: List<DataPoint>){
        listDataPoints = list
    }

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (dataPoint in listDataPoints) {
            if (dataPoint.category == Category.RED)
                paint!!.color = Color.RED
            else if (dataPoint.category == Category.BLUE)
                paint!!.color = Color.BLUE
            else
                paint!!.color = Color.BLACK
            canvas.drawCircle(dataPoint.x.toFloat(), dataPoint.y.toFloat(), 5f, paint!!)
        }
    }
}