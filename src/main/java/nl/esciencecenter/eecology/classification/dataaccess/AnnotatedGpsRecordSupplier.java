package nl.esciencecenter.eecology.classification.dataaccess;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.segmentloading.GpsRecord;

public class AnnotatedGpsRecordSupplier extends GpsRecordSupplier {
    @Override
    public List<GpsRecord> getGpsRecords(String gpsRecordsPath, String annotationsPath) {
        List<GpsRecord> allGpsRecords = super.getGpsRecords(gpsRecordsPath, annotationsPath);
        return getLabeledRecordsFromList(allGpsRecords);
    }

    private LinkedList<GpsRecord> getLabeledRecordsFromList(List<GpsRecord> allGpsRecords) {
        LinkedList<GpsRecord> labeledRecords = new LinkedList<GpsRecord>();
        for (GpsRecord gpsRecord : allGpsRecords) {
            if (gpsRecord.isLabeled() == true) {
                labeledRecords.add(gpsRecord);
            }
        }
        return labeledRecords;
    }
}
