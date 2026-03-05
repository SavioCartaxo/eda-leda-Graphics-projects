package main.java.algoritmos;

import java.util.*;

/**
 * Algoritmo de Tarjan para Strongly Connected Components (SCCs).
 * Complexidade: O(V + E)
 */
public class Tarjan {

    private static final int UNVISITED = -1;

    private int[] ids;
    private int[] low;
    private boolean[] onStack;
    private int[] stack;
    private int stackTop;

    private int[] id_counter = {0};
    private int countSCC;
    private ArrayList<Node>[] adj;
    private Map<Integer,Integer> nodeIndex; // valor do nó → índice 0..n-1

    public int scc(ArrayList<Node> graph) {
        final int n = graph.size();

        nodeIndex = new HashMap<>(n * 2);
        for (int i = 0; i < n; i++) {
            nodeIndex.put(graph.get(i).getValue(), i);
        }

        ids = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new int[n];
        stackTop = 0;

        Arrays.fill(ids, UNVISITED);

        id_counter[0] = 0;
        countSCC = 0;

        // Cache das adjacências para evitar chamadas repetidas a getConnections()
        ArrayList<Node>[] adjLocal = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adjLocal[i] = graph.get(i).getConnections();
        }
        adj = adjLocal;

        for (int i = 0; i < n; i++) {
            if (ids[i] == UNVISITED) {
                dfs(i);
            }
        }

        return countSCC;
    }

    private void dfs(int u) {
        ids[u] = id_counter[0];
        low[u] = id_counter[0];
        id_counter[0]++;
        stack[stackTop++] = u;
        onStack[u] = true;

        for (Node vNode : adj[u]) {
            Integer vBoxed = nodeIndex.get(vNode.getValue());
            if (vBoxed == null) continue; // vizinho fora do grafo
            int v = vBoxed;

            if (ids[v] == UNVISITED) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                // back-edge: usa ids[v], não low[v]
                low[u] = Math.min(low[u], ids[v]);
            }
        }

        // u é raiz de um SCC
        if (ids[u] == low[u]) {
            while (true) {
                int node = stack[--stackTop];
                onStack[node] = false;
                low[node] = ids[u]; // todos do SCC apontam para a mesma raiz
                if (node == u) break;
            }
            countSCC++;
        }
    }
}