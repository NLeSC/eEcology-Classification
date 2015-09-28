package nl.esciencecenter.eecology.classification.configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordAnnotationCsvLoader;
import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordAnnotationLoader;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaCustomCsvReader;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaReader;
import nl.esciencecenter.eecology.classification.machinelearning.GeneralTrainerFactory;
import nl.esciencecenter.eecology.classification.machinelearning.TrainerFactory;
import nl.esciencecenter.eecology.classification.schemaloading.SchemaCombinedLazyProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class GuiceModule extends AbstractModule implements Module {
    private final String baseDir;

    public GuiceModule(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    protected void configure() {
        bind(SchemaReader.class).to(SchemaCustomCsvReader.class);
        bind(SchemaProvider.class).to(SchemaCombinedLazyProvider.class);
        bind(GpsRecordAnnotationLoader.class).to(GpsRecordAnnotationCsvLoader.class);
        bind(TrainerFactory.class).to(GeneralTrainerFactory.class);

        FileReader fileReader = null;
        try {
            Properties properties = new Properties();
            fileReader = new FileReader(baseDir + File.separator + "settings.properties");
            properties.load(fileReader);
            properties.put("base_dir", baseDir);
            Names.bindProperties(binder(), properties);
        } catch (IOException ex) {
            throw new RuntimeException("Error reading settings file.", ex);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    @Provides
    ObjectMapper provideObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return objectMapper;
    }
}
