package nl.esciencecenter.eecology.classification.dataaccess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;

import com.google.inject.Inject;

/**
 * Reads the details of labels, like description or associated color, from a csv/txt file.
 * 
 * @author Christiaan Meijer, NLeSc
 * 
 */
public class SchemaCustomCsvReader implements SchemaReader {
    @Inject
    private PathManager pathManager;

    private final String separator = " ";

    @Override
    public Map<Integer, LabelDetail> readSchema() {
        return readSchema(pathManager.getSchemaPath());
    }

    @Override
    public Map<Integer, LabelDetail> readSchema(String path) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(path);
        } catch (FileNotFoundException e) {
            String message = "The file containing the schema with label information was not found at (" + path + ").";
            throw new RuntimeException(message, e);
        }

        return readMapFromFile(new BufferedReader(fileReader));
    }

    private Map<Integer, LabelDetail> readMapFromFile(BufferedReader reader) {
        Map<Integer, LabelDetail> results = new HashMap<Integer, LabelDetail>();
        try {
            fillMapFromCsvReader(results, reader);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("The file containing the schema with label information could not be loaded.", e);
        }
        return results;
    }

    private void fillMapFromCsvReader(Map<Integer, LabelDetail> results, BufferedReader reader) throws IOException {
        LabelDetail labelDetails = null;
        String line;
        do {
            line = reader.readLine();
            if (line == null) { // EOF
                continue;
            }

            labelDetails = getlabelDetailsFromLine(line);
            if (labelDetails != null) {
                results.put(labelDetails.getLabelId(), labelDetails);
            }

        } while (line != null);
    }

    private LabelDetail getlabelDetailsFromLine(String line) {
        if (line.length() < 3) {
            return null;
        }
        String idString = line.substring(0, 2).trim();

        String[] elements = line.substring(2).split(separator);
        if (elements.length != 4) {
            return null;
        }

        String description = elements[0];
        String colorRString = elements[1];
        String colorGString = elements[2];
        String colorBString = elements[3];

        return createLabelDetailFromStrings(idString, description, colorRString, colorGString, colorBString);

    }

    /**
     * Tries to create a LabelDetail object from strings provided. Returns null on failure.
     * 
     * @param idString
     * @param description
     * @param colorRString
     * @param colorGString
     * @param colorBString
     * @return
     */
    private LabelDetail createLabelDetailFromStrings(String idString, String description, String colorRString,
            String colorGString, String colorBString) {
        LabelDetail labelDetail = new LabelDetail();
        try {
            int labelId = Integer.parseInt(idString);
            double colorR = Double.parseDouble(colorRString);
            double colorG = Double.parseDouble(colorGString);
            double colorB = Double.parseDouble(colorBString);

            labelDetail.setLabelId(labelId);
            labelDetail.setDescription(description);
            labelDetail.setColorR(colorR);
            labelDetail.setColorG(colorG);
            labelDetail.setColorB(colorB);
        } catch (NumberFormatException e) {
            return null;
        }
        return labelDetail;
    }

}
