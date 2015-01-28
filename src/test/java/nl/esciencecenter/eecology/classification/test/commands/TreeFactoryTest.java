package nl.esciencecenter.eecology.classification.test.commands;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import nl.esciencecenter.eecology.classification.commands.TreeFactory;
import nl.esciencecenter.eecology.classification.commands.TreeNode;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;

import org.junit.Before;
import org.junit.Test;

public class TreeFactoryTest {
    private TreeFactory treeFactory;
    private HashMap<Integer, LabelDetail> map;
    private LabelDetail labelDetailFor3;
    private final double errorMargin = 0.00001;

    @Test
    public void createFromDotGraph_null_noExceptionThrown() {
        // Arrange

        // Act
        treeFactory.createFromDotGraph(null);

        // Assert
    }

    @Test
    public void createFromDotGraph_testInput_rootHasCorrectName() {
        // Arrange
        String input = getTestDotGraph();
        String expected = "N0";

        // Act
        TreeNode node = treeFactory.createFromDotGraph(input);

        // Assert
        assertEquals(expected, node.getName());
    }

    @Test
    public void createFromDotGraph_testInput_rootHasCorrectLabel() {
        // Arrange
        String input = getTestDotGraph();
        String expected = "extracted feature #0";

        // Act
        TreeNode node = treeFactory.createFromDotGraph(input);

        // Assert
        assertEquals(expected, node.getLabel());
    }

    @Test
    public void createFromDotGraph_testInput_rootHas2Children() {
        // Arrange
        String input = getTestDotGraph();

        // Act
        TreeNode node = treeFactory.createFromDotGraph(input);

        // Assert
        assertEquals(2, node.getChildren().size());
    }

    @Test
    public void createFromDotGraph_testInput_rootChild1CorrectLabel() {
        // Arrange
        String input = getTestDotGraph();
        String expected = "extracted feature #1";

        // Act
        TreeNode node = treeFactory.createFromDotGraph(input);

        // Assert
        assertEquals(expected, node.getChildren().get(0).getLabel());
    }

    @Test
    public void createFromDotGraph_testInput_leafHasCorrectLabel() {
        // Arrange
        String input = getTestDotGraph();
        String expected = "flight";
        labelDetailFor3.setDescription(expected);

        // Act
        TreeNode node = treeFactory.createFromDotGraph(input);

        // Assert
        assertEquals(expected, node.getChildren().get(1).getLabel());
    }

    @Test
    public void createFromDotGraph_testInput_rootHasCorrectBranchOperator() {
        // Arrange
        String input = getTestDotGraph();
        String expected = "<=";

        // Act
        TreeNode node = treeFactory.createFromDotGraph(input);

        // Assert
        assertEquals(expected, node.getChildren().get(0).getBranchOperator());
    }

    @Test
    public void createFromDotGraph_testInput_rootHasCorrectBranchValue() {
        // Arrange
        String input = getTestDotGraph();
        double expected = 0.036976;

        // Act
        TreeNode node = treeFactory.createFromDotGraph(input);

        // Assert
        assertEquals(expected, node.getChildren().get(0).getBranchValue(), errorMargin);
    }

    @Test
    public void createFromDotGraph_testInput_leafHasCorrectColor() {
        // Arrange
        String input = getTestDotGraph();
        labelDetailFor3.setColorR(1.0);
        labelDetailFor3.setColorG(0.5);
        labelDetailFor3.setColorB(0.0);
        String expected = "ff7f00";

        // Act
        TreeNode node = treeFactory.createFromDotGraph(input);

        // Assert
        assertEquals(expected, node.getChildren().get(1).getColorCode());
    }

    private String getTestDotGraph() {
        // @formatter:off
        String input = "digraph J48Tree {\n" +
                "N0 [label=\"extracted feature #0\" ]\n" +
                "N0->N1 [label=\"<= 0.036976\"]\n" +
                "N1 [label=\"extracted feature #1\" ]\n" +
                "N1->N2 [label=\"<= 0.019333\"]\n" +
                "N2 [label=\"extracted feature #2\" ]\n" +
                "N0->N3 [label=\"<= -0.145104\"]\n" +
                "N3 [label=\"3 (12.0/4.0)\" shape=box style=filled ]\n" +
                "}";
        //@formatter:on
        return input;
    }

    @Before
    public void setUp() {
        SchemaProvider schemaReader = createNiceMock(SchemaProvider.class);
        map = new HashMap<Integer, LabelDetail>();
        labelDetailFor3 = new LabelDetail();
        map.put(3, labelDetailFor3);
        expect(schemaReader.getSchema()).andReturn(map).anyTimes();
        replay(schemaReader);
        treeFactory = new TreeFactory(schemaReader);
    }
}
