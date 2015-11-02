package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.esciencecenter.eecology.classification.commands.Printer;
import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.ClassifierDescriptionSaver;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaToJobDirectorySaver;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class ClassifierDescriptionSaverTest {

    private ClassifierDescriptionSaver classifierDescriptionSaver;
    private SchemaToJobDirectorySaver schemaToJobDirectorySaver;
    private final String descriptionTestPath = "src/test/java/resources/testclassifierdescription.txt";
    private final String treeTestPath = "src/test/java/resources/testclassifierdescriptiontree.json";

    @Test
    public void save_mockedClassifier_outputSaved() {
        // Arrange
        Classifier classifier = getDummyTreeLearner();

        // Act
        classifierDescriptionSaver.saveClassifierDescription(classifier, null);

        // Assert
        assertTrue(Files.exists(Paths.get(descriptionTestPath)));
    }

    @Test
    public void save_mockedClassifier_schemaSaverWasCalled() {
        // Arrange
        Classifier classifier = getDummyTreeLearner();
        schemaToJobDirectorySaver.saveSchemaToJobDirectory();
        expectLastCall().once();
        replay(schemaToJobDirectorySaver);

        // Act
        classifierDescriptionSaver.saveClassifierDescription(classifier, null);

        // Assert
        verify(schemaToJobDirectorySaver);
    }

    @Before
    public void setUp() throws Exception {
        classifierDescriptionSaver = new ClassifierDescriptionSaver();
        classifierDescriptionSaver.setPathManager(new PathManager() {
            @Override
            public String getClassifierDescriptionPath() {
                return descriptionTestPath;
            }

            @Override
            public String getTreeGraphPath() {
                return treeTestPath;
            }
        });
        classifierDescriptionSaver.setObjectMapper(new ObjectMapper());
        classifierDescriptionSaver.setPrinter(createNiceMock(Printer.class));
        schemaToJobDirectorySaver = createNiceMock(SchemaToJobDirectorySaver.class);
        classifierDescriptionSaver.setSchemaToJobDirectorySaver(schemaToJobDirectorySaver);
    }

    @After
    public void cleanUp() throws IOException {
        Files.deleteIfExists(Paths.get(descriptionTestPath));
        Files.deleteIfExists(Paths.get(treeTestPath));
    }

    private J48 getDummyTreeLearner() {
        J48 j48 = new J48() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buildClassifier(Instances data) throws Exception {
            }
        };
        try {
            j48.buildClassifier(null);
        } catch (Exception e) {
            throw new RuntimeException(e); // TODO
        }
        return j48;
    }
}
