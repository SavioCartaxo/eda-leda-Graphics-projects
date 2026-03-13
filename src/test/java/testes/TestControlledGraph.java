package testes;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import algoritmos.Kosaraju;
import algoritmos.Node;
import algoritmos.Tarjan;
import util.GraphGenerator;

/**
 * Conjunto de testes automatizados para validar os algoritmos
 * de Tarjan e Kosaraju na detecção de Componentes Fortemente
 * Conectadas (SCC).
 *
 * Os grafos são gerados automaticamente com parâmetros
 * controlados (número de vértices, arestas e SCCs) usando
 * {@link GraphGenerator}. Cada teste verifica se o número
 * de SCCs encontrado pelo algoritmo corresponde ao esperado.
 *
 * Para executar no terminal use:
 * javac -cp "lib\junit-platform-console-standalone-1.9.3.jar; src" -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
 * java -jar lib\junit-platform-console-standalone-1.9.3.jar -cp out --scan-classpath
 *  
 */
public class TestControlledGraph {

    @Test
    public void testRandomGraphsTarjan1() throws Exception {

        for (int i = 0; i < 100; i++) {

            int expected = 10;

            ArrayList<Node> grafo = GraphGenerator.generateGraph(50, 200, expected);

            ArrayList<Node> grafoTarjan = grafo;
            Tarjan tarjan = new Tarjan();
            int sccTarjan = tarjan.findSCCs(grafoTarjan).size();            
            assertEquals(expected, sccTarjan);

        }
    }

    @Test
    public void testRandomGraphsTarjan2() throws Exception {

        for (int i = 0; i < 100; i++) {

            int expected = 500;

            ArrayList<Node> grafo = GraphGenerator.generateGraph(1000, 3000, expected);

            ArrayList<Node> grafoTarjan = grafo;
            Tarjan tarjan = new Tarjan();
            int sccTarjan = tarjan.findSCCs(grafoTarjan).size();            
            assertEquals(expected, sccTarjan);

        }
    }

    @Test
    public void testRandomGraphsKosaraju1() throws Exception {

        for (int i = 0; i < 100; i++) {

            int expected = 10;

            ArrayList<Node> grafo = GraphGenerator.generateGraph(50, 200, expected);
            
            ArrayList<Node> grafoKosaraju = grafo;
            Kosaraju kosaraju = new Kosaraju();
            int sccKosaraju = kosaraju.findSCCs(grafoKosaraju).size();
            assertEquals(expected, sccKosaraju);
        }
    }

    @Test
    public void testRandomGraphsKosaraju2() throws Exception {

        for (int i = 0; i < 100; i++) {

            int expected = 500;

            ArrayList<Node> grafo = GraphGenerator.generateGraph(1000, 3000, expected);
            
            ArrayList<Node> grafoKosaraju = grafo;
            Kosaraju kosaraju = new Kosaraju();
            int sccKosaraju = kosaraju.findSCCs(grafoKosaraju).size();
            assertEquals(expected, sccKosaraju);
        }
    }
}