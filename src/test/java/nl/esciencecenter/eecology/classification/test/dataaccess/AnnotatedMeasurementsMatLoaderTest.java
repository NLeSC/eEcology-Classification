package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nl.esciencecenter.eecology.classification.commands.Printer;
import nl.esciencecenter.eecology.classification.dataaccess.AnnotatedMeasurementsMatLoader;
import nl.esciencecenter.eecology.classification.dataaccess.LoadingMeasurementsException;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaRemapperReader;
import nl.esciencecenter.eecology.classification.dataaccess.UndefinedLabelException;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;
import nl.esciencecenter.eecology.classification.schemaloading.SchemaRemapper;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

public class AnnotatedMeasurementsMatLoaderTest {

    private final Path path = Paths.get("src/test/java/resources/annotatedmeasurements/");
    private final String testFile1Name = path.resolve("test_annotated_data.mat").toString();
    private final String testFile2Name = path.resolve("test_annotated_data2.mat").toString();
    private final String malformattedName = path.resolve("malformatted.mat").toString();
    private AnnotatedMeasurementsMatLoader measurementsMatLoader;
    private final double delta = 0.0001;
    private final int testFile1MeasurementCount = 130 * 60 + 43 + 55 + 30 + 10 + 10; //according to matlab
    private final int testFile2MeasurementCount = testFile1MeasurementCount;
    private Map<Integer, LabelDetail> schema;
    private SchemaRemapper remapper;

    @Test(expected = LoadingMeasurementsException.class)
    public void load_nonExistentPath_exceptionThrown() {
        // Arrange

        // Act
        measurementsMatLoader.load(getListWith("nonExistent.path"));

        // Assert
    }

    @Test
    public void load_testFilePathWithTrailingSpace_noExceptionThrown() {
        // Arrange

        // Act
        measurementsMatLoader.load(getListWith(testFile1Name + " "));

        // Assert
    }

    @Test
    public void load_testFilePath_returnsCorrectNumberOfMeasurements() {
        // Arrange
        int expectedSize = testFile1MeasurementCount;

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.load(getListWith(testFile1Name));

        // Assert
        assertEquals(expectedSize, output.size());
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectX() {
        // Arrange
        double expected = -0.0465; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.load(getListWith(testFile1Name));

        // Assert
        assertTrue(Math.abs(expected - output.get(0).getX()) < delta);
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectY() {
        // Arrange
        double expected = -0.0176; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.load(getListWith(testFile1Name));

        // Assert
        assertTrue(Math.abs(expected - output.get(0).getY()) < delta);
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectZ() {
        // Arrange
        double expected = 1.0081; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.load(getListWith(testFile1Name));

        // Assert
        assertTrue(Math.abs(expected - output.get(0).getZ()) < delta);
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectTime() {
        // Arrange
        DateTime expected = new DateTime(2010, 6, 9, 0, 11, 7, DateTimeZone.UTC); //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.load(getListWith(testFile1Name));

        // Assert
        assertEquals(expected, output.get(0).getTimeStamp());
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectDeviceId() {
        // Arrange
        int expected = 320; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.load(getListWith(testFile1Name));

        // Assert
        assertEquals(expected, output.get(0).getDeviceId());
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectLabel() {
        // Arrange
        int expected = 5; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.load(getListWith(testFile1Name));

        // Assert
        assertEquals(expected, output.get(0).getLabel());
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectGpsSpd() {
        // Arrange
        double expected = 0.1175; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.load(getListWith(testFile1Name));

        // Assert
        assertTrue(Math.abs(expected - output.get(0).getGpsSpeed()) < delta);
    }

    @Test
    public void load_testFilePath_returnsLastCorrectX() {
        // Arrange
        double expected = -0.4048; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.load(getListWith(testFile1Name));

        // Assert
        assertTrue(Math.abs(expected - output.get(output.size() - 1).getX()) < delta);
    }

    @Test
    public void load_2pathsCommaSeperated_returnDoubleResultNumber() {
        // Arrange
        List<String> pathList = new LinkedList<String>(Arrays.asList(new String[] { testFile1Name, testFile2Name }));
        int expectedTotal = testFile1MeasurementCount + testFile2MeasurementCount;

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.load(pathList);

        // Assert
        assertEquals(expectedTotal, output.size());
    }

    @Test
    public void load_remapperSet_returnsFirstRemappedLabel() {
        // Arrange
        remapper.setRemapping(5, "new", 3); // in test file, label of first instance is 5
        measurementsMatLoader.setLabelIdsMustBeRemapped(true);

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.load(getListWith(testFile1Name));

        // Assert
        assertEquals(3, output.get(0).getLabel());
    }

    @Test(expected = UndefinedLabelException.class)
    public void load_dataContainsLabelsNotInRemapper_throwRuntimeException() {
        // Arrange
        measurementsMatLoader.setLabelIdsMustBeRemapped(true);

        // Act
        measurementsMatLoader.load(getListWith(testFile1Name));

        // Assert
    }

    @Test(expected = UndefinedLabelException.class)
    public void load_labelNotInSchema_throwError() {
        // Arrange
        schema.clear();

        // Act
        measurementsMatLoader.load(getListWith(testFile1Name));

        // Assert
    }

    @Test
    public void load_allLabelsNotInSchemaAndContinueIsTrue_noResults() {
        // Arrange
        schema.clear();
        measurementsMatLoader.setContinueAfterUndefinedLabelException(true);

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.load(getListWith(testFile1Name));

        // Assert
        assertEquals(0, output.size());
    }

    @Test(expected = LoadingMeasurementsException.class)
    public void load_fileIsMalformatted_noResults() {
        measurementsMatLoader.load(getListWith(malformattedName));
    }

    @Before
    public void setUp() throws Exception {
        measurementsMatLoader = new AnnotatedMeasurementsMatLoader();
        setSchema();
        setRemapper();
        measurementsMatLoader.setPrinter(new Printer() {
            @Override
            public void print(String message) {
            }

            @Override
            public void warn(String message) {
            }
        });
    }

    private void setRemapper() {
        SchemaRemapperReader remapReader = createNiceMock(SchemaRemapperReader.class);
        remapper = new SchemaRemapper();
        expect(remapReader.readSchemaRemapper()).andReturn(remapper);
        replay(remapReader);
        measurementsMatLoader.setRemapperReader(remapReader);
    }

    private void setSchema() {
        schema = new HashMap<Integer, LabelDetail>();
        for (int i = 0; i < 100; i++) {
            LabelDetail labelDetail = new LabelDetail();
            labelDetail.setLabelId(i);
            schema.put(i, labelDetail);
        }

        SchemaProvider labelMapReader = createNiceMock(SchemaProvider.class);
        expect(labelMapReader.getSchema()).andReturn(schema).anyTimes();
        replay(labelMapReader);
        measurementsMatLoader.setLabelMapReader(labelMapReader);
    }

    private List<String> getListWith(String string) {
        List<String> list = new LinkedList<String>();
        list.add(string);
        return list;
    }
}
