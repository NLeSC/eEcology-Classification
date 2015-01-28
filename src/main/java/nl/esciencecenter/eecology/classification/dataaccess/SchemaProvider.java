package nl.esciencecenter.eecology.classification.dataaccess;

import java.util.Map;

import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;

public interface SchemaProvider {
    public Map<Integer, LabelDetail> getSchema();

}