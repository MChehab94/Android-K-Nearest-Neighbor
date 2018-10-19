package mchehab.com.knearestneighbor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DataPointCanvas extends View {

    private Paint paint;
    private List<DataPoint> listDataPoints = new ArrayList<>();

    public DataPointCanvas(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    private void setupPaint(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.SQUARE);
    }

    public void setListDataPoints(List<DataPoint> listDataPoints){
        this.listDataPoints = listDataPoints;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(DataPoint dataPoint : listDataPoints){
            if (dataPoint.getCategory() == Category.RED) paint.setColor(Color.RED);
            else if (dataPoint.getCategory() == Category.BLUE) paint.setColor(Color.BLUE);
            else paint.setColor(Color.BLACK);
            canvas.drawCircle((float)dataPoint.getX(), (float)dataPoint.getY(), 5, paint);
        }
    }
}