package main.java.algoritmos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Algoritmo de Tarjan para Strongly Connected Components (SCCs).
 * Complexidade: O(V + E)
 */
public class Tarjan {

    private static final int UNVISITED = -1;

    private int[] ids;          // ordem de descoberta de cada nó
    private int[] low;          // menor id acessível a partir de cada nó
    private boolean[] onStack;  // indica se o nó está atualmente na pilha
    private int[] stack;        // pilha de nós do SCC atual
    private int stackTop;

    private int id;             // contador global de descoberta
    //private int countSCC;
    private ArrayList<Node>[] adj;          // cache das adjacências
    private Map<Integer,Integer> nodeIndex; // valor do nó → índice 0..n-1
    private int[] indexToValue;             // índice → valor original do nó
    private ArrayList<ArrayList<Integer>> out;

    

    public ArrayList<ArrayList<Integer>> scc(ArrayList<Node> graph) {
        final int n = graph.size();

         // Mapeia valores arbitrários dos nós para índices contíguos
        nodeIndex = new HashMap<>(n * 2);
        indexToValue = new int[n];
        for (int i = 0; i < n; i++) {
            int val = graph.get(i).getValue();
            nodeIndex.put(val, i);
            indexToValue[i] = val;
        }

        ids = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new int[n];
        stackTop = 0;
        out = new ArrayList<>();

        Arrays.fill(ids, UNVISITED);

        id = 0;
        //countSCC = 0;

        // Cache das adjacências para evitar chamadas repetidas a getConnections()
        ArrayList<Node>[] adjLocal = new ArrayList[n];
        for (int i = 0; i < n; i++) adjLocal[i] = graph.get(i).getConnections();
        adj = adjLocal;

        // Garante que todos os nós sejam visitados, mesmo em grafos desconexos
        for (int i = 0; i < n; i++) {
            if (ids[i] == UNVISITED) dfs(i);
        }

        return out;
    }

    private void dfs(int u) {
        // Inicializa o nó com o id atual e empilha
        ids[u] = low[u] = id++;
        stack[stackTop++] = u;
        onStack[u] = true;

        for (Node vNode : adj[u]) {
            Integer vBoxed = nodeIndex.get(vNode.getValue());
            if (vBoxed == null) continue; // vizinho fora do grafo
            int v = vBoxed;

            if (ids[v] == UNVISITED) {
                // Tree-edge: desce na DFS e propaga o low de volta
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                // Back-edge: atualiza com ids[v] pois v ainda não pertence a nenhum SCC
                low[u] = Math.min(low[u], ids[v]);
            }
        }

        // Se ids[u] == low[u], u é a raiz do SCC — esvazia a pilha até u
        if (ids[u] == low[u]) {
            ArrayList<Integer> component = new ArrayList<>();
            while (true) {
                int node = stack[--stackTop];
                onStack[node] = false;
                low[node] = ids[u]; // todos do SCC apontam para a mesma raiz
                component.add(indexToValue[node]);
                if (node == u) break;
            }
            out.add(component);
            //countSCC++;
        }
    }
}