package nl.esciencecenter.eecology.classification.test.schemaloading;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import nl.esciencecenter.eecology.classification.dataaccess.SchemaReader;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaRemapperReader;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;
import nl.esciencecenter.eecology.classification.schemaloading.SchemaCombinedLazyProvider;
import nl.esciencecenter.eecology.classification.schemaloading.SchemaRemapper;

import org.junit.Before;
import org.junit.Test;

public class SchemaCombinedLazyProviderTest {

    private SchemaCombinedLazyProvider schemaProvider;
    private final int queryLabelId = 5;
    private LabelDetail targetLabelDetail;
    private final int remappedLabelId = 3;
    private final String targetDescription = "target";

    @Test
    public void getSchema_once_readerWasCalled() {
        // Arrange
        SchemaReader reader = getSchemaReaderMock();
        schemaProvider.setSchemaReader(reader);
        schemaProvider.setSchemaRemapper(getSchemaRemapper());

        // Act
        schemaProvider.getSchema();

        // Assert
        verify(reader);
    }

    @Test
    public void getSchema_twice_readerWasCalledOnce() {
        // Arrange
        SchemaReader reader = getSchemaReaderMock();
        schemaProvider.setSchemaReader(reader);
        schemaProvider.setSchemaRemapper(getSchemaRemapper());

        // Act
        schemaProvider.getSchema();
        schemaProvider.getSchema();

        // Assert
        verify(reader);
    }

    @Test
    public void getSchema_noRemapper_correctMapping() {
        // Arrange
        schemaProvider.setSchemaReader(getSchemaReaderMock());
        schemaProvider.setSchemaRemapper(getSchemaRemapper());

        // Act
        LabelDetail output = schemaProvider.getSchema().get(queryLabelId);

        // Assert
        assertEquals(queryLabelId, output.getLabelId());
    }

    @Test
    public void getSchema_once_remapperReaderWasCalled() {
        // Arrange
        schemaProvider.setSchemaReader(getSchemaReaderMock());
        SchemaRemapperReader schemaRemapperReader = getSchemaRemapper();
        schemaProvider.setSchemaRemapper(schemaRemapperReader);
        schemaProvider.setLabelIdsMustBeRemapped(true);

        // Act
        schemaProvider.getSchema();

        // Assert
        verify(schemaRemapperReader);
    }

    @Test
    public void getSchema_twice_remapperReaderWasCalledOnce() {
        // Arrange
        schemaProvider.setSchemaReader(getSchemaReaderMock());
        SchemaRemapperReader schemaRemapperReader = getSchemaRemapper();
        schemaProvider.setSchemaRemapper(schemaRemapperReader);
        schemaProvider.setLabelIdsMustBeRemapped(true);

        // Act
        schemaProvider.getSchema();
        schemaProvider.getSchema();

        // Assert
        verify(schemaRemapperReader);
    }

    @Test
    public void getSchema_withRemapper_correctDescription() {
        // Arrange
        schemaProvider.setSchemaReader(getSchemaReaderMock());
        schemaProvider.setSchemaRemapper(getSchemaRemapper());
        schemaProvider.setLabelIdsMustBeRemapped(true);

        // Act
        LabelDetail output = schemaProvider.getSchema().get(remappedLabelId);

        // Assert
        assertEquals(targetDescription, output.getDescription());
    }

    @Test
    public void getSchema_withRemapper_correctId() {
        // Arrange
        schemaProvider.setSchemaReader(getSchemaReaderMock());
        schemaProvider.setSchemaRemapper(getSchemaRemapper());
        schemaProvider.setLabelIdsMustBeRemapped(true);

        // Act
        LabelDetail output = schemaProvider.getSchema().get(remappedLabelId);

        // Assert
        assertEquals(remappedLabelId, output.getLabelId());
    }

    @Before
    public void setUp() {
        schemaProvider = new SchemaCombinedLazyProvider();
        targetLabelDetail = getTestLabelDetail();
        schemaProvider.setLabelIdsMustBeRemapped(false); // set false as default for all tests
    }

    private LabelDetail getTestLabelDetail() {
        LabelDetail labelDetail = new LabelDetail();
        labelDetail.setLabelId(queryLabelId);
        labelDetail.setDescription(targetDescription);
        labelDetail.setColorR(0);
        labelDetail.setColorG(0);
        labelDetail.setColorB(0);
        return labelDetail;
    }

    private SchemaReader getSchemaReaderMock() {
        SchemaReader reader = createMock(SchemaReader.class);
        Map<Integer, LabelDetail> schema = new HashMap<Integer, LabelDetail>();
        schema.put(queryLabelId, targetLabelDetail);
        expect(reader.readSchema()).andReturn(schema).times(1);
        replay(reader);
        return reader;
    }

    private SchemaRemapperReader getSchemaRemapper() {
        SchemaRemapperReader schemaRemapperReader = createMock(SchemaRemapperReader.class);
        expect(schemaRemapperReader.readSchemaRemapper()).andReturn(getSchemaMapper());
        replay(schemaRemapperReader);
        return schemaRemapperReader;
    }

    private SchemaRemapper getSchemaMapper() {
        SchemaRemapper schemaRemapper = createNiceMock(SchemaRemapper.class);
        LabelDetail remappedLabelDetail = getTestLabelDetail();
        remappedLabelDetail.setLabelId(remappedLabelId);
        for (int i = 0; i < 1; i++) {
            expect(schemaRemapper.getRemappedId(queryLabelId)).andReturn(remappedLabelId);
            expect(schemaRemapper.getRemapped(isA(LabelDetail.class))).andReturn(remappedLabelDetail);
        }
        replay(schemaRemapper);
        return schemaRemapper;
    }
}
