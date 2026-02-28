import java.util.*;

public class Tarjan{
    private ArrayList<ArrayList<Integer>> graph;
    private static int UNVISITED = -1;
    private int id;
    private int[] ids; // Usado para dizer se um nó foi ou não visitado
    private int[] low; // Usado para dar a cada nó um Low Link Value
    private boolean[] onStack;  // Diz se cada nó está atualmente na pilha;
    private ArrayList<Integer> stack; 
    private ArrayList<ArrayList<Integer>> out; // Saída a qual cada elemento da lista é um SCC
    private int countSCC;


    //public static int count_scc(ArrayList<ArrayList<int>> vector){
    //}

    public ArrayList<ArrayList> scc(ArrayList<ArrayList<Integer>> graph){
        // iniciando variáveis
        this.graph = graph;
        int n = n;
        
        ids = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new ArrayList<>();
        out = new ArrayList<>();

        id = 0;
        countSCC = 0;
        for(int i = 0; i < n; i++) ids[i] = UNVISITED;
           
        // Chamando a DFS para cada vértice  
        for(int i = 0; i < n; i++){
            if(ids[i] == UNVISITED)
                dfs(i);
        }

        return out;
    }

    private void dfs(int u){
        // Como é a primeira vez que u é visitado, atualizamos seu valor nas variáveis auxiliares
        stack.add(u);
        onStack[u] = true;
        ids[u] = low[u] = id++;
        
        // Visitar todos os vizinho e manter o menor valor de id
        // para entre os nós que podem ser visitados partindo do nó atual
        for(int v : graph.get(u)){
            if (ids[v] == UNVISITED) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } 
            else if (onStack[v]) {
                low[u] = Math.min(low[u], ids[v]);
            }
        }

        // Após minimizar o valor de id atual e visitar todos os vizinho 'u'
        // verificamos se estamos em um scc, esvaziamos o dado stack até voltarmos ao início do scc
        if(ids[u] == low[u]){
            ArrayList<Integer> component = new ArrayList<>();

            while (true) {
                int node = stack.remove(stack.size() - 1);
                onStack[node] = false;
                component.add(node);

                if (node == u) break;
            }
            
            out.add(component);
            countSCC++;
        }
    }
}