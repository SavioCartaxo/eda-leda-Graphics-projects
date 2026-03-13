package testes;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import algoritmos.Kosaraju;
import algoritmos.Node;
import algoritmos.Tarjan;
import util.GraphGeneratorCiclico;

/**
 * Testes automatizados para grafos cíclicos.
 *
 * Um grafo cíclico com n vértices forma exatamente 1 SCC,
 * pois todos os vértices se alcançam mutuamente pelo ciclo.
 * Os testes verificam se Tarjan e Kosaraju retornam 1 SCC
 * para grafos de tamanhos variados.
 * 
 * Para executar no terminal use:
 * javac -cp "lib\junit-platform-console-standalone-1.9.3.jar; src" -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
 * java -jar lib\junit-platform-console-standalone-1.9.3.jar -cp out --scan-classpath
 *  
 */
public class TestCyclicGraph {

    @Test
    public void testCyclicGraphTarjan1() throws Exception {
        int expected = 1;
        ArrayList<Node> grafo = GraphGeneratorCiclico.generateGraphCiclico(50);
        assertEquals(expected, new Tarjan().findSCCs(grafo).size());
    }

    @Test
    public void testCyclicGraphTarjan2() throws Exception {
        int expected = 1;
        ArrayList<Node> grafo = GraphGeneratorCiclico.generateGraphCiclico(1000);
        assertEquals(expected, new Tarjan().findSCCs(grafo).size());
    }

    @Test
    public void testCyclicGraphKosaraju1() throws Exception {
        int expected = 1;
        ArrayList<Node> grafo = GraphGeneratorCiclico.generateGraphCiclico(50);
        assertEquals(expected, new Kosaraju().findSCCs(grafo).size());
    }

    @Test
    public void testCyclicGraphKosaraju2() throws Exception {
        int expected = 1;
        ArrayList<Node> grafo = GraphGeneratorCiclico.generateGraphCiclico(1000);
        assertEquals(expected, new Kosaraju().findSCCs(grafo).size());
    }
}