package nl.esciencecenter.eecology.classification.segmentloading;

import java.util.List;

import nl.esciencecenter.eecology.classification.dataaccess.AnnotatedGpsRecordSupplier;
import nl.esciencecenter.eecology.classification.dataaccess.AnnotatedMeasurementsMatLoader;
import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordSupplier;
import nl.esciencecenter.eecology.classification.dataaccess.UnannotatedMeasurementsLoader;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class SegmentProvider {
    @Inject
    private AnnotatedMeasurementsMatLoader annotatedMeasurementsMatLoader;
    @Inject
    private Segmenter segmenter;
    @Inject
    private AnnotatedGpsRecordSupplier annotatedGpsRecordSupplier;
    @Inject
    private GpsRecordSegmenter gpsRecordSegmenter;
    @Inject
    private UnannotatedMeasurementsLoader unannotatedMeasurementLoader;
    @Inject
    private GpsRecordSupplier gpsRecordSupplier;

    @Inject
    @Named("use_gps_records_instead_of_accelerometer_data")
    private boolean useGpsRecordsInsteadOfAccelerometerData;

    public void setGpsRecordSupplier(GpsRecordSupplier gpsRecordSupplier) {
        this.gpsRecordSupplier = gpsRecordSupplier;
    }

    public void setAnnotatedMeasurementsMatLoader(AnnotatedMeasurementsMatLoader annotatedMeasurementsMatLoader) {
        this.annotatedMeasurementsMatLoader = annotatedMeasurementsMatLoader;
    }

    public void setSegmenter(Segmenter segmenter) {
        this.segmenter = segmenter;
    }

    public void setUseGpsRecordsInsteadOfAccelerometerData(boolean useGpsRecordsInsteadOfAccelerometerData) {
        this.useGpsRecordsInsteadOfAccelerometerData = useGpsRecordsInsteadOfAccelerometerData;
    }

    public void setAnnotatedGpsRecordSupplier(AnnotatedGpsRecordSupplier gpsRecordWithAnnotationsSupplier) {
        annotatedGpsRecordSupplier = gpsRecordWithAnnotationsSupplier;
    }

    public void setGpsRecordSegmenter(GpsRecordSegmenter gpsRecordSegmenter) {
        this.gpsRecordSegmenter = gpsRecordSegmenter;
    }

    public void setUnannotatedMeasurementLoader(UnannotatedMeasurementsLoader unannotatedMeasurementLoader) {
        this.unannotatedMeasurementLoader = unannotatedMeasurementLoader;
    }

    public List<Segment> getAnnotatedSegments() {
        if (useGpsRecordsInsteadOfAccelerometerData) {
            return getAnnotatedSegmentsFromGpsRecords();
        }
        return getAnnotatedSegmentsFromAccelerometerData();
    }

    public List<Segment> getUnannotatedSegments() {
        if (useGpsRecordsInsteadOfAccelerometerData) {
            List<GpsRecord> gpsRecords = gpsRecordSupplier.getGpsRecords();
            return gpsRecordSegmenter.createSegmentsIgnoringLabel(gpsRecords);
        }
        List<IndependentMeasurement> measurements = unannotatedMeasurementLoader.load();
        List<Segment> classificationSegments = segmenter.createSegmentsIgnoringLabel(measurements);
        return classificationSegments;
    }

    private List<Segment> getAnnotatedSegmentsFromAccelerometerData() {
        List<IndependentMeasurement> measurements = annotatedMeasurementsMatLoader.load();
        return segmenter.createLabeledSegments(measurements);
    }

    private List<Segment> getAnnotatedSegmentsFromGpsRecords() {
        List<GpsRecord> gpsRecords = annotatedGpsRecordSupplier.getGpsRecords();
        return gpsRecordSegmenter.createLabeledSegments(gpsRecords);
    }

}
