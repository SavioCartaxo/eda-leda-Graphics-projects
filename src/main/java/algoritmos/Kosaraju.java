package main.java.algoritmos;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Implementação do algoritmo de Kosaraju para encontrar
 * o número de Componentes Fortemente Conectadas (SCC)
 * em um grafo direcionado.
 *
 * O algoritmo é executado em três etapas:
 *
 * 1) Realiza uma busca em profundidade (DFS) no grafo original,
 *    armazenando os vértices em uma pilha conforme o tempo de término.
 *
 * 2) Constrói o grafo transposto, invertendo todas as arestas.
 *
 * 3) Executa DFS no grafo transposto, seguindo a ordem definida
 *    pela pilha, identificando e contando as componentes fortemente conectadas.
 *
 * Complexidade:
 * Tempo  -> O(V + E)
 * Espaço -> O(V + E)
 */
public class Kosaraju {
    
	/**
     * Executa o algoritmo de Kosaraju em um grafo direcionado.
     *
     * @param grafo lista de nós representando o grafo (lista de adjacência)
     * @return número de Componentes Fortemente Conectadas (SCCs)
     */
	public int contadorSCC(List<Node> grafo) {

		Deque<Node> pilha = new ArrayDeque<>();
		Set<Integer> visitados = new HashSet<>();

		// Parte 1: DFS no grafo original
		for (Node no : grafo) {
			if (!visitados.contains(no.getValue())) {
				dfs1(no, pilha, visitados);
			}
		}

		// Parte 2: Construção do grafo transposto
		Map<Integer, ArrayList<Node>> grafoInvertido = new HashMap<>();

		for (Node node : grafo) {
			grafoInvertido.put(node.getValue(), new ArrayList<>());	
		}

		for (Node node : grafo) {
			for (Node vizinho : node.getConnections()) {
				grafoInvertido.get(vizinho.getValue()).add(node);
			}
		}

		// Parte 3: DFS no grafo transposto
		Set<Integer> visitados2 = new HashSet<>();
		int contadorSCC = 0;

		while (!pilha.isEmpty()) {
			Node node = pilha.removeLast();

			if (!visitados2.contains(node.getValue())) {
				contadorSCC++;
				dfs2(node, grafoInvertido, visitados2);
			}
		}

		return contadorSCC;
	}

	/**
     * DFS do grafo original para preencher a pilha
     * de acordo com o tempo de término.
     */
	private void dfs1(Node node, Deque<Node> pilha, Set<Integer> visitados) {
		visitados.add(node.getValue());

		for (Node vizinho : node.getConnections()) {
			if (!visitados.contains(vizinho.getValue())) {
				dfs1(vizinho, pilha, visitados);
			}
		}

		pilha.addLast(node);
	}

	/**
     * DFS no grafo transposto para visitar todos
     * os nós de uma mesma SCC.
     */
	private void dfs2(Node node, Map<Integer, ArrayList<Node>> grafoInvertido, Set<Integer> visitados) {
		visitados.add(node.getValue());

		for (Node vizinho : grafoInvertido.get(node.getValue())) {
			if (!visitados.contains(vizinho.getValue())) {
				dfs2(vizinho, grafoInvertido, visitados);
			}
		}
	}
}