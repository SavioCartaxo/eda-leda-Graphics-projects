package algoritmos;
import java.util.*;

public class DepthFirstSearch {

	public DepthFirstSearch(){}

	/**
    * Executa DFS de forma iterativa utilizando uma pilha.
    *
    * @param initialNode nó inicial da busca
    */
	public void iterativeDfs(Node initialNode){
		if (initialNode == null) return;
		Deque<Node> stack = new LinkedList<>();
		stack.push(initialNode);
		ArrayList<Integer> list = new ArrayList<>();
		
		while(!stack.isEmpty()){
			Node current = stack.pop();
			
			if (!current.isVisited()){
				current.setVisited(true);
				
				System.out.println(current.value);	

				for (Node node : current.getNeighbors()){
					stack.push(node);
				}
			}
		}
	}

	/**
    * Executa DFS de forma recursiva.
    *
    * @param node nó atual da busca
    */
	public void recursiveDfs(Node node){
		if (node == null) return;
		node.setVisited(true);
		System.out.println(node.value);
		for (Node neighbor : node.getNeighbors()){
			if (!neighbor.isVisited()){
				recursiveDfs(neighbor);
			}
		}
	} 

	
	/**
    * Classe que representa um nó do grafo.
    */
	static class Node{

		private int value;
		private boolean visited;
		private List<Node> neighbors;
		

		/**
		 * Cria um novo nó com o valor especificado.
		 *
		 * @param value valor identificador do nó
		 */
		public Node (int value){
			this.value = value;
			this.neighbors = new ArrayList<>();
		}

		/**
		 * Retorna se o nó já foi visitado.
		 *
		 * @return true se visitado, false caso contrário
		 */
		public boolean isVisited(){
			return visited;
		}	
		
		/**
		 * Define a lista de vizinhos do nó.
		 *
		 * @param neighbors lista de nós adjacentes
		 */
		public void setNeighbors(List<Node> neighbors){
			this.neighbors = neighbors;
		}
	
	    /**
		 * Retorna a lista de vizinhos do nó.
		 *
		 * @return lista de nós adjacentes
		 */
		public List<Node> getNeighbors(){
			return this.neighbors;
		}	

		/**
		 * Define o estado de visita do nó.
		 *
		 * @param state true para marcar como visitado, false para desmarcar
		 */
		public void setVisited(boolean state){
			this.visited = state;
		}
	}

}
