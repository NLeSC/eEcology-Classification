package nl.esciencecenter.eecology.classification.test.machinelearning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import nl.esciencecenter.eecology.classification.machinelearning.DatasetSplitter;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.junit.Before;
import org.junit.Test;

public class DatasetSplitterTest {

    private DatasetSplitter datasetSplitter;

    @Test
    public void setInputDataset_emptyList_noExceptionThrown() {
        // Arrange
        List<Segment> emptyList = new LinkedList<Segment>();

        // Act          
        datasetSplitter.setInputDataset(emptyList);

        // Assert
    }

    @Test
    public void getTrainDataset_emptyListWasSet_returnEmptyList() {
        // Arrange
        List<Segment> emptyList = new LinkedList<Segment>();
        datasetSplitter.setInputDataset(emptyList);

        // Act          
        List<Segment> trainSet = datasetSplitter.getTrainDataset();

        // Assert
        assertEquals(0, trainSet.size());
    }

    @Test
    public void getTestDataset_emptyListWasSet_returnEmptyList() {
        // Arrange
        List<Segment> emptyList = new LinkedList<Segment>();
        datasetSplitter.setInputDataset(emptyList);

        // Act          
        List<Segment> testSet = datasetSplitter.getTestDataset();

        // Assert
        assertEquals(0, testSet.size());
    }

    @Test
    public void getTestDataset_8InputsSetTrainSizeWhole_trainSizeIs8() {
        // Arrange
        List<Segment> segments = getSegmentsWithConsecutiveDeviceIds(8);
        datasetSplitter.setInputDataset(segments);
        datasetSplitter.setRatio(1, 0, 0);

        // Act          
        List<Segment> trainSet = datasetSplitter.getTrainDataset();

        // Assert
        assertEquals(8, trainSet.size());
    }

    @Test
    public void getTestDataset_8InputsSetTrainSizeQuarter_trainSizeIs2() {
        // Arrange
        List<Segment> segments = getSegmentsWithConsecutiveDeviceIds(8);
        datasetSplitter.setInputDataset(segments);
        datasetSplitter.setRatio(1, 3, 0);

        // Act          
        List<Segment> trainSet = datasetSplitter.getTrainDataset();

        // Assert
        assertEquals(2, trainSet.size());
    }

    @Test
    public void getTestDataset_8InputsSetTrainSizeQuarter_testSizeIs6() {
        // Arrange
        List<Segment> segments = getSegmentsWithConsecutiveDeviceIds(8);
        datasetSplitter.setInputDataset(segments);
        datasetSplitter.setRatio(1, 3, 0);

        // Act          
        List<Segment> testSet = datasetSplitter.getTestDataset();

        // Assert
        assertEquals(6, testSet.size());
    }

    @Test
    public void getTestDataset_30InputsSetSizeAll0_testSizeIs10() { // Default to 1/3 : 1/3 : 1/3
        // Arrange
        List<Segment> segments = getSegmentsWithConsecutiveDeviceIds(30);
        datasetSplitter.setInputDataset(segments);
        datasetSplitter.setRatio(0, 0, 0);

        // Act          
        List<Segment> testSet = datasetSplitter.getTestDataset();

        // Assert
        assertEquals(10, testSet.size());
    }

    @Test
    public void getTestDataset_100InputsSetTrainSizePercentRandomizeOn_testSetDoesntContainTrainInstance() {
        // Arrange
        List<Segment> segments = getSegmentsWithConsecutiveDeviceIds(100);
        datasetSplitter.setInputDataset(segments);
        datasetSplitter.setRatio(1, 99, 0);
        datasetSplitter.setSplitIsDoneRandomly(true);

        // Act          
        List<Segment> testSet = datasetSplitter.getTestDataset();
        Segment trainInstance = datasetSplitter.getTrainDataset().get(0);

        // Assert
        assertFalse(testSet.contains(trainInstance));
    }

    @Test
    public void getTrainDataset_randomizeIsOff_trainContainsInstance0() {
        // Arrange
        List<Segment> segments = getSegmentsWithConsecutiveDeviceIds(100);
        datasetSplitter.setInputDataset(segments);
        datasetSplitter.setRatio(1, 99, 0);

        // Act          
        Segment firstTrainInstance = datasetSplitter.getTrainDataset().get(0);

        // Assert
        assertEquals(0, firstTrainInstance.getDeviceId());
    }

    @Test
    public void getSets_randomizeIsOn_everyInstanceIsContainedByExactlyOneSet() {
        // Arrange
        List<Segment> segments = getSegmentsWithConsecutiveDeviceIds(2000);
        datasetSplitter.setInputDataset(segments);
        datasetSplitter.setRatio(0.5, 0.3, 0.2);
        datasetSplitter.setSplitIsDoneRandomly(true);

        // Act          
        List<Segment> trainSet = datasetSplitter.getTrainDataset();
        List<Segment> testSet = datasetSplitter.getTestDataset();
        List<Segment> validationSet = datasetSplitter.getValidationDataset();

        // Assert
        Set<Integer> trainIds = getIdSet(trainSet);
        Set<Integer> testIds = getIdSet(testSet);
        Set<Integer> validationIds = getIdSet(validationSet);
        for (Integer trainId : trainIds) {
            assertFalse(testIds.contains(trainId));
            assertFalse(validationIds.contains(trainId));
        }
        for (Integer testId : testIds) {
            assertFalse(trainIds.contains(testId));
            assertFalse(validationIds.contains(testId));
        }
        for (Integer validationId : validationIds) {
            assertFalse(trainIds.contains(validationId));
            assertFalse(testIds.contains(validationId));
        }
        assertEquals(2000, trainIds.size() + testIds.size() + validationIds.size());
    }

    private Set<Integer> getIdSet(List<Segment> dataset) {
        Set<Integer> ids = new HashSet<Integer>();
        for (Segment segment : dataset) {
            ids.add(segment.getDeviceId());
        }
        return ids;
    }

    @Test
    public void getValidationDataset_001ratios_returnCompleteSet() {
        // Arrange
        List<Segment> segments = getSegmentsWithConsecutiveDeviceIds(12);
        datasetSplitter.setInputDataset(segments);
        datasetSplitter.setRatio(0, 0, 1);

        // Act          
        List<Segment> validationSet = datasetSplitter.getValidationDataset();

        // Assert
        assertEquals(12, validationSet.size());
    }

    @Test
    public void getValidationDataset_complexRatios_returnCorrectSize() {
        // Arrange
        List<Segment> segments = getSegmentsWithConsecutiveDeviceIds(50);
        datasetSplitter.setInputDataset(segments);
        datasetSplitter.setRatio(10, 30, 60);

        // Act          
        List<Segment> validationSet = datasetSplitter.getValidationDataset();

        // Assert
        assertEquals(30, validationSet.size());
    }

    @Before
    public void setUp() {
        datasetSplitter = new DatasetSplitter(new Random(1234), 0, 0, 0);
    }

    private List<Segment> getSegmentsWithConsecutiveDeviceIds(int segmentCount) {
        List<Segment> segments = new LinkedList<Segment>();
        for (int i = 0; i < segmentCount; i++) {
            Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
            segment.setDeviceId(i);
            segments.add(segment);
        }
        return segments;
    }
}
