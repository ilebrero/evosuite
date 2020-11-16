package com.examples.with.different.packagename.dse.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TestLambdaClass {
    private List<Integer> list = new ArrayList();

    public int lambdaTest(int in) {
        if (in > 3) list.add(1);

        Function<Integer, Integer> test = (a) -> {
            if (list.size() > 0 && a > 6) return 0;
            return 1;
        };

        return test.apply(in);
    }
}
