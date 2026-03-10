package main.java;

import main.java.algoritmos.Node;
import main.java.algoritmos.Tarjan;
import main.java.algoritmos.Kosaraju;
import java.util.ArrayList;

public class Main {

    private static final int EPOCAS = 50;

    public static void main(String[] args) throws Exception {
        String algoritmo = args[0];
        int n = Integer.parseInt(args[1]);

        ArrayList<Node> grafo = gerarGrafoCiclo(n);

        long total = 0;
        for (int e = 0; e < EPOCAS; e++) {
            ArrayList<Node> grafoFresh = gerarGrafoCiclo(n); // grafo limpo a cada execução
            long inicio = System.nanoTime();
            if (algoritmo.equals("tarjan")) {
                new Tarjan().findSCCs(grafoFresh);
            } else {
                new Kosaraju().findSCCs(grafoFresh);
            }
            total += System.nanoTime() - inicio;
        }

        double media = (total / (double) EPOCAS) / 1_000_000_000.0;
        System.out.println(media);
    }

    /**
     * Gera um grafo direcionado em ciclo simples onde cada nó i
     * aponta para i+1 e o último nó aponta para o primeiro.
     *
     * @param n número de nós do grafo
     * @return grafo em formato de lista de adjacência
     */
    private static ArrayList<Node> gerarGrafoCiclo(int n) {
        ArrayList<Node> grafo = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            grafo.add(new Node(i, i + 1));
        }
        for (int i = 0; i < n - 1; i++) {
            grafo.get(i).addConnections(grafo.get(i + 1));
        }
        grafo.get(n - 1).addConnections(grafo.get(0));
        return grafo;
    }
}