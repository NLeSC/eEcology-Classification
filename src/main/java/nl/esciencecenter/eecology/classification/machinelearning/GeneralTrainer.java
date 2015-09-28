package nl.esciencecenter.eecology.classification.machinelearning;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Named;

import nl.esciencecenter.eecology.classification.machinelearning.exceptions.ClassifierBuildingException;

import org.apache.commons.exec.CommandLine;

import weka.classifiers.Classifier;
import weka.core.Instances;

import com.google.inject.Inject;

public class GeneralTrainer implements Trainer {

    private final String classifierName;
    private final String[] classifierOptions;

    @Inject
    public GeneralTrainer(@Named("classifier_string") String classifierString) {
        CommandLine commandLine = CommandLine.parse(classifierString);
        String classifierClass = commandLine.getExecutable();
        String[] options = getClassifierOptions(commandLine);
        classifierOptions = options;
        classifierName = classifierClass;
    }

    @Override
    public Classifier train(Instances instances) {
        Classifier classifier = getClassifier();
        try {
            classifier.buildClassifier(instances);
        } catch (Exception e) {
            throw new ClassifierBuildingException("Exception occured while building classifier: " + e.getMessage(), e);
        }
        return classifier;
    }

    private Classifier getClassifier() {
        try {
            return Classifier.forName(classifierName, classifierOptions);
        } catch (Exception e) {
            throw new ClassifierBuildingException("Exception occured while loading classifier: " + e.getMessage(), e);
        }
    }

    /**
     * Takes a commandline and returns it's arguments, stripped from outer double qoutes if any.
     * 
     * @param commandLine
     * @return
     */
    private String[] getClassifierOptions(CommandLine commandLine) {
        String[] arguments = commandLine.getArguments();
        Stream<String> optionStream = Arrays.stream(arguments).map(a -> trimOuterQuotes(a));
        return optionStream.collect(Collectors.toList()).toArray(new String[arguments.length]);
    }

    private String trimOuterQuotes(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }

        String firstChar = str.substring(0, 1);
        String lastChar = str.substring(str.length() - 1);
        if (firstChar.equals("\"") && lastChar.equals("\"")) {
            return str.subSequence(1, str.length() - 1).toString();
        }

        return str;
    }

}
