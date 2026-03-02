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
		
		public Node (int value){
			this.value = value;
			this.neighbors = new ArrayList<>();
		}

		public boolean isVisited(){
			return visited;
		}	
		
		public void setNeighbors(List<Node> neighbors){
			this.neighbors = neighbors;
		}
	
		public List<Node> getNeighbors(){
			return this.neighbors;
		}	

		public void setVisited(boolean state){
			this.visited = state;
		}
	}

}
