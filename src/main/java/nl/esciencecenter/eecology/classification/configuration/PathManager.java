package nl.esciencecenter.eecology.classification.configuration;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Named;

import com.google.inject.Inject;

public class PathManager {
    @Inject
    @Named("base_dir")
    protected String jobBasePath;
    @Inject
    @Named("train_set_file_path")
    private String trainSetPath;
    @Inject
    @Named("label_schema_file_path")
    protected String schemaPath;
    @Inject
    @Named("test_set_file_path")
    private String testSetPath;
    @Inject
    @Named("validation_set_file_path")
    private String validationSetPath;
    @Inject
    @Named("custom_feature_extractor_file_path")
    private String customFeatureExtractorFilePath;
    @Inject
    @Named("classifier_path")
    private String classifierPath;
    @Inject
    @Named("measurement_classifier_path")
    private String measurementClassifierPath;
    @Inject
    @Named("externally_calculated_feature_value_csv_path")
    private String mapFeaturesPath;
    @Inject
    @Named("label_schema_remapping_path")
    private String labelRemapperPath;
    @Inject
    @Named("train_on_fixed_class_numbers")
    private boolean isClassInstancesNumberFixedForTraining;
    @Inject
    @Named("unannotated_measurement_source_paths")
    private String unannotatedSourcePaths;
    @Inject
    @Named("annotated_measurement_source_paths")
    private String annotatedSourcePaths;
    @Inject
    @Named("gps_record_annotations_path")
    private String gpsRecordAnnotationsPath;
    @Inject
    @Named("gps_records_path")
    private String gpsRecordsPath;
    @Inject
    @Named("all_files_including_data_are_inside_job_dir")
    protected boolean jobDirIsDataDir;

    private final String treeGraphPath = "treegraph.json";
    private final String misclassificationsPath = "misclassifications.json";
    private final String testStatisticsPath = "teststatistics.json";
    private final String testConfusionMatrixExportPath = "confusionMatrix.txt";
    private final String classificationTuplesPath = "classifications.csv";
    private final String featuresCsvPath = "featurescomplete.csv";
    private final String schemaJsonPath = "schema.json";
    private final String dataPath = "data";
    private final String jobDataSubPath = "data";
    private final String jobStyleSubPath = "css";
    private final String jobCodeSubPath = "js";
    private final String outputSubPath = "output";

    public String getTreeGraphPath() {
        return getJoinedPath(getJobDataPath(), treeGraphPath);
    }

    public String getCustomFeaturePath() {
        return getJoinedPath(getDataPath(), customFeatureExtractorFilePath);
    }

    public String getMisclassificationsPath() {
        return getJoinedPath(getJobDataPath(), misclassificationsPath);
    }

    public String getTestResultPath() {
        return getJoinedPath(getJobDataPath(), testStatisticsPath);
    }

    public String getTestResultConfusionMatrixExportPath() {
        return getJoinedPath(getJobDataPath(), testConfusionMatrixExportPath);
    }

    public String getClassificationTuplesPath() {
        return getJoinedPath(getJobDataPath(), classificationTuplesPath);
    }

    public String getFeaturesCsvPath() {
        return getJoinedPath(getJobDataPath(), featuresCsvPath);
    }

    public String getSchemaJsonPath() {
        return getJoinedPath(getJobDataPath(), schemaJsonPath);
    }

    public String getSchemaPath() {
        return getJoinedPath(getDataPath(), schemaPath);
    }

    public String getTrainSetPath() {
        return getJoinedPath(getDataPath(), trainSetPath);
    }

    public String getTestSetPath() {
        return getJoinedPath(getDataPath(), testSetPath);
    }

    public String getValidationSetPath() {
        return getJoinedPath(getDataPath(), validationSetPath);
    }

    public String getClassifierPath() {
        return getJoinedPath(getDataPath(), classifierPath);
    }

    public String getMapFeaturesPath() {
        return getJoinedPath(getDataPath(), mapFeaturesPath);
    }

    public String getMeasurementClassifierPath() {
        return getJoinedPath(getDataPath(), measurementClassifierPath);
    }

    public String getLabelRemapPath() {
        return getJoinedPath(getDataPath(), labelRemapperPath);
    }

    public List<String> getAnnotatedSourcePaths() {
        return getDataPathList(annotatedSourcePaths);
    }

    public List<String> getUnannotatedSourcePaths() {
        return getDataPathList(unannotatedSourcePaths);
    }

    public String getGpsRecordAnnotationsPath() {
        return getJoinedPath(getDataPath(), gpsRecordAnnotationsPath);
    }

    public String getGpsRecordsPath() {
        return getJoinedPath(getDataPath(), gpsRecordsPath);
    }

    public String getJobDataPath() {
        return getJoinedPath(getJobOutputPath(), jobDataSubPath);
    }

    public String getJobStylePath() {
        return getJoinedPath(getJobOutputPath(), jobStyleSubPath);
    }

    public String getJobCodePath() {
        return getJoinedPath(getJobOutputPath(), jobCodeSubPath);
    }

    public String getJobOutputPath() {
        return getJoinedPath(jobBasePath, outputSubPath);
    }

    public String getDataPath() {
        String path = jobDirIsDataDir ? jobBasePath : dataPath;
        return getPath(path);
    }

    private String getJoinedPath(String basePath, String subPath) {
        String base = getPath(basePath);
        return base + File.separator + getPath(subPath);
    }

    private String getPath(String path) {
        return path.trim();
    }

    protected List<String> getDataPathList(String pathString) {
        LinkedList<String> results = new LinkedList<String>();
        for (String subString : pathString.split(",")) {
            results.add(getJoinedPath(getDataPath(), subString));
        }
        return results;
    }

}
