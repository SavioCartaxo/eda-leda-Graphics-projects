package main.java.algoritmos;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Implementação iterativa do algoritmo de Tarjan para encontrar
 * Strongly Connected Components (SCCs) em um grafo dirigido
 *
 * A implementação utiliza DFS iterativa com uma pilha explícita para
 * evitar estouro de pilha em grafos grandes, mantendo a semântica
 * recursiva do algoritmo original.
 *
 * Complexidade: O(V + E), onde V é o número de vértices e
 * E é o número de arestas.
 */
public class Tarjan {

    /** Inicia com -1 para mostrar que o nó ainda não foi visitado pela DFS. */
    private static final int UNVISITED = -1;

    /**
     * Ordem de descoberta de cada nó durante a DFS.
     * ordemDescobertaNo[i] é atribuído no momento em que o nó i é visitado pela primeira vez.
     */
    private int[] ordemDescobertaNo;
    
    /**
     * Menor id acessível a partir de cada nó considerando apenas
     * os nós atualmente na pilha. Usado para identificar raízes de SCCs.
     */
    private int[] menorId;

    /** Indica se cada nó está atualmente na pilha de SCCs. */
    private boolean[] onStack;
    
    /**
     * Pilha auxiliar que mantém os nós do SCC sendo explorado no momento.
     */
    private int[] stack;
    
    /** Índice do topo da pilha {@link #stack}. */
    private int stackTop;

    /** Contador global incrementado a cada novo nó descoberto pela DFS. */
    private int id;

    /**
     * Mapeamento de índice normalizado para valor original do nó.
     * originalValues[i] contém o valor do nó de índice i.
     */
    private int[] originalValues;

    /** Cache das listas de adjacência de cada nó, indexado pelo índice normalizado do nó. */
    private ArrayList<Node>[] adjacencias;

    /**
     * Lista de saída contendo todos os SCCs encontrados.
     * Cada elemento é um componente representado pelos valores originais de seus nós.
     */
    private ArrayList<ArrayList<Integer>> componentes;

    /**
     * Executa o algoritmo de Tarjan sobre o grafo fornecido e retorna
     * todos os Strongly Connected Components encontrados.
     * Não realiza tratamento de erros — pressupõe entradas válidas
     * para fins de desempenho.
     *
     * @param grafo lista de nós do grafo; cada objeto do tipo Node deve possuir
     *              um índice normalizado único no intervalo [0, n-1]
     * 
     * @return lista de SCCs, onde cada SCC é representado por uma lista
     *         dos valores originais de seus nós; a ordem dos componentes
     *         corresponde à ordem de finalização da DFS (ordem topológica
     *         inversa do grafo condensado)
    */
    public ArrayList<ArrayList<Integer>> findSCCs(ArrayList<Node> grafo) {

        final int n = grafo.size();
        
        ordemDescobertaNo = new int[n];
        menorId = new int[n];
        onStack = new boolean[n];
        stack = new int[n];
        originalValues = new int[n];
        stackTop = 0;
        componentes = new ArrayList<>();

        Arrays.fill(ordemDescobertaNo, UNVISITED);
        id = 0;

        // Cache das adjacências e valores originais
        @SuppressWarnings("unchecked")
        ArrayList<Node>[] adjLocal = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adjLocal[i] = grafo.get(i).getConnections();
            originalValues[i] = grafo.get(i).getValue();
        }
        adjacencias = adjLocal;

        // Garante que todos os nós sejam visitados, mesmo em grafos desconexos
        for (int i = 0; i < n; i++) {
            if (ordemDescobertaNo[i] == UNVISITED) dfs(i);
        }

        return componentes;
    }

    /**
     * Realiza a DFS iterativa a partir do nó start, identificando
     * todos os SCCs alcançáveis.
     *
     * @param start índice normalizado do nó inicial da DFS
     */
    private void dfs(int start) {
        // Cada frame guarda [nó atual, índice do próximo vizinho a visitar]
        Deque<int[]> callStack = new ArrayDeque<>();

        // Visita o nó inicial
        ordemDescobertaNo[start] = menorId[start] = id++;
        stack[stackTop++] = start;
        onStack[start] = true;
        callStack.push(new int[]{start, 0});

        while (!callStack.isEmpty()) {
            int[] frame = callStack.peek();
            int u = frame[0];
            ArrayList<Node> neighbors = adjacencias[u];

            if (frame[1] < neighbors.size()) {
                // Ainda há vizinhos a processar
                int v = neighbors.get(frame[1]++).getIdNormalizado(); // índice direto, sem mapa

                if (ordemDescobertaNo[v] == UNVISITED) {
                    // Tree-edge: empilha o vizinho e desce
                    ordemDescobertaNo[v] = menorId[v] = id++;
                    stack[stackTop++] = v;
                    onStack[v] = true;
                    callStack.push(new int[]{v, 0});
                } else if (onStack[v]) {
                    // Back-edge: atualiza menorId do nó atual
                    menorId[u] = Math.min(menorId[u], ordemDescobertaNo[v]);
                }

            } else {
                // Todos os vizinhos de u foram processados — retorno da DFS
                callStack.pop();

                // Propaga menorId para o pai
                if (!callStack.isEmpty()) {
                    int parent = callStack.peek()[0];
                    menorId[parent] = Math.min(menorId[parent], menorId[u]);
                }

                // Se u é raiz de um SCC, esvazia a pilha até u
                if (ordemDescobertaNo[u] == menorId[u]) {
                    ArrayList<Integer> component = new ArrayList<>();
                    while (true) {
                        int node = stack[--stackTop];
                        onStack[node] = false;
                        menorId[node] = ordemDescobertaNo[u];
                        component.add(originalValues[node]);
                        if (node == u) break;
                    }
                    componentes.add(component);
                }
            }
        }
    }
}