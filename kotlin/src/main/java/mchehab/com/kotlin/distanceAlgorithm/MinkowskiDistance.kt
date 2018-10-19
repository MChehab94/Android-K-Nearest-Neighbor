package mchehab.com.kotlin.distanceAlgorithm

import kotlin.math.pow

class MinkowskiDistance(var p: Int): DistanceAlgorithm {
    override fun calculateDistance(x1: Double, x2: Double, y1: Double, y2: Double): Double {
        val x = (x1 - x2).pow(p)
        val y = (y1 - y2).pow(p)
        val distance = (x1 + x2).pow(1.0/p)
        return distance
    }
}