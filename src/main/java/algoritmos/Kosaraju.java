package main.java.algoritmos;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;

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
     * @return os Componentes Fortemente Conectados (SCCs)
     */
	public ArrayList<ArrayList<Integer>> findSCCs(List<Node> grafo) {

		Deque<Node> pilha = new ArrayDeque<>();
		boolean[] visitados = new boolean[grafo.size()];

		// Parte 1: DFS no grafo original
		for (Node no : grafo) {
			if (!visitados[no.getIdNormalizado()]) {
				dfs1(no, pilha, visitados);
			}
		}

		// Parte 2: Construção do grafo transposto
		ArrayList<ArrayList<Node>> grafoInvertido = new ArrayList<>(grafo.size());

		for (int i = 0; i < grafo.size(); i++) {
			grafoInvertido.add(new ArrayList<>());
		}
		
		for (Node node : grafo) {
			for (Node vizinho : node.getConnections()) {
				grafoInvertido.get(vizinho.getIdNormalizado()).add(node);
			}
		}

		// Parte 3: DFS no grafo transposto
		boolean[] visitados2 = new boolean[grafo.size()];
		ArrayList<ArrayList<Integer>> SCCs = new ArrayList<>();

		while (!pilha.isEmpty()) {
			Node node = pilha.removeLast();

			if (!visitados2[node.getIdNormalizado()]) {
				ArrayList<Integer> scc = new ArrayList<>();
				dfs2(node, grafoInvertido, visitados2, scc);
				SCCs.add(scc);
			}
		}

		return SCCs;
		
	}

	/**
     * DFS do grafo original para preencher a pilha
     * de acordo com o tempo de término.
     */
	private void dfs1(Node node, Deque<Node> pilha, boolean[] visitados) {
		Deque<Node> pilhaAuxiliar = new ArrayDeque<>();
		Deque<Node> ordem = new ArrayDeque<>();
		pilhaAuxiliar.addLast(node);

		while (!pilhaAuxiliar.isEmpty()) {
			Node atual = pilhaAuxiliar.removeLast();
			if (!visitados[atual.getIdNormalizado()]) {
				visitados[atual.getIdNormalizado()] = true;
				ordem.addLast(atual);

				for (Node vizinho : atual.getConnections()) {
					if (!visitados[vizinho.getIdNormalizado()]) {
						pilhaAuxiliar.addLast(vizinho);
					}
				}
			}
		}
		
		while (!ordem.isEmpty()) {
			pilha.addLast(ordem.removeLast());
		}
	}

	/**
     * DFS no grafo transposto para visitar todos
     * os nós de uma mesma SCC.
     */
	private void dfs2(Node node, ArrayList<ArrayList<Node>> grafoInvertido, boolean[] visitados, ArrayList<Integer> scc) {
		Deque<Node> pilhaAuxiliar = new ArrayDeque<>();

		pilhaAuxiliar.addLast(node);

		while (!pilhaAuxiliar.isEmpty()) {
			Node atual = pilhaAuxiliar.removeLast();
			if (!visitados[atual.getIdNormalizado()]) {
				visitados[atual.getIdNormalizado()] = true;
				scc.add(atual.getValue());
				
				for (Node vizinho : grafoInvertido.get(atual.getIdNormalizado())) {
					if (!visitados[vizinho.getIdNormalizado()]) {
						pilhaAuxiliar.addLast(vizinho);
					}
				}
			}
		}
	}

	// Versão recursiva da dfs1, mais simples de entender
	private void recursiveDfs1(Node node, Deque<Node> pilha, boolean[] visitados) {
		visitados[node.getIdNormalizado()] = true;
		for (Node vizinho : node.getConnections()) {
			if (!visitados[vizinho.getIdNormalizado()]) {
				recursiveDfs1(vizinho, pilha, visitados);
			}
		}
		pilha.addLast(node);
	}

	// Versão recursiva da dfs2
	private void recursiveDfs2(Node node, ArrayList<ArrayList<Node>> grafoInvertido, boolean[] visitados, ArrayList<Integer> scc) {
		visitados[node.getIdNormalizado()] = true;
		scc.add(node.getValue());
		for (Node vizinho : grafoInvertido.get(node.getIdNormalizado())) {
			if (!visitados[vizinho.getIdNormalizado()]) {
				recursiveDfs2(vizinho, grafoInvertido, visitados, scc);
			}
		}
	}
}