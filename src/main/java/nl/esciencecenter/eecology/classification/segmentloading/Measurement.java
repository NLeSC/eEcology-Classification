package nl.esciencecenter.eecology.classification.segmentloading;

public class Measurement {

    private double x;
    private double y;
    private double z;
    private double gpsSpeed;
    private int index;
    private boolean hasIndex = false;

    public Measurement() {
        super();
    }

    public Measurement(IndependentMeasurement independentMeasurement) {
        setX(independentMeasurement.getX());
        setY(independentMeasurement.getY());
        setZ(independentMeasurement.getZ());
        setGpsSpeed(independentMeasurement.getGpsSpeed());
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

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getGpsSpeed() {
        return gpsSpeed;
    }

    public void setGpsSpeed(double gpsSpeed) {
        this.gpsSpeed = gpsSpeed;
    }

    public int getIndex() {
        return index;
    }

    public boolean hasIndex() {
        return hasIndex;
    }

    public void setIndex(int index) {
        this.index = index;
        hasIndex = true;
    }

}