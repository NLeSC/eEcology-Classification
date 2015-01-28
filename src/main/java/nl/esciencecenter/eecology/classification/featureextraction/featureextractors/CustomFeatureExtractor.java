package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import java.util.HashMap;
import java.util.Map;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

public class CustomFeatureExtractor extends FeatureExtractor {
    MeanExtractor meanExtractor;
    StdExtractor stdExtractor;

    private String name = "";
    private String expression;

    public CustomFeatureExtractor(String name, String expression) {
        setName(name);
        setExpression(expression);
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public void setMeanExtractor(MeanExtractor meanExtractor) {
        this.meanExtractor = meanExtractor;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setStdExtractor(StdExtractor stdExtractor) {
        this.stdExtractor = stdExtractor;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        DoubleMatrix result = new DoubleMatrix(formattedSegments.getNumberOfRows(), 1);
        ExpressionBuilder expressionBuilder = new ExpressionBuilder(expression);
        for (int i = 0; i < formattedSegments.getNumberOfRows(); i++) {
            Map<String, Double> variableMap = getVariableMap(formattedSegments, i);
            double featureValue = getFeatureValue(expressionBuilder, variableMap);
            result.put(i, 0, featureValue);
        }
        return result;
    }

    private double getFeatureValue(ExpressionBuilder expressionBuilder, Map<String, Double> variableMap) {
        expressionBuilder.withVariables(variableMap);
        Calculable calc = null;
        try {
            calc = expressionBuilder.build();
        } catch (UnknownFunctionException | UnparsableExpressionException e) {
            String message = "Expression for feature extractor (" + name + ") is not valid.";
            throw new InvalidExpressionException(message, e);
        }
        return calc.calculate();
    }

    private Map<String, Double> getVariableMap(FormattedSegments formattedSegments, int segmentNumber) {
        Map<String, Double> variableMap = new HashMap<String, Double>();
        addRowVectorToMap(variableMap, formattedSegments.getX().getRow(segmentNumber), "x");
        addRowVectorToMap(variableMap, formattedSegments.getY().getRow(segmentNumber), "y");
        addRowVectorToMap(variableMap, formattedSegments.getZ().getRow(segmentNumber), "z");
        addRowVectorToMap(variableMap, formattedSegments.getGpsSpeed().getRow(segmentNumber), "speed");
        addRowVectorToMap(variableMap, formattedSegments.getLongitude().getRow(segmentNumber), "long");
        addRowVectorToMap(variableMap, formattedSegments.getLatitude().getRow(segmentNumber), "lat");
        addRowVectorToMap(variableMap, formattedSegments.getAltitude().getRow(segmentNumber), "alt");
        return variableMap;
    }

    private void addRowVectorToMap(Map<String, Double> variableMap, DoubleMatrix rowVector, String vectorName) {
        for (int i = 0; i < rowVector.columns; i++) {
            variableMap.put(vectorName + (i + 1), rowVector.get(0, i));
        }
        double mean = meanExtractor.extractMean(rowVector).get(0, 0);
        variableMap.put("mean" + vectorName, mean);
        double standardDeviation = stdExtractor.extractStd(rowVector).get(0, 0);
        variableMap.put("std" + vectorName, standardDeviation);
    }

}
