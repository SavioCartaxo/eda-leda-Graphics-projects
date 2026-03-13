import java.util.ArrayList;
import java.util.Scanner;

import algoritmos.Node;
import algoritmos.Tarjan;
import util.InputFormatter;

/*
 Para testar o algoritmo, execute os seguintes comandos na raiz do projeto:

 1) Gerar o arquivo de entrada (exemplo para N = 100 e K = 100):
    python scripts/script_cycle_graph.py 100 100 > input.txt

 2) Compilar as classes:
    javac -d out src/main/java/algoritmos/*.java src/test/java/*.java

 3) Executar o teste:

    • No PowerShell (Windows):
      Get-Content input.txt | java -Xss512m -cp out test.java.TarjanTest

    • No Linux ou MacOS:
      java -cp out test.java.TarjanTest < input.txt
*/
public class TarjanTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<Node> grafo = InputFormatter.format(sc);

        Tarjan tj = new Tarjan();
        long startTime = System.currentTimeMillis(); // tempo inicial
        int count = tj.findSCCs(grafo).size();
        long endTime = System.currentTimeMillis();   // tempo final

        //Se queira exibir todos os grupos de SCCs
        //System.out.println(SCCs);

        System.out.println("Quantidade de SCC's: " + count);
        System.out.println("Tempo de execução: " + (endTime - startTime) + " ms");

        sc.close();
    }
}