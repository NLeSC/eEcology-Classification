package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

/**
 * Removes the mean from an input vector
 * 
 * @author Elena Ranguelova
 * @since July 2014
 * 
 */
public class ArrayMeanRemover {
    /** the input double array */
    private double[] input;
    /** the array's mean value */
    private double mean;
    /** the output array with removed mean  */
    private double[] output;

    /** class constructor
     * @return output */
    public void constructArrayMeanRemoval(double[] data) {
        input = data.clone();
        mean = 0;
        output = new double[data.length];
    }

    /** get method for the output array
     * @return output*/
    public double[] getOutput() {
        return output;
    }
    /** get method for the mean
     * 
     * @return mean
     */
    public double GetMean() {
        return mean;
    }

    /** mean removal method */
    public void removeMean() {
        if (input.length > 0) {
            calculateMean();
            for (int i = 0; i < input.length; i++) {
                output[i] = input[i] - mean;
            }
        }
    }

    /** calculate the mean method  */
    public void calculateMean() {
        double sum = 0;
        for (int i = 0; i < input.length; i++) {
            sum += input[i];
        }
        mean = sum / input.length;
    }

}
