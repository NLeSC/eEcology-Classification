package nl.esciencecenter.eecology.classification.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.segmentloading.Segment;

public class TreeNode {
    public static String BRANCHOPERATOR_GREATERTHAN = ">";
    public static String BRANCHOPERATOR_LESSOREQUAL = "<=";
    public static String BRANCHOPERATOR_ROOT = "";

    private String name;
    private String label;
    private final List<TreeNode> children;
    private double branchValue;
    private String branchOperator;
    private String colorCode;
    private int segmentCount = 0;
    private int rootSegmentCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getBranchValue() {
        return branchValue;
    }

    public void setBranchValue(double branchValue) {
        this.branchValue = branchValue;
    }

    public String getBranchOperator() {
        return branchOperator;
    }

    public void setEndOperator() {
        branchOperator = BRANCHOPERATOR_ROOT;
    }

    public void setGreaterThanOperator() {
        branchOperator = BRANCHOPERATOR_GREATERTHAN;
    }

    public void setLessOrEqualOperator() {
        branchOperator = BRANCHOPERATOR_LESSOREQUAL;
    }

    public void setBranchOperator(String branchOperator) {
        if (branchOperator.equalsIgnoreCase(BRANCHOPERATOR_GREATERTHAN)) {
            setGreaterThanOperator();
            return;
        }
        if (branchOperator.equalsIgnoreCase(BRANCHOPERATOR_LESSOREQUAL)) {
            setLessOrEqualOperator();
            return;
        }
        setEndOperator();
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public TreeNode() {
        children = new LinkedList<TreeNode>();
        setEndOperator();
    }

    public void feed(List<Segment> segments) {
        for (Segment segment : segments) {
            feed(segment);
        }
    }

    public void feed(Segment segment) {
        segmentCount++;
        syncRootSegmentCounts();
        if (children.size() > 0) {
            feedToChildren(segment);
        }
    }

    public void setRootSegmentCount(int rootSegmentCount) {
        this.rootSegmentCount = rootSegmentCount;
        syncRootSegmentCounts();
    }

    public int getRootSegmentCount() {
        return rootSegmentCount;
    }

    public int getSegmentCount() {
        return segmentCount;
    }

    private void syncRootSegmentCounts() {
        if (segmentCount > rootSegmentCount) {
            rootSegmentCount = segmentCount;
        }
        for (TreeNode child : children) {
            child.setRootSegmentCount(rootSegmentCount);
        }
    }

    private void feedToChildren(Segment segment) {
        int featureIndex = Arrays.asList(segment.getFeatureNames()).indexOf(label);
        double featureValue = segment.getFeatures()[featureIndex];
        for (TreeNode child : children) {
            boolean matchesGtCase = child.branchOperator.equalsIgnoreCase(BRANCHOPERATOR_GREATERTHAN)
                    && featureValue > child.branchValue;
                    boolean matchesLeCase = child.branchOperator.equalsIgnoreCase(BRANCHOPERATOR_LESSOREQUAL)
                            && featureValue <= child.branchValue;
                    if (matchesGtCase || matchesLeCase) {
                        child.feed(segment);
                    }
        }
    }
}
