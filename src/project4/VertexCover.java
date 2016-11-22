package project4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class VertexCover {
    private int [][] inputMatrix;
    private int problemSize;
    // [0,1,2,...n-1]
    private ArrayList<Integer> initialVertices;
    // one of the minimum VCs
    private ArrayList<Integer> solution;

    private VertexCover(int size, int[][] input){
        problemSize = size;
        inputMatrix = input;
        initialVertices = new ArrayList<Integer>();
        for (int i = 0; i < problemSize; i++){
            initialVertices.add(i);
        }
        solution = new ArrayList<Integer>();
    }

    private boolean hasMinimumCoverOf(ArrayList<Integer> vertices, int k) {
        int sumOfRow, sumAll = 0;
        // the base case is requesting a vc of 1, in a sub-graph of n - initial_K -1 (in other word, k == 1)
        if (k == 1) {
            // elements in a new array are 0 by default
            // 1 means there is an edges
            int[][] currentGraph = new int[problemSize][problemSize];
            // generate the graph for current iteration, and get the number of edges (times two)
            for (int i : vertices) {
                for (int j : vertices) {
                    currentGraph[i][j] = inputMatrix[i][j];
                    sumAll += currentGraph[i][j];
                }
            }
            // if all edges in current graph share the same vertex return true, otherwise return false
            for (int i : vertices) {
                sumOfRow = 0;
                for (int j : vertices) {
                    sumOfRow += currentGraph[i][j];
                }
                if (sumOfRow * 2 == sumAll) {
                    // means that if all edges in the graph starts from one vertex
                    solution.add(i);
                    return true;
                }
            }
            return false;
        }

        // for non-base cases, first get an arbitrary edge; to do so, you need to know  every
        // remaining vertex's neighbours( which are also remaining vertices)

        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<ArrayList<Integer>>();
        // create the neighbour array list
        for (int i = 0; i < problemSize; i++) {
            neighbours.add(new ArrayList<Integer>());
            if (vertices.contains(i)) {
                for (int j : vertices) {
                    if (inputMatrix[i][j] == 1) {
                        neighbours.get(i).add(j);
                    }
                }
            }
        }
        int i = 0;
        //If a vertex does not remain in the current sub-graph, or it is not connected, it has no neighbour
        while (neighbours.get(i).size() == 0) {
            i++;
        }
        // now that an edge is found, u,v are its ends
        Integer u = i;
        Integer v = neighbours.get(i).get(0);
        ArrayList<Integer> verticesWithoutU = new ArrayList<Integer>();
        verticesWithoutU.addAll(vertices);
        verticesWithoutU.remove(u);
        ArrayList<Integer> verticesWithoutV = new ArrayList<Integer>();
        verticesWithoutV.addAll(vertices);
        verticesWithoutV.remove(v);
        // the lemma is that,  there is a minimum vertex cover of k for graph G,
        // iff G - {u} or G - {v} (or both) has a minimum vertex cover ok k - 1
        if(hasMinimumCoverOf(verticesWithoutU, k - 1)){
            solution.add(u);
            return true;
        }
        if(hasMinimumCoverOf(verticesWithoutV, k - 1)){
            solution.add(v);
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws FileNotFoundException {
        File[] files = new File("inputs").listFiles();
        if (files == null){
            return;
        }
        Scanner scanner;
        VertexCover vc;
        int requiredSize;
        for(File file : files){
            scanner = new Scanner(file);
            int size = scanner.nextInt();
            int counter = 0;
            int[][] input = new int[size][size];
            while (scanner.hasNextInt()){
                input[counter/size][counter%size] = scanner.nextInt();
                counter++;
            }
            vc = new VertexCover(size, input);
            for (requiredSize = 1; requiredSize < vc.problemSize; requiredSize++){
                if (vc.hasMinimumCoverOf(vc.initialVertices, requiredSize)){
                    System.out.println("----------------");
                    System.out.println("The graph in " + file.getName() + " has a minimum vc of " + requiredSize);
                    System.out.println("One possibility is "+ vc.solution.toString());
                    break;
                }
            }
        }
    }
}