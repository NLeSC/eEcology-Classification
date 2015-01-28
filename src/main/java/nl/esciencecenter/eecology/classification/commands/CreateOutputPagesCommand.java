package nl.esciencecenter.eecology.classification.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import nl.esciencecenter.eecology.classification.configuration.PathManager;

import org.apache.commons.io.FileUtils;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheException;
import com.github.mustachejava.MustacheFactory;
import com.google.inject.Inject;

public class CreateOutputPagesCommand implements Command {
    @Inject
    private PathManager pathManager;
    @Inject
    private Printer printer;

    @Override
    public void execute() {
        String source = "resources";
        String outputPath = pathManager.getJobOutputPath();
        String stylePath = pathManager.getJobStylePath();
        String codePath = pathManager.getJobCodePath();
        String dataPath = pathManager.getJobDataPath();
        mkdir(outputPath);
        mkdir(stylePath);
        mkdir(codePath);
        mkdir(dataPath);
        copyFile("treevisualization.js", source, codePath);
        copyFile("misclassifications.js", source, codePath);
        copyFile("featurescatterplots.js", source, codePath);
        copyFile("testreport.js", source, codePath);
        copyFile("d3.v3.min.js", source, codePath);
        copyFile("bootstrap.min.js", source, codePath);
        copyFile("jquery.min.js", source, codePath);
        copyFile("misclassifications.css", source, stylePath);
        copyFile("treevisualization.css", source, stylePath);
        copyFile("featurescatterplots.css", source, stylePath);
        copyFile("classification-common.css", source, stylePath);
        copyFile("bootstrap.min.css", source, stylePath);
        copyFile("bootstrap-theme.min.css", source, stylePath);
        render("treevisualization.mustache", source, outputPath, "treevisualization.html");
        render("testreport.mustache", source, outputPath, "testreport.html");
        render("misclassifications.mustache", source, outputPath, "misclassifications.html");
        render("featurescatterplots.mustache", source, outputPath, "featurescatterplots.html");
        render("start.mustache", source, outputPath, "start.html");
    }

    private static void mkdir(String path) {
        File file = new File(path);
        if (file.exists() == false) {
            file.mkdir();
        }
    }

    private void render(String fileName, String source, String destinationPath, String destinationFile) {
        MustacheFactory mf = new DefaultMustacheFactory();
        try {
            Mustache mustache = mf.compile(source + File.separator + fileName);
            mustache.execute(new FileWriter(destinationPath + File.separator + destinationFile), null).flush();
        } catch (MustacheException | IOException e) {
            warnCouldNotCreateOutput(fileName, e.getMessage());
        }
    }

    private void warnCouldNotCreateOutput(String fileName, String reason) {
        printer.warn("Could not create output file (" + fileName + "). Reason: " + reason);
    }

    private void copyFile(String fileName, String source, String destinationPath) {
        try {
            File destinationDir = new File(destinationPath);
            FileUtils
                    .copyFile(new File(source + File.separator + fileName), new File(destinationDir + File.separator + fileName));
        } catch (IOException e) {
            // User has to copy it manually then... no problem
            warnCouldNotCreateOutput(fileName, e.getMessage());
        }
    }

}
