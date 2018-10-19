package mchehab.com.kotlin.distanceAlgorithm

import kotlin.math.pow
import kotlin.math.sqrt

class EuclideanDistance: DistanceAlgorithm{
    override fun calculateDistance(x1: Double, x2: Double, y1: Double, y2: Double): Double{
        val xSquare = (x1 - x2).pow(2)
        val ySquare = (y1 - y2).pow(2)
        val distance = sqrt(xSquare + ySquare)
        return distance
    }
}