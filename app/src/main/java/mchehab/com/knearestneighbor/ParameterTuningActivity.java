package mchehab.com.knearestneighbor;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class ParameterTuningActivity extends AppCompatActivity {

    private final String MINKOWSKI = "Minkowski";

    private EditText editTextK;
    private EditText editTextSplitRatio;
    private Spinner spinnerDistance;
    private EditText editTextMinkowski;
    private Button buttonTune;
    private Button buttonCancel;

    private LinearLayout linearLayoutMinkowskiP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter_tuning);

        setupUI();
        setSpinnerListener();
        setButtonTuneListener();
        setButtonCancelListener();
    }

    private void setupUI(){
        editTextK = findViewById(R.id.editTextK);
        editTextSplitRatio = findViewById(R.id.editTextSplitRatio);
        spinnerDistance = findViewById(R.id.spinnerDistance);
        editTextMinkowski = findViewById(R.id.editTextMinkowski);
        linearLayoutMinkowskiP = findViewById(R.id.linearLayoutMinkowskiP);
        buttonTune = findViewById(R.id.buttonTune);
        buttonCancel = findViewById(R.id.buttonCancel);
    }

    private void setSpinnerListener() {
        spinnerDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)spinnerDistance.getSelectedItem();
                if(item.equalsIgnoreCase(MINKOWSKI)){
                    linearLayoutMinkowskiP.setVisibility(View.VISIBLE);
                }else{
                    linearLayoutMinkowskiP.setVisibility(View.GONE);
                    editTextMinkowski.clearFocus();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void setButtonTuneListener(){
        buttonTune.setOnClickListener(e -> {
            if (!canTune()){
                new AlertDialog.Builder(ParameterTuningActivity.this)
                        .setTitle("Error")
                        .setMessage("Make sure to fill all the fields")
                        .setPositiveButton("Ok", null)
                        .create()
                        .show();
                return;
            }

            int spinnerIndex = getSpinnerPosition(spinnerDistance);
            int K = Integer.parseInt(getText(editTextK));
            double splitRatio = Double.parseDouble(getText(editTextSplitRatio));

            Bundle bundle = new Bundle();
            bundle.putInt(Constants.DISTANCE_ALGORITHM, spinnerIndex);
            bundle.putInt(Constants.K, K);
            bundle.putDouble(Constants.SPLITE_RATIO, splitRatio);

            if (spinnerIndex == 2){
                int p = Integer.parseInt(getText(editTextMinkowski));
                bundle.putInt(Constants.MINKOWSKI_P, p);
            }

            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void setButtonCancelListener(){
        buttonCancel.setOnClickListener(e -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private int getSpinnerPosition(Spinner spinner){
        return spinner.getSelectedItemPosition();
    }

    private String getText(EditText editText){
        return editText.getText().toString().trim();
    }

    private boolean isEmpty(EditText editText){
        return getText(editText).length() == 0;
    }

    private boolean canTune(){
        if (isEmpty(editTextSplitRatio))
            return false;
        if (isEmpty(editTextK))
            return false;
        if (getSpinnerPosition(spinnerDistance) == 2)
            if (isEmpty(editTextMinkowski))
                return false;
        return true;
    }
}