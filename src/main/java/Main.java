package main.java;

import main.java.algoritmos.Node;
import main.java.algoritmos.Tarjan;
import main.java.algoritmos.Kosaraju;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        String algoritmo = args[0]; // "tarjan" ou "kosaraju"

        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int K = sc.nextInt();

        // Cria os nós
        ArrayList<Node> grafo = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            int valor = sc.nextInt();
            grafo.add(new Node(i, valor));
        }

        // Lê as arestas
        for (int i = 0; i < K; i++) {
            int u = sc.nextInt() - 1; // converte para índice 0-based
            int v = sc.nextInt() - 1;
            grafo.get(u).addConnections(grafo.get(v));
        }

        sc.close();

        // Executa o algoritmo e mede o tempo
        long inicio = System.nanoTime();

        if (algoritmo.equals("tarjan")) {
            new Tarjan().findSCCs(grafo);
        } else {
            new Kosaraju().findSCCs(grafo);
        }

        long fim = System.nanoTime();

        // Imprime o tempo em segundos para o Python ler
        double tempo = (fim - inicio) / 1_000_000_000.0;
        System.out.println(tempo);
    }
}