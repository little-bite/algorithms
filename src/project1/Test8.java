package project1;

/**
 * The first project for CSCI6212.
 * Created by qichen on 9/18/16.
 */
public class Test8 {
    private static final double BASE = 2;
    private int a, b;
    private long n, instructionCounter;
    private long time;
    private double log_base_n;
    private Test8(long input){
        n = input;
        // logarithm of BASE (base = 2)
        log_base_n = Math.log(n)/ Math.log(BASE);
        a = 1;
        b = 1;
    }

    private void run() {
        long j = 5;
        int sum = 0;
        long k;
        long tempTime = System.nanoTime();
        long endTime;
        instructionCounter = 0;
        while (j < log_base_n) {
            k = 5;
            while (k < n) {
                sum += 1;
                k = (long) Math.pow(k, 1.3);
                instructionCounter++;
            }
            j = (long) (1.3 * j);
        }
        endTime = System.nanoTime();
        time = endTime - tempTime;
    }

    public static void main(String[] args) {
        long[] size = new long[15];
        for (int i = 0; i < 15; i ++) {
            size[i] =(long) (256* Math.pow(2, 4*i));

        }
        double logTime;
        double hypothesis, logOfHypothesis;
        Test8 test;
        // the hypothesis is O((loglogn)^2)
        for (long n : size) {
            test = new Test8(n);
            hypothesis = Math.pow(Math.log(Math.log(n) / Math.log(BASE) )/Math.log(BASE), 2);
            logOfHypothesis = 2 * Math.log(
                    Math.log(
                            Math.log(n)
                                    / Math.log(BASE)
                    )/Math.log(BASE)
            )/Math.log(BASE);
            test.run();
            logTime = Math.log(test.time)/ Math.log(BASE);
            double logCounter = Math.log(test.instructionCounter)/ Math.log(BASE);

            System.out.println("=======================================");
            System.out.println("Test with " + n + "(logn is " + test.log_base_n +  " )" +" " +
                    "Total time elapse is " +  test.time + "/" + logTime);
            System.out.println("Counter is " + test.instructionCounter + " / " + logCounter);
            System.out.println("The hypothesis is " + hypothesis + "/" + logOfHypothesis);
        }

    }
}
