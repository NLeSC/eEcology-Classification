package nl.esciencecenter.eecology.classification.dataaccess;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.commands.Printer;
import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.schemaloading.SchemaRemapper;
import nl.esciencecenter.eecology.classification.schemaloading.SchemaRemappingException;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;

import org.jblas.DoubleMatrix;
import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLCell;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLStructure;

/*-  outputStruct =
 *
 *   nOfSamples: 60
 *     sampleID: [1x60 double]
 *         year: [1x60 double]
 *        month: [1x60 double]
 *          day: [1x60 double]
 *         hour: [1x60 double]
 *          min: [1x60 double]
 *          sec: [60x1 double]
 *         accX: {60x1 cell}
 *         accY: {60x1 cell}
 *         accZ: {60x1 cell}
 *         accP: []
 *         accT: []
 *         tags: {1x60 cell}
 *  annotations: [33x6 double]
 *       gpsSpd: [60x1 double]
 */

/**
 * Loads measurements from mat files. Can handle multiple mat files. For this, multiple paths can be given separated by commas.
 *
 * @author Christiaan Meijer, NLeSc
 *
 */
public class AnnotatedMeasurementsMatLoader extends MeasurementsMatLoader {
    @Inject
    private PathManager pathManager;
    @Inject
    private SchemaRemapperReader remapReader;
    @Inject
    private SchemaProvider labelMapReader;
    @Inject
    private Printer printer;

    @Inject
    @Named("label_ids_must_be_remapped")
    private boolean labelIdsMustBeRemapped;
    @Inject
    @Named("continue_when_encountering_undefined_label")
    private boolean continueAfterUndefinedLabelException;

    private final String rootKey = "outputStruct";
    private final String accXKey = "accX";
    private final String accYKey = "accY";
    private final String accZKey = "accZ";
    private final String deviceIdKey = "sampleID";
    private final String nOfSamplesKey = "nOfSamples";
    private final String yearsKey = "year";
    private final String monthsKey = "month";
    private final String daysKey = "day";
    private final String hoursKey = "hour";
    private final String minutesKey = "min";
    private final String secondsKey = "sec";
    private final String labelKey = "tags";
    private final String gpsKey = "gpsSpd";
    private List<Integer> deviceIds;
    private List<Double> gpsSpeeds;
    private boolean hasPrintedWarning;

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public void setRemapperReader(SchemaRemapperReader remapReader) {
        this.remapReader = remapReader;
    }

    public void setLabelIdsMustBeRemapped(boolean labelIdsMustBeRemapped) {
        this.labelIdsMustBeRemapped = labelIdsMustBeRemapped;
    }

    public void setLabelMapReader(SchemaProvider labelMapReader) {
        this.labelMapReader = labelMapReader;
    }

    public List<IndependentMeasurement> load() {
        return load(pathManager.getAnnotatedSourcePaths());
    }

    public void setContinueAfterUndefinedLabelException(boolean continueAfterUndefinedLabelException) {
        this.continueAfterUndefinedLabelException = continueAfterUndefinedLabelException;
    }

    /**
     * Loads measurements from mat files. Can handle multiple mat files. For this, multiple paths can be given separated by
     * commas.
     *
     * @param sourcePaths
     *            comma separated source paths
     * @return measurements
     */
    public List<IndependentMeasurement> load(List<String> sourcePaths) {
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();
        for (String sourcePath : sourcePaths) {
            measurements.addAll(loadSingleFile(sourcePath));
        }
        return measurements;
    }

    public List<IndependentMeasurement> loadSingleFile(String sourcePath) {
        String trimmedSourcePath = sourcePath.trim();
        MatFileReader matFileReader = getReader(trimmedSourcePath);
        if (matFileReader == null) {
            return new LinkedList<IndependentMeasurement>();
        }

        return getMeasurements(matFileReader, sourcePath);
    }

    private LinkedList<IndependentMeasurement> getMeasurements(MatFileReader matFileReader, String sourcePath) {
        LinkedList<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();

        try {
            MLStructure root = (MLStructure) matFileReader.getMLArray(rootKey);
            MLCell accX = (MLCell) root.getField(accXKey);
            MLCell accY = (MLCell) root.getField(accYKey);
            MLCell accZ = (MLCell) root.getField(accZKey);
            MLCell label = (MLCell) root.getField(labelKey);
            fillSampleProperyLists(root);

            int nOfSamples = (int) Math.round(((MLDouble) root.getField(nOfSamplesKey)).get(0));
            for (int i = 0; i < nOfSamples; i++) {
                double[] accXValues = getSampleRow(accX, i);
                double[] accYValues = getSampleRow(accY, i);
                double[] accZValues = getSampleRow(accZ, i);
                double[] labelValues = getSampleRow(label, i);
                List<IndependentMeasurement> newMeasurements = getMeasurementsFromSample(accXValues, accYValues, accZValues,
                        labelValues, i);
                measurements.addAll(newMeasurements);
            }
        } catch (NullPointerException e) {
            throwInvalidFormatException(sourcePath, e);
        }

        return measurements;
    }

    private void fillSampleProperyLists(MLStructure root) {
        deviceIds = getIntList(root, deviceIdKey);
        years = getIntList(root, yearsKey);
        months = getIntList(root, monthsKey);
        days = getIntList(root, daysKey);
        hours = getIntList(root, hoursKey);
        minutes = getIntList(root, minutesKey);
        seconds = getIntList(root, secondsKey);
        gpsSpeeds = getDoubleList(root, gpsKey);
    }

    private List<IndependentMeasurement> getMeasurementsFromSample(double[] accXValues, double[] accYValues, double[] accZValues,
            double[] labelValues, int sampleNumber) {
        int nOfMeasurements = accXValues.length;
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();

        SchemaRemapper remapper = null;
        if (labelIdsMustBeRemapped) {
            remapper = remapReader.readSchemaRemapper();
        }

        for (int i = 0; i < nOfMeasurements; i++) {
            createAndTryAddMeasurement(accXValues, accYValues, accZValues, labelValues, sampleNumber, measurements, remapper, i);
        }
        List<IndependentMeasurement> newMeasurements = measurements;
        return newMeasurements;
    }

    private void createAndTryAddMeasurement(double[] accXValues, double[] accYValues, double[] accZValues, double[] labelValues,
            int sampleNumber, List<IndependentMeasurement> measurements, SchemaRemapper remapper, int i) {
        double x = accXValues[i];
        double y = accYValues[i];
        double z = accZValues[i];
        DateTime timeStamp = getTimeStamp(sampleNumber);
        Integer deviceId = deviceIds.get(sampleNumber);
        Double gpsSpeed = gpsSpeeds.get(sampleNumber);
        IndependentMeasurement measurement = getUnannotatedMeasurement(x, y, z, timeStamp, deviceId, gpsSpeed, 0, 0, 0);

        try {
            int labelId = getAndValidateLabel(labelValues, remapper, i);
            measurement.setLabel(labelId);
        } catch (UndefinedLabelException e) {
            if (continueAfterUndefinedLabelException) {
                if (hasPrintedWarning == false) {
                    printer.warn(e.getMessage() + " continuing...");
                    hasPrintedWarning = true;
                }
                return;
            } else {
                throw e;
            }
        }

        if (isMeasurementValid(measurement)) {
            measurements.add(measurement);
        }
    }

    private int getAndValidateLabel(double[] labelValues, SchemaRemapper remapper, int i) {
        int labelId = (int) labelValues[i];
        if (remapper != null) {
            try {
                labelId = remapper.getRemappedId(labelId);
            } catch (SchemaRemappingException e) {
                String message = "Annotated data contains a label ("
                        + labelId
                        + ") that is not defined in the label schema remap definitions. Remove such labels from the data or add remap definitions for them to match the data.";
                throw new UndefinedLabelException(message);
            }
        } else {
            if (labelMapReader.getSchema().containsKey(labelId) == false) {
                String message = "Annotated data contains a label ("
                        + labelId
                        + ") that is not defined in the label schema. Remove such labels from the data or add them to the schema to match the data.";
                throw new UndefinedLabelException(message);
            }
        }
        return labelId;
    }

    private double[] getSampleRow(MLCell rows, int i) {
        MLDouble sampleXML = (MLDouble) rows.cells().get(i);
        double[] values = new DoubleMatrix(sampleXML.getArray()).toArray();
        return values;
    }

}
