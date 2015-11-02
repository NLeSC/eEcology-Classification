package nl.esciencecenter.eecology.classification.dataaccess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import nl.esciencecenter.eecology.classification.commands.Printer;
import nl.esciencecenter.eecology.classification.commands.TreeFactory;
import nl.esciencecenter.eecology.classification.commands.TreeNode;
import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;

public class ClassifierDescriptionSaver {
    @Inject
    private TreeFactory treeFactory;
    @Inject
    private ObjectMapper objectMapper;
    @Inject
    private PathManager pathManager;
    @Inject
    private SchemaToJobDirectorySaver schemaToJobDirectorySaver;
    @Inject
    private Printer printer;

    public void setTreeFactory(TreeFactory treeFactory) {
        this.treeFactory = treeFactory;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
    }

    public void setSchemaToJobDirectorySaver(SchemaToJobDirectorySaver schemaToJobDirectorySaver) {
        this.schemaToJobDirectorySaver = schemaToJobDirectorySaver;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void saveClassifierDescription(Classifier classifier, List<Segment> trainSegments) {
        String classifierDescription = classifier.toString();
        String classifierDescriptionPath = pathManager.getClassifierDescriptionPath();
        try {
            Files.write(Paths.get(classifierDescriptionPath), classifierDescription.getBytes());
        } catch (IOException e) {
            printer.print("Failed to save classifier description document at \"" + classifierDescriptionPath + "\". Reason:"
                    + e.toString());
        }
        createTreeGraphIfPossible(classifier, trainSegments);
        schemaToJobDirectorySaver.saveSchemaToJobDirectory();
    }

    private void createTreeGraphIfPossible(Classifier classifier, List<Segment> segments) {
        if (classifier instanceof J48) {
            J48 j48 = (J48) classifier;
            String dotGraph = "";
            String treeGraphPath = pathManager.getTreeGraphPath();
            try {
                dotGraph = j48.graph();
                TreeNode root = treeFactory.createFromDotGraph(dotGraph);
                root.feed(segments);
                objectMapper.writeValue(new File(treeGraphPath), root);
            } catch (Exception e) {
                printer.print("Failed to save tree graph at \"" + treeGraphPath + "\". Reason:" + e.toString());
            }
        }
    }

}
