package nl.esciencecenter.eecology.classification.commands;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;

import org.apache.commons.lang3.ArrayUtils;

import com.google.inject.Inject;

public class TreeFactory {

    private Map<String, TreeNode> treeNodesByName;
    private LinkedList<String[]> edgesToBeCreated;
    private TreeNode root;
    private final SchemaProvider schemaReader;

    @Inject
    public TreeFactory(SchemaProvider schemaReader) {
        this.schemaReader = schemaReader;
    }

    public TreeNode createFromDotGraph(String dotGraph) {
        if (dotGraph == null) {
            return null;
        }
        treeNodesByName = new HashMap<String, TreeNode>();
        edgesToBeCreated = new LinkedList<String[]>();

        String body = dotGraph.split("\\{")[1].split("\\}")[0];
        String[] lines = body.split("\n");
        root = null;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            processLine(line);
        }
        createEdgesAndBranchLabels();

        return root;
    }

    private void createEdgesAndBranchLabels() {
        for (String[] triplet : edgesToBeCreated) {
            String parentName = triplet[0];
            String childName = triplet[1];
            TreeNode childNode = treeNodesByName.get(childName);
            TreeNode parentNode = treeNodesByName.get(parentName);
            childNode.setBranchOperator(triplet[3]);
            childNode.setBranchValue(Double.parseDouble(triplet[4]));
            parentNode.getChildren().add(childNode);
        }
    }

    private void processLine(String line) {
        String[] lineElements = line.split("\\[");
        if (lineElements.length < 2) {
            return;
        }

        String mainClause = lineElements[0].trim();
        String secondaryClause = lineElements[1].split("\\]")[0];

        String[] mainClauseSplit = mainClause.split("->");
        String name = mainClauseSplit[0];

        if (mainClauseSplit.length > 1) {
            processEdgeDefinition(name, mainClauseSplit, secondaryClause);
        } else {
            processNodeDefinition(name, secondaryClause);
        }
    }

    private void processNodeDefinition(String name, String secondaryClauseString) {
        TreeNode node = new TreeNode();
        node.setName(name);
        processSecondaryClausesForNodeCreation(node, secondaryClauseString);
        treeNodesByName.put(name, node);
        if (root == null) {
            root = node;
        }
    }

    private void processEdgeDefinition(String name, String[] mainClauseSplit, String secondaryClause) {
        String childName = mainClauseSplit[1];
        String label = getLabelValue(secondaryClause);
        String[] split = label.split(" ");
        String operator = split[0];
        String value = split[1];
        edgesToBeCreated.add(new String[] { name, childName, label, operator, value });
    }

    private String getLabelColor(String secondaryClauseString) {
        List<Character> immutibleList = Arrays.asList(ArrayUtils.toObject(secondaryClauseString.toCharArray()));
        LinkedList<Character> secondaryClauses = new LinkedList<Character>(immutibleList);
        while (secondaryClauses.size() > 0) {
            String key = popElement(secondaryClauses);
            String value = popElement(secondaryClauses);

            if (key.equals("label")) {
                LabelDetail labelDetail = getLabelFromLabelValue(value);
                if (labelDetail != null) {
                    Color color = new Color(toInt256(labelDetail.getColorR()), toInt256(labelDetail.getColorG()),
                            toInt256(labelDetail.getColorB()));
                    return ColorToHex(color);
                }
            }
        }
        return "";
    }

    private int toInt256(double color) {
        return (int) (255 * color);
    }

    /** * Converts the supplied color into a hexidecimal color string, such as "2B23FF". */
    public static String ColorToHex(Color color) {
        String rgb = Integer.toHexString(color.getRGB());
        return rgb.substring(2, rgb.length());
    }

    private void processSecondaryClausesForNodeCreation(TreeNode node, String secondaryClauseString) {
        node.setLabel(getLabelValue(secondaryClauseString));
        node.setColorCode(getLabelColor(secondaryClauseString));
    }

    private String getLabelValue(String secondaryClauseString) {
        List<Character> immutibleList = Arrays.asList(ArrayUtils.toObject(secondaryClauseString.toCharArray()));
        LinkedList<Character> secondaryClauses = new LinkedList<Character>(immutibleList);
        String label = "";
        while (secondaryClauses.size() > 0) {
            String key = popElement(secondaryClauses);
            String value = popElement(secondaryClauses);

            if (key.equals("label")) {
                LabelDetail labelDetail = getLabelFromLabelValue(value);
                label = labelDetail == null ? value : labelDetail.getDescription();
            }
        }
        return label;
    }

    private LabelDetail getLabelFromLabelValue(String value) {
        String[] split = value.split("\\(");
        String potentialLabelIndex = split[0].trim();
        if (split.length > 1) {
            LabelDetail labelDetail = schemaReader.getSchema().get(Integer.parseInt(potentialLabelIndex));
            return labelDetail;
        }
        return null;
    }

    private String popElement(LinkedList<Character> secondaryClauses) {
        StringBuilder currentElement = new StringBuilder();
        boolean isWithinParenthes = false;
        while (secondaryClauses.size() > 0) {
            char currentChar = secondaryClauses.pop();

            if (currentChar == '=' && isWithinParenthes == false) {
                break;
            }

            if (currentChar == ' ' && isWithinParenthes == false) {
                continue;
            }

            if (currentChar == '"' && isWithinParenthes) {
                break;
            }

            if (currentChar == '"' && currentElement.length() == 0) {
                isWithinParenthes = true;
                continue;
            }

            currentElement.append(currentChar);
        }

        if (isWithinParenthes) {
            return currentElement.toString();
        }
        return currentElement.toString().trim();
    }
}
