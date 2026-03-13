package testes;

import java.util.ArrayList;
import java.util.Scanner;

import algoritmos.Node;
import util.InputFormatter;

/**
 * Gera grafos direcionados para testes automatizados de SCC.
 *
 * O grafo é produzido por um script Python externo
 * ({@code scripts/generate_inputs/script_linear_graph.py}),
 * que recebe como parâmetros o número de vértices, arestas e
 * componentes fortemente conectadas (SCCs).
 *
 * A saída do script é lida por um {@link Scanner} e convertida
 * em uma lista de adjacência usando {@link InputFormatter}.
 */
public class GraphGeneratorLinear {

    /**
     * Executa o script Python para gerar um grafo com parâmetros controlados.
     *
     * @param n número de vértices
     * @throws Exception se ocorrer erro na execução do script
     */
    public static ArrayList<Node> generateGraph(int n) throws Exception {

        ProcessBuilder pb = new ProcessBuilder(
                "python",
                "scripts/generate_inputs/script_linear_graph.py",
                String.valueOf(n)
        );

        Process process = pb.start();

        Scanner sc = new Scanner(process.getInputStream());

        ArrayList<Node> grafo = InputFormatter.format(sc);

        process.waitFor();

        sc.close();

        return grafo;
    }
}