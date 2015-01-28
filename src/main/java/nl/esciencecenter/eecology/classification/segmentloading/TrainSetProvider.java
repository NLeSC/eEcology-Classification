package nl.esciencecenter.eecology.classification.segmentloading;

import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.SegmentLoader;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class TrainSetProvider {
    @Inject
    private SegmentLoader segmentLoader;
    @Inject
    private FixedNumberDatasetFilter fixedNumberDataSetFilter;
    @Inject
    private PathManager pathManager;

    @Inject
    @Named("train_on_fixed_class_numbers")
    private boolean isClassInstancesNumberFixed;

    public void setClassInstancesNumberFixed(boolean isClassInstancesNumberFixed) {
        this.isClassInstancesNumberFixed = isClassInstancesNumberFixed;
    }

    public void setFixedNumberDataSetFilter(FixedNumberDatasetFilter fixedNumberDataSetFilter) {
        this.fixedNumberDataSetFilter = fixedNumberDataSetFilter;
    }

    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
    }

    public void setSegmentLoader(SegmentLoader segmentLoader) {
        this.segmentLoader = segmentLoader;
    }

    public List<Segment> getTrainSet() {
        List<Segment> loadedTrainSegments = segmentLoader.loadFromJson(pathManager.getTrainSetPath());
        if (isClassInstancesNumberFixed) {
            return fixedNumberDataSetFilter.filter(loadedTrainSegments);
        }
        return loadedTrainSegments;
    }

}
