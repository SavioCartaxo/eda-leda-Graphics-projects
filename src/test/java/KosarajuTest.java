package test.java;

import java.util.ArrayList;
import java.util.Scanner;

import main.java.algoritmos.Kosaraju;
import main.java.algoritmos.Node;

public class KosarajuTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ArrayList<Node> grafo = InputFormatter.format(sc);

        Kosaraju kosaraju = new Kosaraju();
        int qtdSCC = kosaraju.contadorSCC(grafo);

        System.out.println("Quantidade de SCC's: " + qtdSCC);
        System.out.println(grafo.toString());
    }
}