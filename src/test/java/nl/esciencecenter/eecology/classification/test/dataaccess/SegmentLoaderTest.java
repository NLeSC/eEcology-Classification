package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import nl.esciencecenter.eecology.classification.dataaccess.SegmentLoader;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SegmentLoaderTest {
    private SegmentLoader segmentLoader;

    @Test
    public void loadSegment_objectMapperWasCalled() {
        // Arrange
        ObjectMapper objectMapper = createNiceMock(ObjectMapper.class);
        try {
            expect(objectMapper.readValue(isA(File.class), isA(TypeReference.class))).andReturn(new LinkedList<Segment>());
        } catch (IOException e) {
            e.printStackTrace();
        }
        replay(objectMapper);
        segmentLoader.setObjectMapper(objectMapper);

        // Act
        segmentLoader.loadFromJson("somepath.json");

        // Assert
        verify(objectMapper);
    }

    @Before
    public void setUp() {
        segmentLoader = new SegmentLoader();
    }
}
