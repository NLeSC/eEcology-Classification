package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.RemapFileNotFoundException;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaRemapperReader;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;
import nl.esciencecenter.eecology.classification.schemaloading.SchemaRemapper;

import org.junit.Before;
import org.junit.Test;

public class RemapReaderTest {
    private final String path = "src/test/java/resources/";
    private final String testFilePath = path + "testremapschema.txt";
    private SchemaRemapperReader remapReader;
    private PathManager pathManager;

    @Test(expected = RemapFileNotFoundException.class)
    public void read_nonExistingPath_throwException() {
        // Arrange
        setLabelRemapPath("thisPathDoesNotEx.ist");

        // Act
        remapReader.readSchemaRemapper();

        // Assert        
    }

    @Test
    public void read_testPath_correctRemappedDescription() {
        // Arrange
        setLabelRemapPath(testFilePath);
        String expected = "Walk";

        // Act
        SchemaRemapper remapper = remapReader.readSchemaRemapper();
        LabelDetail remapped = remapper.getRemapped(getLabelDetail(3, "loop"));

        // Assert        
        assertEquals(expected, remapped.getDescription());
    }

    @Test
    public void read_testPath_correctRemappedLabelId() {
        // Arrange
        setLabelRemapPath(testFilePath);
        int expected = 2;

        // Act
        SchemaRemapper remapper = remapReader.readSchemaRemapper();
        LabelDetail remapped = remapper.getRemapped(getLabelDetail(3, "loop"));

        // Assert        
        assertEquals(expected, remapped.getLabelId());
    }

    @Test
    public void read_testPathManyToOneMapping_correctRemappedLabelId() {
        // Arrange
        setLabelRemapPath(testFilePath);
        int expected = 5;

        // Act
        SchemaRemapper remapper = remapReader.readSchemaRemapper();
        LabelDetail remapped = remapper.getRemapped(getLabelDetail(8, "Soar"));

        // Assert        
        assertEquals(expected, remapped.getLabelId());
    }

    @Before
    public void setUp() {
        remapReader = new SchemaRemapperReader();

    }

    private LabelDetail getLabelDetail(int label, String description) {
        LabelDetail labelDetail = new LabelDetail();
        labelDetail.setDescription(description);
        labelDetail.setLabelId(label);
        labelDetail.setColorR(0.5);
        labelDetail.setColorG(0.5);
        labelDetail.setColorB(0.5);
        return labelDetail;
    }

    private void setLabelRemapPath(String path) {
        pathManager = createNiceMock(PathManager.class);
        expect(pathManager.getLabelRemapPath()).andReturn(path);
        replay(pathManager);
        remapReader.setPathManager(pathManager);
    }

}
