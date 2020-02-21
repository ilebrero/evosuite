package org.evosuite.symbolic.DSE.algorithm.implementations;

import org.evosuite.symbolic.DSE.algorithm.DSEAlgorithm;
import org.evosuite.symbolic.PathCondition;
import org.evosuite.symbolic.expr.Constraint;
import org.evosuite.testcase.TestCase;
import org.evosuite.testcase.utils.TestCaseUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DSEAlgorithmPaper1 extends DSEAlgorithm {

    private int tests = 2;

    @Override
    protected boolean shouldKeepSearching(List<TestCase> generatedTests) {
        if (tests > 0) {
            tests--;
            return true;
        }

        return false;
    }

    @Override
    protected boolean shouldSkipCurrentPath(HashSet<Set<Constraint<?>>> alreadyGeneratedPathConditions, Set<Constraint<?>> constraintSet) {
        return false;
    }

    @Override
    protected TestCase getCurrentIterationBasedTestCase(List<TestCase> generatedTests) {
        return generatedTests.get( generatedTests.size() - 1 );
    }

    @Override
    protected TestCase buildInitialTestCase(Method method) {
        return TestCaseUtils.buildTestCaseWithDefaultValues(method);
    }

    @Override
    protected PathCondition getNextPathToExplore(PathCondition currentPathCondition) {
        return currentPathCondition;
    }

    @Override
    protected Set<Constraint<?>> normalize(List<Constraint<?>> query) {
        return new HashSet<Constraint<?>>(query);
    }

    @Override
    protected Set<Constraint<?>> normalizePathCondition(PathCondition currentPathCondition) {
        return new HashSet();
    }
}
