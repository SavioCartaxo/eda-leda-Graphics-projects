#!/bin/bash

# =============================================================
# BENCHMARK DE MEMÓRIA — Tarjan vs Kosaraju
# =============================================================
# Testa N=1000 e N=1000000 nos grafos linear e cycle
# Um container por execução garante heap limpo em cada medição
# =============================================================

set -e

# -------------------------------------------------------------
# CONFIGURAÇÕES FIXAS
# -------------------------------------------------------------

NS=(1000 1000000)
ALGORITMOS=("tarjan" "kosaraju")
TIPOS=("linear" "cycle")

# -------------------------------------------------------------
# PREPARAÇÃO
# -------------------------------------------------------------

mkdir -p inputs resultados
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
CSV_FINAL="resultados/memoria_tarjan_vs_kosaraju_${TIMESTAMP}.csv"

echo "tipo,n,algoritmo,memoria_mb" > "$CSV_FINAL"

echo "============================================="
echo " Benchmark de Memoria: Tarjan vs Kosaraju"
echo " N: ${NS[*]}"
echo " Tipos: linear e cycle"
echo "============================================="

# -------------------------------------------------------------
# BUILD — uma vez só
# -------------------------------------------------------------

echo ""
echo "[BUILD] Buildando imagem..."
docker compose -f docker/docker-compose.memoria.yml build
echo "  build concluido."

# -------------------------------------------------------------
# LOOP — para cada tipo de grafo, N e algoritmo
# Um container por execução = heap limpo = medição precisa
# -------------------------------------------------------------

echo ""
echo "[MEDICOES]"

for TIPO in "${TIPOS[@]}"; do
  for N in "${NS[@]}"; do

    INPUT_FILE="inputs/${TIPO}_n${N}.txt"

    # Gera o input se não existir
    if [ ! -f "$INPUT_FILE" ]; then
      if [ "$TIPO" = "linear" ]; then
        python3 scripts/generate_inputs/script_linear_graph.py "$N" > "$INPUT_FILE"
      else
        python3 scripts/generate_inputs/script_cycle_graph.py "$N" "$N" > "$INPUT_FILE"
      fi
      echo "  [GERADO]    $INPUT_FILE"
    else
      echo "  [EXISTENTE] $INPUT_FILE"
    fi

    INPUT_CONTAINER="/app/inputs/$(basename $INPUT_FILE)"

    for ALGO in "${ALGORITMOS[@]}"; do
      MB=$(docker compose -f docker/docker-compose.memoria.yml run --rm \
        "memoria-${ALGO}" \
        "$ALGO" "$INPUT_CONTAINER" \
        | grep "^MEMORIA_MB:" | awk -F: '{print $2}')

      echo "$TIPO,$N,$ALGO,$MB" >> "$CSV_FINAL"
      echo "  [OK] $TIPO | n=$N | $ALGO → ${MB} MB"
    done

  done
done

# -------------------------------------------------------------
# FINALIZAÇÃO
# -------------------------------------------------------------

echo ""
echo "============================================="
echo " Benchmark de memoria concluido!"
echo " CSV: $CSV_FINAL"
echo "============================================="
echo ""
cat "$CSV_FINAL"