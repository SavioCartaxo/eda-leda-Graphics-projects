# Componentes Fortemente Conexas (SCC) em Grafos – Tarjan & Kosaraju

## Sumário

- [Estrutura de diretórios](#estrutura-de-diretórios)

- [O que é um grafo?](#o-que-é-um-grafo)

- [DFS](#depth-first-search)

- [Componentes Fortemente Conectados](#componentes-fortemente-conectados)

- [Kosaraju](#algoritmo-de-kosaraju)

- [Tarjan](#algoritmo-de-tarjan)

- [Scripts](#scripts)

- [Experimento](#experimento)

- [Análise do Tarjan Recursivo](#análise-de-implementação-recursiva-do-algoritmo-de-tarjan)

- [Ambiente isolado com Docker](#uso-do-docker-no-projeto-de-benchmark-scc)

- [Referências](#referências)


### Versionamento de código

O versionamento do projeto foi realizado utilizando [_Git_](https://git-scm.com/) e [_Github_](https://docs.github.com/pt), permitindo o controle das alterações ao longo do desenvolvimento. A criação de *branches* possibilitou o desenvolvimento isolado de funcionalidades, enquanto o uso de *pull requests* garantiu a revisão e a integração organizada das modificações ao repositório principal, promovendo um fluxo colaborativo entre os integrantes do grupo.

### Geração de inputs (carga de dados)

A geração das cargas de dados, estruturadas como diferentes *workloads*, foi realizada por meio da linguagem **Python**, escolhida por sua simplicidade, legibilidade e alto nível de abstração. Foram desenvolvidos scripts específicos para a criação de cenários distintos de teste, permitindo a simulação de entradas com comportamentos variados.

### Plotagem (geração) de gráficos

A geração dos gráficos foi realizada a partir dos dados experimentais obtidos durante a execução dos testes. 
Um script de carga foi utilizado para executar os experimentos e produzir a saída contendo as métricas de desempenho dos algoritmos avaliados. 
Com base nesses resultados, foram construídos gráficos para análise comparativa.

Para a visualização dos dados, utilizou-se a biblioteca *Matplotlib* na linguagem **Python**, permitindo representar graficamente o comportamento e o desempenho dos algoritmos.

## Estrutura de diretórios

---

### Diretório src

```
src
├───main
│   └───java
│       ├───algoritmos
│       └───util
└───test
    └───java
```

O diretório `src` contém as implementações, em **Java**, dos algoritmos **Kosaraju** e **Tarjan**. Também está presente o diretório `test`, responsável pelas classes de teste do projeto.

### Diretório scripts

```
scripts
├───generate_inputs
└───plot_graphs
```

O diretório `scripts` reúne os scripts desenvolvidos em **Python** utilizados no suporte aos experimentos realizados no projeto. Nele estão incluídos os scripts responsáveis pela **geração automática das entradas (workloads)** utilizadas nos testes e também os scripts encarregados da **geração de gráficos** a partir dos resultados obtidos, utilizando a biblioteca **Matplotlib**.

# Introdução teórica


### O que é um grafo?

Considere a figura a seguir, a qual representa um mapa de vias de trânsito:

![Roads Representation](README_IMAGES/roads.jpg)
Figura 1.0

Essas vias podem ser representadas diagramaticamente por pontos ligados por linhas: os pontos P, Q, R e S são chamados de vértices, as linhas são chamadas de arestas e todo o diagrama é chamado de grafo. Note que a interseção entre as linhas PR e QS não é um vértice uma vez que não corresponde a um cruzamento, isto é, não é um ponto onde as ruas se encontram. O conceito de grau de um vértice qualquer é a quantidade de arestas que terminam nesse vértice, é o mesmo que dizer qual o número de ruas em um dado cruzamento na figura acima. Por exemplo, o grau do vértice P é 3.

 
![Graph Diagram](README_IMAGES/graph.png)
Figura 1.1

O grafo na figura 1.1 pode, além de um mapa de estradas, representar diversas coisas: uma rede elétrica, moléculas ou redes neurais; essencialmente, portanto, um grafo é uma representação de um conjunto de pontos e das ligações entre eles. Na computação são extremamente úteis para lidar com problemas relacionados à rede de computadores, com conexões via Wi-Fi ou cabo entre roteadores, redes sociais e a conexão de usuários que seguem um ao outro, ou até a internet com páginas da web conectadas através de links. 

Agora imagine que as vias possuem sentido único, ou seja, há apenas uma direção a se seguir de um cruzamento a outro; isso nada mais é que um novo grafo de um tipo especial chamado grafo direcionado: há apenas um sentido entre dois vértices.


![Directed Graph Diagram](README_IMAGES/directed_graph.jpg)
Figura 1.2

## Depth First Search
 
Se é preciso encontrar uma informação específica, há de se ter uma forma de procurá-la sistematicamente no grafo, o que muitas vezes envolve olhar todos os seus vértices até que isso seja possível. Nesse sentido, há dois algoritmos bem conhecidos, [depth first search](src/main/java/algoritmos/DepthFirstSearch.java) e **breadth first search**, ambos percorrem todos os vértices, mas em ordens diferentes. Como neste trabalho tratamos de algoritmos baseados no **depth first search** (também chamado de **DFS**), apenas trataremos dele.

No *depth first search*, procuramos o mais fundo possível no grafo. Esse algoritmo explora um caminho e segue por ele até que não seja mais possível avançar, e então retorna para o vértice de início e explora outro caminho ainda inexplorado, similar a explorar um labirinto sempre por um único caminho de corredores até o fim e, após isso, retornar ao ponto de início para fazer o mesmo por outro caminho, caso esse exista.
 
![Directed Graph Diagram](README_IMAGES/directed_graph2.jpg)
Figura 2.0

Na figura 2.0, que se trata de um grafo direcionado, tomando o vértice A como o inicial, o caminho seguido pelo algoritmo seria A → B → D → C → E → F → G → H. Note que, após não encontrar nenhum vértice inexplorado no ponto C, o algoritmo tem que retornar até encontrar um caminho ainda não explorado, repetindo o mesmo processo para esse.

---

## Componentes Fortemente Conectados

Em um grafo dirigido G, diz-se que ele é fortemente conectado quando, para todo par de vértices u e v, existe um caminho de u até v e, ao mesmo tempo, um caminho de v até u. Em outras palavras, qualquer vértice pode ser alcançado a partir de qualquer outro.

No entanto, um grafo dirigido pode não ser fortemente conectado como um todo. Nesse caso, a forte conectividade pode ocorrer em apenas partes do grafo. Dizemos que dois vértices u e v são fortemente conectados entre si quando existe um caminho de u até v e outro de v até u, mesmo que u = v. Assim, mesmo que G não seja fortemente conectado, ele pode ser decomposto em subconjuntos de vértices nos quais, internamente, todo par u e v é mutuamente alcançável. Cada um desses subconjuntos induz um subgrafo chamado **Componente Fortemente Conectado (CFC)**. Essa ideia é melhor compreendida ao observar o exemplo:

![Exemplo de SCC](README_IMAGES/img_example_SCC.png)

---

# Algoritmo de Kosaraju

### Visão Geral

O algoritmo de Kosaraju, também conhecido como algoritmo Kosaraju-Sharir, é um algoritmo que encontra componentes fortemente conectados (SCC) de um grafo direcionado em tempo linear O(V + E) quando representado por lista de adjacência, onde V é o número de vértices (ou nós) e E o número de arestas do grafo. 

A implementação utilizada constrói explicitamente o grafo transposto em memória, o que usa espaço adicional O(V + E), mas simplifica a implementação e o entendimento em relação a abordagens que evitam essa construção explícita. A ideia é usar busca em profundidade duas vezes, fazendo uso da propriedade de que um grafo e seu transposto possuem exatamente os mesmos SCCs.

Vamos usar o grafo direcionado abaixo como exemplo para executar o algoritmo de Kosaraju.

![Grafo original](README_IMAGES/grafo_imagem1.png)

## Execução do Algortimo
### Primeira Etapa

Qual o nosso objetivo inicial? Primeiro, devemos criar uma pilha que representa a ordem de saída dos vértices através de uma busca em profundidade e guardar os vértices já visitados. A versão da DFS usada para a explicação do algoritmo será a recursiva, pois ela é mais intuitiva e simples de entender. A DFS faz chamadas recursivas para cada vizinho não visitado, e ao retornar de todas essas chamadas — ou seja, quando não há mais vizinhos a explorar — o vértice é adicionado à pilha.

Podemos começar a DFS por qualquer vértice, mas, por convenção, iremos utilizar o 1. Realizando a busca, visitamos o 1 e vemos a quem ele está ligado. Vemos que ele está ligado ao 2, então visitamos o 2, e vemos que ele está ligado ao 3; visitamos o 3. Como o 3 não tem mais vizinhos não visitados, ele é adicionado à pilha. A recursão retorna para o 2, que também não tem mais vizinhos e é adicionado. Por fim, o 1 é adicionado.

```
pilha     = [3, 2, 1]
visitados = {1, 2, 3}
```

Ainda existem vértices não visitados, iniciamos uma nova DFS a partir do vértice 4, escolhido arbitrariamente dentre os não visitados. Visitando o 4, vemos que ele tem o 3 e o 5 como vizinhos; o 3 já foi visitado, mas o 5 não. Visitamos o 5 e vemos que ele está ligado ao 6; visitamos o 6. O 6 está ligado ao 4, que já foi visitado, então adicionamos o 6 à pilha, e a recursão retorna para o 5, que é adicionado, e por fim o 4 é adicionado.

```
pilha     = [3, 2, 1, 6, 5, 4]
visitados = {1, 2, 3, 4, 5, 6}
```

Ainda temos nós não visitados: o 9. O 9 está ligado ao 4, que já foi visitado, então o 9 é adicionado à pilha. Finalmente, nossa pilha está completa, pois não temos mais nós para visitar.

```
pilha     = [3, 2, 1, 6, 5, 4, 9]
visitados = {1, 2, 3, 4, 5, 6, 9}
```

### Segunda Etapa

Agora, o próximo passo é inverter a direção de todas as arestas do grafo, a fim de encontrar o seu transposto. Para cada vértice u do grafo, percorremos seus vizinhos v e adicionamos u como vizinho de v no grafo transposto — ou seja, toda aresta u → v vira v → u. O processo é feito em O(V + E), visitando cada vértice e cada aresta exatamente uma vez.

Agora, o próximo passo é inverter a direção de todas as arestas do grafo, a fim de encontrar o seu transposto. Para cada vértice u do grafo, percorremos seus vizinhos v e adicionamos u como vizinho de v no grafo transposto — ou seja, toda aresta u → v vira v → u. O processo é feito em O(V + E), visitando cada vértice e cada aresta exatamente uma vez.

![Grafo transposto](README_IMAGES/grafo_imagem2.png)

Por que queremos o transposto do grafo? Sabemos que um SCC possui caminhos nos dois sentidos entre todos os seus vértices, ou seja, é, por definição, bidirecional. Ao invertermos as arestas, essa característica se preserva — o que era ciclo continua sendo ciclo. Porém, as arestas que conectavam SCCs distintos agora apontam na direção oposta, bloqueando a DFS de vazar de um SCC para outro. Isso nos permite, na segunda DFS, explorar exatamente um SCC por vez.

### Terceira Etapa

Feito isso, realizamos a segunda busca em profundidade, agora no grafo transposto. Retiramos os vértices do topo da pilha um a um — lembrando que o topo representa o vértice que terminou por último na primeira DFS, ou seja, o de maior alcance. Para cada vértice retirado que ainda não foi visitado, criamos um novo SCC e iniciamos uma DFS no grafo transposto para encontrar todos os seus vértices. Todos os vértices alcançados nessa DFS pertencem ao mesmo SCC. Vértices já visitados são ignorados, pois já foram atribuídos a um componente. Ao esvaziarmos a pilha, temos todos os SCCs do grafo identificados.

Dando início à execução desta parte final, aplicamos a busca em profundidade no elemento do topo da pilha, que é o vértice 9. Como ele não foi visitado, iniciamos a DFS para montar seu SCC. Percebemos que ele não tem vizinhos no grafo transposto; portanto, o 9 sozinho já é um SCC.

```
pilha      = [3, 2, 1, 6, 5, 4]
visitados2 = {9}
SCCs       = [[9]]
```

Temos pilha = [3, 2, 1, 6, 5, 4], o topo é o 4. Como ele não foi visitado, iniciamos a DFS para montar seu SCC. Visitamos o 4 e vemos que ele está conectado ao 6. Visitamos o 6, vemos que está conectado ao 5. Visitamos o 5, e ele está conectado ao 4, que já foi visitado, encerrando as chamadas recursivas. Os vértices 4, 5 e 6 formam o segundo SCC. 

```
pilha      = [3, 2, 1]
visitados2 = {9, 4, 5, 6}
SCCs       = [[9], [4, 5, 6]]
```

Temos pilha = [3, 2, 1]. O próximo topo é o 1, que não foi visitado, então iniciamos a DFS para montar seu SCC. O 1 está ligado ao 3, visitamos, que está ligado ao 2, visitamos. O 2 está ligado ao 1, que já foi visitado, encerrando a DFS. Os vértices 1, 2 e 3 formam o terceiro SCC. 

```
pilha      = []
visitados2 = {9, 4, 5, 6, 1, 2, 3}
SCCs       = [[9], [4, 5, 6], [1, 2, 3]]
```

Nossa pilha está vazia, portanto, encerrou a execução do algoritmo. Finalmente, descobrimos que o nosso grafo do exemplo possui três componentes fortemente conectados.


![SCCs](README_IMAGES/grafo_imagem3.png)


Temos os SCCs {9}, {4, 5, 6} e {1, 2, 3}.

O conceito é simples e o Kosaraju é eficiente; no entanto, não é mais eficiente que outros algoritmos que encontram SCCs, como o Tarjan, que, embora tenha uma complexidade de tempo assintoticamente igual à do Kosaraju, realiza apenas uma busca em profundidade, ao invés de duas, levando-o a ser mais rápido na prática.

---

# Algoritmo de Tarjan
### Visão Geral

Dado um grafo direcionado, queremos identificar quais conjuntos de vértices estão conectados de forma que todos conseguem alcançar todos os outros. O algoritmo de Tarjan resolve esse problema encontrando todas as Componentes Fortemente Conectadas (SCCs) utilizando apenas uma busca em profundidade (DFS), com complexidade linear O(V + E). A ideia central é detectar, durante a própria DFS, quando um grupo de vértices forma um ciclo fechado, sem precisar realizar múltiplas passagens pelo grafo.

### Ideia do Algoritmo

Durante a DFS, ao explorar um vértice u, surge a pergunta fundamental: existe algum caminho que permita alcançar novamente um vértice visitado anteriormente que ainda faça parte da exploração atual? Para medir isso, o algoritmo associa dois valores a cada vértice: id[u], que representa a ordem de visita na DFS, e low[u], chamado de low-link value (LLK), que representa o menor id alcançável a partir de u, incluindo ele próprio, considerando apenas vértices que ainda estão ativos na pilha da DFS. Em termos intuitivos, low[u] responde à pergunta: qual é o vértice mais antigo ainda ativo que consigo alcançar partindo de u?

### Atualização do Low-Link

Ao explorar uma aresta u → v, existem dois casos. Se v ainda não foi visitado, executamos DFS em v e depois propagamos a informação de retorno fazendo low[u] = min(low[u], low[v]), permitindo que ciclos encontrados mais profundamente influenciem vértices ancestrais. Se v já foi visitado, precisamos verificar se ele ainda pertence à exploração atual; caso pertença, encontramos um caminho de retorno dentro da mesma componente e atualizamos low[u] = min(low[u], id[v]). Essa distinção é essencial para evitar misturar componentes diferentes.

### Uso da Pilha

O Tarjan utiliza uma pilha para manter um invariante importante: apenas vértices cujo componente ainda não foi finalizada podem influenciar cálculos de low-link. Quando um vértice é visitado, ele é colocado na pilha e permanece lá enquanto sua SCC ainda está sendo construída. Quando uma componente é descoberta, todos os seus vértices são removidos da pilha. Assim, apenas vértices presentes na pilha podem atualizar valores low, impedindo que SCCs já concluídas interfiram nas próximas.

### Detecção de uma SCC

Após explorar todos os vizinhos de um vértice u, verificamos a condição id[u] == low[u]. Se ela for verdadeira, significa que não existe caminho retornando para um vértice mais antigo na DFS; logo, u é o início de uma componente fortemente conectada. Nesse momento, removemos vértices da pilha até remover u; todos os vértices removidos formam exatamente uma SCC. Uma SCC é definida como um conjunto de vértices onde qualquer vértice alcança qualquer outro e existe caminho de ida e volta entre todos eles, sendo que cada vértice do grafo pertence exatamente a uma única SCC.

### Fluxo Geral do Algoritmo

Inicializamos todos os vértices como não visitados e executamos DFS a partir de cada vértice ainda não explorado. Ao visitar um nó, atribuímos id e low, inserimos o vértice na pilha e exploramos seus vizinhos, atualizando os valores de low-link conforme os casos descritos. Sempre que id == low, removemos vértices da pilha formando uma nova componente fortemente conectada. Cada vértice e cada aresta são processados apenas uma vez, garantindo complexidade O(V + E) em tempo e O(V) em memória.

### Intuição Final

Podemos interpretar o algoritmo da seguinte forma: id indica quando entramos em um vértice, low indica quão longe conseguimos voltar na exploração, e a pilha representa os vértices ainda ativos na DFS. Quando não é mais possível voltar para vértices anteriores, uma região do grafo se fecha e uma SCC é identificada. Dessa maneira, o algoritmo de Tarjan encontra todas as componentes fortemente conectadas em uma única passagem pelo grafo, de forma eficiente e elegante.

Vamos tomar como exemplo o seguinte grafo:

![Grafo](README_IMAGES/GraphTarjan.png)

Com as arestas: 3→1, 1→2, 2→1.

---

### Estruturas auxiliares

Antes de iniciar a DFS, criamos as seguintes estruturas:

**ids**: array de tamanho igual ao número de vértices, iniciado com -1. O valor de ids[u] não representa o valor do node, mas sim o momento(o id) em que ele foi visitado durante a DFS. Enquanto ids[u] == -1, o node ainda não foi visitado.

**low**: array de mesmo tamanho, iniciado com 0. Guarda o menor id acessível a partir de cada node, considerando os nós ainda na stack. É através do low que conseguimos identificar a raiz de um SCC.

**id**: variável que age como um "relógio", incrementada a cada novo node visitado, determinando a ordem de visita.

**stack**: array que funciona como pilha, armazenando os nodes visitados ainda não atribuídos a algum SCC.

**onStack**: array booleano que nos diz, em O(1), se um node está atualmente na stack. Essencial para a atualização correta do low.

Como os valores dos vértices podem ser arbitrários, cada nó recebe na entrada um índice contíguo de 0 a n-1, permitindo que as estruturas auxiliares sejam arrays primitivos acessados diretamente. Cada nó guarda dois valores: o índice normalizado, usado internamente pelo algoritmo para indexar as estruturas, e o valor original, usado apenas na saída para identificar os vértices de cada SCC. O custo do mapeamento é pago uma única vez na leitura da entrada.

```
Valor original:  10    57    23
Índice interno:   0     1     2
```
---

Estado inicial:
```
ids     = [-1, -1, -1]
low     = [ 0,  0,  0]
stack   = [ 0,  0,  0]
onStack = [false, false, false]
id = 0
```

### Execução

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

---

# Scripts

---

## Geração de Grafo Linear

Para a realização dos experimentos, foi utilizado um script responsável por gerar automaticamente grafos direcionados com *estrutura linear*. Nesse tipo de grafo, os vértices são organizados em sequência, onde cada vértice _i_ possui uma aresta direcionada para o vértice _i+1_, formando uma cadeia de nós conectados em uma única direção.

--- Imagem ---

Como não existem caminhos de retorno entre os vértices, não há ciclos no grafo. Dessa forma, nenhum par de vértices é mutuamente alcançável. Consequentemente, cada vértice forma sua própria Componente Fortemente Conectada (SCC), resultando em _N_ SCCs para um grafo com _N_ vértices.

Esse tipo de estrutura permite avaliar o comportamento dos algoritmos de detecção de SCC em grafos sem ciclos, onde o número de componentes fortemente conectadas é máximo.

## Geração de Grafo Cíclico

Também foi utilizado um script para gerar grafos direcionados com *estrutura cíclica*. Nesse caso, cada vértice _i_ possui uma aresta para o vértice _i+1_, e o último vértice possui uma aresta que retorna para o primeiro, formando um único ciclo.

--- Imagem ---

Nessa estrutura, existe um caminho entre qualquer par de vértices ao percorrer o ciclo, permitindo também o retorno ao vértice de origem. Assim, todos os vértices são mutuamente alcançáveis.

Portanto, todo o grafo forma uma única Componente Fortemente Conectada (SCC), independentemente do número de vértices.

## Geração de Grafo Aleatório

Grafos Aleatórios Controlados (Controlled Random Graphs),  grafos gerados da seguinte forma: a partir de 3 parâmetros, (N, M,  K), os vértices serão distribuídos aleatoriamente por K grupos de diferentes tamanhos e as arestas serão usadas de forma a garantir a formação de um SCC em cada grupo, bem como para formar um Grafo Acíclico Direcionado entre tais grupos, o que mantém a quantidade de SCC’s esperada. Isso permite testar grafos de diferentes densidades controlando a quantidade de componentes fortemente conectados.

---

# Experimento

A experimentação compara o desempenho do algoritmo de Kosaraju com o de Tarjan para análise de tempo de execução e uso de memória. Ambos os algoritmos possuem complexidade de tempo O(V + E), onde V é o número de vértices e E o número de arestas do grafo, porém diferem significativamente em sua abordagem: o Tarjan realiza apenas uma busca em profundidade enquanto o Kosaraju realiza duas, além de construir explicitamente o grafo transposto em memória, resultando em complexidade de espaço O(V + E) contra O(V) do Tarjan. Essa diferença estrutural, embora invisível na notação assintótica, tem impacto direto no desempenho prático dos algoritmos, especialmente para entradas grandes.

Os grafos foram gerados com entradas de tamanho 10², 10³, 10⁴, 10⁵ e 10⁶ vértices e arestas. Cada configuração foi executada 20 vezes por algoritmo, e o tempo médio de execução foi obtido utilizando System.currentTimeMillis() antes e após cada chamada, com o resultado expresso em milissegundos. A média de 20 execuções foi utilizada para reduzir o impacto de variações pontuais causadas por fatores externos, como garbage collection da JVM e variações de escalonamento do sistema operacional.
O experimento foi realizado em uma máquina com as seguintes especificações:

# Uso do Docker no Projeto de Benchmark SCC

## Motivação

Para comparar algoritmos de SCC de forma cientificamente válida, é essencial que todos os testes rodem em um ambiente controlado e reproduzível. Sem Docker, fatores como versão do Java, configurações do sistema operacional e diferenças entre as máquinas dos membros do grupo comprometeriam a consistência dos resultados.

O Docker resolve isso empacotando o código, o compilador e o ambiente de execução em uma imagem isolada — garantindo que o benchmark rode da mesma forma em qualquer máquina.

## Implementação

A imagem base `maven:3.9-eclipse-temurin-21` já inclui Java 21 e Maven, eliminando dependências locais. O `Dockerfile.java` compila o projeto no build e define a JVM com `-Xss512m` para suportar a recursão profunda dos algoritmos recursivos.

Cada algoritmo é um serviço independente no `docker-compose.yml`, com limites de memória e CPU fixos e iguais para todos — reserva igual ao limite garante recursos sempre disponíveis, tornando a comparação justa. Dois volumes são mapeados: `inputs` para os grafos gerados pelo Python e `resultados` para os CSVs gerados pelo Java.

O script `benchmark.sh` usa `docker compose run --rm`, criando e removendo o container a cada execução sem acumular estado entre testes. O build é feito uma única vez, reutilizando cache do Docker quando o código não muda.

## Especificações da Máquina

| Recurso | Especificação |
|--------|---------------|
| Processador | Intel Core i5-13450HX (13ª geração) |
| Núcleos disponíveis no experimento | 6 |
| Memória disponível | 8 GB |
| Ambiente de execução | Container Docker |

## Resultados

Isolamento de recursos, reprodutibilidade entre máquinas e condições idênticas de execução para todos os algoritmos — tornando os resultados do benchmark confiáveis e comparáveis.

| Entrada | Kosaraju cíclico (ms) | Tarjan cíclico (ms) | Kosaraju linear (ms) | Tarjan linear (ms) |
|:-------:|:---------------------:|:-------------------:|:--------------------:|:------------------:|
| 10²     | 1                     | 1                   | 1                    | 1                  |
| 10³     | 3                     | 2                   | 4                    | 3                  |
| 10⁴     | 18                    | 10                  | 15                   | 11                 |
| 10⁵     | 67                    | 44                  | 71                   | 45                 |
| 10⁶     | 235                   | 129                 | 267                  | 148                |

A Tabela apresenta o tempo de execução (em milissegundos) dos algoritmos de Kosaraju e Tarjan para encontrar Componentes Fortemente Conectados (SCC), considerando grafos com estruturas cíclicas e lineares. Os experimentos foram realizados com entradas variando de 10² a 10⁶ vértices.

---

# Análise de Implementação Recursiva do Algoritmo de Tarjan
## Investigação de Performance

Originalmente o algoritmo Tarjan e o algoritmo Kosaraju seriam implementados de forma recursiva, essa ideia foi descartada devido a alguns motivos, um deles é que a profundidade da pilha de recursão poderia afetar significativamente o desempenho e o uso de memória, especialmente em grafos que induzem cadeias longas de chamadas recursivas. Outro motivo foi um comportamento inesperado observado ao implementar o Tarjan recursivo em duas variações diferentes, discutido nas seções seguintes.

### 1. Versões implementadas

#### 1.1 [Versão com HashMap](src/main/java/algoritmos/TarjanRecursivoHashMap.java) (original)

A versão original utilizava `Map<Integer, Integer> nodeIndex` para mapear valores arbitrários dos nós para índices `0..n-1`, e `int[] indexToValue` para o caminho inverso. Dentro da DFS, cada visita a um vizinho realizava um lookup no HashMap:

```java
Integer vBoxed = nodeIndex.get(vNode.getValue());
if (vBoxed == null) continue;
int v = vBoxed;
```

---

#### 1.2 [Versão com acesso direto](src/main/java/algoritmos/TarjanRecursivoAcessoDireto.java) (Node normalizado)

A versão otimizada adicionou `index` e `originalValue` ao `Node`. `getValue()` passou a retornar o índice normalizado `0..n-1` diretamente, eliminando o HashMap da DFS. O `originalValue` é recuperado via `getOriginalValue()` apenas na saída.

```java
int v = vNode.getValue(); // índice direto, sem mapa
```

---

### 2. Resultado inesperado

| Versão | DFS | Estruturas | Tempo (N=10⁵) |
|---|---|---|---|
| Tarjan com HashMap | recursiva | HashMap + arrays | ~90ms |
| Tarjan com acesso direto | recursiva | arrays primitivos | ~350ms |
A versão com acesso direto — estruturalmente mais simples e sem overhead de HashMap — se mostrou **~4x mais lenta** na DFS que a versão com HashMap. Esse resultado é contraintuitivo e levou a uma investigação detalhada.

---

### 3. Hipóteses investigadas

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

### 4. Solução adotada: DFS iterativa

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

### 5. Conclusão dos experimentos

- Para grafos com ciclos longos (pior caso de recursão), a DFS iterativa é obrigatória para o Tarjan ser competitivo.
- A diferença inesperada entre as duas versões recursivas permanece sem explicação definitiva — o comportamento sugere algo interno ao JIT da JVM que não foi possível diagnosticar sem ferramentas de profiling adequadas.
- Com ambos os algoritmos iterativos e estruturas equivalentes, o Tarjan é consistentemente mais rápido por fazer apenas uma DFS, sem construir grafo transposto.

## Conclusão

Os resultados obtidos confirmam o que a análise teórica já sugeria: embora Kosaraju e Tarjan compartilhem a mesma complexidade de tempo assintótica O(V + E), o Tarjan se mostrou consistentemente mais rápido em todos os cenários testados, para ambos os tipos de grafo e em todas as entradas analisadas.

A diferença de desempenho se torna cada vez mais evidente conforme o tamanho da entrada cresce. Para entradas pequenas, como 10², ambos os algoritmos completam a execução em 1ms, tornando a diferença imperceptível na prática. No entanto, à medida que o número de vértices aumenta, a vantagem do Tarjan se acentua de forma expressiva. Para o grafo cíclico com 10⁶ vértices, o Kosaraju levou 235ms contra 129ms do Tarjan, o Tarjan foi aproximadamente 45% mais rápido. Para o grafo linear com a mesma entrada, a diferença é ainda maior: 267ms para o Kosaraju contra 148ms do Tarjan, uma redução de aproximadamente 45% no tempo de execução.

Esse comportamento era esperado e pode ser explicado pela diferença estrutural entre os dois algoritmos. O Kosaraju realiza duas buscas em profundidade completas no grafo, além de construir explicitamente o grafo transposto em memória. Essa construção do transposto percorre todos os V vértices e E arestas uma vez adicional, e o grafo resultante ocupa O(V + E) de memória extra. O Tarjan, por sua vez, resolve o problema em uma única passagem pelo grafo, utilizando apenas estruturas auxiliares de tamanho O(V) — os arrays de ids, low e onStack, além da pilha. Na prática, isso significa que o Tarjan processa cada vértice e cada aresta exatamente uma vez, enquanto o Kosaraju os processa pelo menos três vezes: uma na primeira DFS, uma na construção do transposto e uma na segunda DFS.

Conclui-se portanto que, apesar da equivalência assintótica, o Tarjan é superior ao Kosaraju tanto em tempo quanto em memória na prática. A principal vantagem do Kosaraju reside na sua simplicidade conceitual e facilidade de implementação — a ideia de duas DFS com inversão do grafo é mais intuitiva do que o mecanismo de low-link values utilizado pelo Tarjan, o que justifica seu uso em contextos didáticos ou quando a clareza do código é prioritária em relação à performance.

---

# Referências

- [CP-Algorithms](https://cp-algorithms.com/graph/strongly-connected-components.html)

---

# Contribuintes

- [@KalebeSouza-dev - Kalebe Souza](https://github.com/KalebeSouza-dev)

- [@00joaoguilherme - Joao Guilherme](https://github.com/00joaoguilherme)

- [@2004gustavoPaiva - Luis Gustavo](https://github.com/2004gustavoPaiva)

- [@RenanAF18 - Savio Renan](https://github.com/RenanAF18)

- [@SavioCartaxo - Savio Cartaxo](https://github.com/SavioCartaxo)

Projeto feito como trabalho final da disciplina de Estrutura de Dados e Algoritmos (EDA) e Laboratório de Estrutura de Dados e Algoritmos (LEDA) da graduação em Ciência da Computação na Universidade Federal de Campina Grande (UFCG) no período 2025.2.
