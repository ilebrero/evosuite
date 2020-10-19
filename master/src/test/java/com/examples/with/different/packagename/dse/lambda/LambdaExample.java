package com.examples.with.different.packagename.dse.lambda;

public class LambdaExample {

    public static int test(int a) {
        TestLambdaClass lambda = new TestLambdaClass();
        return lambda.lambdaTest(a);
    }
}
