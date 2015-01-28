package nl.esciencecenter.eecology.classification.machinelearning;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Splits a dataset, a list of segments, into a train set and a test set.
 * 
 * @author Christiaan Meijer, NLeSc
 * 
 */
public class DatasetSplitter {
    private double trainDatasetRatio;
    private double testDatasetRatio;
    private List<Segment> inputDataset;
    private List<Integer> trainIndices;
    private List<Integer> testIndices;
    private List<Integer> validationIndices;
    private boolean areIndicesReady;

    @Inject
    @Named("dataset_split_over_train_and_test_is_done_randomly")
    private boolean splitIsDoneRandomly;

    private final Random random;

    @Inject
    public DatasetSplitter(Random random, @Named("dataset_split_train_ratio") double trainDatasetRatio,
            @Named("dataset_split_test_ratio") double testDatasetRatio,
            @Named("dataset_split_validation_ratio") double validationDatasetRatio) {
        this.random = random;
        setRatio(trainDatasetRatio, testDatasetRatio, validationDatasetRatio);
    }

    public void setSplitIsDoneRandomly(boolean splitIsDoneRandomly) {
        this.splitIsDoneRandomly = splitIsDoneRandomly;
    }

    private List<Integer> getTrainIndices() {
        initializeIndicesIfNecessary();
        return trainIndices;
    }

    private List<Integer> getTestIndices() {
        initializeIndicesIfNecessary();
        return testIndices;
    }

    private List<Integer> getValidationIndices() {
        initializeIndicesIfNecessary();
        return validationIndices;
    }

    private void initializeIndicesIfNecessary() {
        if (areIndicesReady == false) {
            initializeIndices();
        }
    }

    private void initializeIndices() {
        int totalSize = inputDataset.size();
        LinkedList<Integer> allIndices = listConsecutiveNumbersFromZero(totalSize);
        if (splitIsDoneRandomly) {
            Collections.shuffle(allIndices, random);
        }
        int trainSize = (int) (trainDatasetRatio * totalSize);
        int testSize = (int) (testDatasetRatio * totalSize);
        trainIndices = allIndices.subList(0, trainSize);
        testIndices = allIndices.subList(trainSize, trainSize + testSize);
        validationIndices = allIndices.subList(trainSize + testSize, totalSize);
        areIndicesReady = true;
    }

    private LinkedList<Integer> listConsecutiveNumbersFromZero(int totalSize) {
        LinkedList<Integer> allIndices = new LinkedList<Integer>();
        for (int index = 0; index < totalSize; index++) {
            allIndices.add(index);
        }
        return allIndices;
    }

    private List<Segment> createDatasetFromIndices(List<Integer> indices) {
        LinkedList<Segment> dataset = new LinkedList<Segment>();
        for (int i = 0; i < indices.size(); i++) {
            dataset.add(inputDataset.get(indices.get(i)));
        }
        return dataset;
    }

    public void setInputDataset(List<Segment> segments) {
        inputDataset = segments;
        areIndicesReady = false;
    }

    public List<Segment> getTrainDataset() {
        return createDatasetFromIndices(getTrainIndices());
    }

    public List<Segment> getTestDataset() {
        return createDatasetFromIndices(getTestIndices());
    }

    public List<Segment> getValidationDataset() {
        return createDatasetFromIndices(getValidationIndices());
    }

    public void setRatio(double trainDatasetRatio, double testDatasetRatio, double validationSetRatio) {
        double total = trainDatasetRatio + testDatasetRatio + validationSetRatio;
        if (total > 0d) {
            this.trainDatasetRatio = trainDatasetRatio / total;
            this.testDatasetRatio = testDatasetRatio / total;
            // validation Dataset Ratio is set implicitly
        } else { // default each to 1/3
            this.trainDatasetRatio = 1d / 3;
            this.testDatasetRatio = 1d / 3;
            // validation Dataset Ratio is set implicitly
        }
        areIndicesReady = false;
    }
}
