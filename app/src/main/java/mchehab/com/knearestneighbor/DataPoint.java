package mchehab.com.knearestneighbor;

public class DataPoint {

    private double x;
    private double y;
    private Category category;

    public DataPoint(double x, double y, Category category){
        this.x = x;
        this.y = y;
        this.category = category;
    }

    public DataPoint(DataPoint dataPoint){
        this.x = dataPoint.getX();
        this.y = dataPoint.getY();
        this.category = dataPoint.getCategory();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}