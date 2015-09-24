package nl.esciencecenter.eecology.classification.test.machinelearning;

import static org.junit.Assert.assertEquals;
import nl.esciencecenter.eecology.classification.machinelearning.Trainer;
import nl.esciencecenter.eecology.classification.machinelearning.exceptions.ClassifierBuildingException;

import org.junit.Test;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public abstract class TrainerTest {
    protected final double delta = 0.00001d;
    protected Trainer trainer;

    @Test(expected = ClassifierBuildingException.class)
    public void train_exceptionDuringTraining_classifierBuildingException() {
        // Arrange
        FastVector emptyAttInfo = new FastVector();
        Instances invalidInstances = new Instances("name", emptyAttInfo, 0);

        // Act
        trainer.train(invalidInstances);

        // Assert
    }

    @Test
    public void train_2Instances_canClassify() {
        // Arrange
        Instances instances = getTestInstances();

        // Act
        Classifier classifier = trainer.train(instances);
        try {
            classifier.classifyInstance(instances.firstInstance());
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }

        // Assert
    }

    @Test
    public void train_2Instances_classifierClassifiesCorrect() {
        // Arrange
        Instances instances = getTestInstances();
        instances.firstInstance().setClassValue(0d);
        double expected = 0d;

        // Act
        double classification = -1;
        Classifier classifier = trainer.train(instances);
        try {
            classification = classifier.classifyInstance(instances.firstInstance());
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }

        // Assert
        assertEquals(expected, classification, delta);
    }

    protected Instances getTestInstances() {
        FastVector attInfo = new FastVector();
        Attribute classAttribute = getAttribute("class", "a", "b");
        attInfo.addElement(classAttribute);
        Attribute attribute = new Attribute("my_numerical_attribute");
        attInfo.addElement(attribute);
        Instances instances = new Instances("test", attInfo, 0);
        instances.setClass(classAttribute);
        instances.add(getTestInstance(instances, 0, 1));
        instances.add(getTestInstance(instances, 0, 1));
        instances.add(getTestInstance(instances, 1, 0));
        instances.add(getTestInstance(instances, 1, 0));
        return instances;
    }

    private Instance getTestInstance(Instances instances, double value, double label) {
        Instance instance = new Instance(1, new double[] { label, value });
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

}
