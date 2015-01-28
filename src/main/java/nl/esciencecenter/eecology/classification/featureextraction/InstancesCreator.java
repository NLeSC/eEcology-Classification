package nl.esciencecenter.eecology.classification.featureextraction;

import java.util.List;
import java.util.Set;

import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.apache.commons.lang3.ArrayUtils;
import org.jblas.DoubleMatrix;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import com.google.inject.Inject;

/**
 * Creates Instances object to use with Weka machine learning library.
 * 
 * @author Christiaan Meijer, NleSc
 */
public class InstancesCreator {
    @Inject
    private SchemaProvider labelMapReader;

    private final String relationName = "bird window relation";
    private final double weight = 1; // Value chosen for no specific reason.
    private final String className = "bird behavior";
    private List<Segment> segments;

    public void setLabelMapReader(SchemaProvider labelMapReader) {
        this.labelMapReader = labelMapReader;
    }

    /**
     * Creates Instances object to use with Weka machine learning library.
     * 
     * @param segments
     * @param features
     * @param featureNames
     * @return
     */
    public Instances createInstances(List<Segment> segments, DoubleMatrix features) {
        this.segments = segments;
        checkArguments(segments, features);
        Instances dataSet = createEmptyInstances(segments, features.columns);
        for (int i = 0; i < segments.size(); i++) {
            double[] featureRow = features.getRow(i).toArray();
            Instance instance = createInstanceFromSegment(segments.get(i), featureRow, dataSet);
            dataSet.add(instance);
        }
        return dataSet;
    }

    private CoupledInstance createInstanceFromSegment(Segment segment, double[] featureRow, Instances dataSet) {
        double placeholder = 0; // gets overwritten in same method
        double[] attValues = ArrayUtils.addAll(new double[] { placeholder }, featureRow);
        CoupledInstance instance = new CoupledInstance(weight, attValues);
        instance.setCoupledObject(segment);
        instance.setDataset(dataSet);

        if (segment.isLabeled()) {
            String labelValue = getLabelValue(segment.getLabel());

            try {
                instance.setClassValue(labelValue);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Cannot set label " + labelValue + " to instance.", e);
            }
        } else {
            instance.setClassMissing();
        }

        return instance;
    }

    private Instances createEmptyInstances(List<Segment> segments, int featureCount) {
        FastVector attributeInfo = new FastVector();
        Attribute classAttribute = getClassAttribute();
        attributeInfo.addElement(classAttribute);
        String[] featureNames = getFeatureNames(segments, featureCount);
        for (int i = 0; i < featureCount; i++) {
            attributeInfo.addElement(new Attribute(featureNames[i]));
        }
        Instances dataSet = new Instances(relationName, attributeInfo, segments.size());
        dataSet.setClass(classAttribute);
        return dataSet;
    }

    private String[] getFeatureNames(List<Segment> segments, int featureCount) {
        if (segments.size() == 0 || segments.get(0).getFeatureNames() == null
                || segments.get(0).getFeatureNames().length != featureCount) {
            return getDefaultFeatureNames(featureCount);
        } else {
            return segments.get(0).getFeatureNames();
        }
    }

    private String[] getDefaultFeatureNames(int featureCount) {
        String[] featureNames = new String[featureCount];
        for (int i = 0; i < featureNames.length; i++) {
            featureNames[i] = "feature " + i;
        }
        return featureNames;
    }

    private Attribute getClassAttribute() {
        FastVector attributeValues = getAllLabelsFromSchema();
        Attribute classAttribute = new Attribute(className, attributeValues);
        return classAttribute;
    }

    private FastVector getAllLabelsFromSchema() {
        FastVector attributeValues = new FastVector();
        Set<Integer> labels = labelMapReader.getSchema().keySet();
        for (Integer label : labels) {
            String labelValue = label.toString();
            attributeValues.addElement(labelValue);
        }
        return attributeValues;
    }

    /**
     * Gets the string representation of a label. In the future this might be "soaring" or "eating" etc.
     * 
     * @param label
     * @return
     */
    private String getLabelValue(int label) {
        return Integer.toString(label);
    }

    private void checkArguments(List<Segment> segments, DoubleMatrix features) {
        if (features == null) {
            throw new IllegalArgumentException("Feature matrix was null while creating Instances object.");
        }
        if (segments == null) {
            throw new IllegalArgumentException("Segment list was null while creating Instances object.");
        }
        if (features.rows != segments.size()) {
            String message = String.format(
                    "Number of elements (%d) in window list was unequal to number of rows (%d) in feature matrix.",
                    segments.size(), features.rows);
            throw new IllegalArgumentException(message);
        }
    }
}
