package com.examples.with.different.packagename.dse.array;

public class RealArrayAssignmentExample2 {

    public static int test1(double b, double[] array) {
    double a = array[0];

    if (a > 2.4d) {
      array[0] = b;

      if (array[0] > 5.1d && array[0] < 5.8d) {
          return 3;
      }

      if (array[2] == 0.2d) {
        return -1;
      } else {
        return 0;
      }
    } else {
      return 2;
    }
  }

//  NOTE: this tests causes the execution to crash with a VerifyError: Bad type on operand stack.
//        This is due to a class trying to be loaded after it was modified (instrumented) with some errors.
//        (The JVM notice that the class was already loaded but it contains changes). Haven't found where but
//        I assume that there should be some error on the pre-instrumentation phase (when creating the execution graph, etc...).
//
//  public static int realAssignment(double b, double[] array) {
//    double a = array[0];
//
//    if (a > 2.3d) {
//      array[0] = b;
//
//      if (array[0] > 5.1d && array[0] < 5.8d) {
//        if (array[0] == 5.6d) {
//          return 6;
//        } else {
//          return 7;
//        }
//      }
//
//      if (array[2] == 0.2d) {
//        return -1;
//      } else {
//        return 0;
//      }
//    } else {
//      return 2;
//    }
//  }

}
