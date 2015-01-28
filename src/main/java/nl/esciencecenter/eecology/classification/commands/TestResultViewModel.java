package nl.esciencecenter.eecology.classification.commands;

import java.util.Collection;

import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;

public class TestResultViewModel {

    private double[][] confusionMatrix;
    private Collection<LabelDetail> labelSchema;
    private double errorRate;
    private double[] precisionVector;
    private double[] recallVector;
    private int instancesCorrect;
    private int instancesIncorrect;
    private double percentageCorrect;
    private double percentageIncorrect;
    private double kappa;
    private String classifierDescription;

    public double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
    }

    public double[][] getConfusionMatrix() {
        return confusionMatrix;
    }

    public void setConfusionMatrix(double[][] confusionMatrix) {
        this.confusionMatrix = confusionMatrix;
    }

    public Collection<LabelDetail> getLabelSchema() {
        return labelSchema;
    }

    public void setLabelSchema(Collection<LabelDetail> labelSchema) {
        this.labelSchema = labelSchema;
    }

    public void setPrecisionVector(double[] precisionVector) {
        this.precisionVector = precisionVector;
    }

    public double[] getPrecisionVector() {
        return precisionVector;
    }

    public void setRecallVector(double[] recallVector) {
        this.recallVector = recallVector;
    }

    public double[] getRecallVector() {
        return recallVector;
    }

    public void setInstancesCorrect(int instancesCorrect) {
        this.instancesCorrect = instancesCorrect;
    }

    public int getInstancesCorrect() {
        return instancesCorrect;
    }

    public void setInstancesIncorrect(int instancesIncorrect) {
        this.instancesIncorrect = instancesIncorrect;
    }

    public int getInstancesIncorrect() {
        return instancesIncorrect;
    }

    public void setPercentageCorrect(double percentageCorrect) {
        this.percentageCorrect = percentageCorrect;
    }

    public double getPercentageCorrect() {
        return percentageCorrect;
    }

    public void setPercentageIncorrect(double percentageIncorrect) {
        this.percentageIncorrect = percentageIncorrect;
    }

    public double getPercentageIncorrect() {
        return percentageIncorrect;
    }

    public void setKappa(double kappa) {
        this.kappa = kappa;
    }

    public double getKappa() {
        return kappa;
    }

    public void setClassifierDescription(String classifierDescription) {
        this.classifierDescription = classifierDescription;
    }

    public String getClassifierDescription() {
        return classifierDescription;
    }

}
