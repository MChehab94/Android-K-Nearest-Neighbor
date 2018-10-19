package mchehab.com.knearestneighbor;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import mchehab.com.knearestneighbor.distanceAlgorithm.DistanceAlgorithm;
import mchehab.com.knearestneighbor.distanceAlgorithm.EuclideanDistance;
import mchehab.com.knearestneighbor.distanceAlgorithm.ManhattenDistance;
import mchehab.com.knearestneighbor.distanceAlgorithm.MinkowskiDistance;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 101;

    private DistanceAlgorithm[] distanceAlgorithms = {new EuclideanDistance(), new
            ManhattenDistance(), new MinkowskiDistance(0)};
    private Classifier classifier;
    private List<DataPoint> listDataPoint = new ArrayList<>();
    private List<DataPoint> listDataPointOriginal = new ArrayList<>();

    private DataPointCanvas dataPointCanvas;
    private Button buttonTune;
    private Button buttonPredict;
    private Button buttonReset;
    private TextView textViewAccuracy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        classifier = new Classifier();

        populateList();
        setupUI();
        setupCanvas();
        setButtonTuneListener();
        setButtonPredictListener();
        setButtonResetListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if (data == null)
                return;
            Bundle bundle = data.getExtras();
            if (bundle == null)
                return;
            int spinnerIndex = bundle.getInt(Constants.DISTANCE_ALGORITHM);
            int K = bundle.getInt(Constants.K);
            double splitRatio = bundle.getDouble(Constants.SPLITE_RATIO);

            DistanceAlgorithm distanceAlgorithm = distanceAlgorithms[spinnerIndex];
            if (distanceAlgorithm instanceof MinkowskiDistance){
                int p = bundle.getInt(Constants.MINKOWSKI_P);
                ((MinkowskiDistance)distanceAlgorithm).setP(p);
            }
            classifier.reset();
            classifier.setDistanceAlgorithm(distanceAlgorithms[spinnerIndex]);
            classifier.setK(K);
            classifier.setSplitRatio(splitRatio);
            classifier.setListDataPoint(listDataPoint);
            classifier.splitData();
            listDataPoint.clear();
            listDataPoint.addAll(classifier.getListTestData());
            listDataPoint.addAll(classifier.getListTrainData());
            dataPointCanvas.invalidate();
            buttonPredict.setEnabled(true);
        }
    }

    private void setupUI(){
        dataPointCanvas = findViewById(R.id.dataPointCanvas);
        buttonTune = findViewById(R.id.buttonTune);
        buttonPredict = findViewById(R.id.buttonPredict);
        buttonReset = findViewById(R.id.buttonReset);
        textViewAccuracy = findViewById(R.id.textViewAccuracy);
    }

    private void setupCanvas(){
        dataPointCanvas.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dataPointCanvas.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                dataPointCanvas.setListDataPoints(listDataPoint);
            }
        });
    }

    private void setButtonTuneListener(){
        buttonTune.setOnClickListener(e -> {
            Intent intent = new Intent(MainActivity.this, ParameterTuningActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    private void setButtonPredictListener(){
        buttonPredict.setOnClickListener(e -> {
            classifier.classify();
            textViewAccuracy.setText("Accuracy = " + classifier.getAccuracy());
            dataPointCanvas.invalidate();
        });
    }

    private void setButtonResetListener(){
        buttonReset.setOnClickListener(e -> {
            classifier.reset();
            listDataPoint.clear();
            listDataPoint.addAll(listDataPointOriginal);
            classifier.setListDataPoint(listDataPoint);
            dataPointCanvas.invalidate();
            buttonPredict.setEnabled(false);
            textViewAccuracy.setText("");
        });
    }

    private void populateList() {
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open
                    ("points.txt")));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] point = line.split(",");
                double x = Double.parseDouble(point[0]);
                double y = Double.parseDouble(point[1]);
                int category = Integer.parseInt(point[2]);
                DataPoint dataPoint = new DataPoint(x, y, Category.values()[category]);
                listDataPointOriginal.add(new DataPoint(dataPoint));
                listDataPoint.add(dataPoint);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}