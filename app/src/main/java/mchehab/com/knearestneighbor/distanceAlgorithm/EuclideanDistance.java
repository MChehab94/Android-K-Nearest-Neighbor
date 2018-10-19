package mchehab.com.knearestneighbor.distanceAlgorithm;

public class EuclideanDistance implements DistanceAlgorithm {
    @Override
    public double calculateDistance(double x1, double y1, double x2, double y2) {
        double xSquare = Math.pow(x1 - x2, 2);
        double ySquare = Math.pow(y1 - y2, 2);
        double distance = Math.sqrt(xSquare + ySquare);
        return distance;
    }
}
