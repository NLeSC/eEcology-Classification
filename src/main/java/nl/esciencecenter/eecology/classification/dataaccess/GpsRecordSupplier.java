package nl.esciencecenter.eecology.classification.dataaccess;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecord;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecordAnnotation;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecordDto;

import org.joda.time.DateTime;

import com.google.inject.Inject;

public class GpsRecordSupplier {
    @Inject
    private GpsRecordAnnotationLoader annotationLoader;
    @Inject
    private GpsRecordDtoCsvLoader recordLoader;
    @Inject
    private PathManager pathManager;

    public void setAnnotationLoader(GpsRecordAnnotationLoader annotationLoader) {
        this.annotationLoader = annotationLoader;
    }

    public void setRecordLoader(GpsRecordDtoCsvLoader recordLoader) {
        this.recordLoader = recordLoader;
    }

    public List<GpsRecord> getGpsRecords() {
        return getGpsRecords(pathManager.getGpsRecordsPath(), pathManager.getGpsRecordAnnotationsPath());
    }

    public List<GpsRecord> getGpsRecords(String gpsRecordsPath, String annotationsPath) {
        HashMap<String, Integer> labelsByIdTimeStamp = getLabelsByIdTimeStampMap(annotationsPath);
        List<GpsRecord> gpsRecords = getGpsRecordsByLabelMap(gpsRecordsPath, labelsByIdTimeStamp);
        return gpsRecords;
    }

    private List<GpsRecord> getGpsRecordsByLabelMap(String gpsRecordsPath, HashMap<String, Integer> labelsByIdTimeStamp) {
        List<GpsRecord> gpsRecords = new LinkedList<GpsRecord>();
        List<GpsRecordDto> gpsRecordDtos = recordLoader.load(gpsRecordsPath);
        for (GpsRecordDto gpsRecordDto : gpsRecordDtos) {
            GpsRecord gpsRecord = createGpsRecordFromDto(gpsRecordDto, labelsByIdTimeStamp);

            gpsRecords.add(gpsRecord);
        }
        return gpsRecords;
    }

    private GpsRecord createGpsRecordFromDto(GpsRecordDto gpsRecordDto, HashMap<String, Integer> labelsByIdTimeStamp) {
        GpsRecord gpsRecord = new GpsRecord(gpsRecordDto);

        Integer labelId = labelsByIdTimeStamp.get(getIdTimeStamp(gpsRecordDto.getDeviceId(), gpsRecordDto.getTimeStamp()));
        if (labelId != null) {
            gpsRecord.setLabel(labelId);
        }
        return gpsRecord;
    }

    private HashMap<String, Integer> getLabelsByIdTimeStampMap(String annotationsPath) {
        List<GpsRecordAnnotation> annotationList = annotationLoader.load(annotationsPath);
        HashMap<String, Integer> labelsByIdTimeStamp = new HashMap<String, Integer>();
        for (GpsRecordAnnotation annotation : annotationList) {
            String idTimeStamp = getIdTimeStamp(annotation.getDeviceId(), annotation.getTimeStamp());
            labelsByIdTimeStamp.put(idTimeStamp, annotation.getAnnotation());
        }
        return labelsByIdTimeStamp;
    }

    private String getIdTimeStamp(int id, DateTime timeStamp) {
        String separator = ";";
        String idTimeStamp = id + separator + timeStamp.getMillis();
        return idTimeStamp;
    }

}
