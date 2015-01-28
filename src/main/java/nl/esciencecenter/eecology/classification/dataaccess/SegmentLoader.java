package nl.esciencecenter.eecology.classification.dataaccess;

import java.io.File;
import java.io.IOException;
import java.util.List;

import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

public class SegmentLoader {
    @Inject
    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Segment> loadFromJson(String path) {
        List<Segment> segments;
        try {
            segments = objectMapper.readValue(new File(path), new TypeReference<List<Segment>>() {
            });
        } catch (JsonParseException | JsonMappingException e) {
            String message = "The file containing segments (" + path + ") doesn't appear to be in the correct format.";
            throw new RuntimeException(message, e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading from the file containing segments (" + path + ").", e);
        }
        return segments;
    }
}
