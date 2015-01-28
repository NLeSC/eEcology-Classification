package nl.esciencecenter.eecology.classification.machinelearning;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.featureextraction.CoupledInstance;
import nl.esciencecenter.eecology.classification.machinelearning.exceptions.ClassNotDefinedException;
import nl.esciencecenter.eecology.classification.machinelearning.exceptions.ClassifierPredictionException;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;

import com.google.inject.Inject;

public class Evaluator {

    private List<Segment> misclassifications;
    private List<Segment> correctClassifications;
    private Evaluation evaluationStatistics;

    @Inject
    private SchemaProvider labelMapReader;

    public void setLabelMapReader(SchemaProvider labelMapReader) {
        this.labelMapReader = labelMapReader;
    }

    public EvaluationResult evaluate(Classifier classifier, Instances validationSet) {
        EvaluationResult evaluation = new EvaluationResult();
        makeAndValidatePredictions(classifier, validationSet);
        evaluation.setMisclassifications(misclassifications);
        evaluation.setCorrectClassifications(correctClassifications);
        evaluation.setEvaluationStatistics(evaluationStatistics);
        return evaluation;
    }

    private void makeAndValidatePredictions(Classifier classifier, Instances validationSet) {
        inititializeResultFields(validationSet);
        double[] predictions = makePredictions(classifier, validationSet, evaluationStatistics);
        for (int i = 0; i < validationSet.numInstances(); i++) {
            Instance instance = validationSet.instance(i);
            int correctClass = (int) instance.classValue();
            int predictedClass = (int) predictions[i];
            updateInstanceAndPutInField(instance, correctClass, predictedClass);
        }
    }

    private void updateInstanceAndPutInField(Instance instance, int correctClass, int predictedClassIndex) {
        Segment segment = toSegment(instance);
        String classAsString = instance.classAttribute().value(predictedClassIndex);
        int predictedLabelId = Integer.parseInt(classAsString);
        segment.setPredictedLabelDetail(labelMapReader.getSchema().get(predictedLabelId));
        putInField(segment, correctClass, predictedClassIndex);
    }

    private Segment toSegment(Instance instance) {
        if (instance instanceof CoupledInstance == false) {
            throw new InvalidParameterException("Invalid instance: no measurement segment is coupled.");
        }
        CoupledInstance coupledInstance = (CoupledInstance) instance;
        Segment coupledSegment = coupledInstance.getCoupledSegment();
        return coupledSegment;
    }

    private void putInField(Segment segment, int correctClass, int predictedClass) {
        if (predictedClass == correctClass) {
            correctClassifications.add(segment);
        } else {
            misclassifications.add(segment);
        }
    }

    private void inititializeResultFields(Instances validationSet) {
        misclassifications = new LinkedList<Segment>();
        correctClassifications = new LinkedList<Segment>();
        createNewEvaluationStatistics(validationSet);
    }

    private void createNewEvaluationStatistics(Instances validationSet) {
        try {
            evaluationStatistics = new Evaluation(validationSet);
        } catch (Exception e) {
            throw new ClassNotDefinedException(e);
        }
    }

    private double[] makePredictions(Classifier classifier, Instances validationSet, Evaluation evaluation) {
        double[] predictions = null;
        try {
            predictions = evaluation.evaluateModel(classifier, validationSet);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ClassifierPredictionException(
                    "Error applying the trained classifier to the train instances. The number of features of the instance exceeds the number of features the classifier was trained on.",
                    e);
        } catch (Exception e) {
            throw new ClassifierPredictionException("Error applying the trained classifier to the test instances.", e);
        }
        return predictions;
    }
}
