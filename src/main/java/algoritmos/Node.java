package main.java.algoritmos;

import java.util.ArrayList;

/**
 * Representa um vértice de um grafo direcionado.
 * 
 * Cada {@code Node} possui um valor identificador e uma lista de
 * conexões direcionadas para outros nós (lista de adjacência).
 */
public class Node {

    private final int value;
    private final ArrayList<Node> connections;

    /**
     * Cria um novo nó com o valor especificado.
     *
     * @param value identificador do nó
     */
    public Node(int value) {
        this.value = value;
        this.connections = new ArrayList<>();
    }

    /**
     * Retorna o valor identificador do nó.
     *
     * @return valor do nó
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Retorna a lista de nós adjacentes (conexões direcionadas).
     * 
     * @return retorna a lista com todos o Nodes
     */
    public ArrayList<Node> getConnections() {
        return this.connections;
    }

    /**
     * Adiona uma nova conexão direcional entre os Nodes.
     * 
     * @param newConnection Node que vai ser adicionado uma nova conexão.
     */
    public void addConnections(Node newConnection){
        this.connections.add(newConnection);
    }
}

