package nl.esciencecenter.eecology.classification.dataaccess;

import java.util.Map;

import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;

public interface SchemaReader {
    public Map<Integer, LabelDetail> readSchema();

    public Map<Integer, LabelDetail> readSchema(String path);

}
