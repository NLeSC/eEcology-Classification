package nl.esciencecenter.eecology.classification.featureextraction;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.segmentloading.GpsRecord;
import nl.esciencecenter.eecology.classification.segmentloading.Measurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.jblas.DoubleMatrix;
import org.joda.time.DateTime;

/**
 * Converts windows (or segments of measurements) to data that is ready to be used for feature extraction.
 *
 * @author Christiaan Meijer, NLeSc
 *
 */
public class SegmentFormatter {
    private DoubleMatrix x;
    private DoubleMatrix y;
    private DoubleMatrix z;
    private DoubleMatrix gpsSpeed;
    private DoubleMatrix latitude;
    private DoubleMatrix longitude;
    private DoubleMatrix altitude;
    private DoubleMatrix pressure;
    private DoubleMatrix temperature;
    private DoubleMatrix satellitesUsed;
    private DoubleMatrix gpsFixtime;
    private DoubleMatrix speed2d;
    private DoubleMatrix speed3d;
    private DoubleMatrix direction;
    private DoubleMatrix altitudeAboveGround;
    private DateTime[][] timeStamp;
    private int[][] deviceId;

    /**
     * Converts windows (or segments of measurements) to data that is ready to be used for feature extraction.
     *
     * @param segments
     * @return formatted data
     */
    public FormattedSegments format(List<Segment> segments) {
        int segmentRowCount = segments.size();
        int accelerometerColumnCount = getAccelerometerMeasurementCountPerSegment(segments);
        instantiateAccelerometerDataFields(segmentRowCount, accelerometerColumnCount);
        fillAccelerometerDataFields(segments, segmentRowCount, accelerometerColumnCount);
        int gpsRecordColumnCount = getGpsRecordCountPerSegment(segments);
        instantiateGpsRecordDataFields(segmentRowCount, gpsRecordColumnCount);
        fillGpsRecordDataFields(segments, segmentRowCount, gpsRecordColumnCount);
        return new FormattedSegments(x, y, z, gpsSpeed, latitude, longitude, altitude, pressure, temperature, satellitesUsed,
                gpsFixtime, speed2d, speed3d, altitudeAboveGround, timeStamp, deviceId);
    }

    private int getGpsRecordCountPerSegment(List<Segment> segments) {
        if (segments.size() == 0) {
            return 0;
        }
        Segment firstSegment = segments.get(0);
        int gpsRecordCount = firstSegment.getLastGpsRecords().size() + firstSegment.getNextGpsRecords().size() + 1;
        return gpsRecordCount;
    }

    private int getAccelerometerMeasurementCountPerSegment(List<Segment> segments) {
        return segments.size() > 0 ? segments.get(0).getMeasurements().size() : 0;
    }

    private void instantiateAccelerometerDataFields(int rowCount, int columnCount) {
        x = new DoubleMatrix(rowCount, columnCount);
        y = new DoubleMatrix(rowCount, columnCount);
        z = new DoubleMatrix(rowCount, columnCount);
    }

    private void instantiateGpsRecordDataFields(int rowCount, int columnCount) {
        gpsSpeed = new DoubleMatrix(rowCount, columnCount);
        latitude = new DoubleMatrix(rowCount, columnCount);
        longitude = new DoubleMatrix(rowCount, columnCount);
        altitude = new DoubleMatrix(rowCount, columnCount);
        pressure = new DoubleMatrix(rowCount, columnCount);
        temperature = new DoubleMatrix(rowCount, columnCount);
        satellitesUsed = new DoubleMatrix(rowCount, columnCount);
        gpsFixtime = new DoubleMatrix(rowCount, columnCount);
        speed2d = new DoubleMatrix(rowCount, columnCount);
        speed3d = new DoubleMatrix(rowCount, columnCount);
        direction = new DoubleMatrix(rowCount, columnCount);
        altitudeAboveGround = new DoubleMatrix(rowCount, columnCount);
        timeStamp = new DateTime[rowCount][columnCount];
        deviceId = new int[rowCount][columnCount];
    }

    private void fillAccelerometerDataFields(List<Segment> segments, int rowCount, int columnCount) {
        for (int row = 0; row < rowCount; row++) {
            Segment currentSegment = segments.get(row);
            for (int column = 0; column < columnCount; column++) {
                Measurement currentMeasurement = currentSegment.getMeasurements().get(column);
                x.put(row, column, currentMeasurement.getX());
                y.put(row, column, currentMeasurement.getY());
                z.put(row, column, currentMeasurement.getZ());
            }
        }
    }

    private void fillGpsRecordDataFields(List<Segment> segments, int segmentCount, int gpsRecordColumnCount) {
        for (int segmentRow = 0; segmentRow < segmentCount; segmentRow++) {
            Segment currentSegment = segments.get(segmentRow);
            LinkedList<GpsRecord> gpsRecords = getAllGpsRecordsFromSegment(currentSegment);
            for (int gpsRecordColumn = 0; gpsRecordColumn < gpsRecordColumnCount; gpsRecordColumn++) {
                GpsRecord currentGpsRecord = gpsRecords.get(gpsRecordColumn);
                gpsSpeed.put(segmentRow, gpsRecordColumn, currentGpsRecord.getGpsSpeed());
                latitude.put(segmentRow, gpsRecordColumn, currentGpsRecord.getLatitude());
                longitude.put(segmentRow, gpsRecordColumn, currentGpsRecord.getLongitude());
                altitude.put(segmentRow, gpsRecordColumn, currentGpsRecord.getAltitude());
                pressure.put(segmentRow, gpsRecordColumn, currentGpsRecord.getPressure());
                temperature.put(segmentRow, gpsRecordColumn, currentGpsRecord.getTemperature());
                satellitesUsed.put(segmentRow, gpsRecordColumn, currentGpsRecord.getSatellitesUsed());
                gpsFixtime.put(segmentRow, gpsRecordColumn, currentGpsRecord.getGpsFixTime());
                speed2d.put(segmentRow, gpsRecordColumn, currentGpsRecord.getSpeed2d());
                speed3d.put(segmentRow, gpsRecordColumn, currentGpsRecord.getSpeed3d());
                direction.put(segmentRow, gpsRecordColumn, currentGpsRecord.getDirection());
                altitudeAboveGround.put(segmentRow, gpsRecordColumn, currentGpsRecord.getAltitudeAboveGround());
                timeStamp[segmentRow][gpsRecordColumn] = currentGpsRecord.getTimeStamp();
                deviceId[segmentRow][gpsRecordColumn] = currentGpsRecord.getDeviceId();
            }
        }
    }

    private LinkedList<GpsRecord> getAllGpsRecordsFromSegment(Segment currentSegment) {
        LinkedList<GpsRecord> gpsRecords = new LinkedList<GpsRecord>();
        gpsRecords.addAll(currentSegment.getLastGpsRecords());
        gpsRecords.add(currentSegment.getGpsRecord());
        gpsRecords.addAll(currentSegment.getNextGpsRecords());
        return gpsRecords;
    }
}
