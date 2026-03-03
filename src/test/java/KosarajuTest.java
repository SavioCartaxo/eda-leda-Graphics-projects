package test.java;

import java.util.ArrayList;
import java.util.Scanner;

import main.java.algoritmos.Kosaraju;
import main.java.algoritmos.Node;


/*
 Para testar o algoritmo, execute os seguintes comandos na raiz do projeto:

 1) Gerar o arquivo de entrada (exemplo para N = 100 e K = 100):
    python scripts/scripty_cycle_graph.py 100 100 > input.txt

 2) Compilar as classes:
    javac -d out src/main/java/algoritmos/*.java src/test/java/*.java

 3) Executar o teste:

    • No PowerShell (Windows):
      Get-Content input.txt | java -cp out test.java.KosarajuTest

    • No Linux ou MacOS:
      java -cp out test.java.KosarajuTest < input.txt
*/
public class KosarajuTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ArrayList<Node> grafo = InputFormatter.format(sc);

        Kosaraju kosaraju = new Kosaraju();

        long startTime = System.currentTimeMillis(); // tempo inicial
        int qtdSCC = kosaraju.contadorSCC(grafo);
        long endTime = System.currentTimeMillis();   // tempo final

        System.out.println("Quantidade de SCC's: " + qtdSCC);
        System.out.println("Tempo de execução: " + (endTime - startTime) + " ms");
        
    }
}