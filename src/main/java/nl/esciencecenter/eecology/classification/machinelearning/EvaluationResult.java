package nl.esciencecenter.eecology.classification.machinelearning;

import java.util.List;

import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import weka.classifiers.Evaluation;

public class EvaluationResult {

    private List<Segment> misclassifications;
    private List<Segment> correctClassifications;
    private Evaluation evaluationStatistics;

    public void setMisclassifications(List<Segment> misclassifications) {
        this.misclassifications = misclassifications;
    }

    public List<Segment> getMisclassifications() {
        return misclassifications;
    }

    public void setCorrectClassifications(List<Segment> correctClassifications) {
        this.correctClassifications = correctClassifications;
    }

    public List<Segment> getCorrectClassifications() {
        return correctClassifications;
    }

    public void setEvaluationStatistics(Evaluation evaluationStatistics) {
        this.evaluationStatistics = evaluationStatistics;
    }

    public Evaluation getEvaluationStatistics() {
        return evaluationStatistics;
    }
}
