package com.examples.with.different.packagename.dse.array;

public class IntegerArrayAssignmentExample2 {

    public static int test1(int b, int[] array) {
    int a = array[0];
    int[] arr = new int[3];
    arr[2] = array[3];

    if (arr[2] > 2) {
      array[2] = b;

      if (array[2] == 0) {
        return -1;
      } else {
        return 0;
      }
    } else {
      return 2;
    }
  }

}
