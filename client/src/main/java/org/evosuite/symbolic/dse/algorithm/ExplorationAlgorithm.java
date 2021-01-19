/*
 * Copyright (C) 2010-2018 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 *
 * This file is part of EvoSuite.
 *
 * EvoSuite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3.0 of the License, or
 * (at your option) any later version.
 *
 * EvoSuite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
package org.evosuite.symbolic.dse.algorithm;

import org.evosuite.Properties;
import org.evosuite.symbolic.MethodComparator;
import org.evosuite.symbolic.PathCondition;
import org.evosuite.symbolic.PathConditionUtils;
import org.evosuite.symbolic.dse.ConcolicExecutor;
import org.evosuite.symbolic.dse.ConcolicExecutorImpl;
import org.evosuite.symbolic.dse.DSEStatistics;
import org.evosuite.symbolic.dse.DSETestCase;
import org.evosuite.symbolic.dse.algorithm.strategies.KeepSearchingCriteriaStrategy;
import org.evosuite.symbolic.dse.algorithm.strategies.PathExtensionStrategy;
import org.evosuite.symbolic.dse.algorithm.strategies.CachingStrategy;
import org.evosuite.symbolic.dse.algorithm.strategies.implementations.PathPruningStrategies.CacheQueryResult;
import org.evosuite.symbolic.dse.algorithm.strategies.TestCaseBuildingStrategy;
import org.evosuite.symbolic.dse.algorithm.strategies.TestCaseSelectionStrategy;
import org.evosuite.symbolic.expr.Constraint;
import org.evosuite.symbolic.solver.Solver;
import org.evosuite.symbolic.solver.SolverEmptyQueryException;
import org.evosuite.symbolic.solver.SolverErrorException;
import org.evosuite.symbolic.solver.SolverFactory;
import org.evosuite.symbolic.solver.SolverParseException;
import org.evosuite.symbolic.solver.SolverResult;
import org.evosuite.symbolic.solver.SolverTimeoutException;
import org.evosuite.symbolic.solver.SolverUtils;
import org.evosuite.testcase.DefaultTestCase;
import org.evosuite.testcase.TestCase;
import org.evosuite.testcase.TestCaseUpdater;
import org.evosuite.testcase.execution.TestCaseExecutor;
import org.evosuite.testsuite.TestSuiteChromosome;
import org.evosuite.utils.ClassUtil;
import org.evosuite.utils.LoggingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

  /**
   * Structure of a DSE Exploration algorithm,
   *
   * Current implementation represents an abstracted version of SAGE's generational algorithm.
   *
   * For more details, please take a look at:
   *     Godefroid P., Levin Y. M. & Molnar D. (2008) Automated Whitebox Fuzz Testing
   *
   * @author Ignacio Lebrero
   */
public abstract class ExplorationAlgorithm extends ExplorationAlgorithmBase {

    private static final transient Logger logger = LoggerFactory.getLogger(ExplorationAlgorithm.class);

    /**
     * Logger Messages
     **/

    // Solver
    public static final String SOLVER_ERROR_DEBUG_MESSAGE              = "Solver threw an exception when running: {}";
    public static final String SOLVER_QUERY_STARTED_MESSAGE            = "Solving query with {} constraints";
    public static final String SOLVER_SOLUTION_DEBUG_MESSAGE           = "solver found solution {}";
    public static final String SOLVER_OUTCOME_NULL_DEBUG_MESSAGE       = "Solver outcome is null (probably failure/unknown/timeout)";
    public static final String SOLVER_OUTCOME_IS_SAT_DEBUG_MESSAGE     = "query is SAT (solution found)";
    public static final String SOLVER_OUTCOME_IS_UNSAT_DEBUG_MESSAGE   = "query is UNSAT (no solution found)";
    public static final String SOLVING_CURRENT_SMT_QUERY_DEBUG_MESSAGE = "* Solving current SMT query";

    // Concolic Engine
    public static final String FINISHED_CONCOLIC_EXECUTION_DEBUG_MESSAGE                  = "* Finished concolic execution.";
    public static final String EXECUTING_CONCOLICALLY_THE_CURRENT_TEST_CASE_DEBUG_MESSAGE = "* Executing concolically the current test case";

    // TestCase generation
    public static final String NEW_TEST_CASE_SCORE_DEBUG_MESSAGE   = "New test case score: {}";
    public static final String NEW_TEST_CASE_CREATED_DEBUG_MESSAGE = "Created new test case from SAT solution: {}";

    // Exploration Algorithm
    public static final String PROGRESS_MSG_INFO                                             = "Total progress: {}";
    public static final String STRATEGY_CANNOT_BE_NULL                                       = "Strategy cannot be null";
    public static final String ENTRY_POINTS_FOUND_DEBUG_MESSAGE                              = "Found {} as entry points for DSE";
    public static final String STOPPING_CONDITION_MET_DEBUG_MESSAGE                          = "A stopping condition was met. No more tests can be generated using DSE.";
    public static final String GENERATING_TESTS_FOR_ENTRY_DEBUG_MESSAGE                      = "Generating tests for entry method {}";
    public static final String TESTS_WERE_GENERATED_FOR_ENTRY_METHOD_DEBUG_MESSAGE           = "{} tests were generated for entry method {}";
    public static final String EXPLORATION_STRATEGIES_MUST_BE_INITIALIZED_TO_START_SEARCHING = "Exploration strategies must be initialized to start searching.";

    /**
     * A cache of previous results from the constraint solver
     **/
    protected final transient Map<Set<Constraint<?>>, SolverResult> queryCache = new HashMap<>();

    /**
     * Exploration strategies
     **/
    private transient CachingStrategy cachingStrategy;
    private transient PathExtensionStrategy pathsExpansionStrategy;
    private transient TestCaseBuildingStrategy testCaseBuildingStrategy;
    private transient TestCaseSelectionStrategy testCaseSelectionStrategy;
    private transient KeepSearchingCriteriaStrategy keepSearchingCriteriaStrategy;

    /**
     * Internal executor and solver
     **/
    private final transient ConcolicExecutor engine;
    private final transient Solver solver;

    public ExplorationAlgorithm() {
        this(
            SHOW_PROGRESS_DEFAULT_VALUE,
            DSEStatistics.getInstance(),
            new ConcolicExecutorImpl(),
            SolverFactory.getInstance().buildNewSolver()
        );
    }

    public ExplorationAlgorithm(
        DSEStatistics statisticsLogger,
        boolean showProgress
    ) {
        this(
            showProgress,
            statisticsLogger,
            new ConcolicExecutorImpl(),
            SolverFactory.getInstance().buildNewSolver()
        );
    }

    public ExplorationAlgorithm(
            boolean showProgress,
            DSEStatistics dseStatistics,
            ConcolicExecutor engine,
            Solver solver
    ) {
        super(dseStatistics, showProgress);

        this.engine = engine;
        this.solver = solver;
    }

    /**
     * Exploration strategies setters
     */
    public void setCachingStrategy(CachingStrategy cachingStrategy) {
        checkStrategy(cachingStrategy);
        this.cachingStrategy = cachingStrategy;
    }

    public void setPathsExpansionStrategy(PathExtensionStrategy pathsExpansionStrategy) {
        checkStrategy(pathsExpansionStrategy);
        this.pathsExpansionStrategy = pathsExpansionStrategy;
    }

    public void setTestCaseBuildingStrategy(TestCaseBuildingStrategy testCaseBuildingStrategy) {
        checkStrategy(testCaseBuildingStrategy);
        this.testCaseBuildingStrategy = testCaseBuildingStrategy;
    }

    public void setTestCaseSelectionStrategy(TestCaseSelectionStrategy testCaseSelectionStrategy) {
        checkStrategy(testCaseSelectionStrategy);
        this.testCaseSelectionStrategy = testCaseSelectionStrategy;
    }

    public void setKeepSearchingCriteriaStrategy(KeepSearchingCriteriaStrategy keepSearchingCriteriaStrategy) {
        checkStrategy(keepSearchingCriteriaStrategy);
        this.keepSearchingCriteriaStrategy = keepSearchingCriteriaStrategy;
    }

    /**
     * Generates a solution for a given class
     *
     * @return
     */
     @Override
     public TestSuiteChromosome explore() {
         if (!strategiesInitialized()) throw new DSEExplorationException(EXPLORATION_STRATEGIES_MUST_BE_INITIALIZED_TO_START_SEARCHING);
         notifyGenerationStarted();
         final Class<?> targetClass = Properties.getTargetClassAndDontInitialise();

         List<Method> targetStaticMethods = ClassUtil.getTargetClassStaticMethods(targetClass);
         Collections.sort(targetStaticMethods, new MethodComparator());
         logger.debug(ENTRY_POINTS_FOUND_DEBUG_MESSAGE, targetStaticMethods.size());

         for (Method entryMethod : targetStaticMethods) {
             if (this.isFinished()) {
                  logger.debug(STOPPING_CONDITION_MET_DEBUG_MESSAGE);
                  break;
             }

             LoggingUtils.getEvoLogger().info("* " + GENERATING_TESTS_FOR_ENTRY_DEBUG_MESSAGE, entryMethod.getName());
             int testCaseCount = testSuite.getTests().size();

             /** Setting up current method being targeted */
             Properties.TARGET_METHOD = entryMethod.getName();

             explore(entryMethod);
             int numOfGeneratedTestCases = testSuite.getTests().size() - testCaseCount;
             logger.debug(TESTS_WERE_GENERATED_FOR_ENTRY_METHOD_DEBUG_MESSAGE, numOfGeneratedTestCases, entryMethod.getName());
         }

         // Run this before finish
         notifyGenerationFinished();
         statisticsLogger.reportTotalTestExecutionTime(TestCaseExecutor.timeExecuted);
         statisticsLogger.logStatistics();
         return testSuite;
     }

    /**
     * Performs DSE on the given method
     *
     * @param method
     */
    @Override
    protected void explore(Method method) {
        // Children cache
        HashSet<Set<Constraint<?>>> seenChildren = new HashSet();

        // WorkList
        PriorityQueue<DSETestCase> testCasesWorkList = new PriorityQueue<>();

        // Initial element
        DSETestCase initialTestCase = testCaseBuildingStrategy.buildInitialTestCase(method);

        // Run & check
        testCasesWorkList.add(initialTestCase);

        while (keepSearchingCriteriaStrategy.ShouldKeepSearching(testCasesWorkList)) {
            // This gets wrapped into the building and fitness strategy selected due to the PriorityQueue sorting nature
            DSETestCase currentTestCase = testCaseSelectionStrategy.getCurrentIterationBasedTestCase(testCasesWorkList);

            // NOTE: We consider Adding a testCase an iteration
            addNewTestCaseToTestSuite(currentTestCase);
            notifyIteration();

            // After iteration checks and logs
            if (showProgress) logger.info(PROGRESS_MSG_INFO, getProgress());
            if (isFinished()) return;

            // Runs the current test case
            GenerationalSearchPathCondition currentExecutedPathCondition = executeTestCaseConcolically(currentTestCase);

            // Checks for a divergence
            boolean hasPathConditionDiverged = checkPathConditionDivergence(
                currentExecutedPathCondition.getPathCondition(),
                currentTestCase.getOriginalPathCondition().getPathCondition()
            );

            Set<Constraint<?>> normalizedPathCondition = normalize(
                     currentExecutedPathCondition.getPathCondition().getConstraints());

            // If a diverged path condition was previously explored, skip it
            if (!shouldSkipCurrentPathcondition(hasPathConditionDiverged, normalizedPathCondition, seenChildren)) {

                // Adds the new path condition to the already visited set
                seenChildren.add(normalizedPathCondition);

                // Generates the children
                List<GenerationalSearchPathCondition> children = pathsExpansionStrategy.generateChildren(currentExecutedPathCondition);

                processChildren(testCasesWorkList, currentTestCase, children, hasPathConditionDiverged);
            }

        }
    }

    private boolean shouldSkipCurrentPathcondition(boolean hasPathConditionDiverged, Set<Constraint<?>> seenPathCondition, HashSet<Set<Constraint<?>>> seenChildren) {
        return hasPathConditionDiverged && (
            seenChildren.contains(seenPathCondition)
              || PathConditionUtils.isConstraintSetSubSetOf(seenPathCondition, seenChildren));
    }

    private void processChildren(PriorityQueue<DSETestCase> testCasesWorkList, DSETestCase currentTestCase, List<GenerationalSearchPathCondition> children, boolean hasPathConditionDiverged) {
        // We look at all the children
        for (GenerationalSearchPathCondition child : children) {
            List<Constraint<?>> childQuery = SolverUtils.buildQuery(child.getPathCondition());
            Set<Constraint<?>> normalizedChildQuery = normalize(childQuery);

            CacheQueryResult cacheQueryResult = cachingStrategy.checkCache(normalizedChildQuery, queryCache);

            // Path condition previously explored and unsatisfiable
            if (!cacheQueryResult.hitUnSat()) {
                statisticsLogger.reportNewConstraints(childQuery);
                Map<String, Object> smtSolution;

                // Path condition already solved before
                if (cacheQueryResult.hitSat()) {
                    smtSolution = cacheQueryResult.getSmtSolution();

                } else {
                    // Path condition not explored
                    assert(cacheQueryResult.missed());

                    childQuery.addAll(
                      SolverUtils.createBoundsForQueryVariables(childQuery)
                    );

                    // Solves the SMT query
                    logger.debug(SOLVER_QUERY_STARTED_MESSAGE, childQuery.size());
                    SolverResult smtQueryResult = solveQuery(childQuery);
                    smtSolution = getQuerySolution(
                      normalizedChildQuery,
                      smtQueryResult
                    );
                }

                if (smtSolution != null) {
                    // Generates the new tests based on the current solution
                    DSETestCase newTestCase = generateNewTestCase(
                        currentTestCase,
                        child,
                        smtSolution,
                        hasPathConditionDiverged);

                    testCasesWorkList.offer(newTestCase);
                }
            }
        }
    }

      /**
     * Generates a new test case from the concolic execution data.
     *
     * @param currentConcreteTest
     * @param currentPathCondition
     * @param smtSolution
     * @param hasPathConditionDiverged
     * @return
     */
    private DSETestCase generateNewTestCase(DSETestCase currentConcreteTest, GenerationalSearchPathCondition currentPathCondition, Map<String, Object> smtSolution, boolean hasPathConditionDiverged) {
        TestCase newTestCase = TestCaseUpdater.updateTest(currentConcreteTest.getTestCase(), smtSolution);

        DSETestCase newDSETestCase = new DSETestCase(
            newTestCase,
            currentPathCondition,
            getTestScore(newTestCase, hasPathConditionDiverged)
        );

        logger.debug(NEW_TEST_CASE_CREATED_DEBUG_MESSAGE, newDSETestCase.getTestCase().toCode());
        logger.debug(NEW_TEST_CASE_SCORE_DEBUG_MESSAGE, newDSETestCase.getScore());

        return newDSETestCase;
    }

    /**
     * Analyzes the results of an smtQuery.
     *
     * @param query
     * @param smtQueryResult
     * @return
     */
    private Map<String, Object> getQuerySolution(Set<Constraint<?>> query, SolverResult smtQueryResult) {
         Map<String, Object> solution = null;

        if (smtQueryResult == null) {
            logger.debug(SOLVER_OUTCOME_NULL_DEBUG_MESSAGE);

            // This doesn't necessarily is a timeout, but we model it this way
            statisticsLogger.reportSolverError();
        } else {
            queryCache.put(query, smtQueryResult);
            statisticsLogger.reportNewQueryCachedValue();

            if (smtQueryResult.isSAT()) {
                logger.debug(SOLVER_OUTCOME_IS_SAT_DEBUG_MESSAGE);
                statisticsLogger.reportNewSAT();

                solution = smtQueryResult.getModel();
                logger.debug(SOLVER_SOLUTION_DEBUG_MESSAGE, solution.toString());
            } else {
                assert (smtQueryResult.isUNSAT());
                statisticsLogger.reportNewUNSAT();
                logger.debug(SOLVER_OUTCOME_IS_UNSAT_DEBUG_MESSAGE);
            }
        }

        return solution;
    }

      /**
       * Checks that the internal algorithm strategies were initialized.
       * @return
       */
      private boolean strategiesInitialized() {
          return testCaseSelectionStrategy != null
            && testCaseBuildingStrategy != null
            && pathsExpansionStrategy != null
            && keepSearchingCriteriaStrategy != null
            && cachingStrategy != null;
      }

    /**
     * Solves an SMT query
     *
     * TODO: check how much moving the time estimation to a lower implementation layer improves precision.
     *
     * @param SMTQuery
     * @return
     */
    private SolverResult solveQuery(List<Constraint<?>> SMTQuery) {
        long startSolvingTime;
        long estimatedSolvingTime;
        SolverResult smtQueryResult;

			  logger.debug(SOLVING_CURRENT_SMT_QUERY_DEBUG_MESSAGE);

			  /** Track solving time and solve the query **/
        startSolvingTime = System.currentTimeMillis();
        smtQueryResult = doSolveQuery(SMTQuery);
        estimatedSolvingTime = System.currentTimeMillis() - startSolvingTime;
			  DSEStatistics.getInstance().reportNewSolvingTime(estimatedSolvingTime);

        return smtQueryResult;
    }

    /**
     * Normalizes the query
     *
     * @param query
     * @return
     */
     private Set<Constraint<?>> normalize(List<Constraint<?>> query) {
        return new HashSet<>(query);
     }

    /**
     * Executes concolically the current TestCase
     *
     * @param currentTestCase
     * @return
     */
    private GenerationalSearchPathCondition executeTestCaseConcolically(DSETestCase currentTestCase) {
        logger.debug(EXECUTING_CONCOLICALLY_THE_CURRENT_TEST_CASE_DEBUG_MESSAGE);

        TestCase clonedCurrentTestCase = currentTestCase.getTestCase().clone();
        PathCondition result = engine.execute((DefaultTestCase) clonedCurrentTestCase);

        // In case of a divergence, we need to keep the lowest value
        int currentGeneratedFromIndex = Math.min(
          currentTestCase.getOriginalPathCondition().getGeneratedFromIndex(),
          result.getBranchConditions().size()
        );

        logger.debug(FINISHED_CONCOLIC_EXECUTION_DEBUG_MESSAGE);

        return new GenerationalSearchPathCondition(result, currentGeneratedFromIndex);
    }

    private SolverResult doSolveQuery(List<Constraint<?>> SMTQuery) {
        SolverResult smtQueryResult = null;

        try {
            smtQueryResult = solver.solve(SMTQuery);
        } catch (SolverTimeoutException
                | SolverParseException
                | SolverEmptyQueryException
                | SolverErrorException
                | IOException e) {
            logger.debug(SOLVER_ERROR_DEBUG_MESSAGE, e.getMessage());
        }
        return smtQueryResult;
    }

    private void checkStrategy(Object strategy) {
        if (strategy == null) throw new IllegalArgumentException(STRATEGY_CANNOT_BE_NULL);
    }
  }