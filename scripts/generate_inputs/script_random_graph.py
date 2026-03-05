import sys
import random

def generate_random_graph(N):
    E = N * 2
    
    print(f"{N} {E}")

    for i in range(1, N + 1):
        print(i)

    for _ in range(E):
        u = random.randint(1, N)
        v = random.randint(1, N)
        print(f"{u} {v}")

"""
Para executar via terminal:
python scripts/generate_inputs/script_cycle_graph.py N K > arquivo_saida.txt

Exemplo:
python scripts/generate_inputs/script_cycle_graph.py 1000000 1000000 > input.txt
"""
if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python script.py <numero_de_nos>")
    else:
        N = int(sys.argv[1])
        generate_random_graph(N)