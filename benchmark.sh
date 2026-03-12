#!/bin/bash

# =============================================================
# PIPELINE DE BENCHMARK — SCC (comparação dois a dois)
# =============================================================
# Uso:
#   Dois iterativos — grafo controlado:
#     ./benchmark.sh tarjan kosaraju <densidade> <nivel_scc>
#
#   Dois iterativos — grafo linear ou cycle:
#     ./benchmark.sh tarjan kosaraju linear
#     ./benchmark.sh tarjan kosaraju cycle
#
#   Tarjan iterativo vs recursivo — apenas linear:
#     ./benchmark.sh tarjan tarjan-recursivo
#     ./benchmark.sh tarjan tarjan-recursivo-hash
#
#   Dois recursivos — linear automático:
#     ./benchmark.sh tarjan-recursivo tarjan-recursivo-hash
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
  echo "  ./benchmark.sh tarjan kosaraju linear"
  echo "  ./benchmark.sh tarjan kosaraju cycle"
  echo "  ./benchmark.sh tarjan tarjan-recursivo"
  echo "  ./benchmark.sh tarjan tarjan-recursivo-hash"
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

# Detecta modo: recursivo, linear, cycle ou controlado
# Mistura de recursivo com iterativo é permitida apenas com linear
if eh_recursivo "$ALGO1" && eh_recursivo "$ALGO2"; then
  # Dois recursivos — sempre linear
  MODO="recursivo"
elif eh_recursivo "$ALGO1" || eh_recursivo "$ALGO2"; then
  # Mistura recursivo + iterativo — só Tarjan pode ser comparado com suas versões recursivas
  ITERATIVO=$(eh_recursivo "$ALGO1" && echo "$ALGO2" || echo "$ALGO1")
  RECURSIVO=$(eh_recursivo "$ALGO1" && echo "$ALGO1" || echo "$ALGO2")

  if [[ "$ITERATIVO" != "tarjan" ]]; then
    echo "Erro: apenas Tarjan pode ser comparado com suas versões recursivas."
    echo "Use: tarjan tarjan-recursivo | tarjan tarjan-recursivo-hash"
    exit 1
  fi

  if [[ "$RECURSIVO" != "tarjan-recursivo" && "$RECURSIVO" != "tarjan-recursivo-hash" ]]; then
    echo "Erro: versao recursiva invalida '$RECURSIVO'."
    echo "Use: tarjan-recursivo | tarjan-recursivo-hash"
    exit 1
  fi

  # Linear é implícito para comparação iterativo vs recursivo
  MODO="linear"
  echo "Modo linear aplicado automaticamente para comparacao iterativo vs recursivo."

else
  # Dois iterativos — aceita linear, cycle ou controlado
  if [ "$#" -eq 3 ] && [[ "$3" == "linear" || "$3" == "cycle" ]]; then
    MODO="$3"
  elif [ "$#" -eq 4 ]; then
    MODO="controlado"
    DENSIDADE=$3
    NIVEL_SCC=$4

    if [[ "$DENSIDADE" != "baixa" && "$DENSIDADE" != "media" && "$DENSIDADE" != "alta" ]]; then
      echo "Erro: densidade invalida '$DENSIDADE'. Use: baixa | media | alta"; exit 1
    fi
    if [[ "$NIVEL_SCC" != "muitos" && "$NIVEL_SCC" != "medios" && "$NIVEL_SCC" != "poucos" ]]; then
      echo "Erro: nivel_scc invalido '$NIVEL_SCC'. Use: muitos | medios | poucos"; exit 1
    fi
  else
    echo "Uso: ./benchmark.sh $ALGO1 $ALGO2 <densidade> <nivel_scc>"
    echo "     ./benchmark.sh $ALGO1 $ALGO2 linear"
    echo "     ./benchmark.sh $ALGO1 $ALGO2 cycle"
    exit 1
  fi
fi

# -------------------------------------------------------------
# CONFIGURAÇÕES
# -------------------------------------------------------------

NS=(100 20000 40000 60000 80000 100000 120000 140000 160000 180000 200000 220000 240000 260000 280000 300000 320000 340000 360000 380000 400000 420000 440000 460000 480000 500000 520000 540000 560000 580000 600000 620000 640000 660000 680000 700000 720000 740000 760000 780000 800000 820000 840000 860000 880000 900000 920000 940000 960000 980000 1000000)

if [ "$MODO" = "controlado" ]; then
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

case $MODO in
  recursivo)  echo "============================================="
              echo " Benchmark: $ALGO1 vs $ALGO2 (linear)"
              echo "=============================================" ;;
  linear)     echo "============================================="
              echo " Benchmark: $ALGO1 vs $ALGO2 (linear)"
              echo "=============================================" ;;
  cycle)      echo "============================================="
              echo " Benchmark: $ALGO1 vs $ALGO2 (cycle)"
              echo "=============================================" ;;
  controlado) echo "============================================="
              echo " Benchmark: $ALGO1 vs $ALGO2 | densidade: $DENSIDADE | sccs: $NIVEL_SCC"
              echo "=============================================" ;;
esac

# -------------------------------------------------------------
# ETAPA 1 — Gera/reutiliza inputs
# -------------------------------------------------------------

echo ""
echo "[1/4] Verificando inputs..."

for N in "${NS[@]}"; do
  case $MODO in
    recursivo|linear)
      INPUT_FILE="inputs/linear_n${N}.txt"
      if [ ! -f "$INPUT_FILE" ]; then
        python3 scripts/generate_inputs/script_linear_graph.py "$N" > "$INPUT_FILE"
        echo "  [GERADO]     $INPUT_FILE"
      else
        echo "  [EXISTENTE]  $INPUT_FILE"
      fi
      ;;

    cycle)
      INPUT_FILE="inputs/cycle_n${N}.txt"
      if [ ! -f "$INPUT_FILE" ]; then
        python3 scripts/generate_inputs/script_cycle_graph.py "$N" > "$INPUT_FILE"
        echo "  [GERADO]     $INPUT_FILE"
      else
        echo "  [EXISTENTE]  $INPUT_FILE"
      fi
      ;;

    controlado)
      M=$(( N * FATOR_M ))
      K=$(( N / DIVISOR_K ))
      if [ "$K" -lt 1 ]; then K=1; fi
      INPUT_FILE="inputs/grafo_n${N}_m${M}_k${K}.txt"
      if [ ! -f "$INPUT_FILE" ]; then
        python3 scripts/script_controlled_graph.py "$N" "$M" "$K" > "$INPUT_FILE"
        echo "  [GERADO]     $INPUT_FILE"
      else
        echo "  [EXISTENTE]  $INPUT_FILE"
      fi
      ;;
  esac

  INPUT_FILES+=("/app/inputs/$(basename $INPUT_FILE)")
done

# -------------------------------------------------------------
# ETAPA 2 — Build
# -------------------------------------------------------------

echo ""
echo "[2/4] Buildando imagem..."
docker compose build
echo "  build concluido."

# -------------------------------------------------------------
# ETAPA 3 — Roda os dois algoritmos sequencialmente
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

  case $MODO in
    recursivo|linear) CSV_PARCIAL="resultados/resultado_${ALGO_SAFE}_linear_${TIMESTAMP}.csv" ;;
    cycle)            CSV_PARCIAL="resultados/resultado_${ALGO_SAFE}_cycle_${TIMESTAMP}.csv" ;;
    controlado)       CSV_PARCIAL="resultados/resultado_${ALGO_SAFE}_${DENSIDADE}_${NIVEL_SCC}_${TIMESTAMP}.csv" ;;
  esac

  if [ -f "$CSV_ORIGINAL" ]; then
    mv "$CSV_ORIGINAL" "$CSV_PARCIAL"
  else
    echo "Aviso: CSV nao encontrado para $ALGO"
    exit 1
  fi
done

# -------------------------------------------------------------
# ETAPA 4 — Merge automático
# -------------------------------------------------------------

echo ""
echo "[4/4] Gerando CSV comparativo..."

ALGO1_SAFE=$(echo "$ALGO1" | tr '-' '_')
ALGO2_SAFE=$(echo "$ALGO2" | tr '-' '_')

case $MODO in
  recursivo|linear)
    CSV1="resultados/resultado_${ALGO1_SAFE}_linear_${TIMESTAMP}.csv"
    CSV2="resultados/resultado_${ALGO2_SAFE}_linear_${TIMESTAMP}.csv"
    CSV_FINAL="resultados/comparacao_${ALGO1_SAFE}_vs_${ALGO2_SAFE}_linear_${TIMESTAMP}.csv"
    ;;
  cycle)
    CSV1="resultados/resultado_${ALGO1_SAFE}_cycle_${TIMESTAMP}.csv"
    CSV2="resultados/resultado_${ALGO2_SAFE}_cycle_${TIMESTAMP}.csv"
    CSV_FINAL="resultados/comparacao_${ALGO1_SAFE}_vs_${ALGO2_SAFE}_cycle_${TIMESTAMP}.csv"
    ;;
  controlado)
    CSV1="resultados/resultado_${ALGO1_SAFE}_${DENSIDADE}_${NIVEL_SCC}_${TIMESTAMP}.csv"
    CSV2="resultados/resultado_${ALGO2_SAFE}_${DENSIDADE}_${NIVEL_SCC}_${TIMESTAMP}.csv"
    CSV_FINAL="resultados/comparacao_${ALGO1_SAFE}_vs_${ALGO2_SAFE}_${DENSIDADE}_${NIVEL_SCC}_${TIMESTAMP}.csv"
    ;;
esac

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

echo ""
echo "============================================="
echo " Pipeline concluido!"
echo " CSV comparativo: $CSV_FINAL"
echo "============================================="
echo ""
cat "$CSV_FINAL"