package nl.esciencecenter.eecology.classification.test.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PathManagerTest {
    private PathManagerFixture pathManager;

    @Test
    public void getPathList_onePath_returnPath() {
        // Arrange
        String onePath = "data";

        // Act
        List<String> output = pathManager.getDataPathList(onePath);

        // Assert
        assertTrue(output.get(0).equals("data/" + onePath));
    }

    @Test
    public void getPathList_twoPaths_returnCorrectNumberOfPaths() {
        // Arrange
        String twoPaths = "data,output";

        // Act
        List<String> output = pathManager.getDataPathList(twoPaths);

        // Assert
        assertEquals(2, output.size());
    }

    @Test
    public void getPathList_twoPaths_returnCorrectSecondPath() {
        // Arrange
        String twoPaths = "data,output";

        // Act
        List<String> output = pathManager.getDataPathList(twoPaths);

        // Assert
        assertTrue(output.get(1).equals("data/output"));
    }

    @Test
    public void getPathList_onePathWithTrailingSpace_returnPath() {
        // Arrange
        String onePath = "data ";

        // Act
        List<String> output = pathManager.getDataPathList(onePath);

        // Assert
        assertTrue(output.get(0).equals("data/data"));
    }

    @Test
    public void getPathList_multiplePathsWithLotsOfSpaces_returnPath() {
        // Arrange
        String onePath = "    what, a , mess, this is ";

        // Act
        List<String> output = pathManager.getDataPathList(onePath);

        // Assert
        assertTrue(output.get(3).equals("data/this is"));
    }

    @Test
    public void getSchemaPath_jobDirIsNotDataDir_returnCorrectPath() {
        // Arrange
        pathManager.setJobBasePath("jobbase");
        pathManager.setSchemaPath("schema");
        pathManager.setJobDirIsDataDir(false);
        String expected = "data/schema";

        // Act
        String output = pathManager.getSchemaPath();

        // Assert
        assertEquals(expected, output);
    }

    @Test
    public void getSchemaPath_jobDirIsDataDir_returnCorrectPath() {
        // Arrange
        pathManager.setJobBasePath("jobbase");
        pathManager.setSchemaPath("schema");
        pathManager.setJobDirIsDataDir(true);
        String expected = "jobbase/schema";

        // Act
        String output = pathManager.getSchemaPath();

        // Assert
        assertEquals(expected, output);
    }

    @Test
    public void getMapFeaturesPath_emptyPathSet_returnDataDirWithoutTrailingSlash() {
        // Arrange
        pathManager.setMapFeaturesPath("");
        pathManager.setDataPath("data");
        String expected = "data";

        // Act
        String output = pathManager.getMapFeaturesPath();

        // Assert
        assertEquals(expected, output);
    }

    @Before
    public void setUp() {
        pathManager = new PathManagerFixture();
    }
}
