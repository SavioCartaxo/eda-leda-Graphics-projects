package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import algoritmos.Node;

public class InputFormatter {

    /**
     * Formata a entrada lida pelo Scanner e constrói o grafo
     * como lista de adjacência.
     *
     * A entrada deve seguir o formato:
     * - Primeira linha: N (número de vértices) e K (número de arestas)
     * - N linhas seguintes: valor de cada vértice
     * - K linhas seguintes: pares u v representando arestas direcionadas u → v
     *
     * @param sc Scanner com a entrada a ser lida
     * @return lista de nós representando o grafo em lista de adjacência,
     *         com índices normalizados de 0 a N-1
     */
    public static ArrayList<Node> format(Scanner sc) {

        int n = sc.nextInt();
        int k = sc.nextInt();

        ArrayList<Node> grafo = new ArrayList<>();
        HashMap<Integer, Node> conexoes = new HashMap<>();

        for (int i = 0; i < n; i++) {
            int u = sc.nextInt();
            Node newNode = new Node(i, u);
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
