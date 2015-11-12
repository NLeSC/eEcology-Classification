package nl.esciencecenter.eecology.classification.test.segmentloading;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.LinkedList;

import nl.esciencecenter.eecology.classification.dataaccess.AnnotatedGpsRecordSupplier;
import nl.esciencecenter.eecology.classification.dataaccess.AnnotatedMeasurementsMatLoader;
import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordSupplier;
import nl.esciencecenter.eecology.classification.dataaccess.UnannotatedMeasurementsLoader;
import nl.esciencecenter.eecology.classification.machinelearning.DatasetSplitter;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecord;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecordSegmenter;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.SegmentProvider;
import nl.esciencecenter.eecology.classification.segmentloading.Segmenter;

import org.junit.Before;
import org.junit.Test;

public class SegmentProviderTest {
    private SegmentProvider segmentProvider;

    @Test
    public void getAnnotatedSegments_useAccel_annotatedMeasurementsMatLoaderWasCalled() {
        // Arrange
        AnnotatedMeasurementsMatLoader annotatedMeasurementsMatLoader = createMock(AnnotatedMeasurementsMatLoader.class);
        expect(annotatedMeasurementsMatLoader.load()).andReturn(null);
        replay(annotatedMeasurementsMatLoader);
        segmentProvider.setAnnotatedMeasurementsMatLoader(annotatedMeasurementsMatLoader);
        segmentProvider.setUseGpsRecordsInsteadOfAccelerometerData(false);

        // Act
        segmentProvider.getAnnotatedSegments();

        // Assert
        verify(annotatedMeasurementsMatLoader);
    }

    @Test
    public void getAnnotatedSegments_useAccel_gpsSupplierWasNotCalled() {
        // Arrange
        AnnotatedGpsRecordSupplier gpsAnnotatedGpsRecordSupplier = createMock(AnnotatedGpsRecordSupplier.class);
        replay(gpsAnnotatedGpsRecordSupplier);
        segmentProvider.setAnnotatedGpsRecordSupplier(gpsAnnotatedGpsRecordSupplier);
        segmentProvider.setUseGpsRecordsInsteadOfAccelerometerData(false);

        // Act
        segmentProvider.getAnnotatedSegments();

        // Assert
        verify(gpsAnnotatedGpsRecordSupplier);
    }

    @Test
    public void getAnnotatedSegments_useGpsRecords_gpsSupplierWasCalled() {
        // Arrange
        AnnotatedGpsRecordSupplier gpsAnnotatedGpsRecordSupplier = createMock(AnnotatedGpsRecordSupplier.class);
        expect(gpsAnnotatedGpsRecordSupplier.getGpsRecords()).andReturn(null);
        replay(gpsAnnotatedGpsRecordSupplier);
        segmentProvider.setAnnotatedGpsRecordSupplier(gpsAnnotatedGpsRecordSupplier);
        segmentProvider.setUseGpsRecordsInsteadOfAccelerometerData(true);

        // Act
        segmentProvider.getAnnotatedSegments();

        // Assert
        verify(gpsAnnotatedGpsRecordSupplier);
    }

    @Test
    public void getAnnotatedSegments_useGpsRecords_annotatedMeasurementsMatLoaderWasNotCalled() {
        // Arrange
        AnnotatedMeasurementsMatLoader annotatedMeasurementsMatLoader = createMock(AnnotatedMeasurementsMatLoader.class);
        replay(annotatedMeasurementsMatLoader);
        segmentProvider.setAnnotatedMeasurementsMatLoader(annotatedMeasurementsMatLoader);
        segmentProvider.setUseGpsRecordsInsteadOfAccelerometerData(true);

        // Act
        segmentProvider.getAnnotatedSegments();

        // Assert
        verify(annotatedMeasurementsMatLoader);
    }

    @Test
    public void getUnannotatedSegments_useAccel_unannotatedMeasurementsMatLoaderWasCalled() {
        // Arrange
        UnannotatedMeasurementsLoader unannotatedLoader = createMock(UnannotatedMeasurementsLoader.class);
        expect(unannotatedLoader.load()).andReturn(new LinkedList<IndependentMeasurement>());
        replay(unannotatedLoader);
        segmentProvider.setUnannotatedMeasurementLoader(unannotatedLoader);
        segmentProvider.setUseGpsRecordsInsteadOfAccelerometerData(false);

        // Act
        segmentProvider.getUnannotatedSegments();

        // Assert
        verify(unannotatedLoader);
    }

    @Test
    public void getUnannotatedSegments_useGpsRecords_unannotatedMeasurementsLoaderWasNotCalled() {
        // Arrange
        UnannotatedMeasurementsLoader unannotatedLoader = createMock(UnannotatedMeasurementsLoader.class);
        replay(unannotatedLoader);
        segmentProvider.setUnannotatedMeasurementLoader(unannotatedLoader);
        segmentProvider.setUseGpsRecordsInsteadOfAccelerometerData(true);

        // Act
        segmentProvider.getUnannotatedSegments();

        // Assert
        verify(unannotatedLoader);
    }

    @Test
    public void getUnannotatedSegments_useGpsRecords_annotatedGpsSupplierWasNotCalled() {
        // Arrange
        AnnotatedGpsRecordSupplier gpsAnnotatedGpsRecordSupplier = createMock(AnnotatedGpsRecordSupplier.class);
        replay(gpsAnnotatedGpsRecordSupplier);
        segmentProvider.setAnnotatedGpsRecordSupplier(gpsAnnotatedGpsRecordSupplier);
        segmentProvider.setUseGpsRecordsInsteadOfAccelerometerData(true);

        // Act
        segmentProvider.getUnannotatedSegments();

        // Assert
        verify(gpsAnnotatedGpsRecordSupplier);
    }

    @Test
    public void getUnannotatedSegments_useGpsRecords_gpsSupplierWasCalled() {
        // Arrange
        GpsRecordSupplier gpsGpsRecordSupplier = createMock(GpsRecordSupplier.class);
        expect(gpsGpsRecordSupplier.getGpsRecords()).andReturn(new LinkedList<GpsRecord>());
        replay(gpsGpsRecordSupplier);
        segmentProvider.setGpsRecordSupplier(gpsGpsRecordSupplier);
        segmentProvider.setUseGpsRecordsInsteadOfAccelerometerData(true);

        // Act
        segmentProvider.getUnannotatedSegments();

        // Assert
        verify(gpsGpsRecordSupplier);
    }

    @Before
    public void setUp() {
        segmentProvider = new SegmentProvider();
        segmentProvider.setAnnotatedMeasurementsMatLoader(createNiceMock(AnnotatedMeasurementsMatLoader.class));
        segmentProvider.setSegmenter(createNiceMock(Segmenter.class));
        DatasetSplitter datasetSplitter = createNiceMock(DatasetSplitter.class);
        replay(datasetSplitter);
        segmentProvider.setAnnotatedGpsRecordSupplier(createNiceMock(AnnotatedGpsRecordSupplier.class));
        segmentProvider.setGpsRecordSegmenter(createNiceMock(GpsRecordSegmenter.class));
        segmentProvider.setAnnotatedMeasurementsMatLoader(createNiceMock(AnnotatedMeasurementsMatLoader.class));
        segmentProvider.setGpsRecordSupplier(createNiceMock(GpsRecordSupplier.class));
        segmentProvider.setUnannotatedMeasurementLoader(createNiceMock(UnannotatedMeasurementsLoader.class));
    }

}