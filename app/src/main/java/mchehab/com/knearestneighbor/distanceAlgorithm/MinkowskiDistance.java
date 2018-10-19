package mchehab.com.knearestneighbor.distanceAlgorithm;

public class MinkowskiDistance implements DistanceAlgorithm {

    private int p;

    public MinkowskiDistance(int p){
        this.p = p;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    @Override
    public double calculateDistance(double x1, double y1, double x2, double y2) {
        double x = Math.pow(x1 - x2, p);
        double y = Math.pow(y1 - y2, p);
        double distance = Math.pow(x + y, 1/p);
        return distance;
    }
}