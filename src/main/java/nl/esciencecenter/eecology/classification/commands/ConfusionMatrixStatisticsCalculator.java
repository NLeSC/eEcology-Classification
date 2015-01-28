package nl.esciencecenter.eecology.classification.commands;

import org.jblas.DoubleMatrix;

public class ConfusionMatrixStatisticsCalculator {

    public double[] getRecallVector(double[][] confusionMatrix) {
        DoubleMatrix doubleMatrix = new DoubleMatrix(confusionMatrix);
        double[] rowSums = doubleMatrix.rowSums().toArray();
        double[] recallVector = new double[confusionMatrix.length];
        for (int i = 0; i < confusionMatrix.length; i++) {
            recallVector[i] = confusionMatrix[i][i] / rowSums[i];
        }
        return recallVector;
    }

    public double[] getPrecisionVector(double[][] confusionMatrix) {
        DoubleMatrix doubleMatrix = new DoubleMatrix(confusionMatrix);
        double[] columnSums = doubleMatrix.columnSums().toArray();
        double[] precisionVector = new double[confusionMatrix.length];
        for (int i = 0; i < confusionMatrix.length; i++) {
            precisionVector[i] = confusionMatrix[i][i] / columnSums[i];
        }
        return precisionVector;
    }
}