package nl.esciencecenter.eecology.classification.dataaccess;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;

import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLStructure;

/*-

data =

    device_info_serial: [10000x1 double]
             date_time: {10000x1 cell}
                  year: [10000x1 double]
                 month: [10000x1 double]
                   day: [10000x1 double]
                  hour: [10000x1 double]
                minute: [10000x1 double]
                second: [10000x1 double]
                 speed: [10000x1 double]
             longitude: [10000x1 double]
              latitude: [10000x1 double]
              altitude: [10000x1 double]
                tspeed: [10000x1 double]
                 index: [10000x1 double]
                 x_cal: [10000x1 double]
                 y_cal: [10000x1 double]
                 z_cal: [10000x1 double]

 */

/**
 * Loads measurements from mat files. Can handle multiple mat files. For this, multiple paths can be given separated by commas.
 *
 * @author Christiaan Meijer, NLeSc
 *
 */
public class UnannotatedMeasurementsMatLoader extends MeasurementsMatLoader {
    @Inject
    private PathManager pathManager;

    private final String rootKey = "data";
    private final String accXKey = "x_cal";
    private final String accYKey = "y_cal";
    private final String accZKey = "z_cal";
    private final String deviceIdKey = "device_info_serial";
    private final String yearsKey = "year";
    private final String monthsKey = "month";
    private final String daysKey = "day";
    private final String hoursKey = "hour";
    private final String minutesKey = "minute";
    private final String secondsKey = "second";
    private final String gpsSpeedKey = "speed";
    private final String longitudeKey = "longitude";
    private final String latitudeKey = "latitude";
    private final String altitudeKey = "altitude";
    private final String indexKey = "index";
    private MLStructure root;
    private List<Double> accX;
    private List<Double> accY;
    private List<Double> accZ;
    private List<Integer> deviceIds;
    private List<Double> gpsSpeeds;
    private List<Double> longitudes;
    private List<Double> latitudes;
    private List<Double> altitudes;
    private List<Integer> indices;

    /*-
     * data =
     *
     *   device_info_serial: [22x1 double]
     *            date_time: {22x1 cell}
     *                 year: [22x1 double]
     *                month: [22x1 double]
     *                  day: [22x1 double]
     *                 hour: [22x1 double]
     *               minute: [22x1 double]
     *               second: [22x1 double]
     *                speed: [22x1 double]
     *            longitude: [22x1 double]
     *             latitude: [22x1 double]
     *             altitude: [22x1 double]
     *               tspeed: [22x1 double]
     *                index: [22x1 double]
     *                x_cal: [22x1 double]
     *                y_cal: [22x1 double]
     *                z_cal: [22x1 double]
     */

    /**
     * Loads unannotated measurements from mat files.
     *
     * @param sourcePath
     * @return measurements
     */
    public List<IndependentMeasurement> loadFromSingleSource(String sourcePath) {
        MatFileReader reader = getReader(sourcePath.trim());
        fillDataFields(reader);
        return getMeasurements(reader);
    }

    private void fillDataFields(MatFileReader reader) {
        root = (MLStructure) reader.getMLArray(rootKey);
        accX = getDoubleList(root, accXKey);
        accY = getDoubleList(root, accYKey);
        accZ = getDoubleList(root, accZKey);
        gpsSpeeds = getDoubleList(root, gpsSpeedKey);
        longitudes = getDoubleList(root, longitudeKey);
        latitudes = getDoubleList(root, latitudeKey);
        altitudes = getDoubleList(root, altitudeKey);
        indices = getIntList(root, indexKey);
        deviceIds = getIntList(root, deviceIdKey);
        years = getIntList(root, yearsKey);
        months = getIntList(root, monthsKey);
        days = getIntList(root, daysKey);
        hours = getIntList(root, hoursKey);
        minutes = getIntList(root, minutesKey);
        seconds = getIntList(root, secondsKey);
        gpsSpeeds = getDoubleList(root, gpsSpeedKey);
    }

    private LinkedList<IndependentMeasurement> getMeasurements(MatFileReader matFileReader) {
        LinkedList<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();

        int nOfSamples = accX.size();

        for (int i = 0; i < nOfSamples; i++) {
            double x = accX.get(i);
            double y = accY.get(i);
            double z = accZ.get(i);
            DateTime timeStamp = getTimeStamp(i);
            int deviceId = deviceIds.get(i);
            int index = indices.get(i);
            double gpsSpeed = gpsSpeeds.get(i);
            double longitude = longitudes.get(i);
            double latitude = latitudes.get(i);
            double altitude = altitudes.get(i);
            IndependentMeasurement measurement = getUnannotatedMeasurement(x, y, z, timeStamp, deviceId, gpsSpeed, longitude,
                    latitude, altitude);
            measurement.setIndex(index);
            if (isMeasurementValid(measurement)) {
                measurements.add(measurement);
            }
        }
        return measurements;
    }
}
