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

Inicialente criamos a lista de ids com o mesmo tamanho do grafo e iniciamos todas as posições como -1, o que representa que o vértice ainda não foi visitado, assim para cada índice representando um node, o array id = [-1, -1, -1, -1, -1]