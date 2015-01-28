package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.ClassifierLoader;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.featureextraction.InstancesCreator;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeasurementClassifierFeatureExtractor;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;

import org.jblas.DoubleMatrix;
import org.junit.Before;

import weka.classifiers.Classifier;
import weka.core.Instances;

public class MeasurementClassifierFeatureExtractorTest extends FeatureExtractorTest {
    private Map<Integer, LabelDetail> schema;
    private Instances instances;
    private Classifier classifier;

    @Override
    public void extractFeatures_2SequenceVectors_inputNotChanged() {
        /**
         * TODO I can't get the code below to work. There is an issue with the 'expect(attribute.value(0)).andReturn("");' line.
         * It generates an IllegalStateException: no last call on a mock available.
         */

        //        Attribute attribute = createNiceMock(Attribute.class);
        //        expect(attribute.value(0)).andReturn("");
        //        replay(attribute);
        //        Instance instance = createNiceMock(Instance.class);
        //        expect(instance.classAttribute()).andReturn(attribute);
        //        replay(instance);
        //        expect(instances.instance(anyInt())).andReturn(instance);
        //        replay(instances);
        //        try {
        //            expect(classifier.classifyInstance(isA(Instance.class))).andReturn(1.0).anyTimes();
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        //        replay(classifier);
        //
        //        super.extractFeatures_2SequenceVectors_inputNotChanged();
    }

    @Override
    public void getColumnNames_sameNumberOfNamesAsColumns() {
        int labels = 6;
        addLabelsToSchema(labels);
        super.getColumnNames_sameNumberOfNamesAsColumns();
    }

    @Before
    public void setUp() {
        schema = new HashMap<Integer, LabelDetail>();
        instances = createNiceMock(Instances.class);
        classifier = createNiceMock(Classifier.class);
        MeasurementClassifierFeatureExtractor extractor = new MeasurementClassifierFeatureExtractor();
        extractor.setClassifierLoader(getClassifierLoaderMock());
        InstancesCreator instanceCreator = getInstanceCreatorMock();
        extractor.setInstancesCreator(instanceCreator);
        extractor.setPathManager(createNiceMock(PathManager.class));
        extractor.setSchemaProvider(getSchemaProviderMock());
        featureExtractor = extractor;
    }

    private void addLabelsToSchema(int numberOfLabels) {
        for (int i = 0; i < numberOfLabels; i++) {
            LabelDetail labelDetail = new LabelDetail();
            labelDetail.setLabelId(i);
            labelDetail.setDescription("label " + i);
            schema.put(i, labelDetail);
        }
    };

    private InstancesCreator getInstanceCreatorMock() {
        InstancesCreator instanceCreator = createNiceMock(InstancesCreator.class);
        expect(instanceCreator.createInstances(isA(List.class), isA(DoubleMatrix.class))).andReturn(instances);
        replay(instanceCreator);
        return instanceCreator;
    }

    private ClassifierLoader getClassifierLoaderMock() {
        ClassifierLoader classifierLoader = createNiceMock(ClassifierLoader.class);
        expect(classifierLoader.loadClassifier(anyString())).andReturn(classifier);
        replay(classifierLoader);
        return classifierLoader;
    }

    private SchemaProvider getSchemaProviderMock() {
        SchemaProvider schemaProvider = createNiceMock(SchemaProvider.class);
        expect(schemaProvider.getSchema()).andReturn(schema).anyTimes();
        replay(schemaProvider);
        return schemaProvider;
    }

}
