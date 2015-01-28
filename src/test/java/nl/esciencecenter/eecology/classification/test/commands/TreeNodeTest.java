package nl.esciencecenter.eecology.classification.test.commands;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import nl.esciencecenter.eecology.classification.commands.TreeNode;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.junit.Before;
import org.junit.Test;

public class TreeNodeTest {

    private TreeNode root;
    private TreeNode grandchild10;
    private TreeNode grandchild11;
    private final String[] featureNames = new String[] { "feature0", "feature1" };

    @Test
    public void create_defaultOperator_isEnd() {
        // Arrange
        TreeNode node = new TreeNode();

        // Act

        // Assert
        assertEquals(TreeNode.BRANCHOPERATOR_ROOT, node.getBranchOperator());
    }

    @Test
    public void setBranchOperator_gt_resultIsGt() {
        // Arrange
        TreeNode node = new TreeNode();

        // Act
        node.setBranchOperator(">");

        // Assert
        assertEquals(TreeNode.BRANCHOPERATOR_GREATERTHAN, node.getBranchOperator());
    }

    @Test
    public void setBranchOperator_Le_resultIsLe() {
        // Arrange
        TreeNode node = new TreeNode();

        // Act
        node.setBranchOperator("<=");

        // Assert
        assertEquals(TreeNode.BRANCHOPERATOR_LESSOREQUAL, node.getBranchOperator());
    }

    @Test
    public void setBranchOperator_undefined_resultIsDefault() {
        // Arrange
        TreeNode node = new TreeNode();

        // Act
        node.setBranchOperator("blabla");

        // Assert
        assertEquals(TreeNode.BRANCHOPERATOR_ROOT, node.getBranchOperator());
    }

    @Test
    public void getRootSegmentCount_smallTree5Segments_childLeafIs5() {
        // Arrange
        createTestTree();
        LinkedList<Segment> segments = getSegments();

        // Act
        for (Segment segment : segments) {
            root.feed(segment);
        }

        // Assert
        assertEquals(segments.size(), grandchild10.getRootSegmentCount());
    }

    @Test
    public void getSegmentCount_smallTree5Segments_rootNodeIs5() {
        // Arrange
        createTestTree();
        LinkedList<Segment> segments = getSegments();

        // Act
        for (Segment segment : segments) {
            root.feed(segment);
        }

        // Assert
        assertEquals(segments.size(), root.getSegmentCount());
    }

    @Test
    public void getSegmentCount_smallTree15SegmentsWithOneLabelC_grandchild11Returns2() {
        // Arrange
        createTestTree();
        LinkedList<Segment> segments = getSegments();

        // Act
        for (Segment segment : segments) {
            root.feed(segment);
        }

        // Assert
        assertEquals(2, grandchild10.getSegmentCount());
    }

    @Test
    public void getSegmentCount_smallTree15SegmentsAtOnce_grandchild11Returns2() {
        // Arrange
        createTestTree();
        LinkedList<Segment> segments = getSegments();

        // Act
        root.feed(segments);

        // Assert
        assertEquals(2, grandchild10.getSegmentCount());
    }

    private LinkedList<Segment> getSegments() {
        LinkedList<Segment> segments = new LinkedList<Segment>();
        segments.add(getSegmentWithFeatureValues(2.5, -1000)); // child0
        segments.add(getSegmentWithFeatureValues(3.5, 20)); // child0
        segments.add(getSegmentWithFeatureValues(1.5, -20.5)); // grandchild11
        segments.add(getSegmentWithFeatureValues(-4, 3)); // grandchild10
        segments.add(getSegmentWithFeatureValues(0, 6)); // grandchild10
        return segments;
    }

    private Segment getSegmentWithFeatureValues(double featureValue0, double featureValue1) {
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        segment.setFeatures(new double[] { featureValue0, featureValue1 }, featureNames);
        return segment;
    }

    private void createTestTree() {
        /*-
         * Creates tree below:
         *
         * root                 child0
         * feature0     >2      labelA
         *
         *                      child1                  grandchild10
         *              <=2      feature1        >-1     labelB
         *
         *                                      <=-1    grandchild11
         *                                              labelC
         */

        root = new TreeNode();
        root.setLabel("feature0");

        TreeNode child0 = new TreeNode();
        root.getChildren().add(child0);
        child0.setGreaterThanOperator();
        child0.setBranchValue(2);
        child0.setLabel("labelA");

        TreeNode child1 = new TreeNode();
        root.getChildren().add(child1);
        child1.setLessOrEqualOperator();
        child1.setBranchValue(2);
        child1.setLabel("feature1");

        grandchild10 = new TreeNode();
        child1.getChildren().add(grandchild10);
        grandchild10.setGreaterThanOperator();
        grandchild10.setBranchValue(-1);
        grandchild10.setLabel("labelB");

        grandchild11 = new TreeNode();
        child1.getChildren().add(grandchild11);
        grandchild11.setLessOrEqualOperator();
        grandchild11.setBranchValue(-1);
        grandchild11.setLabel("labelC");
    }

    @Before
    public void setUp() {

    }
}
