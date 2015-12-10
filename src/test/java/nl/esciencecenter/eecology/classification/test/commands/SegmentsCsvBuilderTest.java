package nl.esciencecenter.eecology.classification.test.commands;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import nl.esciencecenter.eecology.classification.commands.SegmentsCsvBuilder;
import nl.esciencecenter.eecology.classification.featureextraction.SegmentToInstancesCreator;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import weka.core.Instances;

public class SegmentsCsvBuilderTest {
    private List<Segment> trainSet;
    private List<Segment> testSet;
    private List<Segment> validationSet;
    private List<Segment> unclassified;
    private SegmentsCsvBuilder csvBuilder;

    @Test
    public void buildCsv() {
        // Arrange
        String expected = "device_info_serial,date_time,lon,lat,alt,class_id,testfeature1,testfeature2,set\n"
                + "5,2015-12-10T15:07:00.000+0000,9.0,8.0,6.0,0,1.0,2.0,train\n"
                + "5,2015-12-10T15:07:00.000+0000,9.0,8.0,6.0,0,1.0,2.0,test\n"
                + "5,2015-12-10T15:07:00.000+0000,9.0,8.0,6.0,0,1.0,2.0,validation\n"
                + "5,2015-12-10T15:07:00.000+0000,9.0,8.0,6.0,0,1.0,2.0,unclassified\n";

        // Act
        String csv = csvBuilder.buildCsv(trainSet, testSet, validationSet, unclassified);

        // Assert
        assertEquals(expected, csv);
    }

    @Before
    public void setUp() {
        trainSet = getTrainSet();
        testSet = getTrainSet();
        validationSet = getTrainSet();
        unclassified = getTrainSet();
        csvBuilder = new SegmentsCsvBuilder();
        csvBuilder.setSegmentToinstancesCreator(new SegmentToInstancesCreator(null, null, null) {
            @Override
            public Instances createInstancesAndUpdateSegments(List<Segment> segments) {
                return null;
            }
        });
    }

    private List<Segment> getTrainSet() {
        LinkedList<Segment> list = new LinkedList<Segment>();
        List<IndependentMeasurement> measurements = getMeasurements();
        Segment segment = new Segment(measurements);
        IndependentMeasurement measurement = measurements.get(0);
        segment.setAltitude(measurement.getAltitude());
        segment.setDeviceId(measurement.getDeviceId());
        segment.setFirstIndex(measurement.getIndex());
        segment.setGpsSpeed(measurement.getGpsSpeed());
        segment.setLatitude(measurement.getLatitude());
        segment.setLongitude(measurement.getLongitude());
        segment.setTimeStamp(measurement.getTimeStamp());
        double[] features = { 1, 2 };
        String[] featureNames = { "testfeature1", "testfeature2" };
        segment.setFeatures(features, featureNames);
        list.add(segment);
        return list;
    }

    private List<IndependentMeasurement> getMeasurements() {
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();
        IndependentMeasurement measurement = new IndependentMeasurement();
        measurement.setX(1);
        measurement.setY(2);
        measurement.setZ(3);
        measurement.setGpsSpeed(4);
        measurement.setDeviceId(5);
        measurement.setAltitude(6);
        measurement.setLabel(7);
        measurement.setLatitude(8);
        measurement.setLongitude(9);
        measurement.setTimeStamp(new DateTime(2015, 12, 10, 15, 07, DateTimeZone.UTC));
        measurements.add(measurement);
        return measurements;
    }
}
