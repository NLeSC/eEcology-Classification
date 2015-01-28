package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import nl.esciencecenter.eecology.classification.dataaccess.SchemaCustomCsvReader;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaReader;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;

import org.junit.Before;
import org.junit.Test;

public class SchemaCustomCsvReaderTest {
    private final String path = "src/test/java/resources/";
    private final String testFileNameSimple = path + "testschema.txt";
    protected final String testFileNameWithComments = path + "testschemawithcomments.txt";
    protected final String testFileNameWithEmptyLines = path + "testschemawithemptylines.txt";
    protected final String testFileNameDoubleDigit = path + "testschemadoubledigit.txt";
    protected final String testFileNameInvalidDouble = path + "testschemawithinvaliddouble.txt";

    protected SchemaReader labelMapReader;
    protected final double delta = 0.00001;

    @Test(expected = RuntimeException.class)
    public void read_nonExistent_throwException() {
        // Arrange

        // Act
        labelMapReader.readSchema("non_existant_path.txt");

        // Assert
    }

    @Test
    public void read_testFilePath_resultSizeIs9() {
        // Arrange

        // Act
        Map<Integer, LabelDetail> labelMap = labelMapReader.readSchema(testFileNameSimple);

        // Assert
        assertEquals(9, labelMap.size());
    }

    @Test
    public void read_testFilePath_mapContainsKeyFromFile() {
        // Arrange

        // Act
        Map<Integer, LabelDetail> labelMap = labelMapReader.readSchema(testFileNameSimple);

        // Assert
        assertTrue(labelMap.containsKey(5));
    }

    @Test
    public void read_testFilePath_mapHasCorrectDescription() {
        // Arrange

        // Act
        Map<Integer, LabelDetail> labelMap = labelMapReader.readSchema(testFileNameSimple);

        // Assert        
        LabelDetail labelDetails = labelMap.get(4);
        assertEquals("Stand", labelDetails.getDescription());
    }

    @Test
    public void read_testFilePath_mapHasCorrectColorR() {
        // Arrange

        // Act
        Map<Integer, LabelDetail> labelMap = labelMapReader.readSchema(testFileNameSimple);

        // Assert        
        LabelDetail labelDetails = labelMap.get(3);
        assertEquals(0.75, labelDetails.getColorR(), delta);
    }

    @Test
    public void read_testFilePath_mapHasCorrectColorG() {
        // Arrange

        // Act
        Map<Integer, LabelDetail> labelMap = labelMapReader.readSchema(testFileNameSimple);

        // Assert        
        LabelDetail labelDetails = labelMap.get(7);
        assertEquals(0.40, labelDetails.getColorG(), delta);
    }

    @Test
    public void read_testFilePath_mapHasCorrectColorB() {
        // Arrange

        // Act
        Map<Integer, LabelDetail> labelMap = labelMapReader.readSchema(testFileNameSimple);

        // Assert        
        LabelDetail labelDetails = labelMap.get(9);
        assertEquals(1.00, labelDetails.getColorB(), delta);
    }

    @Test
    public void read_testFilePathWithEmptyLines_mapHasCorrectDescription() {
        // Arrange

        // Act
        Map<Integer, LabelDetail> labelMap = labelMapReader.readSchema(testFileNameWithEmptyLines);

        // Assert        
        LabelDetail labelDetails = labelMap.get(4);
        assertEquals("Stand", labelDetails.getDescription());
    }

    @Test
    public void read_testFilePathWithDoubleDigits_mapHasCorrectDescription() {
        // Arrange

        // Act
        Map<Integer, LabelDetail> labelMap = labelMapReader.readSchema(testFileNameDoubleDigit);

        // Assert        
        LabelDetail labelDetails = labelMap.get(11);
        assertEquals("Other", labelDetails.getDescription());
    }

    @Test
    public void read_testFilePathWithComments_mapHasCorrectDescription() {
        // Arrange

        // Act
        Map<Integer, LabelDetail> labelMap = labelMapReader.readSchema(testFileNameWithComments);

        // Assert        
        LabelDetail labelDetails = labelMap.get(4);
        assertEquals("Stand", labelDetails.getDescription());
    }

    @Test
    public void read_testFilePathWithInvalidDoubles_resultSizeIsOneLess() {
        // Arrange

        // Act
        Map<Integer, LabelDetail> labelMap = labelMapReader.readSchema(testFileNameInvalidDouble);

        // Assert
        assertEquals(8, labelMap.size());
    }

    @Before
    public void setUp() {
        labelMapReader = new SchemaCustomCsvReader();
    }
}
