package mchehab.com.knearestneighbor.distanceAlgorithm;

public class ManhattenDistance implements DistanceAlgorithm {
    @Override
    public double calculateDistance(double x1, double y1, double x2, double y2) {
        double x = Math.abs(x1 - x2);
        double y = Math.abs(y1 - y2);
        double distance = x + y;
        return distance;
    }
}