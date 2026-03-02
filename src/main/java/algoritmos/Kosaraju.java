package main.java.algoritmos;
// Nesta classe está implementada a versão em Java do algoritmo de Kosaraju

import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;

public class Kosaraju {
    
	public int kosaraju(ArrayList<Node> grafo) {
		Deque<Node> pilha = new ArrayDeque<>();
		HashSet<Integer> visitados = new HashSet<>();

		// parte 1: percorrer o grafo e adicionar na pilha
		for (Node node : grafo) {
			if (!visitados.contains(node.getValue())) {
				dfs1(node, pilha, visitados);
			}
		}

		// parte 2: inverter o grafo
		HashMap<Integer, ArrayList<Node>> grafoInvertido = new HashMap<>();

		for (Node node : grafo) {
			grafoInvertido.put(node.getValue(), new ArrayList<>());	
		}

		for (Node node : grafo) {
			for (Node vizinho : node.getConnections()) {
				grafoInvertido.get(vizinho.getValue()).add(node);
			}
		}

		// parte 3: dfs no grafo transposto
		HashSet<Integer> visitados2 = new HashSet<>();
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

	private void dfs1(Node node, Deque<Node> pilha, HashSet<Integer> visitados) {
		visitados.add(node.getValue());

		for (Node vizinho : node.getConnections()) {
			if (!visitados.contains(vizinho.getValue())) {
				dfs1(vizinho, pilha, visitados);
			}
		}

		pilha.addLast(node);
	}

	private void dfs2(Node node, HashMap<Integer, ArrayList<Node>> grafoInvertido, HashSet<Integer> visitados2) {
		visitados2.add(node.getValue());

		for (Node vizinho : grafoInvertido.get(node.getValue())) {
			if (!visitados2.contains(vizinho.getValue())) {
				dfs2(vizinho, grafoInvertido, visitados2);
			}
		}
	}
}