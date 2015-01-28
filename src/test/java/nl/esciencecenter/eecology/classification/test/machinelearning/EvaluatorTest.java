package nl.esciencecenter.eecology.classification.test.machinelearning;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.featureextraction.CoupledInstance;
import nl.esciencecenter.eecology.classification.machinelearning.EvaluationResult;
import nl.esciencecenter.eecology.classification.machinelearning.Evaluator;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;
import nl.esciencecenter.eecology.classification.machinelearning.exceptions.ClassifierPredictionException;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class EvaluatorTest {

    private Evaluator evaluator;
    private final double delta = 0.0001;

    @Test
    public void evaluate_trainedClassifier_evaluationReportNoMisclassifications() {
        // Arrange
        Instances validationSet = getTestCoupledInstances();
        Classifier classifier = getTrainedClassifier(validationSet);

        // Act
        EvaluationResult evaluation = evaluator.evaluate(classifier, validationSet);

        // Assert
        assertEquals(0, evaluation.getMisclassifications().size());
    }

    @Test
    public void evaluate_trainedClassifier_evaluationReport2CorrectClassifications() {
        // Arrange
        Instances validationSet = getTestCoupledInstances();
        Classifier classifier = getTrainedClassifier(validationSet);

        // Act
        EvaluationResult evaluation = evaluator.evaluate(classifier, validationSet);

        // Assert
        assertEquals(2, evaluation.getCorrectClassifications().size());
    }

    @Test
    public void evaluate_oneChangedInstance_evaluationReport1Misclassifications() {
        // Arrange
        Instances validationSet = getTestCoupledInstances();
        Classifier classifier = getTrainedClassifier(validationSet);
        validationSet.firstInstance().setClassValue(0); // was 1 while training

        // Act
        EvaluationResult evaluation = evaluator.evaluate(classifier, validationSet);

        // Assert
        assertEquals(1, evaluation.getMisclassifications().size());
    }

    @Test
    public void evaluate_firstInstanceIs0_firstMissclassificationSegmentPredictionIs0() {
        // Arrange
        Instances validationSet = getTestCoupledInstances();
        Classifier classifier = getTrainedClassifier(validationSet);
        validationSet.firstInstance().setClassValue(0); // was 1 while training

        // Act
        EvaluationResult evaluation = evaluator.evaluate(classifier, validationSet);
        Segment segment = evaluation.getMisclassifications().get(0);

        // Assert
        assertEquals(1, segment.getPredictedLabel());
    }

    @Test
    public void evaluate_coupledInstance_hasPrediction() {
        // Arrange
        Instances validationSet = getTestCoupledInstances();
        Classifier classifier = getTrainedClassifier(validationSet);
        validationSet.firstInstance().setClassValue(0); // was 1 while training

        // Act
        EvaluationResult evaluation = evaluator.evaluate(classifier, validationSet);
        Segment segment = evaluation.getMisclassifications().get(0);

        // Assert
        assertTrue(segment.hasPrediction());
    }

    @Test
    public void evaluate_oneChangedInstance_evaluationReport50PctErrorRate() {
        // Arrange
        Instances validationSet = getTestCoupledInstances();
        Classifier classifier = getTrainedClassifier(validationSet);
        validationSet.firstInstance().setClassValue(0); // was 1 while training

        // Act
        EvaluationResult evaluation = evaluator.evaluate(classifier, validationSet);

        // Assert
        assertEquals(0.5, evaluation.getEvaluationStatistics().errorRate(), delta);
    }

    @Test
    public void evaluate_labelsAreStringsOfRandomInts_firstInstancePredictionIsSameAsLabel() {
        // Arrange
        FastVector attInfo = new FastVector();
        Attribute classAttribute = getAttribute("class", "4", "3");
        attInfo.addElement(classAttribute);
        Attribute attribute = new Attribute("my_numerical_attribute");
        attInfo.addElement(attribute);
        Instances instances = new Instances("test", attInfo, 0);
        instances.setClass(classAttribute);
        instances.add(getTestCoupledInstance(instances, "3", 42));
        instances.add(getTestCoupledInstance(instances, "4", 44));

        Classifier classifier = getTrainedClassifier(instances);

        // Act
        EvaluationResult evaluation = evaluator.evaluate(classifier, instances);

        // Assert
        Segment segment = evaluation.getCorrectClassifications().get(0);
        assertEquals(segment.getLabel(), segment.getPredictedLabel());
    }

    @Test(expected = ClassifierPredictionException.class)
    public void evaluate_untrainedClassifier_throwClassifierPredictionException() {
        // Arrange
        Instances validationSet = getTestCoupledInstances();
        Classifier untrainedClassifier = new IBk();

        // Act
        EvaluationResult evaluation = evaluator.evaluate(untrainedClassifier, validationSet);

        // Assert
        assertEquals(1, evaluation.getMisclassifications().size());
    }

    @Test(expected = ClassifierPredictionException.class)
    public void evaluate_classifierTrainedOnMoreFeatures_throwClassifierPredictionException() {
        // Arrange
        Instances trainSetWith2Features = getTestCoupledInstances();
        Classifier classifierTrainedOn2Features = getTrainedClassifier(trainSetWith2Features);

        FastVector attInfo = new FastVector();
        Attribute classAttribute = getAttribute("class", "4", "3");
        attInfo.addElement(classAttribute);
        attInfo.addElement(new Attribute("my_numerical_attribute"));
        attInfo.addElement(new Attribute("my_numerical_attribute2"));
        Instances instancesWith3Features = new Instances("test", attInfo, 0);
        instancesWith3Features.setClass(classAttribute);

        CoupledInstance instanceWith3Features = new CoupledInstance(1, new double[] { 0, 2, 3 });
        instanceWith3Features.setDataset(instancesWith3Features);
        instanceWith3Features.setCoupledObject(new Segment(new LinkedList<IndependentMeasurement>()));
        instanceWith3Features.setClassValue(1);
        instanceWith3Features.setDataset(instancesWith3Features);
        instancesWith3Features.add(instanceWith3Features);

        // Act
        EvaluationResult evaluation = evaluator.evaluate(classifierTrainedOn2Features, instancesWith3Features);

        // Assert
    }

    @Before
    public void setUp() {
        evaluator = new Evaluator();
        SchemaProvider labelMapReader = createNiceMock(SchemaProvider.class);
        expect(labelMapReader.getSchema()).andReturn(getMap()).anyTimes();
        replay(labelMapReader);
        evaluator.setLabelMapReader(labelMapReader);
    }

    private Map<Integer, LabelDetail> getMap() {
        HashMap<Integer, LabelDetail> map = new HashMap<Integer, LabelDetail>();
        for (int i = 0; i < 10; i++) {
            LabelDetail labelDetail = new LabelDetail();
            labelDetail.setLabelId(i);
            map.put(i, labelDetail);
        }
        return map;
    }

    private Instances getTestCoupledInstances() {
        Instances instances = createEmptyTestInstances();
        instances.add(getTestCoupledInstance(instances, 1, 0));
        instances.add(getTestCoupledInstance(instances, 0, 1));
        return instances;
    }

    private Instances createEmptyTestInstances() {
        FastVector attInfo = new FastVector();
        Attribute classAttribute = getAttribute("class", "0", "1");
        attInfo.addElement(classAttribute);
        Attribute attribute = new Attribute("my_numerical_attribute");
        attInfo.addElement(attribute);
        Instances instances = new Instances("test", attInfo, 0);
        instances.setClass(classAttribute);
        return instances;
    }

    private Instance getTestCoupledInstance(Instances instances, double label, double value) {
        CoupledInstance instance = new CoupledInstance(1, new double[] { label, value });
        instance.setCoupledObject(new Segment(new LinkedList<IndependentMeasurement>()));
        instance.setDataset(instances);
        instance.setClassValue(label);
        return instance;
    }

    private Instance getTestCoupledInstance(Instances instances, String label, double value) {
        CoupledInstance instance = new CoupledInstance(1, new double[] { 0, value });
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        LabelDetail labelDetail = new LabelDetail();
        labelDetail.setLabelId(Integer.parseInt(label));
        segment.setLabelDetail(labelDetail);
        instance.setCoupledObject(segment);
        instance.setDataset(instances);
        instance.setClassValue(label);
        return instance;
    }

    private Attribute getAttribute(String name, String val1, String val2) {
        FastVector attributeValues = new FastVector();
        attributeValues.addElement(val1);
        attributeValues.addElement(val2);
        Attribute classAttribute = new Attribute(name, attributeValues);
        return classAttribute;
    }

    private Classifier getTrainedClassifier(Instances validationSet) {
        Classifier classifier = new IBk(); // IBk is k-nearest neighbor
        try {
            classifier.buildClassifier(validationSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return classifier;
    }
}
