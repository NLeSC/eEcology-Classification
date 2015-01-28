package nl.esciencecenter.eecology.classification.schemaloading;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaReader;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaRemapperReader;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class SchemaCombinedLazyProvider implements SchemaProvider {
    @Inject
    private SchemaReader schemaReader;
    @Inject
    private SchemaRemapperReader schemaRemapperReader;

    @Inject
    @Named("label_ids_must_be_remapped")
    private boolean labelIdsMustBeRemapped;

    private Map<Integer, LabelDetail> schema = null;
    private SchemaRemapper schemaRemapper;
    private HashMap<Integer, LabelDetail> remappedSchema;

    public void setSchemaReader(SchemaReader schemaReader) {
        this.schemaReader = schemaReader;
    }

    public void setSchemaRemapper(SchemaRemapperReader schemaRemapperReader) {
        this.schemaRemapperReader = schemaRemapperReader;
    }

    public void setLabelIdsMustBeRemapped(boolean labelIdsMustBeRemapped) {
        this.labelIdsMustBeRemapped = labelIdsMustBeRemapped;
    }

    @Override
    public Map<Integer, LabelDetail> getSchema() {
        if (schema == null) {
            schema = schemaReader.readSchema();
        }

        Map<Integer, LabelDetail> result = null;
        if (labelIdsMustBeRemapped) {
            if (remappedSchema == null) {
                remappedSchema = getRemappedSchema();
            }
            result = remappedSchema;
        } else {
            result = schema;
        }
        return result;
    }

    private HashMap<Integer, LabelDetail> getRemappedSchema() {
        if (schemaRemapper == null) {
            schemaRemapper = schemaRemapperReader.readSchemaRemapper();
        }

        Map<Integer, LabelDetail> oldSchema = schema;
        HashMap<Integer, LabelDetail> remappedSchema = new HashMap<Integer, LabelDetail>();
        for (Entry<Integer, LabelDetail> pair : oldSchema.entrySet()) {
            int originalLabelId = pair.getKey();
            LabelDetail originalLabelDetail = pair.getValue();
            int remappedLabelId = schemaRemapper.getRemappedId(originalLabelId);
            LabelDetail remappedLabelDetail = schemaRemapper.getRemapped(originalLabelDetail);
            remappedSchema.put(remappedLabelId, remappedLabelDetail);
        }
        return remappedSchema;
    }
}
