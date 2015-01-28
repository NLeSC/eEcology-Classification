package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import static org.jblas.DoubleMatrix.concatHorizontally;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.ClassifierLoader;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.InstancesCreator;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;
import nl.esciencecenter.eecology.classification.machinelearning.exceptions.ClassifierPredictionException;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.jblas.DoubleMatrix;

import scala.collection.mutable.StringBuilder;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import com.google.inject.Inject;

public class MeasurementClassifierFeatureExtractor extends FeatureExtractor {
    @Inject
    ClassifierLoader classifierLoader;
    @Inject
    PathManager pathManager;
    @Inject
    SchemaProvider schemaProvider;
    @Inject
    InstancesCreator instancesCreator;

    private Classifier classifier;
    private Map<Integer, LabelDetail> schema;

    /**
     * For unit testing only.
     */
    public void setClassifierLoader(ClassifierLoader classifierLoader) {
        this.classifierLoader = classifierLoader;
    }

    /**
     * For unit testing only.
     */
    public void setInstancesCreator(InstancesCreator instancesCreator) {
        this.instancesCreator = instancesCreator;
    }

    /**
     * For unit testing only.
     */
    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
    }

    /**
     * For unit testing only.
     */
    public void setSchemaProvider(SchemaProvider schemaProvider) {
        this.schemaProvider = schemaProvider;
    }

    /**
     * Extracts features from the data. Will throw a ClassifierPredictionException if the classifier fails to classify any
     * measurement in the data.
     */
    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        String path = pathManager.getMeasurementClassifierPath();
        classifier = classifierLoader.loadClassifier(path);
        schema = schemaProvider.getSchema();
        int numberOfLabels = schema.size();
        DoubleMatrix results = new DoubleMatrix(formattedSegments.getNumberOfRows(), numberOfLabels);
        for (int iRow = 0; iRow < formattedSegments.getNumberOfRows(); iRow++) {
            DoubleMatrix rowX = formattedSegments.getX().getRow(iRow);
            DoubleMatrix rowY = formattedSegments.getY().getRow(iRow);
            DoubleMatrix rowZ = formattedSegments.getZ().getRow(iRow);
            DoubleMatrix gpsSpeed = formattedSegments.getGpsSpeed().getRow(iRow);
            DoubleMatrix rowResult = getRowResult(rowX, rowY, rowZ, gpsSpeed);
            results.putRow(iRow, rowResult);
        }
        return results;
    }

    private DoubleMatrix getRowResult(DoubleMatrix rowX, DoubleMatrix rowY, DoubleMatrix rowZ, DoubleMatrix gpsSpeed) {
        int measurementCount = rowX.columns;
        Instances instances = createInstances(rowX, rowY, rowZ, gpsSpeed, measurementCount);

        LinkedList<Integer> labels = getSortedLabelIds();

        DoubleMatrix labelCounts = new DoubleMatrix(1, labels.size());

        for (int i = 0; i < measurementCount; i++) {
            int index = labels.indexOf(getPredictedLabelId(instances, i));
            labelCounts.put(index, labelCounts.get(index) + 1);
        }

        double norm = Math.max(labelCounts.norm2(), 1);
        return labelCounts.div(norm);
    }

    private Instances createInstances(DoubleMatrix rowX, DoubleMatrix rowY, DoubleMatrix rowZ, DoubleMatrix gpsSpeed,
            int measurementCount) {
        DoubleMatrix features = getFeatureMatrix(rowX, rowY, rowZ, gpsSpeed);
        List<Segment> dummySegments = getDummySegments(measurementCount);

        Instances instances = instancesCreator.createInstances(dummySegments, features);
        return instances;
    }

    private List<Segment> getDummySegments(int measurementCount) {
        List<Segment> dummySegments = new LinkedList<Segment>();
        for (int i = 0; i < measurementCount; i++) {
            dummySegments.add(new Segment(new LinkedList<IndependentMeasurement>()));
        }
        return dummySegments;
    }

    private DoubleMatrix getFeatureMatrix(DoubleMatrix rowX, DoubleMatrix rowY, DoubleMatrix rowZ, DoubleMatrix gpsSpeed) {
        DoubleMatrix gpsColumn = gpsSpeed.repmat(rowX.columns, 1);
        DoubleMatrix features = concatHorizontally(
                concatHorizontally(concatHorizontally(rowX.transpose(), rowY.transpose()), rowZ.transpose()), gpsColumn);
        return features;
    }

    private int getPredictedLabelId(Instances instances, int i) {
        int predictionIndex = 0;
        Instance instance = instances.instance(i);
        try {
            predictionIndex = (int) classifier.classifyInstance(instance);
        } catch (Exception e) {
            throw new ClassifierPredictionException(new StringBuilder()
                    .append("Error applying the trained measurement classifier to measurements. ")
                    .append("Possible cause might be that the measurement classifier wasn't trained on the correct features. ")
                    .append("See the explanation of 'measurement_classifier' in the feature list document.").toString(), e);
        }

        String classAsString = instance.classAttribute().value(predictionIndex);
        int predictedLabelId = Integer.parseInt(classAsString);
        return predictedLabelId;
    }

    @Override
    public List<String> getColumnNames() {
        schema = schemaProvider.getSchema();
        LinkedList<String> columnNames = new LinkedList<>();
        LinkedList<Integer> sortedLabelIds = getSortedLabelIds();
        for (Integer labelId : sortedLabelIds) {
            String columnName = schema.get(labelId).getDescription();
            columnNames.add(columnName);
        }
        return columnNames;
    }

    @Override
    public String getName() {
        return "measurement_classifier";
    }

    private LinkedList<Integer> getSortedLabelIds() {
        LinkedList<Integer> labels = new LinkedList<Integer>(schema.keySet());
        Collections.sort(labels);
        return labels;
    }
}
