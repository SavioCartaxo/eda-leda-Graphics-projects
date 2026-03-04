O algoritmo de Kosaraju, também conhecido como algoritmo Kosaraju-Sharir, é um algoritmo que encontra componentes fortemente conectados (SCC) de um grafo direcionado em tempo linear O(V + E) quando representado por lista de adjascência, onde V é o número de vértices (ou nós) e E o número de arestas do grafo. 

A implementação utilizada constrói explicitamente o grafo transposto em memória, o que usa espaço adicional O(V + E), mas simplifica a implementação e o entendimento em relação a abordagens que evitam essa construção explícita. A ideia é usar busca em profundidade duas vezes, fazendo uso da propriedade de que um grafo e seu transposto possuem exatamente os mesmos SCCs.

Vamos usar o grafo direcionado abaixo como exemplo para executar o algoritmo de Kosaraju.


![Grafo original](../../../../README_IMAGES/grafo_imagem1.png)


Qual o nosso objetivo inicial? primeiro, devemos criar uma pilha que representa a ordem de saída dos vértices através de uma busca em profundidade e guardamos os vértices já visitados. A DFS faz chamadas recursivas para cada vizinho não visitado, e ao retornar de todas essas chamadas — ou seja, quando não há mais vizinhos a explorar — o vértice é adicionado à pilha. 

Podemos começar a DFS por qualquer vértice, mas por convenção iremos utilizar o 1. Realizando a busca, visitamos o 1 e vemos a quem ele esta ligado. Vemos que ele está ligado ao 2, então visitamos o 2, e vemos que ele está ligado ao 3, visitamos o 3. Como o 3 não tem mais vizinhos não visitados, ele é adicionado à pilha. A recursão retorna para o 2, que também não tem mais vizinhos, e é adicionado. Por fim o 1 é adicionado, e temos pilha = [3, 2, 1]. 

Ainda existem vértices não visitados, iniciamos uma nova DFS a partir do vértice 4, escolhido arbitrariamente dentre os não visitados. Visitando o 4, vemos que ele tem o 3 e o 5 como vizinhos, o 3 já foi visitado, mas o 5 não. Visitamos 5 e vemos que ele está ligado ao 6, visitamos o 6. O 6 está ligado ao 4, que já foi visitado, então adicionamos o 6 à pilha, e a recursão retorna para o 5, que é adicionado, e por fim o 4 é adicionado. Agora temos pilha = [3, 2, 1, 6, 5, 4]. 

Ainda temos nós não visitados, o 9. O 9 está ligado ao 4, que já foi visitado, então o 9 é adicionado na pilha. Finalmente, nossa pilha está completa [3, 2, 1, 6, 5, 4, 9]. 

Agora, o próximo passo é inverter a direção de todas as arestas do grafo, a fim de termos o seu transposto.


![Grafo transposto](../../../../README_IMAGES/grafo_imagem2.png)


Por que queremos o transposto do grafo? Sabemos que um SCC possui caminhos nos dois sentidos entre todos os seus vértices, ou seja, é por definição bidirecional. Ao invertermos as arestas, essa característica se preserva — o que era ciclo continua sendo ciclo. Porém, as arestas que conectavam SCCs distintos agora apontam na direção oposta, bloqueando a DFS de vazar de um SCC para outro. Isso nos permite, na segunda DFS, explorar exatamente um SCC por vez. 

Feito isso, realizamos a segunda busca em profundidade, agora no grafo transposto. Retiramos os vértices do topo da pilha um a um — lembrando que o topo representa o vértice que terminou por último na primeira DFS, ou seja, o de maior alcance. Para cada vértice retirado que ainda não foi visitado, criamos um novo SCC e iniciamos uma DFS no grafo transposto para encontrar todos os seus vértices. Todos os vértices alcançados nessa DFS pertencem ao mesmo SCC. Vértices já visitados são ignorados, pois já foram atribuídos a um componente. Ao esvaziarmos a pilha, temos todos os SCCs do grafo identificados. 

Dando início a execução desta parte final, aplicamos a busca em profundidade no elemento do topo da pilha, que é o vértice 9. Como ele não foi visitado, iniciamos a DFS para montar seu SCC. Percebemos que ele não tem vizinhos no grafo transposto, portanto o 9 sozinho já é um SCC. 

Temos pilha = [3, 2, 1, 6, 5, 4], o topo é o 4. Como ele não foi visitado, iniciamos a DFS para montar seu SCC. Visitamos o 4 e vemos que ele está conectado ao 6. Visitamos o 6, vemos que está conectado ao 5. Visitamos o 5, e ele está conectado ao 4, que já foi visitado, encerrando as chamadas recursivas. Os vértices 4, 5 e 6 formam o segundo SCC. 

Temos pilha = [3, 2, 1]. O próximo topo é o 1, que não foi visitado, então iniciamos a DFS para montar seu SCC. O 1 está ligado ao 3, visitamos, que está ligado ao 2, visitamos. O 2 está ligado ao 1, que já foi visitado, encerrando a DFS. Os vértices 1, 2 e 3 formam o terceiro SCC. 

Nossa pilha está vazia, portanto encerrou a execução do algoritmo. Finalmente, descobrimos que o nosso grafo do exemplo possui três componentes fortemente conectados.


![SCCs](../../../../README_IMAGES/grafo_imagem3.png)


Temos os SCCs {9}, {4, 5, 6} e {1, 2, 3}.

O conceito é simples e o Kosaraju é eficiente, no entanto, não é mais eficiente que outros algoritmos que encontram SCCs como o Tarjan, que embora tenha uma complexidade de tempo assintoticamente igual ao Kosaraju, realiza apenas uma busca em profundidade, ao invés de duas, levando-o a ser mais rápido na prática.