package nl.esciencecenter.eecology.classification.commands;

import java.util.List;

import nl.esciencecenter.eecology.classification.dataaccess.ClassifierLoader;
import nl.esciencecenter.eecology.classification.dataaccess.SegmentAsTupleSaver;
import nl.esciencecenter.eecology.classification.machinelearning.ClassificationService;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import nl.esciencecenter.eecology.classification.segmentloading.SegmentProvider;
import weka.classifiers.Classifier;

import com.google.inject.Inject;

public class ClassifyCommand implements Command {
    @Inject
    private ClassificationService classificationService;
    @Inject
    private SegmentAsTupleSaver segmentsAsTupleSaver;
    @Inject
    private ClassifierLoader classifierLoader;
    @Inject
    private Printer printer;
    @Inject
    private SegmentProvider segmentProvider;

    @Override
    public void execute() {
        printer.print("Executing classification process: ");
        printer.print("loading and segmenting unannotated data...");
        List<Segment> classificationSegments = segmentProvider.getUnannotatedSegments();
        printer.print("classifying unannotated...");
        classify(classificationSegments);
        printer.print("done.\n");
    }

    private void classify(List<Segment> segments) {
        Classifier classifier = classifierLoader.loadClassifier();
        List<Segment> classifiedSegments = classificationService.classify(segments, classifier);
        segmentsAsTupleSaver.saveAsIdTimeTuple(classifiedSegments);
    }
}
