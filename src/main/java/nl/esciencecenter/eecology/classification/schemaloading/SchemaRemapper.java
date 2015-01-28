package nl.esciencecenter.eecology.classification.schemaloading;

import java.util.HashMap;
import java.util.Map;

import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;

public class SchemaRemapper {
    private final Map<Integer, String> oldIdToNewDescription = new HashMap<Integer, String>();
    private final Map<Integer, Integer> oldIdToNewId = new HashMap<Integer, Integer>();

    public LabelDetail getRemapped(LabelDetail labelDetail) {
        int oldId = labelDetail.getLabelId();
        throwIfIdIsUnknown(oldId);
        labelDetail.setDescription(oldIdToNewDescription.get(oldId));
        labelDetail.setLabelId(oldIdToNewId.get(oldId));
        return labelDetail;
    }

    public void setRemapping(int oldId, String newDescription, int newId) {
        oldIdToNewDescription.put(oldId, newDescription);
        oldIdToNewId.put(oldId, newId);
    }

    public int getRemappedId(int id) {
        throwIfIdIsUnknown(id);
        return oldIdToNewId.get(id);
    }

    private void throwIfIdIsUnknown(int oldId) {
        if (oldIdToNewId.containsKey(oldId) == false) {
            throw new SchemaRemappingException(String.format("Exception trying to remap unknown class '" + oldId + "'."));
        }
    }
}
