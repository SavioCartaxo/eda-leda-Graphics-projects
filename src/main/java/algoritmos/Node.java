package main.java.algoritmos;

import java.util.ArrayList;

/**
 * Representa um vértice de um grafo direcionado.
 * 
 * Cada {@code Node} possui um valor identificador e uma lista de
 * conexões direcionadas para outros nós (lista de adjacência).
 */
public class Node {

    private final int index;
    private final int originalValue;
    private final ArrayList<Node> connections;

    /**
     * Cria um novo nó com o valor especificado.
     *
     * @param index index normalizado, 0.. n-1
     * @param value valor original da entrada
     */
    public Node(int index, int originalValue) {
        this.index = index;
        this.value = originalValue;
        this.connections = new ArrayList<>();
    }

    /**
     * Retorna o índice normalizado do nó.
     *
     * @return index do nó
     */
    public int getValue() {
        return this.index;
    }

    /**
     * Retorna o valor original do nó.
     *
     * @return valor original do nó
     */
    public int getOriginalValue() {
        return this.originalValue;
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

    /**
     * Retorna uma representação textual do nó.
     * 
     * O formato da string é:
     * valor -> v1 v2 v3 ...
     * 
     * @return Uma string contendo o valor do nó seguido pelos valores 
     * dos nós aos quais ele está conectado.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(originalValue).append(" -> ");

        for (Node node : connections) {
            sb.append(node.getOriginalValue()).append(" ");
        }

        return sb.toString();
    }
}

