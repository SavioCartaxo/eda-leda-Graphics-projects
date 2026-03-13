package main.java;

import main.java.algoritmos.Node;
import main.java.algoritmos.Tarjan;
import main.java.algoritmos.Kosaraju;
import main.java.algoritmos.TarjanRecursivo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;

public class Main {

    // Épocas de aquecimento — rodam antes da medição para o JIT otimizar o bytecode
    // Não são contabilizadas no resultado
    private static final int WARMUP = 5;

    // Épocas de medição — usadas para calcular a mediana
    // 11 épocas: o valor do meio (6º) é a mediana, eliminando outliers naturalmente
    private static final int EPOCAS = 11;

    // Algoritmos válidos — usados na validação e no switch de execução
    private static final java.util.Set<String> ALGORITMOS_VALIDOS = new java.util.HashSet<>(
        java.util.Arrays.asList("tarjan", "kosaraju", "tarjan-recursivo")
    );

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Uso: java Main <algoritmo> <input1.txt> [input2.txt ...]");
            System.out.println("Algoritmos: tarjan | kosaraju | tarjan-recursivo");
            return;
        }

        String algoritmo = args[0];

        if (!ALGORITMOS_VALIDOS.contains(algoritmo)) {
            System.out.println("Algoritmo invalido: " + algoritmo);
            System.out.println("Opcoes: tarjan | kosaraju | tarjan-recursivo");
            return;
        }

        // Prepara o CSV de saída — um arquivo por execução do container
        new File("/app/resultados").mkdirs();
        String csvPath = "/app/resultados/resultado_" + algoritmo + ".csv";
        FileWriter fw = new FileWriter(csvPath);
        fw.write("n,arestas,k_sccs,mediana_ms\n");

        System.out.println("Algoritmo : " + algoritmo);
        System.out.println("Warmup    : " + WARMUP + " epocas (descartadas)");
        System.out.println("Medicao   : " + EPOCAS + " epocas (mediana)");
        System.out.println("===========================================");

        // Itera sobre cada arquivo de input recebido como argumento
        // Cada arquivo representa um valor de N diferente
        for (int a = 1; a < args.length; a++) {
            String arquivo = args[a];
            String nomeArquivo = new File(arquivo).getName();

            System.out.println("\n[INPUT] " + nomeArquivo);

            // Lê o grafo UMA vez e reutiliza nas épocas
            // Isso elimina o tempo de I/O da medição
            ArrayList<Node> grafo = lerGrafo(arquivo);
            int n       = grafo.size();
            int arestas = contarArestas(grafo);
            int kSccs   = extrairK(nomeArquivo);

            // -----------------------------------------------
            // WARMUP — JIT aquece sem contabilizar tempo
            // -----------------------------------------------
            System.out.print("[WARMUP] ");
            for (int w = 0; w < WARMUP; w++) {
                executar(algoritmo, grafo);
                System.out.print(".");
            }
            System.out.println(" OK");

            // -----------------------------------------------
            // MEDIÇÃO — só o algoritmo de SCC é cronometrado
            // -----------------------------------------------
            double[] tempos = new double[EPOCAS];
            for (int e = 0; e < EPOCAS; e++) {
                long inicio = System.nanoTime();
                executar(algoritmo, grafo);
                tempos[e] = (System.nanoTime() - inicio) / 1_000_000.0;
            }

            // Ordena os tempos e pega o valor do meio (mediana)
            // Isso elimina picos causados por GC, interrupções do SO, etc.
            Arrays.sort(tempos);
            double mediana = tempos[EPOCAS / 2];

            System.out.printf("[OK] n=%-8d → mediana: %.4f ms%n", n, mediana);

            fw.write(n + "," + arestas + "," + kSccs + "," + mediana + "\n");
            fw.flush(); // garante escrita mesmo se o programa for interrompido
        }

        fw.close();
        System.out.println("\n===========================================");
        System.out.println("CSV salvo em: " + csvPath);
    }

    // Despacha para a implementação correta
    // Iterativas usam findSCCs(), recursivas usam scc()
    private static void executar(String algoritmo, ArrayList<Node> grafo) {
        switch (algoritmo) {
            case "tarjan"                -> new Tarjan().findSCCs(grafo);
            case "kosaraju"              -> new Kosaraju().findSCCs(grafo);
            case "tarjan-recursivo"      -> new TarjanRecursivo().scc(grafo);
        }
    }

    // Lê o arquivo e monta a estrutura de Node
    // Chamado apenas UMA vez por arquivo — fora do loop de medição
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

    // Conta o total de arestas no grafo para registrar no CSV
    private static int contarArestas(ArrayList<Node> grafo) {
        int total = 0;
        for (Node n : grafo) total += n.getConnections().size();
        return total;
    }

    // Extrai o valor de K do nome do arquivo (formato: grafo_nN_mM_kK.txt)
    private static int extrairK(String nomeArquivo) {
        try {
            String[] partes = nomeArquivo.replace(".txt", "").split("_");
            for (String p : partes) {
                if (p.startsWith("k")) return Integer.parseInt(p.substring(1));
            }
        } catch (Exception ignored) {}
        return -1;
    }
}