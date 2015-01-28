package nl.esciencecenter.eecology.classification.test.dataaccess;

import java.util.List;

import nl.esciencecenter.eecology.classification.dataaccess.SegmentAsTupleSaver;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

public class SegmentAsTupleSaverFixture extends SegmentAsTupleSaver {
    public void setDefaultFirstIndexText(String defaultFirstIndexText) {
        this.defaultFirstIndexText = defaultFirstIndexText;
    }

    @Override
    public String getResultMessage(List<Segment> segments) {
        return super.getResultMessage(segments);
    }

}
