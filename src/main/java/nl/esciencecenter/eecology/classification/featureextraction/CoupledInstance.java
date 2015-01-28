package nl.esciencecenter.eecology.classification.featureextraction;

import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import weka.core.Instance;

/**
 * An extended instance in the sense that it will let you couple any object to this instance. This way some raw data like the
 * window from which the features were created can be attached.
 * 
 * @author Christiaan Meijer, NLeSc
 * 
 * @param <T>
 */
public class CoupledInstance extends Instance {
    private static final long serialVersionUID = 1l;
    private Segment coupledSegment;

    public CoupledInstance(double weight, double[] attValues) {
        super(weight, attValues);
    }

    public CoupledInstance(Instance instance) {
        m_AttValues = instance.toDoubleArray();
        m_Weight = instance.weight();
        m_Dataset = null;
        if (instance instanceof CoupledInstance) {
            coupledSegment = ((CoupledInstance) instance).getCoupledSegment();
        }
    }

    public Segment getCoupledSegment() {
        return coupledSegment;
    }

    public void setCoupledObject(Segment segment) {
        coupledSegment = segment;
    }

    @Override
    public CoupledInstance copy() {
        return new CoupledInstance(this);
    }
}
