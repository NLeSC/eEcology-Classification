package nl.esciencecenter.eecology.classification.test.schemaloading;

import static org.junit.Assert.assertEquals;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;
import nl.esciencecenter.eecology.classification.schemaloading.SchemaRemapper;
import nl.esciencecenter.eecology.classification.schemaloading.SchemaRemappingException;

import org.junit.Before;
import org.junit.Test;

public class SchemaRemapperTest {
    private SchemaRemapper labelRemapper;
    private final int oldId = 5;
    private final int newId = 88;
    private final String newDescription = "remapped";
    private final String oldDescription = "old";

    @Test(expected = SchemaRemappingException.class)
    public void getRemapped_nothingSet_throwLabelRemappingException() {
        // Arrange
        LabelDetail labelDetail = getLabelDetail();

        // Act
        labelRemapper.getRemapped(labelDetail);

        // Assert
    }

    @Test
    public void getRemapped_remappingSet_correctRemappedId() {
        // Arrange
        LabelDetail labelDetail = getLabelDetail();
        labelRemapper.setRemapping(oldId, "remapped", newId);

        // Act
        LabelDetail remapped = labelRemapper.getRemapped(labelDetail);

        // Assert
        assertEquals(newId, remapped.getLabelId());
    }

    @Test
    public void getRemappedId_remappingSet_correctRemappedId() {
        // Arrange
        labelRemapper.setRemapping(oldId, "remapped", newId);

        // Act
        int remappedId = labelRemapper.getRemappedId(oldId);

        // Assert
        assertEquals(newId, remappedId);
    }

    @Test
    public void getRemapped_remappingSet_correctRemappedDescription() {
        // Arrange
        LabelDetail labelDetail = getLabelDetail();
        labelRemapper.setRemapping(oldId, newDescription, newId);

        // Act
        LabelDetail remapped = labelRemapper.getRemapped(labelDetail);

        // Assert
        assertEquals(newDescription, remapped.getDescription());
    }

    @Before
    public void setUp() {
        labelRemapper = new SchemaRemapper();
    }

    private LabelDetail getLabelDetail() {
        LabelDetail labelDetail = new LabelDetail();
        labelDetail.setDescription(oldDescription);
        labelDetail.setLabelId(5);
        labelDetail.setColorR(0.5);
        labelDetail.setColorG(0.5);
        labelDetail.setColorB(0.5);
        return labelDetail;
    }
}
