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

/**
 * DSE Algorithms implemented.
 * Please add a citation to the article, source of information or small explanation from which the algorithm is based.
 *
 * @author ignacio lebrero
 */
public enum DSEAlgorithms {
    SAGE("SAGE",
      "Based on generational search in Automated Whitebox Fuzz Testing, Godefroid, Levin, Molnar",
      new Properties.Criterion[]{Properties.Criterion.BRANCH},
      new Properties.DSEStoppingConditionCriterion[]{});

    private String name;
    private String description;
    private Properties.Criterion[] criteria;
    private Properties.DSEStoppingConditionCriterion[] stoppingConditionCriterions;

    DSEAlgorithms(String name,
                  String description,
                  Properties.Criterion[] criteria,
                  Properties.DSEStoppingConditionCriterion[] stoppingConditionCriterions) {
        this.name = name;
        this.criteria = criteria;
        this.description = description;
        this.stoppingConditionCriterions = stoppingConditionCriterions;
    }

    public String getName() { return this.name; }
    public String getDescription() { return description; }
    public Properties.Criterion[] getCriteria() { return criteria; }
    public Properties.DSEStoppingConditionCriterion[] getStoppingConditionCriterions() { return stoppingConditionCriterions; }
}
