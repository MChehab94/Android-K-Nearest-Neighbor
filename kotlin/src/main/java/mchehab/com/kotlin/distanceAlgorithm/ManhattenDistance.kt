package mchehab.com.kotlin.distanceAlgorithm

import kotlin.math.abs

class ManhattenDistance: DistanceAlgorithm {
    override fun calculateDistance(x1: Double, x2: Double, y1: Double, y2: Double): Double {
        val x = abs(x1 - x2)
        val y = abs(y1 - y2)
        val distance = x + y
        return distance
    }
}