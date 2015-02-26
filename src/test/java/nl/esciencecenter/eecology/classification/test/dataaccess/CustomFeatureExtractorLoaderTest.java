package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.CustomFeatureExtractorLoader;
import nl.esciencecenter.eecology.classification.dataaccess.CustomFeatureFileMalformedException;
import nl.esciencecenter.eecology.classification.dataaccess.CustomFeatureFileNotFoundException;
import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.CustomFeatureExtractor;

import org.junit.Before;
import org.junit.Test;

public class CustomFeatureExtractorLoaderTest {

    private final String path = "src/test/java/resources/customfeatures/";
    private final String testFileName = path + "testcustomfeaturesnovariables.txt";
    private final String fileWithIncorrectFormatName = path + "testinvalidcustomfeatures.txt";
    private CustomFeatureExtractorLoader customFeatureExtractorLoader;
    private PathManager pathManager;

    @Test
    public void getCustomFeatureExtractors_noFileSet_zeroResults() {
        // Arrange

        // Act
        List<FeatureExtractor> customFeatureExtractors = customFeatureExtractorLoader.getCustomFeatureExtractors();

        // Assert
        assertEquals(0, customFeatureExtractors.size());
    }

    @Test
    public void getCustomFeatureExtractors_testFile_correctFeatureExtractorNumber() {
        // Arrange
        setTestFilePath(testFileName);

        // Act
        List<FeatureExtractor> customFeatureExtractors = customFeatureExtractorLoader.getCustomFeatureExtractors();

        // Assert
        assertEquals(2, customFeatureExtractors.size());
    }

    @Test(expected = CustomFeatureFileNotFoundException.class)
    public void getCustomFeatureExtractors_nonExistantFile_throwCorrectException() {
        // Arrange
        setTestFilePath(path + "nonexistentfilename1234");

        // Act
        customFeatureExtractorLoader.getCustomFeatureExtractors();

        // Assert
    }

    @Test
    public void getCustomFeatureExtractors_testFile_firstExtractorHasCorrectName() {
        // Arrange
        setTestFilePath(testFileName);

        // Act
        List<FeatureExtractor> customFeatureExtractors = customFeatureExtractorLoader.getCustomFeatureExtractors();

        // Assert
        assertEquals("testfeature1", customFeatureExtractors.get(0).getName());
    }

    @Test
    public void getCustomFeatureExtractors_testFile_secondExtractorHasCorrectExpression() {
        // Arrange
        setTestFilePath(testFileName);

        // Act
        List<FeatureExtractor> customFeatureExtractors = customFeatureExtractorLoader.getCustomFeatureExtractors();

        // Assert
        CustomFeatureExtractor featureExtractor = (CustomFeatureExtractor) customFeatureExtractors.get(1);
        assertEquals("13 + 5 + 96", featureExtractor.getExpression());
    }

    @Test(expected = CustomFeatureFileMalformedException.class)
    public void getCustomFeatureExtractors_invalidFileFormat_secondExtractorHasCorrectExpression() {
        // Arrange
        setTestFilePath(fileWithIncorrectFormatName);

        // Act
        customFeatureExtractorLoader.getCustomFeatureExtractors();
    }

    @Before
    public void setUp() {
        customFeatureExtractorLoader = new CustomFeatureExtractorLoader();
        pathManager = createNiceMock(PathManager.class);
        customFeatureExtractorLoader.setPathmanager(pathManager);
    }

    private void setTestFilePath(String fileName) {
        expect(pathManager.getCustomFeaturePath()).andReturn(fileName).anyTimes();
        replay(pathManager);
    }

}
