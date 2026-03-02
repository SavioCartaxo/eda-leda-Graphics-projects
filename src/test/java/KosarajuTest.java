package test.java;

import java.util.ArrayList;
import java.util.Scanner;

import main.java.algoritmos.Node;

public class KosarajuTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ArrayList<Node> grafo = InputFormatter.format(sc);

        // kosaraju(grafo);

        System.out.println(grafo.toString());
    }
}