package project2;

import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * The tests to compare d&c and brute-force
 * the d&c approach has a bug if vertical edges of different rectangles align
 * Created by qichen on 10/7/16.
 */
public class Test {
    private static final String TAG_DNC = "Divide and Conquer# ";
    private static final String TAG_BF = "Brute-force# ";
    private static Rectangle[] samples;
    public static void main(String[] args) {

        long elapseTime, startTime;

        //zeroKTestCase(20000);

        linearKTestCase(180000);

        //quadraticTestCase(5000);

        //linearLayeredTestCase(sumOfNN(5));


        RectangleIntersections dnc = new RectangleIntersections(samples);
        startTime = System.nanoTime();
        dnc.report();
        // convert time in ns to ms
        elapseTime = (System.nanoTime() - startTime) / 1000000;

        printResult(TAG_DNC, samples.length, elapseTime, dnc.result);


        BruteForce bf = new BruteForce(samples);
        startTime = System.nanoTime();
        bf.search();
        elapseTime = (System.nanoTime() - startTime) / 1000000;
        printResult(TAG_BF, samples.length, elapseTime, bf.result);

        compareResults(dnc.result, bf.result);

    }



    private static void compareResults(HashSet<TwoSet> dncResult, HashSet<TwoSet> bfResult) {
        HashSet<TwoSet> difference = bfResult.stream()
                .filter(s -> !dncResult.contains(s)).collect(Collectors.toCollection(HashSet::new));
        System.out.println("\ncompareResults\n" + "The difference is " + difference.toString());

    }



    private static void quadraticTestCase(int size) {
        float delta = 1/size;
        float b = 0, l = 0;
        samples = new Rectangle[size];
        for (int i = 1; i<= size; i++){
            //samples[i - 1] = new Rectangle(i*2 + 1,i*2,i * 2, i*2 + 1,i - 1);
            samples[i - 1] = new Rectangle(b + 3, b,l, l + 3,i - 1);
            b += delta;
            l += delta;
        }
    }
    private static void linearLayeredTestCase(int size) {
        int remain = size;
        float left = 0, bottom = 0;
        int edgeLength = 3;
        int layer = 1, rectID =0 ;
        int remainingOfCurrentLayer = 1;
        float deltaX = (float) 0.01;
        samples = new Rectangle[size];
        while (remain !=0 ){
            samples[rectID] = new Rectangle(bottom + edgeLength, bottom, left, left + edgeLength, rectID);
            left -= deltaX;
            rectID++;
            remainingOfCurrentLayer--;
            if (remainingOfCurrentLayer == 0 ) {
                layer++;
                remainingOfCurrentLayer = layer;
                left = 2* (layer - 1);
                bottom += 2 + 4* (layer - 2);
            } else {
                bottom -= 4;
            }
            remain--;
        }
    }


    private static void linearKTestCase(int size) {
        samples = new Rectangle[size];
        for (int i = 1; i<= size; i++){
            //samples[i - 1] = new Rectangle(i*2 + 1,i*2,i * 2, i*2 + 1,i - 1);
            samples[i - 1] = new Rectangle(i*2 + 3,i*2,i * 2, i*2 + 3,i - 1);
        }
    }

    private static void zeroKTestCase(int size) {
        samples = new Rectangle[size];
        for (int i = 1; i<= size; i++){
            //samples[i - 1] = new Rectangle(i*2 + 1,i*2,i * 2, i*2 + 1,i - 1);
            samples[i - 1] = new Rectangle(i*4 + 3,i*4,i * 4, i*4 + 3,i - 1);
        }
    }

    private static int sumOfNN(int n){
        int sum = 0;
        for (int i = 1; i <= n; i++ ){
            sum += i ;
        }
        return  sum;
    }

    private static void printResult(String tag, int amountOfRectangles, long elapseTime, HashSet<TwoSet> result) {
        System.out.println(tag);
        System.out.println("Total number of intersect pairs: "+ result.size()
                        + " found amongst " +amountOfRectangles + " rectangles in " + elapseTime + " ms\n");
        //System.out.println(result.toString());
    }
}
