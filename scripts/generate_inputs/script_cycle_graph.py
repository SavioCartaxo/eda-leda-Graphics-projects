import sys

"""
Gera um grafo direcionado em formato de ciclo simples para testes de desempenho
do algoritmo, onde cada nó i possui uma aresta para i+1 e o último nó aponta
para o primeiro. A função será utilizada para testar diferentes escalas de
entrada, especificamente para N = 10^2, 10^3, 10^4, 10^5 e 10^6 (com K = N),
permitindo avaliar o comportamento e a eficiência do algoritmo em volumes
crescentes de dados.
"""
def generate_cycle_graph(N):
    K = N

    print(f"{N} {K}")

    # Nós
    for i in range(1, N + 1):
        print(i)

    # Arestas formando ciclo
    for i in range(1, N):
        print(f"{i} {i+1}")
    print(f"{N} 1")

"""
Para executar via terminal:
python scripts/generate_inputs/script_cycle_graph.py N K > arquivo_saida.txt

Exemplo:
python scripts/generate_inputs/script_cycle_graph.py 1000000 1000000 > input.txt
"""
if __name__ == "__main__":
    N = int(sys.argv[1])
    generate_cycle_graph(N)