#!/bin/bash

# =============================================================
# PIPELINE DE BENCHMARK — SCC (comparação dois a dois)
# =============================================================
# Uso:
#   Iterativos:
#     ./benchmark.sh tarjan kosaraju <densidade> <nivel_scc>
#
#   Recursivos:
#     ./benchmark.sh tarjan-recursivo tarjan-recursivo-hash
#
# Roda os dois algoritmos sequencialmente usando os mesmos inputs,
# e gera automaticamente um CSV comparativo ao final
# =============================================================

set -e

# -------------------------------------------------------------
# VALIDAÇÃO DOS ARGUMENTOS
# -------------------------------------------------------------

eh_recursivo() {
  [[ "$1" == "tarjan-recursivo" || "$1" == "tarjan-recursivo-hash" ]]
}

eh_valido() {
  [[ "$1" == "tarjan" || "$1" == "kosaraju" || \
     "$1" == "tarjan-recursivo" || "$1" == "tarjan-recursivo-hash" ]]
}

if [ "$#" -lt 2 ]; then
  echo "Uso:"
  echo "  ./benchmark.sh tarjan kosaraju <densidade> <nivel_scc>"
  echo "  ./benchmark.sh tarjan-recursivo tarjan-recursivo-hash"
  exit 1
fi

ALGO1=$1
ALGO2=$2

if ! eh_valido "$ALGO1"; then
  echo "Erro: algoritmo invalido '$ALGO1'."; exit 1
fi
if ! eh_valido "$ALGO2"; then
  echo "Erro: algoritmo invalido '$ALGO2'."; exit 1
fi

# Os dois precisam ser do mesmo tipo
if eh_recursivo "$ALGO1" && eh_recursivo "$ALGO2"; then
  MODO="recursivo"
elif ! eh_recursivo "$ALGO1" && ! eh_recursivo "$ALGO2"; then
  MODO="iterativo"
else
  echo "Erro: mistura de algoritmos recursivos e iterativos não é permitida."
  echo "Use dois iterativos ou dois recursivos."
  exit 1
fi

if [ "$MODO" = "iterativo" ]; then
  if [ "$#" -ne 4 ]; then
    echo "Uso: ./benchmark.sh $ALGO1 $ALGO2 <densidade> <nivel_scc>"
    exit 1
  fi
  DENSIDADE=$3
  NIVEL_SCC=$4

  if [[ "$DENSIDADE" != "baixa" && "$DENSIDADE" != "media" && "$DENSIDADE" != "alta" ]]; then
    echo "Erro: densidade invalida '$DENSIDADE'. Use: baixa | media | alta"; exit 1
  fi
  if [[ "$NIVEL_SCC" != "muitos" && "$NIVEL_SCC" != "medios" && "$NIVEL_SCC" != "poucos" ]]; then
    echo "Erro: nivel_scc invalido '$NIVEL_SCC'. Use: muitos | medios | poucos"; exit 1
  fi
else
  if [ "$#" -ne 2 ]; then
    echo "Uso: ./benchmark.sh $ALGO1 $ALGO2"
    echo "Algoritmos recursivos não recebem densidade nem nivel_scc."
    exit 1
  fi
fi

# -------------------------------------------------------------
# CONFIGURAÇÕES
# Progressão dobrando de 100 até 1.000.000
# -------------------------------------------------------------

NS=(100 20000 40000 60000 80000 100000 120000 140000 160000 180000 200000 220000 240000 260000 280000 300000 320000 340000 360000 380000 400000 420000 440000 460000 480000 
500000 520000 540000 560000 580000 600000 620000 640000 660000 680000 700000 720000 740000 760000 780000 800000 820000 840000 860000 880000 900000 920000 940000 960000 980000 1000000)

if [ "$MODO" = "iterativo" ]; then
  case $DENSIDADE in
    baixa) FATOR_M=2  ;;
    media) FATOR_M=5  ;;
    alta)  FATOR_M=10 ;;
  esac

  case $NIVEL_SCC in
    muitos) DIVISOR_K=3  ;;
    medios) DIVISOR_K=10 ;;
    poucos) DIVISOR_K=30 ;;
  esac
fi

# -------------------------------------------------------------
# PREPARAÇÃO
# -------------------------------------------------------------

mkdir -p inputs resultados
INPUT_FILES=()

if [ "$MODO" = "recursivo" ]; then
  echo "============================================="
  echo " Benchmark: $ALGO1 vs $ALGO2 (grafo linear)"
  echo "============================================="
else
  echo "============================================="
  echo " Benchmark: $ALGO1 vs $ALGO2 | densidade: $DENSIDADE | sccs: $NIVEL_SCC"
  echo "============================================="
fi

# -------------------------------------------------------------
# ETAPA 1 — Gera/reutiliza inputs
# Gerados uma vez e compartilhados pelos dois algoritmos,
# garantindo comparação justa no mesmo grafo
# -------------------------------------------------------------

echo ""
echo "[1/4] Verificando inputs..."

for N in "${NS[@]}"; do
  if [ "$MODO" = "recursivo" ]; then
    INPUT_FILE="inputs/linear_n${N}.txt"

    if [ ! -f "$INPUT_FILE" ]; then
      python3 scripts/generate_inputs/script_linear_graph.py "$N" > "$INPUT_FILE"
      echo "  [GERADO]     $INPUT_FILE"
    else
      echo "  [EXISTENTE]  $INPUT_FILE (reutilizando)"
    fi
  else
    M=$(( N * FATOR_M ))
    K=$(( N / DIVISOR_K ))
    if [ "$K" -lt 1 ]; then K=1; fi

    INPUT_FILE="inputs/grafo_n${N}_m${M}_k${K}.txt"

    if [ ! -f "$INPUT_FILE" ]; then
      python3 scripts/script_controlled_graph.py "$N" "$M" "$K" > "$INPUT_FILE"
      echo "  [GERADO]     $INPUT_FILE"
    else
      echo "  [EXISTENTE]  $INPUT_FILE (reutilizando)"
    fi
  fi

  INPUT_FILES+=("/app/inputs/$(basename $INPUT_FILE)")
done

# -------------------------------------------------------------
# ETAPA 2 — Build da imagem (uma vez só para os dois)
# -------------------------------------------------------------

echo ""
echo "[2/4] Buildando imagem..."
docker compose build
echo "  build concluido."

# -------------------------------------------------------------
# ETAPA 3 — Roda os dois algoritmos sequencialmente
# Cada um roda sozinho com todos os recursos disponíveis,
# garantindo medições sem interferência entre si
# -------------------------------------------------------------

echo ""
echo "[3/4] Executando benchmarks..."

TIMESTAMP=$(date +%Y%m%d_%H%M%S)

for ALGO in "$ALGO1" "$ALGO2"; do
  echo ""
  echo "--- $ALGO ---"

  ALGO_SAFE=$(echo "$ALGO" | tr '-' '_')

  docker compose run --rm \
    "$ALGO" \
    "$ALGO" "${INPUT_FILES[@]}" \
    | tee "resultados/log_${ALGO_SAFE}_${TIMESTAMP}.txt"

  CSV_ORIGINAL="resultados/resultado_${ALGO}.csv"

  if [ "$MODO" = "recursivo" ]; then
    CSV_PARCIAL="resultados/resultado_${ALGO_SAFE}_linear_${TIMESTAMP}.csv"
  else
    CSV_PARCIAL="resultados/resultado_${ALGO_SAFE}_${DENSIDADE}_${NIVEL_SCC}_${TIMESTAMP}.csv"
  fi

  if [ -f "$CSV_ORIGINAL" ]; then
    mv "$CSV_ORIGINAL" "$CSV_PARCIAL"
  else
    echo "Aviso: CSV nao encontrado para $ALGO"
    exit 1
  fi
done

# -------------------------------------------------------------
# ETAPA 4 — Merge automático dos dois CSVs
# -------------------------------------------------------------

echo ""
echo "[4/4] Gerando CSV comparativo..."

ALGO1_SAFE=$(echo "$ALGO1" | tr '-' '_')
ALGO2_SAFE=$(echo "$ALGO2" | tr '-' '_')

if [ "$MODO" = "recursivo" ]; then
  CSV1="resultados/resultado_${ALGO1_SAFE}_linear_${TIMESTAMP}.csv"
  CSV2="resultados/resultado_${ALGO2_SAFE}_linear_${TIMESTAMP}.csv"
  CSV_FINAL="resultados/comparacao_${ALGO1_SAFE}_vs_${ALGO2_SAFE}_linear_${TIMESTAMP}.csv"
else
  CSV1="resultados/resultado_${ALGO1_SAFE}_${DENSIDADE}_${NIVEL_SCC}_${TIMESTAMP}.csv"
  CSV2="resultados/resultado_${ALGO2_SAFE}_${DENSIDADE}_${NIVEL_SCC}_${TIMESTAMP}.csv"
  CSV_FINAL="resultados/comparacao_${ALGO1_SAFE}_vs_${ALGO2_SAFE}_${DENSIDADE}_${NIVEL_SCC}_${TIMESTAMP}.csv"
fi

python3 - "$ALGO1_SAFE" "$CSV1" "$ALGO2_SAFE" "$CSV2" "$CSV_FINAL" << 'PYEOF'
import sys, csv

nome1, arq1, nome2, arq2, saida = sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5]

def ler(caminho):
    dados = {}
    with open(caminho) as f:
        for row in csv.DictReader(f):
            n = int(row['n'])
            dados[n] = row
    return dados

d1 = ler(arq1)
d2 = ler(arq2)

todos_n = sorted(set(d1) | set(d2))

with open(saida, 'w', newline='') as f:
    w = csv.writer(f)
    w.writerow(['n', 'arestas', 'k_sccs', f'{nome1}_ms', f'{nome2}_ms'])
    for n in todos_n:
        meta = d1.get(n, d2.get(n, {}))
        w.writerow([
            n,
            meta.get('arestas', ''),
            meta.get('k_sccs', ''),
            d1.get(n, {}).get('mediana_ms', ''),
            d2.get(n, {}).get('mediana_ms', '')
        ])

print(f"CSV comparativo salvo em: {saida}")
PYEOF

# -------------------------------------------------------------
# FINALIZAÇÃO
# -------------------------------------------------------------

echo ""
echo "============================================="
echo " Pipeline concluido!"
echo " CSV comparativo: $CSV_FINAL"
echo "============================================="
echo ""
cat "$CSV_FINAL"