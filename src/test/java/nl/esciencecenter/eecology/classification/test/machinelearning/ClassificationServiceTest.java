package nl.esciencecenter.eecology.classification.test.machinelearning;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.featureextraction.SegmentToInstancesCreator;
import nl.esciencecenter.eecology.classification.machinelearning.ClassificationService;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.junit.Before;
import org.junit.Test;

import weka.core.Instances;

public class ClassificationServiceTest {
    private ClassificationService classificationService;
    private SegmentToInstancesCreator segmentToInstancesCreator;
    private SchemaProvider schemaProvider;

    @Test
    public void classify_segmentToInstancesCreatorWasCalled() {
        // Arrange
        List<Segment> segments = new LinkedList<Segment>();
        Instances instances = createNiceMock(Instances.class);
        expect(segmentToInstancesCreator.createInstancesAndUpdateSegments(isA(List.class))).andReturn(instances);
        replay(segmentToInstancesCreator);

        // Act
        classificationService.classify(segments, null);

        // Assert
        verify(segmentToInstancesCreator);
    }

    @Test
    public void classify_resultNotNull() {
        // Arrange
        List<Segment> segments = new LinkedList<Segment>();
        Instances instances = createNiceMock(Instances.class);
        expect(segmentToInstancesCreator.createInstancesAndUpdateSegments(isA(List.class))).andReturn(instances);
        replay(segmentToInstancesCreator);

        // Act
        List<Segment> output = classificationService.classify(segments, null);

        // Assert
        assertTrue(output != null);
    }

    @Before
    public void setUp() {
        segmentToInstancesCreator = createNiceMock(SegmentToInstancesCreator.class);
        schemaProvider = createNiceMock(SchemaProvider.class);
        classificationService = new ClassificationService();
        classificationService.setSchemaProvider(schemaProvider);
        classificationService.setSegmentToInstancesCreator(segmentToInstancesCreator);
    }
}
