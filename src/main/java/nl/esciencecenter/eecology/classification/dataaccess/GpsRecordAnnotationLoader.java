package nl.esciencecenter.eecology.classification.dataaccess;

import java.util.List;

import nl.esciencecenter.eecology.classification.segmentloading.GpsRecordAnnotation;

public interface GpsRecordAnnotationLoader {

    List<GpsRecordAnnotation> load(String csvFileName);

}