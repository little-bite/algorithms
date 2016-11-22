package project3;

import java.util.Random;

/**
 * Project 3
 * Created by Q on 11/1/2016.
 */
public class Teleport {
    private static final int INFINITY = Integer.MAX_VALUE;
    private static int numberOfSites;
    private static int hauntLimit;
    private static final int MAX_COST = 50;
    private static int[][] costs;
    private static boolean[] isHaunted;
    private static int[][] resultMatrix;
    private static int[][] oldResultMatrix;
    private static int[][] baseCase;
    private static APSPforAH apsp;
    private static TeleportPath path;

    private static void initiate() throws Exception {
        isHaunted = new boolean[numberOfSites];
        costs = new int[numberOfSites][numberOfSites];

        resultMatrix =
                new int[numberOfSites][numberOfSites];
        oldResultMatrix =
                new int[numberOfSites][numberOfSites];
        Random random = new Random();

        for(int i = 0; i < numberOfSites - 1; i++){
            for(int j = i + 1; j < numberOfSites; j++){
                costs[i][j] = 1 +  random.nextInt(MAX_COST);
                // may be changed to asymmetric
                costs[j][i] = costs[i][j];
            }
        }

        //The start and end is always not haunted
//        isHaunted[0] = false;
//        isHaunted[numberOfSites - 1] = false;
//        //TODO test only
//        isHaunted[1] = isHaunted[2] = isHaunted[3] = true;


        int numberOfAHgalaxies = numberOfSites  - 2 - random.nextInt( numberOfSites - 2 - hauntLimit);
        int temp;
        for ( int i = 0; i < numberOfAHgalaxies; i++){
            temp = 1 + random.nextInt(numberOfSites - 2);
            while(isHaunted[temp]){
                temp = 1 + random.nextInt(numberOfSites - 2);
            }
            isHaunted[temp] = true;
        }

        path = new TeleportPath(numberOfSites);
        apsp = new APSPforAH(costs, isHaunted, path);
    }


    private static void solve(int costLimit) {
        int tempMin= INFINITY;
        int temp, temp2;
        // get the haunted = 0 base case
        oldResultMatrix = apsp.getResult();


        if(hauntLimit == 0) {
            resultMatrix = oldResultMatrix;
            return;
        }


        //get the usable base case for number_of_haunted = 0
        baseCase = new int[numberOfSites][numberOfSites];
        for (int i = 0; i < numberOfSites;i++){
            System.arraycopy(oldResultMatrix[i], 0, baseCase[i], 0, numberOfSites);
        }


        int tempK = 0;
        for(int c = 1; c <= costLimit; c++){
            for (int i = 0; i < numberOfSites; i++){
                for (int j = 0; j < numberOfSites; j++){

                    tempMin = INFINITY;

                    for(int k = 0; k< numberOfSites; k++) {
                        temp = (oldResultMatrix[i][k] == INFINITY || baseCase[k][j] == INFINITY)
                                ? INFINITY : oldResultMatrix[i][k] + baseCase[k][j];
                        if(temp < tempMin) {
                            tempMin = temp;
                            tempK = k;
                        }
                    }
                    resultMatrix[i][j] = tempMin;
                    if(tempK == i || tempK == j) {
                        path.copy(i,j);
                    } else {
                        path.concatenate(i, tempK, j);
                    }
                }
            }

            if (c != costLimit) {
                for (int m = 0; m < numberOfSites; m++) {
                    oldResultMatrix[m] = resultMatrix[m].clone();
                    path.updateMatrix();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        long elapseTime, startTime;
        Teleport.hauntLimit = 50;
        Teleport.numberOfSites = 250;
        initiate();
        startTime = System.nanoTime();
        solve(Teleport.hauntLimit);
        elapseTime = (System.nanoTime() - startTime) / 1000000;
        System.out.println(elapseTime);
//        System.out.println("\nThe cost matrix is:");
//        for (int i = 0; i < numberOfSites; i++) {
//            System.out.println(Arrays.toString(costs[i]));
//        }
//        System.out.println("\nThe haunted vector is:" + Arrays.toString(isHaunted));
//
//        System.out.println("\nThe cost limit is:" + hauntLimit);
//
//        if (Teleport.hauntLimit == 0) {
//            System.out.println("There is no base case.");
//        } else {
//                System.out.println("\nThe base case is:");
//                for (int i = 0; i < numberOfSites; i++) {
//                    System.out.println(Arrays.toString(baseCase[i]));
//                }
//        }
//
//        System.out.println("\nThe result is:");
//        for (int i = 0; i < numberOfSites; i++) {
//            System.out.println(Arrays.toString(resultMatrix[i]));
//        }
//
//        System.out.println("\nThe result  path is:");
//        for (int i = 0; i < numberOfSites; i++) {
//            for(int j  =0; j < numberOfSites; j++){
//                System.out.print(path.nextPathMatrix[i][j].toString() + "\t");
//            }
//            System.out.println("");
//        }
    }
}