package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.MapFeatureExtractorFileNotFoundException;
import nl.esciencecenter.eecology.classification.dataaccess.MapFeatureExtractorLoader;
import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

public class MapFeatureExtractorLoaderTest {

    private final Path testPath = Paths.get("src/test/java/resources/mapfeatures");
    private final Path features2Path = testPath.resolve("2features.csv");

    private MapFeatureExtractorLoader mapFeatureExtractorLoader;
    private PathManager pathManager;
    private final double errorMargin = 0.00001;

    @Test
    public void getMapFeatureExtractors_pathIsNull_zeroResults() {
        // Arrange
        setTestFilePath(null);

        // Act
        List<FeatureExtractor> mapFeatureExtractors = mapFeatureExtractorLoader.getMapFeatureExtractors();

        // Assert
        assertEquals(0, mapFeatureExtractors.size());
    }

    @Test
    public void getMapFeatureExtractors_pathIsEmpty_zeroResults() {
        // Arrange
        setTestFilePath("");

        // Act
        List<FeatureExtractor> mapFeatureExtractors = mapFeatureExtractorLoader.getMapFeatureExtractors();

        // Assert
        assertEquals(0, mapFeatureExtractors.size());
    }

    @Test
    public void getMapFeatureExtractors_pathIsIdenticalToDataFolder_zeroResults() {
        // Arrange
        setTestFilePathAndDataPath(testPath.toString(), testPath.toString());

        // Act
        List<FeatureExtractor> mapFeatureExtractors = mapFeatureExtractorLoader.getMapFeatureExtractors();

        // Assert
        assertEquals(0, mapFeatureExtractors.size());
    }

    @Test(expected = MapFeatureExtractorFileNotFoundException.class)
    public void getMapFeatureExtractors_pathIsNonExistent_throwError() {
        // Arrange
        setTestFilePath("somePathThatDoesNotExist123654");

        // Act
        mapFeatureExtractorLoader.getMapFeatureExtractors();
    }

    @Test
    public void getMapFeatureExtractors_2Columns_2Results() {
        // Arrange
        setTestFilePath(features2Path.toString());

        // Act
        List<FeatureExtractor> mapFeatureExtractors = mapFeatureExtractorLoader.getMapFeatureExtractors();

        // Assert
        assertEquals(2, mapFeatureExtractors.size());
    }

    @Test
    public void getMapFeatureExtractors_2Columns_resultsContainCorrectNames() {
        // Arrange
        setTestFilePath(features2Path.toString());

        // Act
        List<FeatureExtractor> mapFeatureExtractors = mapFeatureExtractorLoader.getMapFeatureExtractors();

        // Assert
        List<String> featureNames = mapFeatureExtractors.stream().map(m -> m.getName()).collect(Collectors.toList());
        assertTrue("The results do not contain a feature with the expected name 'testfeature1'.",
                featureNames.contains("testfeature1"));
        assertTrue("The results do not contain a feature with the expected name 'testfeature2'.",
                featureNames.contains("testfeature2"));
    }

    @Test
    public void getMapFeatureExtractors_2Columns_resultsContainCorrectColumnNames() {
        // Arrange
        setTestFilePath(features2Path.toString());

        // Act
        List<FeatureExtractor> mapFeatureExtractors = mapFeatureExtractorLoader.getMapFeatureExtractors();

        // Assert
        List<String> columnNames = mapFeatureExtractors.stream().flatMap(m -> m.getColumnNames().stream())
                .collect(Collectors.toList());
        assertTrue("The results do not contain a feature with the expected column name 'testfeature1'.",
                columnNames.contains("testfeature1"));
        assertTrue("The results do not contain a feature with the expected column name 'testfeature2'.",
                columnNames.contains("testfeature2"));
    }

    @Test
    public void getMapFeatureExtractors_2Columns_resultingFeatureMapsCorrectlyByTimeStamp() {
        // Arrange
        setTestFilePath(features2Path.toString());
        DoubleMatrix x = new DoubleMatrix(2, 0);
        DoubleMatrix g = new DoubleMatrix(2, 0);
        DateTime[][] timeStamps = { { new DateTime(2015, 2, 2, 9, 46, DateTimeZone.UTC) },
                { new DateTime(2015, 2, 2, 9, 47, DateTimeZone.UTC) } };
        int[][] deviceIds = { { 48 }, { 43 } };
        FormattedSegments formattedSegments = new FormattedSegments(x, x, x, g, g, g, g, timeStamps, deviceIds);

        // Act
        List<FeatureExtractor> mapFeatureExtractors = mapFeatureExtractorLoader.getMapFeatureExtractors();
        FeatureExtractor map = mapFeatureExtractors.stream().filter(m -> m.getName().equalsIgnoreCase("testfeature1"))
                .collect(Collectors.toList()).get(0);
        DoubleMatrix extractFeatures = map.extractFeatures(formattedSegments);

        // Assert
        assertEquals("Feature of first instance is incorrect.", 15.3, extractFeatures.get(0, 0), errorMargin);
        assertEquals("Feature of second instance is incorrect.", -2.4, extractFeatures.get(1, 0), errorMargin);
    }

    @Before
    public void setUp() {
        mapFeatureExtractorLoader = new MapFeatureExtractorLoader();
        pathManager = createNiceMock(PathManager.class);
        mapFeatureExtractorLoader.setPathManager(pathManager);
    }

    private void setTestFilePath(String fileName) {
        setTestFilePathAndDataPath(fileName, testPath.toString());
    }

    private void setTestFilePathAndDataPath(String fileName, String dataPath) {
        expect(pathManager.getMapFeaturesPath()).andReturn(fileName).anyTimes();
        expect(pathManager.getDataPath()).andReturn(dataPath).anyTimes();
        replay(pathManager);
    }

}
