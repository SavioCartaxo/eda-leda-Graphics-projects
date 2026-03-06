# Análise de Implementação Recursiva do Algoritmo de Tarjan — Investigação de Performance
Originalmente o algoritmo Tarjan e o algoritmo Kosaraju seriam implementados de forma recursiva, essa ideia foi descartada devido a alguns motivos, um deles é que a profundidade da pilha de recursão poderia afetar significativamente o desempenho e o uso de memória, especialmente em grafos que induzem cadeias longas de chamadas recursivas. Outro motivo foi um comportamento inesperado observado ao implementar o Tarjan recursivo em duas variações diferentes, discutido nas seções seguintes.

## 1. Versões implementadas

### 1.1 Versão com HashMap (original)

A versão original utilizava `Map<Integer, Integer> nodeIndex` para mapear valores arbitrários dos nós para índices `0..n-1`, e `int[] indexToValue` para o caminho inverso. Dentro da DFS, cada visita a um vizinho realizava um lookup no HashMap:

```java
Integer vBoxed = nodeIndex.get(vNode.getValue());
if (vBoxed == null) continue;
int v = vBoxed;
```

---

### 1.2 Versão com acesso direto (Node normalizado)

A versão otimizada adicionou `index` e `originalValue` ao `Node`. `getValue()` passou a retornar o índice normalizado `0..n-1` diretamente, eliminando o HashMap da DFS. O `originalValue` é recuperado via `getOriginalValue()` apenas na saída.

```java
int v = vNode.getValue(); // índice direto, sem mapa
```

---

## 2. Resultado inesperado

| Versão | DFS | Estruturas | Tempo (N=10⁵) |
|---|---|---|---|
| Tarjan com HashMap | recursiva | HashMap + arrays | ~90ms |
| Tarjan com acesso direto | recursiva | arrays primitivos | ~350ms |
A versão com acesso direto — estruturalmente mais simples e sem overhead de HashMap — se mostrou **~4x mais lenta** na DFS que a versão com HashMap. Esse resultado é contraintuitivo e levou a uma investigação detalhada.

---

## 3. Hipóteses investigadas

### Hipótese 1: Grafo ou número de chamadas diferente entre as versões
**Teste:** adicionado contador de chamadas à DFS e log de `n` e número de arestas.

**Resultado:** ambas as versões realizaram exatamente **100.000 chamadas**, com mesmo `n` e mesmo número de arestas.

**Conclusão:** Hipótese descartada.

---

### Hipótese 2: JIT warmup — HashMap atrasava o início da DFS dando tempo ao JIT compilar
**Raciocínio:** o setup com HashMap levava ~12ms, durante os quais o JIT detectaria `dfs` como método quente e o compilaria para código nativo antes de ele começar. Sem HashMap, o setup termina em ~1ms e a DFS começa ainda interpretada.

**Teste:** rodado com -XX:CompileThreshold=1 para reduzir drasticamente o número de chamadas necessárias antes da compilação JIT.

**Resultado:** tempo aumentou por cerca de ~200ms.

**Conclusão:** Hipótese descartada — o JIT forçado piorou ainda mais o resultado.

---

### Hipótese 3: Recursão profunda — tipo de grafo
**Descoberta:** o script `script_cycle_graph.py` gerava um **ciclo único de N nós**:
```
1→2→3→4→...→100000→1
```
Isso é o pior caso para recursão — a DFS desce 100.000 níveis em linha reta antes de voltar, criando cerca de 100.000 stack frames simultaneamente. Isso aumenta o consumo de memória da pilha e piora a localidade de cache, já que cada frame acessa múltiplas estruturas auxiliares.

**Relação com a diferença:** o Kosaraju recursivo era ~4x mais rápido mesmo fazendo duas DFS no mesmo grafo. Isso porque cada frame da DFS do Tarjan é mais pesado — acessa `ids`, `low`, `onStack`, `stack` e `originalValues` simultaneamente, potencialmente causando mais cache misses devido ao grande número de frames ativos e ao acesso simultâneo a múltiplas estruturas auxiliares.

**Conclusão:** Hipótese confirmada como causa do Tarjan ser lento em geral nesse grafo. Mas não explica a diferença *entre as duas versões do Tarjan*.

---

### Hipótese 4 (em aberto): Comportamento interno da JVM
A diferença de ~90ms vs ~350ms na DFS (para N = 10⁵), com mesmo número de chamadas, mesmo grafo e mesma lógica, **não foi explicada conclusivamente**. O comportamento aponta para algo interno à JVM — possivelmente relacionado a como o JIT lida com a presença ou ausência do HashMap no contexto de execução, ou diferenças sutis no layout de memória dos objetos entre as duas versões.

**Status:** ⚠️ não resolvida — requereria um profiler real (ex: JFR, async-profiler) para diagnóstico preciso.

---

## 4. Solução adotada: DFS iterativa

A DFS recursiva foi substituída por uma DFS iterativa usando `ArrayDeque` como pilha de frames explícita. Cada frame guarda `[nó atual, índice do próximo vizinho]`:

```java
Deque<int[]> callStack = new ArrayDeque<>();
callStack.push(new int[]{start, 0});

while (!callStack.isEmpty()) {
    int[] frame = callStack.peek();
    int u = frame[0];

    if (frame[1] < adj[u].size()) {
        int v = adj[u].get(frame[1]++).getValue();
        // processa vizinho...
    } else {
        callStack.pop();
        // propaga low e verifica raiz de SCC...
    }
}
```

Com a DFS iterativa, tanto o Tarjan quanto o Kosaraju se tornaram mais rápidos, mas agora sem um frame stack enorme o Tarjan passa a demonstrar sua vantagem em passar apenas uma vez pelo gráfico com a dfs, tornando-se assim mais rápido que o Kosaraju.

---

## 5. Conclusão

- Para grafos com ciclos longos (pior caso de recursão), a DFS iterativa é obrigatória para o Tarjan ser competitivo.
- A diferença inesperada entre as duas versões recursivas permanece sem explicação definitiva — o comportamento sugere algo interno ao JIT da JVM que não foi possível diagnosticar sem ferramentas de profiling adequadas.
- Com ambos os algoritmos iterativos e estruturas equivalentes, o Tarjan é consistentemente mais rápido por fazer apenas uma DFS, sem construir grafo transposto.