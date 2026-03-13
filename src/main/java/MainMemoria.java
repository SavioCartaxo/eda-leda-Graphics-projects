import algoritmos.Node;
import algoritmos.Tarjan;
import algoritmos.Kosaraju;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

public class MainMemoria {

    private static final java.util.Set<String> ALGORITMOS_VALIDOS = new java.util.HashSet<>(
        java.util.Arrays.asList("tarjan", "kosaraju")
    );

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Uso: java MainMemoria <algoritmo> <arquivo_input>");
            System.out.println("Algoritmos: tarjan | kosaraju");
            return;
        }

        String algoritmo = args[0];
        String arquivo   = args[1];

        if (!ALGORITMOS_VALIDOS.contains(algoritmo)) {
            System.out.println("Algoritmo invalido: " + algoritmo);
            return;
        }

        // Lê o grafo — processo limpo, sem execuções anteriores no heap
        ArrayList<Node> grafo = lerGrafo(arquivo);

        // Força o GC antes de medir para partir de um heap limpo
        System.gc();
        Thread.sleep(500);

        // Captura o uso de heap antes da execução
        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        long antes = mem.getHeapMemoryUsage().getUsed();

        // Executa o algoritmo — apenas ele é medido
        if (algoritmo.equals("tarjan")) {
            new Tarjan().findSCCs(grafo);
        } else {
            new Kosaraju().findSCCs(grafo);
        }

        // Captura memória após execução e imprime no formato capturável pelo shell
        long deltaMb = (mem.getHeapMemoryUsage().getUsed() - antes) / (1024 * 1024);
        System.out.println("MEMORIA_MB:" + deltaMb);
    }

    private static ArrayList<Node> lerGrafo(String caminhoArquivo) throws Exception {
        Scanner sc = new Scanner(new File(caminhoArquivo));

        int n = sc.nextInt();
        int k = sc.nextInt();

        ArrayList<Node> grafo = new ArrayList<>();
        HashMap<Integer, Node> mapa = new HashMap<>();

        for (int i = 0; i < n; i++) {
            int valor = sc.nextInt();
            Node node = new Node(i, valor);
            grafo.add(node);
            mapa.put(valor, node);
        }

        for (int i = 0; i < k; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            Node pai   = mapa.get(u);
            Node filho = mapa.get(v);
            if (pai != null && filho != null) {
                pai.addConnections(filho);
            }
        }

        sc.close();
        return grafo;
    }
}