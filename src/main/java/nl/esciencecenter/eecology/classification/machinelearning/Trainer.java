package nl.esciencecenter.eecology.classification.machinelearning;

import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 * Trains and returns a classifier with instances.
 * 
 * @author Christiaan Meijer, NLeSc
 * 
 */
public interface Trainer {

    public abstract Classifier train(Instances instances);

}
