Algoritmo de Tarjan — Componentes Fortemente Conectados (SCC)
Visão Geral

Dado um grafo direcionado, queremos identificar quais conjuntos de vértices estão conectados de forma que todos conseguem alcançar todos os outros. O algoritmo de Tarjan resolve esse problema encontrando todas as Componentes Fortemente Conectadas (SCCs) utilizando apenas uma busca em profundidade (DFS), com complexidade linear O(V + E). A ideia central é detectar, durante a própria DFS, quando um grupo de vértices forma um ciclo fechado, sem precisar realizar múltiplas passagens pelo grafo.

Ideia do Algoritmo

Durante a DFS, ao explorar um vértice u, surge a pergunta fundamental: existe algum caminho que permita voltar para um vértice visitado anteriormente? Se a resposta for sim, então estamos dentro de um ciclo e também dentro de um componente fortemente conectado. Para medir isso, o algoritmo associa dois valores a cada vértice: id[u], que representa a ordem de visita na DFS, e low[u], chamado de low-link value(LLK), que representa o menor id alcançável a partir de u, incluindo ele próprio. Em termos intuitivos, low[u] responde à pergunta: qual é o vértice mais antigo que ainda consigo alcançar partindo de u?

Atualização do Low-Link

Ao explorar uma aresta u → v, existem dois casos. Se v ainda não foi visitado, executamos DFS em v e depois propagamos a informação de retorno fazendo low[u] = min(low[u], low[v]), permitindo que ciclos encontrados mais profundamente influenciem vértices ancestrais. Se v já foi visitado, precisamos verificar se ele ainda pertence à exploração atual; caso pertença, encontramos um caminho de retorno dentro da mesma componente e atualizamos low[u] = min(low[u], id[v]). Essa distinção é essencial para evitar misturar componentes diferentes.

Uso da Pilha

O Tarjan utiliza uma pilha para manter um invariante importante: apenas vértices cujo componente ainda não foi finalizada podem influenciar cálculos de low-link. Quando um vértice é visitado, ele é colocado na pilha e permanece lá enquanto sua SCC ainda está sendo construída. Quando uma componente é descoberta, todos os seus vértices são removidos da pilha. Assim, apenas vértices presentes na pilha podem atualizar valores low, impedindo que SCCs já concluídas interfiram nas próximas.

Detecção de uma SCC

Após explorar todos os vizinhos de um vértice u, verificamos a condição id[u] == low[u]. Se ela for verdadeira, significa que não existe caminho retornando para um vértice mais antigo na DFS, logo u é o início de uma componente fortemente conectada. Nesse momento removemos vértices da pilha até remover u; todos os vértices removidos formam exatamente uma SCC. Uma SCC é definida como um conjunto de vértices onde qualquer vértice alcança qualquer outro e existe caminho de ida e volta entre todos eles, sendo que cada vértice do grafo pertence exatamente a uma única SCC.

Fluxo Geral do Algoritmo

Inicializamos todos os vértices como não visitados e executamos DFS a partir de cada vértice ainda não explorado. Ao visitar um nó, atribuímos id e low, inserimos o vértice na pilha e exploramos seus vizinhos atualizando os valores de low-link conforme os casos descritos. Sempre que id == low, removemos vértices da pilha formando uma nova componente fortemente conectada. Cada vértice e cada aresta são processados apenas uma vez, garantindo complexidade O(V + E) em tempo e O(V) em memória.

Intuição Final

Podemos interpretar o algoritmo da seguinte forma: id indica quando entramos em um vértice, low indica quão longe conseguimos voltar na exploração, e a pilha representa os vértices ainda ativos na DFS. Quando não é mais possível voltar para vértices anteriores, uma região do grafo se fecha e uma SCC é identificada. Dessa maneira, o algoritmo de Tarjan encontra todas as componentes fortemente conectadas em uma única passagem pelo grafo, de forma eficiente e elegante.


Vamos tomar como exemplo o seguinte grafo:

![Grafo](../../../../README_IMAGES/GraphTarjan.png)

Com as arestas: 3→1, 1→2, 2→1.

---

## Estruturas auxiliares

Antes de iniciar a DFS, criamos as seguintes estruturas:

**ids**: array de tamanho igual ao número de vértices, iniciado com -1. O valor de ids[v] não representa o valor do node, mas sim o momento em que ele foi visitado durante a DFS. Enquanto ids[v] == -1, o node ainda não foi visitado.

**low**: array de mesmo tamanho, iniciado com 0. Guarda o menor ids acessível a partir de cada node, considerando os nós ainda na stack. É através do low que conseguimos identificar a raiz de um SCC.

**id**: variável que age como um "relógio", incrementada a cada novo node visitado, determinando a ordem de visita.

**stack**: array que funciona como pilha, armazenando os nodes visitados ainda não atribuídos a algum SCC.

**onStack**: array booleano que nos diz, em O(1), se um node está atualmente na stack. Essencial para a atualização correta do low.

Estado inicial:

```
ids     = [-1, -1, -1]
low     = [ 0,  0,  0]
stack   = [ 0,  0,  0]
onStack = [false, false, false]
id = 0
```

---

## Execução

Iteramos pelos nodes. O node 1 tem ids[1] == -1, chamamos DFS(1), atribuímos ids[1] = low[1] = 0, incrementamos id e adicionamos à stack:

```
ids     = [0, -1, -1]  |  low = [0, 0, 0]  |  id = 1
stack   = [1,  0,  0]  |  onStack = [true, false, false]
```

Node 1 tem vizinho 2, não visitado, chamamos DFS(2), atribuímos ids[2] = low[2] = 1, incrementamos id e adicionamos à stack:

```
ids     = [0, 1, -1]  |  low = [0, 1, 0]  |  id = 2
stack   = [1, 2,  0]  |  onStack = [true, true, false]
```

Node 2 tem vizinho 1, já visitado e na stack, então atualizamos low[2] = min(low[2], ids[1]) = min(1, 0) = 0:

```
ids     = [0, 1, -1]  |  low = [0, 0, 0]  |  id = 2
stack   = [1, 2,  0]  |  onStack = [true, true, false]
```

DFS(2) retorna. ids[2]=1 != low[2]=0, node 2 não é raiz de SCC. Voltando ao node 1, propagamos low[1] = min(low[1], low[2]) = min(0, 0) = 0. Node 1 não tem mais vizinhos. ids[1] == low[1]? 0 == 0, sim! Desempilhamos até o node 1, formando o primeiro SCC: **{2, 1}**:

```
ids     = [0, 1, -1]  |  low = [0, 0, 0]  |  id = 2
stack   = [0, 0,  0]  |  onStack = [false, false, false]
```

Continuamos a iteração. O node 3 tem ids[3] == -1, chamamos DFS(3), atribuímos ids[3] = low[3] = 2, incrementamos id e adicionamos à stack:

```
ids     = [0, 1, 2]  |  low = [0, 0, 2]  |  id = 3
stack   = [3, 0, 0]  |  onStack = [false, false, true]
```

Node 3 tem vizinho 1, já visitado mas **fora da stack** (onStack[1] == false), portanto não atualizamos o low. DFS(3) retorna. ids[3] == low[3]? 2 == 2, sim! Desempilhamos formando o segundo SCC: **{3}**:

```
ids     = [0, 1, 2]  |  low = [0, 0, 2]  |  id = 3
stack   = [0, 0, 0]  |  onStack = [false, false, false]
```

Todos os nodes foram visitados. SCCs encontrados: **{1, 2}** e **{3}**.

O node 3 forma um SCC sozinho pois, apesar de alcançar o node 1, não há caminho de volta até ele, ou seja, não há ciclo envolvendo o node 3.