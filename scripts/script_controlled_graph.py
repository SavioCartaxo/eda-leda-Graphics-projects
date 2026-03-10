import sys
import random

"""
Gera um grafo direcionado controlado para testes de desempenho dos algoritmos
de Tarjan e Kosaraju. Permite definir exatamente N (vértices), M (arestas) e
K (número de Componentes Fortemente Conectadas - SCCs).
"""
def generate_controlled_graph(N, M, K):
    if K < 1 or K > N:
        raise ValueError("K deve estar entre 1 e N.")

    print(f"{N} {M}")

    for i in range(1, N + 1):
        print(i)

    groups = {i: [] for i in range(1, K + 1)}
    group_of_node = {}

    for i in range(1, K + 1):
        groups[i].append(i)
        group_of_node[i] = i

    for i in range(K + 1, N + 1):
        g = random.randint(1, K)
        groups[g].append(i)
        group_of_node[i] = g

    edges = set()

    for g_id, nodes in groups.items():
        sz = len(nodes)
        if sz > 1:
            for j in range(sz):
                u = nodes[j]
                v = nodes[(j + 1) % sz]
                edges.add((u, v))

    if len(edges) > M:
        raise ValueError(f"M é muito pequeno para {N} nós e {K} SCCs.")

    attempts = 0
    max_attempts = M * 10 

    while len(edges) < M and attempts < max_attempts:
        attempts += 1
        u = random.randint(1, N)
        v = random.randint(1, N)
        
        gu = group_of_node[u]
        gv = group_of_node[v]

        if gu == gv or gu < gv:
            if u != v and (u, v) not in edges:
                edges.add((u, v))

    for u, v in edges:
        print(f"{u} {v}")

"""
Para executar via terminal:
python script_controlled_graph.py N M K > arquivo_saida.txt

Exemplo:
python script_controlled_graph.py 10000 25000 50 > input.txt
"""
if __name__ == "__main__":
    N = int(sys.argv[1])
    M = int(sys.argv[2])
    K = int(sys.argv[3])
    generate_controlled_graph(N, M, K)