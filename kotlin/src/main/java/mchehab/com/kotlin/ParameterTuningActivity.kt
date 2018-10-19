package mchehab.com.kotlin

import android.app.Activity
import android.content.Intent
import kotlinx.android.synthetic.main.activity_parameter_tuning.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner

class ParameterTuningActivity : AppCompatActivity() {

    val MINKOWSKI = "Minkowski"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parameter_tuning)

        setSpinnerListener()
        setButtonTuneListener()
        setButtonCancelListener()
    }

    private fun setSpinnerListener() {
        spinnerDistance.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = spinnerDistance.selectedItem as String
                if (item.equals(MINKOWSKI, ignoreCase = true)) {
                    linearLayoutMinkowskiP.visibility = View.VISIBLE
                } else {
                    linearLayoutMinkowskiP.visibility = View.GONE
                    editTextMinkowski.clearFocus()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setButtonTuneListener() {
        buttonTune.setOnClickListener {
            if (!canTune()) {
                AlertDialog.Builder(this@ParameterTuningActivity)
                        .setTitle("Error")
                        .setMessage("Make sure to fill all the fields")
                        .setPositiveButton("Ok", null)
                        .create()
                        .show()
                return@setOnClickListener
            }

            val spinnerIndex = getSpinnerPosition(spinnerDistance)
            val K = Integer.parseInt(getText(editTextK))
            val splitRatio = java.lang.Double.parseDouble(getText(editTextSplitRatio))

            val bundle = Bundle()
            bundle.putInt(Constants.DISTANCE_ALGORITHM, spinnerIndex)
            bundle.putInt(Constants.K, K)
            bundle.putDouble(Constants.SPLITE_RATIO, splitRatio)

            if (spinnerIndex == 2) {
                val p = Integer.parseInt(getText(editTextMinkowski))
                bundle.putInt(Constants.MINKOWSKI_P, p)
            }

            val intent = Intent()
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun setButtonCancelListener() {
        buttonCancel.setOnClickListener { e ->
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun getSpinnerPosition(spinner: Spinner): Int {
        return spinner.selectedItemPosition
    }

    private fun getText(editText: EditText): String {
        return editText.text.toString().trim { it <= ' ' }
    }

    private fun isEmpty(editText: EditText): Boolean {
        return getText(editText).isEmpty()
    }

    private fun canTune(): Boolean {
        if (isEmpty(editTextSplitRatio))
            return false
        if (isEmpty(editTextK))
            return false
        if (getSpinnerPosition(spinnerDistance) == 2)
            if (isEmpty(editTextMinkowski))
                return false
        return true
    }
}
