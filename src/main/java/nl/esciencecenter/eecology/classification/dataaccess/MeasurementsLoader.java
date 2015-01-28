package nl.esciencecenter.eecology.classification.dataaccess;

import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;

import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public abstract class MeasurementsLoader {

    @Inject
    @Named("remove_measurements_containing_nan")
    private boolean removeMeasurementsContainingNan;

    public void setRemoveMeasurementsContainingNan(boolean value) {
        removeMeasurementsContainingNan = value;
    }

    protected IndependentMeasurement getUnannotatedMeasurement(double x, double y, double z, DateTime timeStamp,
            Integer deviceId, Double gpsSpeed, double longitude, double latitude, double altitude) {
        IndependentMeasurement measurement = new IndependentMeasurement();
        measurement.setX(x);
        measurement.setY(y);
        measurement.setZ(z);
        measurement.setTimeStamp(timeStamp);
        measurement.setDeviceId(deviceId);
        measurement.setGpsSpeed(gpsSpeed);
        measurement.setLongitude(longitude);
        measurement.setLatitude(latitude);
        measurement.setAltitude(altitude);
        return measurement;
    }

    protected boolean isMeasurementValid(IndependentMeasurement measurement) {
        boolean isGpsSpeedValid = isValid(measurement.getGpsSpeed());
        boolean isXValid = isValid(measurement.getX());
        boolean isYValid = isValid(measurement.getY());
        boolean isZValid = isValid(measurement.getZ());
        return isGpsSpeedValid && isXValid && isYValid && isZValid;
    }

    private boolean isValid(double value) {
        if (removeMeasurementsContainingNan == false) {
            return true;
        }
        return Double.isNaN(value) == false;
    }

}