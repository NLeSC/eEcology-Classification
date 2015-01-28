package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

public class SegmentAsTupleSaverTest {

    private SegmentAsTupleSaverFixture segmentAsTupleSaver;
    private final String headers = "device_info_serial,date_time,first_index,class_id,class_name,class_red,class_green,class_blue,longitude,latitude,altitude,gpsspeed\n";

    @Test
    public void getResultMessage_emptyInput_onlyHeaders() {
        // Arrange
        String expected = headers;

        // Act
        String output = segmentAsTupleSaver.getResultMessage(new LinkedList<Segment>());

        // Assert
        assertEquals(expected, output);
    }

    @Test
    public void getResultMessage_2Segments_correctResult() {
        String expected = headers + "15,2014-03-31T11:52:13.000Z,4,3,foraging,0.4,0.5,0.8,1.0,2.0,3.0,0.0\n"
                + "67,2014-03-31T11:52:13.000Z,NULL,3,foraging,0.4,0.5,0.8,1.0,2.0,3.0,0.0\n";
        LinkedList<Segment> segments = new LinkedList<Segment>();
        segments.add(getSegmentWithDeviceId(15, 4));
        segments.add(getSegmentWithDeviceId(67, null));

        // Act
        String output = segmentAsTupleSaver.getResultMessage(segments);

        // Assert
        assertEquals(expected, output);
    }

    @Test
    public void getResultMessage_3SegmentsSameIdTimeStampSingleClassificationTrue_OnlyOneResultLine() {
        segmentAsTupleSaver.setOutputSingleClassificationPerDeviceTimeStampCombination(true);
        LinkedList<Segment> segments = get3SegmentsWithSameIdAndTimeStamp();

        // Act
        String output = segmentAsTupleSaver.getResultMessage(segments);
        int resultSize = output.split("\n").length - 1; //-1 for headers

        // Assert
        assertEquals(1, resultSize);
    }

    @Test
    public void getResultMessage_3SegmentsSameIdTimeStampSingleClassificationFalse_ThreeResultLines() {
        segmentAsTupleSaver.setOutputSingleClassificationPerDeviceTimeStampCombination(false);
        LinkedList<Segment> segments = get3SegmentsWithSameIdAndTimeStamp();

        // Act
        String output = segmentAsTupleSaver.getResultMessage(segments);
        int resultSize = output.split("\n").length - 1; //-1 for headers

        // Assert
        assertEquals(3, resultSize);
    }

    private LinkedList<Segment> get3SegmentsWithSameIdAndTimeStamp() {
        LinkedList<Segment> segments = new LinkedList<Segment>();
        segments.add(getSegmentWithDeviceId(15, 4));
        segments.add(getSegmentWithDeviceId(15, 10));
        segments.add(getSegmentWithDeviceId(15, 18));
        return segments;
    }

    private Segment getSegmentWithDeviceId(int deviceId, Integer firstIndex) {
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();
        Segment segment = new Segment(measurements);
        DateTime timeStamp = new DateTime(2014, 3, 31, 11, 52, 13, DateTimeZone.UTC);
        segment.setTimeStamp(timeStamp);
        segment.setDeviceId(deviceId);
        segment.setLongitude(1);
        segment.setLatitude(2);
        segment.setAltitude(3);
        if (firstIndex != null) {
            segment.setFirstIndex(firstIndex.intValue());
        }
        LabelDetail predictedLabelDetail = new LabelDetail();
        predictedLabelDetail.setLabelId(3);
        predictedLabelDetail.setDescription("foraging");
        predictedLabelDetail.setColorR(0.4);
        predictedLabelDetail.setColorG(0.5);
        predictedLabelDetail.setColorB(0.8);
        segment.setPredictedLabelDetail(predictedLabelDetail);
        return segment;
    }

    @Before
    public void setUp() throws Exception {
        segmentAsTupleSaver = new SegmentAsTupleSaverFixture();
        segmentAsTupleSaver.setDefaultFirstIndexText("NULL");
    }
}
