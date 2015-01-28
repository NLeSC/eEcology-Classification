package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaToJobDirectorySaver;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SchemaToJobDirectorySaverTest {

    private SchemaToJobDirectorySaver schemaToJobDirectorySaver;
    private final String testSchemaPath = "src/test/java/resources/testschema.json";

    @Test
    public void getResultMessage_emptyInput_onlyHeaders() {
        // Arrange

        // Act
        schemaToJobDirectorySaver.saveSchemaToJobDirectory();

        // Assert
        assertTrue(Files.exists(Paths.get(testSchemaPath)));
    }

    @Before
    public void setUp() throws Exception {
        schemaToJobDirectorySaver = new SchemaToJobDirectorySaver();
        schemaToJobDirectorySaver.setSchemaProvider(new SchemaProvider() {
            @Override
            public Map<Integer, LabelDetail> getSchema() {
                return new HashMap<Integer, LabelDetail>();
            }
        });
        schemaToJobDirectorySaver.setPathManager(new PathManager() {
            @Override
            public String getSchemaJsonPath() {
                return testSchemaPath;
            }
        });
        schemaToJobDirectorySaver.setObjectMapper(new ObjectMapper());
    }

    @After
    public void cleanUp() throws IOException {
        Files.deleteIfExists(Paths.get(testSchemaPath));
    }
}
