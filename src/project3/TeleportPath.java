package project3;

import java.util.ArrayList;

/**
 * Created by qichen on 11/11/16.
 */
public class TeleportPath {
    ArrayList[][] currentPathMatrix;
    ArrayList[][] nextPathMatrix;
    int size;

    public TeleportPath(int numberOfGalaxies){
        size = numberOfGalaxies;
        currentPathMatrix = new ArrayList[numberOfGalaxies][numberOfGalaxies];
        nextPathMatrix = new ArrayList[numberOfGalaxies][numberOfGalaxies];

        for(int i =0; i < size; i++){
            for (int j =0 ; j < size; j++){
                currentPathMatrix[i][j] = new ArrayList<Integer>();
                nextPathMatrix[i][j] = new ArrayList<Integer>();
                currentPathMatrix[i][j].add(i);
                currentPathMatrix[i][j].add(j);
            }
        }

    }

    public void concatenate(int f, int s, int t){
        nextPathMatrix[f][t] = (ArrayList<Integer>) currentPathMatrix[f][s].clone();
        nextPathMatrix[f][t].remove(nextPathMatrix[f][t].size() - 1);
        nextPathMatrix[f][t].addAll(currentPathMatrix[s][t]);
    }

    public void copy(int start, int end){
        nextPathMatrix[start][end] = (ArrayList<Integer>) currentPathMatrix[start][end].clone();
    }

    public  void updateMatrix(){
        for(int i = 0; i < size; i++){
            for(int j =0; j < size; j++){
                currentPathMatrix[i][j] = (ArrayList<Integer>) nextPathMatrix[i][j].clone();
            }
        }
    }
}
