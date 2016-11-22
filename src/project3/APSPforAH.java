package project3;

/**

 The apsp dp approach

 can be used for astro-haunted problem
 isBlocked means isHaunted

 Created by qichen_wang on 11/3/2016.
 */
public class APSPforAH {
    private static final int INFINITY = Integer.MAX_VALUE;
    private int numberOfNodes;
    private int[][] oldResult;
    private int[][] result;
    private boolean[] isBlocked;
    private int[][] weight;
    private TeleportPath teleportPath;
    public APSPforAH(int[][] costMatrix, boolean[] isBlocked, TeleportPath path) throws Exception {
        numberOfNodes = costMatrix[0].length;
        if (numberOfNodes != isBlocked.length){
            throw new Exception("invalid parameters");
        }
        this.isBlocked = isBlocked;
        oldResult = new int[numberOfNodes][numberOfNodes];
        result = new int[numberOfNodes][numberOfNodes];

        weight = costMatrix;
        teleportPath = path;
        solve();
    }

    private void solve(){
// base case oldResult[i][j][0]
        for (int i = 0; i < numberOfNodes; i++){
            for (int j = i; j < numberOfNodes; j++) {
                oldResult[i][j] = weight[i][j];
                oldResult[j][i] = weight[j][i];
            }
        }
        int tempCost;
        for (int k = 0; k < numberOfNodes; k++){
            for (int i = 0; i < numberOfNodes; i++){
                for (int j = 0; j < numberOfNodes; j++) {
                    if (!isBlocked[k]) {
                        if (oldResult[i][k] != INFINITY && oldResult[k][j] != INFINITY) {
                            tempCost = oldResult[i][k] + oldResult[k][j];
                            if (tempCost < oldResult[i][j]){
                                result[i][j] = tempCost;
                                teleportPath.concatenate(i,k,j);
                            } else {
                                result[i][j] = oldResult[i][j];
                                teleportPath.copy(i,j);
                            }
                        } else {
                            result[i][j] = oldResult[i][j];
                            teleportPath.copy(i,j);
                        }

                    }  else {
                        result[i][j] = oldResult[i][j];
                        teleportPath.copy(i,j);
                    }
                }
            }

            teleportPath.updateMatrix();

            if (k != numberOfNodes - 1) {
                // update oldResult for next iteration
                for (int m = 0; m < numberOfNodes; m++) {
                    oldResult[m] = result[m].clone();
                }
            }
        }
    }

    int[][] getResult() {
        int[][] theResult = new int[numberOfNodes][numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            theResult[i] = result[i].clone();
        }
        return theResult;
    }


    public static void main(String[] args) throws Exception {
        int[][] cost = {{0,INFINITY,2,1},{INFINITY,0,1,2},{2,1,0,1},{1,2,1,0}};
        boolean[] isBlocked ={false,false,true,false};
        APSPforAH apsp = new APSPforAH(cost, isBlocked, new TeleportPath((4)));
        apsp.solve();
        int[][] result = apsp.getResult();
        for(int i = 0; i < cost.length; i++){
            for (int j = 0; j < cost[0].length; j++) {
                System.out.print(result[i][j] + "\t");
            }
            System.out.println();
        }

    }
}