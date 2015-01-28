package nl.esciencecenter.eecology.classification.dataaccess;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;

import com.google.inject.Inject;

public class UnannotatedMeasurementsLoader {
    @Inject
    private PathManager pathManager;
    @Inject
    private UnannotatedMeasurementsCsvLoader unannotatedMeasurementsCsvLoader;
    @Inject
    private UnannotatedMeasurementsMatLoader unannotatedMeasurementsMatLoader;

    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
    }

    public void setUnannotatedMeasurementsCsvLoader(UnannotatedMeasurementsCsvLoader unannotatedMeasurementsCsvLoader) {
        this.unannotatedMeasurementsCsvLoader = unannotatedMeasurementsCsvLoader;
    }

    public void setUnannotatedMeasurementsMatLoader(UnannotatedMeasurementsMatLoader unannotatedMeasurementsMatLoader) {
        this.unannotatedMeasurementsMatLoader = unannotatedMeasurementsMatLoader;
    }

    /**
     * Loads unannotated measurements from files.
     *
     */
    public List<IndependentMeasurement> load() {
        return load(pathManager.getUnannotatedSourcePaths());
    }

    public List<IndependentMeasurement> load(List<String> unannotatedSourcePaths) {
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();
        for (String sourcePath : unannotatedSourcePaths) {
            measurements.addAll(loadFromSingleSource(sourcePath));
        }
        return measurements;
    }

    private List<IndependentMeasurement> loadFromSingleSource(String sourcePath) {
        String[] elements = sourcePath.split("\\.");
        String extension = elements[elements.length - 1].toLowerCase();
        switch (extension) {
        case "mat":
            return unannotatedMeasurementsMatLoader.loadFromSingleSource(sourcePath);
        case "csv":
            return unannotatedMeasurementsCsvLoader.loadFromSingleSource(sourcePath);
        default:
            return unannotatedMeasurementsCsvLoader.loadFromSingleSource(sourcePath);
        }
    }
}
