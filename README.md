<!-- definição de SCCs - Sávio Cartaxo -->

# Introdução teórica

## O que é um grafo?

Considere a figura a seguir, a qual representa um mapa de vias de trânsito:

![Roads Representation](README_IMAGES/roads.jpg)
Figura 1.0


Essas vias podem ser representadas diagramicamente por pontos ligados entre linhas: os pontos P, Q, R e S são chamados de vértices, as linhas são chamadas de arestas e todo o diagrama é chamade de grafo. Note que a intersectção entre as linhas PR e QS não é um vértice uma vez que não corresponde a um cruzamento, isto é, não é um ponto onde as ruas se encontram. O conceito de grau de um vértice qualquer é a quantidade de arestas que terminam nesse vértice; é o mesmo que dizer qual o número de ruas em um dado cruzamento na figura acima. Por exemplo, o grau do vértice P é 3.

 
![Graph Diagram](README_IMAGES/graph.png)
Figura 1.1


O grafo na figura 1.1 pode, além de um mapa de estradas, representar diversas coisas: uma rede elétrica, moléculas ou redes neurais, essencialmente, portanto, um  grafo é uma representação de um conjunto de pontos e as ligações entre eles. Na computação são extremamente uteis para lidar com problemas relacionados a rede de computadores com conexões via WI-FI ou cabo entre roteadores, rede sociais e a conexão de usuários que seguem um ao outro, ou até a internet com páginas da web conectadas através de um link.

Agora imagine que as vias possuem sentido único, ou seja, há apenas uma direção a se seguir de um cruzamento há outro; isto nada mais é que  um novo grafo de um tipo especial chamado grafo direcionado: há apenas um sentido entre dois vértices.


![Directed Graph Diagram](README_IMAGES/directed_graph.jpg)
Figura 1.2

## Depth First Search
 
Se é preciso encontrar uma informação específica, há de se ter uma forma  de procurá-la sistematicamente no grafo, o que muitas vezes envolve olhar todos os seus vértices até que isso seja possível. Nesse sentido, há dois algoritmos bem conhecidos, **depth first search** e **breadth first search**, ambos percorrem por todos os vértices, mas em ordens diferentes. Como nesse trabalho tratamos de algoritmos baseados no depth first search (também chamado de DFS), apenas trataremos dele.

No *depth first search*, procuramos o mais fundo possível no grafo. Esse algoritmo explora um caminho e segue por ele até que não seja possível mais avançar, e então retorna para o  vértice de início e explora outro caminho ainda inexplorado, similar a explorar um labirinto sempre por um único caminho de corredores até o fim e após isso retornar ao ponto de início para fazer o mesmo por outro caminho, caso esse exista.
 
![Directed Graph Diagram](README_IMAGES/directed_graph2.jpg)
Figura 2.0

Na figura 2.0, que se trata de um grafo direcionado, tomando o vértice A como o inicial, o caminho seguido pelo algoritmo seria A → B → D → C → E → F → G → H. Note que, após não encontrar nenhum vértice inexplorado no ponto C, o algorito tem que retornar até encontrar um caminho ainda não explorado, repetindo o mesmo processo para esse.

<hr>

<h1>Componentes Fortemente Conectados</h1>

Em um grafo dirigido G, diz-se que ele é fortemente conectado quando, para todo par de vértices u e v, existe um caminho de u até v e, ao mesmo tempo, um caminho de v até u. Em outras palavras, qualquer vértice pode ser alcançado a partir de qualquer outro.

No entanto, um grafo dirigido pode não ser fortemente conectado como um todo. Nesse caso, a forte conectividade pode ocorrer em apenas partes do grafo. Dizemos que dois vértices u e v são fortemente conectados entre si quando existe um caminho de u até v e outro de v até u, mesmo que u = v. Assim, mesmo que G não seja fortemente conectado, ele pode ser decomposto em subconjuntos de vértices nos quais, internamente, todo par u e v é mutuamente alcançável. Cada um desses subconjuntos induz um subgrafo chamado Componente Fortemente Conectado (CFC). Essa ideia é melhor compreendida ao observar-se o exemplo:

<figure>
    <img src="README_IMAGES/img_example_SCC.png" alt="Exemplo de SCC" style="display: block; margin: auto;">
</figure>

<hr>
