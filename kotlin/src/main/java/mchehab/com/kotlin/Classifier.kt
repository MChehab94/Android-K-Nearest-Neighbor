package mchehab.com.kotlin

import mchehab.com.kotlin.distanceAlgorithm.DistanceAlgorithm
import mchehab.com.kotlin.distanceAlgorithm.EuclideanDistance
import java.util.*

class Classifier {

    var K: Int = 0
    var splitRatio: Double = 0.toDouble()
    var accuracy = 0.0
        private set

    var distanceAlgorithm: DistanceAlgorithm? = null
    private val listDataPoint: MutableList<DataPoint>
    private val listTrainData: MutableList<DataPoint>
    private val listTestData: MutableList<DataPoint>
    private val listTestValidator: MutableList<DataPoint>

    init {
        K = 3
        splitRatio = 0.8
        distanceAlgorithm = EuclideanDistance()
        listDataPoint = ArrayList()
        listTrainData = ArrayList()
        listTestData = ArrayList()
        listTestValidator = ArrayList()
    }

    fun getListDataPoint(): List<DataPoint> {
        return listDataPoint
    }

    fun setListDataPoint(listDataPoint: List<DataPoint>) {
        this.listDataPoint.clear()
        this.listDataPoint.addAll(listDataPoint)
    }

    fun getListTrainData(): List<DataPoint> {
        return listTrainData
    }

    fun getListTestData(): List<DataPoint> {
        return listTestData
    }

    fun splitData() {
        listTestData.clear()
        listTrainData.clear()
        val trainSize = (listDataPoint.size * splitRatio).toInt()
        val testSize = listDataPoint.size - trainSize
        Collections.shuffle(listDataPoint)
        for (i in 0 until trainSize)
            listTrainData.add(listDataPoint[i])
        for (i in 0 until testSize) {
            val dataPointTest = DataPoint(listDataPoint[i + trainSize])
            val dataPointValidator = DataPoint(dataPointTest)
            dataPointTest.category = Category.TEST
            listTestData.add(dataPointTest)
            listTestValidator.add(dataPointValidator)
        }
    }

    private fun calculateDistances(point: DataPoint): MutableList<Double> {
        val listDistance = ArrayList<Double>()
        for (dataPoint in listTrainData) {
            val distance = distanceAlgorithm!!.calculateDistance(point.x, point.y, dataPoint.x, dataPoint.y)
            listDistance.add(distance)
        }
        return listDistance
    }

    private fun getMaxCategory(hashMap: HashMap<Category, Int>): Category? {
        val iterator = hashMap.entries.iterator()
        val maxCategory = Integer.MIN_VALUE
        var category: Category? = null
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.value > maxCategory) {
                category = item.key
            }
        }
        return category
    }

    private fun classifyDataPoint(point: DataPoint): Category? {
        val hashMap = HashMap<Category, Int>()
        val listDistance = calculateDistances(point)
        for (i in 0 until K) {
            var min = java.lang.Double.MAX_VALUE
            var minIndex = -1
            for (j in listDistance.indices) {
                if (listDistance[j] < min) {
                    min = listDistance[j]
                    minIndex = j
                }
            }
            val category = listTrainData[minIndex].category
            if (hashMap.containsKey(category)) {
                hashMap[category] = hashMap[category]!! + 1
            } else {
                hashMap[category] = 1
            }
            listDistance[minIndex] = java.lang.Double.MAX_VALUE
        }
        return getMaxCategory(hashMap)
    }

    fun classify() {
        accuracy = 0.0
        for (i in listTestData.indices) {
            val dataPoint = listTestData[i]
            val category = classifyDataPoint(dataPoint)
            if (isCorrect(category!!, listTestValidator[i].category))
                accuracy++
            dataPoint.category = category
        }
        accuracy /= listTestData.size.toDouble()
    }

    private fun isCorrect(predictedCategory: Category, trueCategory: Category): Boolean {
        return predictedCategory == trueCategory
    }

    fun reset() {
        listDataPoint.clear()
        listTestData.clear()
        listTrainData.clear()
    }
}