package nl.esciencecenter.eecology.classification;

import java.io.File;

import nl.esciencecenter.eecology.classification.commands.ClassifyCommand;
import nl.esciencecenter.eecology.classification.commands.CreateOutputPagesCommand;
import nl.esciencecenter.eecology.classification.commands.OutputFeaturesInCsvCommand;
import nl.esciencecenter.eecology.classification.commands.SplitDatasetCommand;
import nl.esciencecenter.eecology.classification.commands.TestCommand;
import nl.esciencecenter.eecology.classification.commands.TrainCommand;
import nl.esciencecenter.eecology.classification.configuration.GuiceModule;
import nl.esciencecenter.eecology.classification.exceptionhandling.MessagePrintingExceptionHandler;
import nl.esciencecenter.eecology.classification.exceptionhandling.StackTraceSavingExceptionHandler;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

public class App {
    @Inject
    private SplitDatasetCommand splitDatasetCommand;
    @Inject
    private ClassifyCommand classifyCommand;
    @Inject
    private TrainCommand trainCommand;
    @Inject
    private TestCommand testCommand;
    @Inject
    private OutputFeaturesInCsvCommand outputFeaturesInCsvCommand;
    @Inject
    private CreateOutputPagesCommand createOutputPagesCommand;

    @Inject
    @Named("execute_train_process")
    private boolean executeTrain;
    @Inject
    @Named("execute_test_process")
    private boolean executeTest;
    @Inject
    @Named("execute_classification_process")
    private boolean executeClassify;
    @Inject
    @Named("execute_dataset_splitting_process")
    private boolean executeDatasetSplitting;
    @Inject
    @Named("execute_output_features_csv_process")
    private boolean executeOutputFeaturesCsv;

    public void runCommands() {
        createOutputPagesCommand.execute();

        if (executeDatasetSplitting) {
            splitDatasetCommand.execute();
        }
        if (executeTrain) {
            trainCommand.execute();
        }
        if (executeTest) {
            testCommand.execute();
        }
        if (executeClassify) {
            classifyCommand.execute();
        }
        if (executeOutputFeaturesCsv) {
            outputFeaturesInCsvCommand.execute();
        }
    }

    public static void main(String[] args) {
        File[] files = getJobs(args);
        for (File path : files) {
            if (path.isDirectory() == false) {
                continue;
            }

            for (File subFile : path.listFiles()) {
                if (subFile.getName().equals("settings.properties")) {
                    runJob(path);
                }
            }
        }
    }

    private static void runJob(File path) {
        try {
            Injector injector = Guice.createInjector(new GuiceModule(path.getPath()));
            App app = injector.getInstance(App.class);
            app.runCommands();
        } catch (Exception e) {
            new MessagePrintingExceptionHandler().handle(e, path);
            new StackTraceSavingExceptionHandler().handle(e, path);
        }

    }

    private static File[] getJobs(String[] args) {
        File[] files;
        if (args.length == 1) {
            files = new File[] { new File(args[0]) };
        } else {
            files = getAllJobsFromDefaultJobsDir();
        }
        return files;
    }

    private static File[] getAllJobsFromDefaultJobsDir() {
        String root = "." + File.separator + "jobs";
        File jobs = new File(root);
        File[] files = jobs.listFiles();
        return files;
    }
}
