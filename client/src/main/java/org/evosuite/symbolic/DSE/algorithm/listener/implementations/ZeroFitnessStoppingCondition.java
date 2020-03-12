package org.evosuite.symbolic.DSE.algorithm.listener.implementations;

import org.evosuite.ga.Chromosome;
import org.evosuite.symbolic.DSE.algorithm.DSEBaseAlgorithm;
import org.evosuite.symbolic.DSE.algorithm.listener.StoppingCondition;

public class ZeroFitnessStoppingCondition implements StoppingCondition {
    @Override
    public void forceCurrentValue(long value) {

    }

    @Override
    public long getCurrentValue() {
        return 0;
    }

    @Override
    public long getLimit() {
        return 0;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLimit(long limit) {

    }

    @Override
    public void searchStarted(DSEBaseAlgorithm algorithm) {

    }

    @Override
    public void iteration(DSEBaseAlgorithm algorithm) {

    }

    @Override
    public void searchFinished(DSEBaseAlgorithm algorithm) {

    }

    @Override
    public void fitnessEvaluation(Chromosome individual) {

    }

    @Override
    public void modification(Chromosome individual) {

    }
}
