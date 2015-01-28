package nl.esciencecenter.eecology.classification.dataaccess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.schemaloading.SchemaRemapper;

import com.google.inject.Inject;

public class SchemaRemapperReader {
    @Inject
    private PathManager pathManager;
    private final List<Integer> currentOldIds = new LinkedList<Integer>();
    private String currentNewDescription;
    private int currentNewId;
    private final String separator = " ";

    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
    }

    public SchemaRemapper readSchemaRemapper() {

        FileReader fileReader = null;
        String path = pathManager.getLabelRemapPath();
        try {
            fileReader = new FileReader(path);
        } catch (FileNotFoundException e) {
            String message = "The file containing the schema with label information was not found at (" + path + ").";
            throw new RemapFileNotFoundException(message, e);
        }

        return readMapFromFile(new BufferedReader(fileReader));
    }

    private SchemaRemapper readMapFromFile(BufferedReader reader) {
        SchemaRemapper results = new SchemaRemapper();
        try {
            fillMapFromCsvReader(results, reader);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("The file containing the schema with label information could not be loaded.", e);
        }
        return results;
    }

    private void fillMapFromCsvReader(SchemaRemapper results, BufferedReader reader) throws IOException {
        String line;
        do {
            line = reader.readLine();
            if (line == null) { // EOF
                continue;
            }

            getRemappingDataFromLine(line);
            for (Integer currentOldId : currentOldIds) {
                results.setRemapping(currentOldId, currentNewDescription, currentNewId);
            }

        } while (line != null);
    }

    private void getRemappingDataFromLine(String line) {
        currentOldIds.clear();

        String[] elements = line.split(separator);
        if (elements.length != 3) {
            return;
        }

        List<String> oldIdStrings = new LinkedList<String>(Arrays.asList(elements[2].split(",")));
        for (String oldIdString : oldIdStrings) {
            currentOldIds.add(Integer.parseInt(oldIdString.trim()));
        }

        currentNewDescription = elements[1];
        currentNewId = Integer.parseInt(elements[0]);
    }
}
