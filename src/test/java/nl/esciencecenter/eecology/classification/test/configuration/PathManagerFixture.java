package nl.esciencecenter.eecology.classification.test.configuration;

import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.PathManager;

public class PathManagerFixture extends PathManager {
    public void setJobDirIsDataDir(boolean jobDirIsDataDir) {
        this.jobDirIsDataDir = jobDirIsDataDir;
    }

    public void setJobBasePath(String jobBasePath) {
        this.jobBasePath = jobBasePath;
    }

    public void setSchemaPath(String schemaPath) {
        this.schemaPath = schemaPath;
    }

    @Override
    public List<String> getDataPathList(String pathString) {
        return super.getDataPathList(pathString);
    }

}
