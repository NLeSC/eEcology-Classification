package nl.esciencecenter.eecology.classification.dataaccess;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

public class SchemaToJobDirectorySaver {
    @Inject
    private ObjectMapper objectMapper;
    @Inject
    public PathManager pathManager;
    @Inject
    public SchemaProvider schemaProvider;

    public SchemaToJobDirectorySaver() {
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
    }

    public void setSchemaProvider(SchemaProvider schemaProvider) {
        this.schemaProvider = schemaProvider;
    }

    public void saveSchemaToJobDirectory() {
        writeToJson(schemaProvider.getSchema(), pathManager.getSchemaJsonPath());
    }

    private void writeToJson(Map<Integer, LabelDetail> map, String fileName) {
        try {
            objectMapper.writeValue(new File(fileName), map);
        } catch (JsonGenerationException | JsonMappingException e) {
            throw new RuntimeException("Error serializing schema map to write to file (" + fileName + ").", e);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file (" + fileName + ").", e);
        }
    }

}