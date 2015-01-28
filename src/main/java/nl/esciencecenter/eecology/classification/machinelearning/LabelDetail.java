package nl.esciencecenter.eecology.classification.machinelearning;

public class LabelDetail {
    private int labelId;
    private String description;
    private double colorR;
    private double colorG;
    private double colorB;

    public int getLabelId() {
        return labelId;
    }

    public void setLabelId(int labelId) {
        this.labelId = labelId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getColorR() {
        return colorR;
    }

    public void setColorR(double colorR) {
        this.colorR = colorR;
    }

    public double getColorG() {
        return colorG;
    }

    public void setColorG(double colorG) {
        this.colorG = colorG;
    }

    public double getColorB() {
        return colorB;
    }

    public void setColorB(double colorB) {
        this.colorB = colorB;
    }
}
