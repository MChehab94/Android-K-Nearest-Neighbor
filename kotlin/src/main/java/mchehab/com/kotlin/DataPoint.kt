package mchehab.com.kotlin

data class DataPoint(val x: Double, val y: Double, var category: Category) {
    constructor(dataPoint: DataPoint) : this(dataPoint.x, dataPoint.y, dataPoint.category)
}