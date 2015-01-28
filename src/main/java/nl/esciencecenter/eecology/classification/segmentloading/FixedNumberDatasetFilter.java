package nl.esciencecenter.eecology.classification.segmentloading;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class FixedNumberDatasetFilter {
    @Inject
    @Named("train_instances_per_class")
    private String trainInstancesNumberPerClass;

    private Map<Integer, Integer> requirementsPerClass;
    private Map<Integer, Integer> counterPerClass;

    public void setTrainInstancesNumberPerClass(String trainInstancesNumberPerClass) {
        this.trainInstancesNumberPerClass = trainInstancesNumberPerClass;
    }

    public List<Segment> filter(List<Segment> segments) {
        initCounterAndRequirementsPerClass(trainInstancesNumberPerClass);
        return getRequiredSegments(segments);
    }

    private LinkedList<Segment> getRequiredSegments(List<Segment> segments) {
        LinkedList<Segment> results = new LinkedList<Segment>();
        for (Segment segment : segments) {
            int labelId = segment.getLabelDetail().getLabelId();
            if (requirementsPerClass.containsKey(labelId) == false) {
                continue;
            }

            if (requirementsPerClass.get(labelId) > counterPerClass.get(labelId)) {
                results.add(segment);
                counterPerClass.put(labelId, counterPerClass.get(labelId) + 1);
            }
        }

        checkIfRequirementsAreSatisfied();
        return results;
    }

    private void checkIfRequirementsAreSatisfied() {
        boolean trainSetWasInsufficient = false;
        StringBuilder message = new StringBuilder();
        for (Integer labelId : requirementsPerClass.keySet()) {
            Integer required = requirementsPerClass.get(labelId);
            Integer available = counterPerClass.get(labelId);
            if (required > available) {
                message.append("For label " + labelId + ", the number of required instances (" + required
                        + ") exceeds the number that is available (" + available + "). ");
                trainSetWasInsufficient = true;
            }
        }
        if (trainSetWasInsufficient) {
            throw new NotEnoughSegmentsException(message.toString());
        }
    }

    private void initCounterAndRequirementsPerClass(String integersAsString) {
        requirementsPerClass = new HashMap<Integer, Integer>();
        counterPerClass = new HashMap<Integer, Integer>();
        for (String pairAsString : trainInstancesNumberPerClass.split(",")) {
            String[] pair = pairAsString.trim().split(":");
            int key = Integer.parseInt(pair[0].trim());
            int value = Integer.parseInt(pair[1].trim());
            requirementsPerClass.put(key, value);
            counterPerClass.put(key, 0);
        }

    }

}
