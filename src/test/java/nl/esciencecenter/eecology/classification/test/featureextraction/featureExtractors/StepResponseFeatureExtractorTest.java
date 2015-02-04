package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.Convolver;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StepResponseFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class StepResponseFeatureExtractorTest extends FeatureExtractorTest {
    DoubleMatrix vultureWalkX = new DoubleMatrix(new double[][] { { 0.3782, 0.3774, 0.0987, 0.1487, 0.0321, -0.2160, -0.3610,
        0.3034, 0.6644, 0.5523, 0.4567, 0.4103, 0.4395, 0.3266, 0.1510, 0.1181, 0.0396, -0.3326, -0.3303, 0.1091, 0.7960,
        0.5785, 0.2773, 0.3199, 0.2354, 0.3191, 0.2130, 0.0082, -0.1308, -0.3490, 0.1263, 0.5770, 0.5904, 0.4664, 0.4380,
        0.3797, 0.1876, 0.0703, -0.0269, -0.2556 } });
    DoubleMatrix vultureWalkY = new DoubleMatrix(new double[][] { { 0.1635, -0.0113, -0.0588, 0.1281, 0.0512, 0.0226, 0.2110,
        -0.4363, 0.2743, 0.0791, -0.2298, -0.0445, -0.1281, -0.0678, -0.2396, -0.2261, -0.0490, -0.0256, -0.2698, 0.3029,
        -0.0256, -0.2464, 0.1417, 0.1658, 0.0708, 0.0241, 0.0791, 0.0746, -0.0286, -0.0874, -0.5373, 0.3323, 0.2374, -0.1507,
        -0.1198, -0.2336, -0.1485, -0.1176, 0.0430, -0.1221 } });
    DoubleMatrix vultureWalkZ = new DoubleMatrix(new double[][] { { 0.8864, 0.9847, 0.9355, 0.9847, 1.0092, 0.9847, 0.9601,
        1.3054, 1.1082, 0.8120, 0.7874, 0.8864, 0.9110, 0.9847, 0.9355, 0.9355, 0.9601, 0.9110, 0.9355, 1.3791, 1.1819,
        0.8611, 0.8365, 0.8864, 0.8864, 0.9847, 0.9847, 0.9601, 0.9847, 0.8864, 1.2809, 1.1573, 0.8864, 0.8611, 0.9110,
        0.8611, 0.8365, 0.9355, 1.0338, 1.0338 } });
    DoubleMatrix vultureFlapX = new DoubleMatrix(new double[][] { { -0.2676, -0.3229, -0.0254, 0.3722, -0.3909, -0.2504, -0.2653,
        0.2855, -0.3924, -0.0815, -0.3378, 0.4970, -0.2138, 0.1622, -0.3371, 0.4783, 0.0643, 0.1084, -0.4544, 0.4155, 0.2608,
        0.0942, -0.4432, 0.3057, 0.3221, -0.1016, -0.3759, 0.1024, 0.4619, -0.2377, -0.4447, -0.0762, 0.1824, -0.4679,
        -0.1644, -0.1577, 0.4888, -0.2601, 0.1413, -0.3004 } });
    DoubleMatrix vultureFlapY = new DoubleMatrix(new double[][] { { 0.4069, -0.1590, -0.0181, 0.4755, 0.3504, -0.2766, 0.0836,
        0.5516, -0.6820, 0.6956, -0.0113, 0.4469, -0.1213, 0.0143, -0.1243, 0.1801, -0.1952, 0.6081, -0.1281, 0.0445,
        -0.3248, 0.4755, 0.2027, 0.1221, -0.1010, 0.8078, -0.3745, 0.0934, 0.3203, 0.1696, -0.1989, -0.1741, 0.4439, -0.1658,
        0.2148, 0.0528, 0.2118, -0.1138, 0.3542, 0.0543 } });
    DoubleMatrix vultureFlapZ = new DoubleMatrix(new double[][] { { 1.9708, 0.2448, -0.1005, 1.5272, 1.9217, 0.8864, -0.2233,
        1.4781, 1.9708, 1.1082, -0.3223, 1.4282, 1.8726, 0.9847, -0.3960, 1.0092, 1.7982, 1.4282, -0.2978, 1.0092, 1.7736,
        1.7490, -0.3715, 0.5165, 1.8972, 1.7982, -0.0507, 0.1220, 1.7245, 1.9217, 0.4666, -0.0507, 1.6255, 1.9954, 0.9102,
        -0.2233, 1.4528, 1.8473, 1.1328, -0.3469 } });
    double vultureWalkResponse = 7.287;
    double vultureFlapResponse = 3.476;
    private final double largeErrorMargin = 0.05;

    @Test
    public void extractFeatures_vultureWalk_highWalkResponse() {
        // Arrange
        DoubleMatrix x = new DoubleMatrix(1, 0);
        DoubleMatrix a = new DoubleMatrix(1, 3);
        DateTime[][] t = new DateTime[1][3];
        FormattedSegments formattedSegments = createFormattedSegments(vultureWalkX, vultureWalkY, vultureWalkZ, a, a, a, a, t);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(formattedSegments);

        // Assert
        assertEquals(vultureWalkResponse, features.get(0, 0), largeErrorMargin);
    }

    @Test
    public void extractFeatures_vultureFlap_lowFlapResponse() {
        // Arrange
        DoubleMatrix x = new DoubleMatrix(1, 0);
        DoubleMatrix a = new DoubleMatrix(1, 3);
        DateTime[][] t = new DateTime[1][3];
        FormattedSegments formattedSegments = createFormattedSegments(vultureFlapX, vultureFlapY, vultureFlapZ, a, a, a, a, t);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(formattedSegments);

        // Assert
        assertEquals(vultureFlapResponse, features.get(0, 0), largeErrorMargin);
    }

    @Before
    public void setUp() {
        StepResponseFeatureExtractor stepResponseFeatureExtractor = new StepResponseFeatureExtractor();
        stepResponseFeatureExtractor.setMeanExtractor(new MeanExtractor());
        stepResponseFeatureExtractor.setConvolver(new Convolver());
        featureExtractor = stepResponseFeatureExtractor;
    }

}
