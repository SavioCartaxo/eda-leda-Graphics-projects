package testes;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import algoritmos.Kosaraju;
import algoritmos.Node;
import algoritmos.Tarjan;
import util.GraphGeneratorLinear;

/**
 * Testes automatizados para grafos lineares (cadeias).
 *
 * Em um grafo linear com n vértices, cada vértice forma
 * sua própria SCC, pois não há como retornar ao vértice
 * anterior. O número esperado de SCCs é igual a n.
 * Os testes verificam se Tarjan e Kosaraju retornam n SCCs
 * para grafos de tamanhos variados.
 * 
 * Para executar no terminal rode na raiz do projeto:
 * javac -cp "lib\junit-platform-console-standalone-1.9.3.jar; src" -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
 * java -jar lib\junit-platform-console-standalone-1.9.3.jar -cp out --scan-classpath
 *  
 */
public class TestLinearGraph {

    @Test
    public void testLinearGraphTarjan1() throws Exception {
        int n = 50;
        ArrayList<Node> grafo = GraphGeneratorLinear.generateGraph(n);
        assertEquals(n, new Tarjan().findSCCs(grafo).size());
    }

    @Test
    public void testLinearGraphTarjan2() throws Exception {
        int n = 1000;
        ArrayList<Node> grafo = GraphGeneratorLinear.generateGraph(n);
        assertEquals(n, new Tarjan().findSCCs(grafo).size());
    }

    @Test
    public void testLinearGraphKosaraju1() throws Exception {
        int n = 50;
        ArrayList<Node> grafo = GraphGeneratorLinear.generateGraph(n);
        assertEquals(n, new Kosaraju().findSCCs(grafo).size());
    }

    @Test
    public void testLinearGraphKosaraju2() throws Exception {
        int n = 1000;
        ArrayList<Node> grafo = GraphGeneratorLinear.generateGraph(n);
        assertEquals(n, new Kosaraju().findSCCs(grafo).size());
    }
}