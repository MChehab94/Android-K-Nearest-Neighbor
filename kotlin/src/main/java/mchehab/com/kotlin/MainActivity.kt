package mchehab.com.kotlin

import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewTreeObserver
import mchehab.com.kotlin.distanceAlgorithm.EuclideanDistance
import mchehab.com.kotlin.distanceAlgorithm.ManhattenDistance
import mchehab.com.kotlin.distanceAlgorithm.MinkowskiDistance
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 101

    private val distanceAlgorithms = arrayOf(EuclideanDistance(), ManhattenDistance(), MinkowskiDistance(0))
    private var classifier = Classifier()
    private val listDataPoint = ArrayList<DataPoint>()
    private val listDataPointOriginal = ArrayList<DataPoint>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        classifier = Classifier()

        populateList()
        setupCanvas()
        setButtonTuneListener()
        setButtonPredictListener()
        setButtonResetListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null)
                return
            val bundle = data.extras ?: return
            val spinnerIndex = bundle.getInt(Constants.DISTANCE_ALGORITHM)
            val K = bundle.getInt(Constants.K)
            val splitRatio = bundle.getDouble(Constants.SPLITE_RATIO)

            val distanceAlgorithm = distanceAlgorithms[spinnerIndex]
            if (distanceAlgorithm is MinkowskiDistance) {
                val p = bundle.getInt(Constants.MINKOWSKI_P)
                distanceAlgorithm.p = p
            }
            classifier.reset()
            classifier.distanceAlgorithm = distanceAlgorithms[spinnerIndex]
            classifier.K = K
            classifier.splitRatio = splitRatio
            classifier.setListDataPoint(listDataPoint)
            classifier.splitData()
            listDataPoint.clear()
            listDataPoint.addAll(classifier.getListTestData())
            listDataPoint.addAll(classifier.getListTrainData())
            dataPointCanvas.invalidate()
            buttonPredict!!.isEnabled = true
        }
    }

    private fun setupCanvas() {
        dataPointCanvas.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                dataPointCanvas.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                dataPointCanvas.setListDataPoints(listDataPoint)
            }
        })
    }

    private fun setButtonTuneListener() {
        buttonTune!!.setOnClickListener { e ->
            val intent = Intent(this@MainActivity, ParameterTuningActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    private fun setButtonPredictListener() {
        buttonPredict!!.setOnClickListener { e ->
            classifier.classify()
            textViewAccuracy!!.text = "Accuracy = " + classifier.accuracy
            dataPointCanvas.invalidate()
        }
    }

    private fun setButtonResetListener() {
        buttonReset!!.setOnClickListener { e ->
            classifier.reset()
            listDataPoint.clear()
            listDataPoint.addAll(listDataPointOriginal)
            classifier.setListDataPoint(listDataPoint)
            dataPointCanvas.invalidate()
            buttonPredict!!.isEnabled = false
            textViewAccuracy!!.text = ""
        }
    }

    private fun populateList() {
        try {
            assets.open("points.txt").bufferedReader().useLines {
                lines -> lines.forEach {
                    val point = it.split(',')
                    val x = java.lang.Double.parseDouble(point[0])
                    val y = java.lang.Double.parseDouble(point[1])
                    val category = Integer.parseInt(point[2])
                    val dataPoint = DataPoint(x, y, Category.values()[category])
                    listDataPointOriginal.add(DataPoint(dataPoint))
                    listDataPoint.add(dataPoint)
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}