package nl.esciencecenter.eecology.classification.machinelearning;

import com.google.inject.Inject;

public class GeneralTrainerFactory implements TrainerFactory {
    @Inject
    private GeneralTrainer trainer;

    public void setTrainer(GeneralTrainer trainer) {
        this.trainer = trainer;
    }

    @Override
    public Trainer getTrainer() {
        return trainer;
    }
}
