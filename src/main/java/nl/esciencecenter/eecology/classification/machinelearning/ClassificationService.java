package nl.esciencecenter.eecology.classification.machinelearning;

import java.util.List;

import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.featureextraction.CoupledInstance;
import nl.esciencecenter.eecology.classification.featureextraction.SegmentToInstancesCreator;
import nl.esciencecenter.eecology.classification.machinelearning.exceptions.ClassifierPredictionException;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import com.google.inject.Inject;

public class ClassificationService {
    @Inject
    private SegmentToInstancesCreator segmentToInstancesCreator;
    @Inject
    private SchemaProvider schemaProvider;

    public void setSegmentToInstancesCreator(SegmentToInstancesCreator segmentToInstancesCreator) {
        this.segmentToInstancesCreator = segmentToInstancesCreator;
    }

    public void setSchemaProvider(SchemaProvider schemaProvider) {
        this.schemaProvider = schemaProvider;
    }

    public List<Segment> classify(List<Segment> segments, Classifier classifier) {
        Instances instances = segmentToInstancesCreator.createInstancesAndUpdateSegments(segments);
        for (int i = 0; i < instances.numInstances(); i++) {
            CoupledInstance instance = (CoupledInstance) instances.instance(i);
            Segment segment = instance.getCoupledSegment();
            LabelDetail labelDetail = getPredictedLabelDetail(instance, classifier);
            segment.setPredictedLabelDetail(labelDetail);
        }
        return segments;
    }

    private LabelDetail getPredictedLabelDetail(CoupledInstance instance, Classifier classifier) {
        int classIndex = getClassIndex(instance, classifier);
        String classAsString = instance.classAttribute().value(classIndex);
        int labelId = Integer.parseInt(classAsString);
        LabelDetail labelDetail = schemaProvider.getSchema().get(labelId);
        return labelDetail;
    }

    private int getClassIndex(Instance instance, Classifier classifier) {
        int classification = 0;
        try {
            classification = (int) classifier.classifyInstance(instance);
        } catch (Exception e) {
            throw new ClassifierPredictionException("Error classifying instance with trained classifier.", e);
        }
        return classification;
    }

}
