package algoritmos;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Algoritmo de Tarjan para Strongly Connected Components (SCCs).
 * Versão recursiva com acesso direto via Node normalizado.
 * Complexidade: O(V + E)
 */
public class TarjanRecursivo {

    private static final int UNVISITED = -1;

    private int[] ids;          // ordem de descoberta de cada nó
    private int[] low;          // menor id acessível a partir de cada nó
    private boolean[] onStack;  // indica se o nó está atualmente na pilha
    private int[] stack;        // pilha de nós do SCC atual
    private int stackTop;

    private int id;               // contador global de descoberta
    private int[] originalValues; // índice → valor original do nó
    private ArrayList<Node>[] adj;          // cache das adjacências
    private ArrayList<ArrayList<Integer>> out;

    /**
     * Executa o algoritmo de Tarjan em um grafo direcionado.
     *
     * @param graph lista de nós representando o grafo (lista de adjacência)
     * @return os Componentes Fortemente Conectados (SCCs)
     */
    public ArrayList<ArrayList<Integer>> scc(ArrayList<Node> graph) {
        final int n = graph.size();

        ids = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new int[n];
        originalValues = new int[n];
        stackTop = 0;
        out = new ArrayList<>();

        Arrays.fill(ids, UNVISITED);
        id = 0;

        // Cache das adjacências e valores originais
        @SuppressWarnings("unchecked")
        ArrayList<Node>[] adjLocal = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adjLocal[i] = graph.get(i).getConnections();
            originalValues[i] = graph.get(i).getValue();
        }
        adj = adjLocal;

        // Garante que todos os nós sejam visitados, mesmo em grafos desconexos
        for (int i = 0; i < n; i++) {
            if (ids[i] == UNVISITED) dfs(i);
        }

        return out;
    }

    /**
     * DFS recursiva para calcular ids e low-link values.
     * Quando ids[u] == low[u], u é raiz de um SCC.
     *
     * @param u índice normalizado do vértice a ser visitado
     */
    private void dfs(int u) {
        // Inicializa o nó com o id atual e empilha
        ids[u] = low[u] = id++;
        stack[stackTop++] = u;
        onStack[u] = true;

        for (Node vNode : adj[u]) {
            int v = vNode.getIdNormalizado(); // índice direto, sem mapa

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
                component.add(originalValues[node]);
                if (node == u) break;
            }
            out.add(component);
        }
    }
}
