package nl.esciencecenter.eecology.classification.dataaccess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.jblas.DoubleMatrix;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLStructure;

public abstract class MeasurementsMatLoader extends MeasurementsLoader {
    protected List<Integer> years;
    protected List<Integer> months;
    protected List<Integer> days;
    protected List<Integer> hours;
    protected List<Integer> minutes;
    protected List<Integer> seconds;

    protected List<Double> getDoubleList(MLStructure root, String key) {
        MLDouble mlDouble = (MLDouble) root.getField(key);
        double[] primitiveArray = new DoubleMatrix(mlDouble.getArray()).toArray();
        Double[] objectArray = ArrayUtils.toObject(primitiveArray);
        return Arrays.asList(objectArray);
    }

    protected List<Integer> getIntList(MLStructure root, String key) {
        List<Double> doubleList = getDoubleList(root, key);
        List<Integer> results = new LinkedList<Integer>();
        for (int i = 0; i < doubleList.size(); i++) {
            double currentDouble = doubleList.get(i);
            results.add((int) currentDouble);
        }
        return results;
    }

    protected DateTime getTimeStamp(int sampleNumber) {
        int year = years.get(sampleNumber);
        int month = months.get(sampleNumber);
        int day = days.get(sampleNumber);
        int hour = hours.get(sampleNumber);
        int minute = minutes.get(sampleNumber);
        int second = seconds.get(sampleNumber);
        DateTime dateTime = new DateTime(year, month, day, hour, minute, second, DateTimeZone.UTC);
        return dateTime;
    }

    protected MatFileReader getReader(String sourcePath) {
        MatFileReader matFileReader = null;
        try {
            matFileReader = new MatFileReader(sourcePath);
        } catch (FileNotFoundException e) {
            throwFileNotFoundException(sourcePath, e);
        } catch (IOException e) {
            throwGeneralLoadingException(sourcePath, e);
        }
        return matFileReader;
    }

    private void throwFileNotFoundException(String sourcePath, FileNotFoundException e) {
        String message = "The source file was not found at '" + sourcePath + "'.";
        throw new LoadingMeasurementsException(message, e);
    }

    protected void throwGeneralLoadingException(String sourcePath, Throwable e) {
        String message = "Could not read from source file '" + sourcePath + "'.";
        throw new LoadingMeasurementsException(message, e);
    }

    protected void throwInvalidFormatException(String sourcePath, Throwable e) {
        String message = "The source file at '" + sourcePath + "' does not have the expected format.";
        throw new LoadingMeasurementsException(message, e);
    }

}