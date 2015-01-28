package nl.esciencecenter.eecology.classification.test.featureextraction;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.featureextraction.CoupledInstance;
import nl.esciencecenter.eecology.classification.featureextraction.InstancesCreator;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.jblas.DoubleMatrix;

import weka.core.Attribute;
import weka.core.Instances;

public class InstancesCreatorTest extends TestCase {
    InstancesCreator instancesCreator;

    public void test_createInstances_emptyInput_emptyOutput() {
        List<Segment> segments = new LinkedList<Segment>();
        DoubleMatrix features = new DoubleMatrix();
        int expected = 0;

        // Act
        Instances output = instancesCreator.createInstances(segments, features);

        // Assert
        assertEquals(expected, output.numInstances());
    }

    public void test_createInstances_1Input0Features_1Output() {
        // Arrange        
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        List<Segment> segments = Arrays.asList(new Segment[] { segment });
        DoubleMatrix features = new DoubleMatrix(1, 0);
        int expected = 1;

        // Act
        Instances output = instancesCreator.createInstances(segments, features);

        // Assert
        assertEquals(expected, output.numInstances());
    }

    public void test_createInstances_1Input0FeaturesLabel_instanceHasCorrectLabel() {
        // Arrange        
        List<Segment> segments = Arrays.asList(new Segment[] { getSegmentWithLabel(4) });
        DoubleMatrix features = new DoubleMatrix(1, 0);
        double expectedClass = 4;

        // Act
        Instances output = instancesCreator.createInstances(segments, features);

        // Assert
        int index = (int) output.instance(0).classValue();
        String value = output.classAttribute().value(index);
        assertEquals(expectedClass, (double) Integer.parseInt(value));
    }

    private Segment getSegmentWithLabel(int label) {
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        LabelDetail labelDetail = new LabelDetail();
        labelDetail.setLabelId(label);
        segment.setLabelDetail(labelDetail);
        return segment;
    }

    public void test_createInstances_1Input0FeaturesNoLabel_instanceHasMissingClass() {
        // Arrange        
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        List<Segment> segments = Arrays.asList(new Segment[] { segment });
        DoubleMatrix features = new DoubleMatrix(1, 0);

        // Act
        Instances output = instancesCreator.createInstances(segments, features);

        // Assert
        assertTrue(output.instance(0).classIsMissing());
    }

    public void test_createInstances_1Input9Features_instanceHasCorrectFeatureCount() {
        // Arrange        
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        List<Segment> segments = Arrays.asList(new Segment[] { segment });
        DoubleMatrix features = new DoubleMatrix(1, 9);
        int expectedNumberOfAttributes = 9 + 1; // +label

        // Act
        Instances output = instancesCreator.createInstances(segments, features);

        // Assert
        assertEquals(expectedNumberOfAttributes, output.instance(0).numAttributes());
    }

    public void test_createInstances_1Input1Features_instanceHasCorrectFeatures() {
        // Arrange        
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        List<Segment> segments = Arrays.asList(new Segment[] { segment });
        DoubleMatrix features = new DoubleMatrix(1, 1);
        features.put(0, 0, 11);
        double expected = 11;

        // Act
        Instances output = instancesCreator.createInstances(segments, features);

        // Assert
        assertEquals(expected, output.instance(0).value(1)); // 1 because index of label = 0.
    }

    public void test_createInstances_1Input1Features_instancesHasCorrectFeatureNames() {
        // Arrange        
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        List<Segment> segments = Arrays.asList(new Segment[] { segment });
        DoubleMatrix features = new DoubleMatrix(1, 2);
        String[] featureNames = { "jut", "jul" };
        segment.setFeatures(features.toArray(), featureNames);
        String expected = "jul";

        // Act
        Instances output = instancesCreator.createInstances(segments, features);

        // Assert
        assertEquals(expected, output.attribute(2).name());
    }

    public void test_createInstances_1Input1Features_labelAttributeIsNominal() {
        // Arrange        
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        List<Segment> segments = Arrays.asList(new Segment[] { segment });
        DoubleMatrix features = new DoubleMatrix(1, 1);

        // Act
        Instances output = instancesCreator.createInstances(segments, features);

        // Assert
        assertEquals(Attribute.NOMINAL, output.instance(0).classAttribute().type());
    }

    public void test_createInstances_1Input1Features_labelAttributeOrderingSymbolic() {
        // Arrange        
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        List<Segment> segments = Arrays.asList(new Segment[] { segment });
        DoubleMatrix features = new DoubleMatrix(1, 1);

        // Act
        Instances output = instancesCreator.createInstances(segments, features);

        // Assert
        Attribute attribute = output.instance(0).classAttribute();
        assertEquals(Attribute.ORDERING_SYMBOLIC, attribute.ordering());
    }

    public void test_createInstances_1Input1Features_labelCorrectNumberOfAttributes() {
        // Arrange        
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        List<Segment> segments = Arrays.asList(new Segment[] { segment });
        DoubleMatrix features = new DoubleMatrix(1, 5);

        // Act
        Instances output = instancesCreator.createInstances(segments, features);

        // Assert        
        assertEquals(6, output.numAttributes());
    }

    public void test_createInstances_1Input1Features_instanceHasIsCoupledToOriginalWindow() {
        // Arrange        
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        List<Segment> segments = Arrays.asList(new Segment[] { segment });
        DoubleMatrix features = new DoubleMatrix(1, 1);

        // Act
        Instances output = instancesCreator.createInstances(segments, features);
        CoupledInstance coupledInstance = (CoupledInstance) output.instance(0);

        // Assert        
        assertEquals(segment, coupledInstance.getCoupledSegment()); // 1 because index of label = 0.
    }

    @Override
    protected void setUp() {
        instancesCreator = new InstancesCreator();
        SchemaProvider labelMapReader = createNiceMock(SchemaProvider.class);
        expect(labelMapReader.getSchema()).andReturn(getTestMap());
        replay(labelMapReader);
        instancesCreator.setLabelMapReader(labelMapReader);
    }

    private Map<Integer, LabelDetail> getTestMap() {
        Map<Integer, LabelDetail> map = new HashMap();
        for (int i = 0; i < 5; i++) {
            map.put(i, null);
        }
        return map;
    }
}
