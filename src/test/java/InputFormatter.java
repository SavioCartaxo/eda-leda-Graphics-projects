package test.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import main.java.algoritmos.Node;

public class InputFormatter {

    public static ArrayList<Node> format(Scanner sc) {

        int n = sc.nextInt();
        int k = sc.nextInt();

        ArrayList<Node> grafo = new ArrayList<>();
        HashMap<Integer, Node> conexoes = new HashMap<>();

        for (int i = 0; i < n; i++) {
            int u = sc.nextInt();
            Node newNode = new Node(u);
            grafo.add(newNode);
            conexoes.put(u, newNode);
        }

        for (int i = 0; i < k; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();

            Node pai = conexoes.get(u);
            Node filho = conexoes.get(v);
            pai.addConnections(filho);
        }

        return grafo;
    }
}
