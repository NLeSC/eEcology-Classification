package nl.esciencecenter.eecology.classification.commands;

import java.util.List;

import com.google.inject.Inject;

public class ConfusionMatrixFormatter {
    @Inject
    private ConfusionMatrixStatisticsCalculator confusionMatrixStatisticsCalculator;

    private int margin;
    private final String actualText = "Actual:";
    private int cellWidth = 8;
    private final String correctText = "Correct:";

    public void setConfusionMatrixStatisticsCalculator(ConfusionMatrixStatisticsCalculator confusionMatrixStatisticsCalculator) {
        this.confusionMatrixStatisticsCalculator = confusionMatrixStatisticsCalculator;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }

    public String format(double[][] confusionMatrix, List<String> labels) {
        StringBuilder message = new StringBuilder();
        setMargin(labels);
        appendHeader(labels, message);
        double[] recallVector = confusionMatrixStatisticsCalculator.getRecallVector(confusionMatrix);
        for (int i = 0; i < confusionMatrix.length; i++) {
            message.append(String.format("%-" + margin + "s", labels.get(i)));
            appendDataRow(confusionMatrix[i], message, recallVector[i]);
            message.append("\n");
        }
        appendFooter(confusionMatrix, message);
        return message.toString();
    }

    private void appendFooter(double[][] confusionMatrix, StringBuilder message) {
        int numberOfLabels = confusionMatrix.length;
        message.append(String.format(getMarginText("")));
        for (int i = 0; i < cellWidth * numberOfLabels; i++) {
            message.append("-");
        }
        message.append("\n");

        message.append(getMarginText(correctText));
        double[] precisionRow = confusionMatrixStatisticsCalculator.getPrecisionVector(confusionMatrix);
        for (int i = 0; i < numberOfLabels; i++) {
            message.append(getCellText(toPercentage(precisionRow[i])));
        }
        message.append("\n");
    }

    private String toPercentage(double fracture) {
        int integer = (int) Math.round(100 * fracture);
        return Integer.toString(integer) + "%";
    }

    private String getMarginText(String text) {
        return String.format("%-" + margin + "s", text);
    }

    private String getCellText(String text) {
        return String.format("%-" + cellWidth + "s", text);
    }

    private void appendDataRow(double[] dataRow, StringBuilder message, double correctColumn2) {
        for (int i = 0; i < dataRow.length; i++) {
            String cell = getCellText(Integer.toString((int) dataRow[i]));
            message.append(cell);
        }
        message.append(toPercentage(correctColumn2));
    }

    private void appendHeader(List<String> labels, StringBuilder message) {
        message.append("Confusion matrix\n");
        for (int i = 0; i < margin; i++) {
            message.append(" ");
        }
        message.append("Predicted classes:\n");
        message.append(String.format("%-" + margin + "s", actualText));
        for (String label : labels) {
            String header = label.substring(0, Math.min(cellWidth - 1, label.length()));
            message.append(String.format("%-" + cellWidth + "s", header));
        }
        message.append("\n");
    }

    private void setMargin(List<String> labels) {
        margin = Math.max(actualText.length(), correctText.length()) + 1;
        for (String label : labels) {
            int labelMinimumMargin = label.length() + 1;
            if (labelMinimumMargin > margin) {
                margin = labelMinimumMargin;
            }
        }
    }

}
