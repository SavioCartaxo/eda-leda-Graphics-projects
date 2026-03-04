<!-- definição de SCCs - Sávio Cartaxo -->

# Introdução teórica

## O que é um grafo?

Considere a figura a seguir, a qual representa um mapa entre vias:

![Roads_Representation](README_IMAGES/roads.png)


Essas vias podem ser representadas diagramicamente por pontos ligados entre linhas: os pontos P, Q, R e S são chamados de vértices, as linhas são chamadas de arestas e todo o diagrama é chamade de grafo. Note que a intersectção entre as linhas PR e QS não é um vértice uma vez que não corresponde a um cruzamento, isto é, não é um ponto onde as ruas se encontram. O conceito de grau de um vértice qualquer é a quantidade de arestas que terminam nesse vértice; é o mesmo que dizer qual o número de ruas em um dado cruzamento na figura acima. Por exemplo, o grau do vértice P é 3.

 
![Graph Diagram](README_IMAGES/graph.png)


O grafo na figura 1.1 pode, além de um mapa de estradas, representar diversas coisas: uma rede elétrica, moléculas ou redes neurais, essencialmente, portanto, um  grafo é uma representação de um conjunto de pontos e as ligações entre eles. Na computação são extremamente uteis para lidar com problemas relacionados a rede de computadores com conexões via WI-FI ou cabo entre roteadores, rede sociais e a conexão de usuários que seguem um ao outro, ou até a internet com páginas da web conectadas através de um link.


 






<hr>

<h1>Componentes Fortemente Conectados</h1>

Em um grafo dirigido G, diz-se que ele é fortemente conectado quando, para todo par de vértices u e v, existe um caminho de u até v e, ao mesmo tempo, um caminho de v até u. Em outras palavras, qualquer vértice pode ser alcançado a partir de qualquer outro.

No entanto, um grafo dirigido pode não ser fortemente conectado como um todo. Nesse caso, a forte conectividade pode ocorrer em apenas partes do grafo. Dizemos que dois vértices u e v são fortemente conectados entre si quando existe um caminho de u até v e outro de v até u, mesmo que u = v. Assim, mesmo que G não seja fortemente conectado, ele pode ser decomposto em subconjuntos de vértices nos quais, internamente, todo par u e v é mutuamente alcançável. Cada um desses subconjuntos induz um subgrafo chamado Componente Fortemente Conectado (CFC). Essa ideia é melhor compreendida ao observar-se o exemplo:

<figure>
    <img src="README_IMAGES/img_example_SCC.png" alt="Exemplo de SCC" style="display: block; margin: auto;">
</figure>

<hr>
