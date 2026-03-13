import os
import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
import sys

def benchmark(caminho_csv: str, tipo: str, path_saida: str = "resultados") -> None:
    """
    Plota o gráfico comparativo de desempenho entre dois algoritmos a partir de um CSV.
    Salva o gráfico como imagem PNG na pasta especificada.
    :param caminho_csv: caminho para o arquivo CSV com os resultados do benchmark
    :type caminho_csv: str
    :param tipo: descrição do cenário testado, usada no título do gráfico e no nome do arquivo
    :type tipo: str
    :param path_saida: caminho da pasta onde o gráfico será salvo
    :type path_saida: str
    :return: None
    """
    df = pd.read_csv(caminho_csv)
    algo1 = [col for col in df.columns if col.endswith("_ms")][0].replace("_ms", "")
    algo2 = [col for col in df.columns if col.endswith("_ms")][1].replace("_ms", "")
    col1 = f"{algo1}_ms"
    col2 = f"{algo2}_ms"

    fig, ax = plt.subplots(figsize=(9, 5))
    fig.patch.set_facecolor("#0f0f0f")
    ax.set_facecolor("#1a1a2e")

    ax.fill_between(df['n'], df[col1], alpha=0.15, color="#00d4ff")
    ax.plot(df['n'], df[col1], color="#00d4ff", linewidth=2.5, marker='o', markersize=7,
            markerfacecolor="#ff6b6b", markeredgecolor="white", markeredgewidth=1.2,
            zorder=5, label=algo1)

    ax.fill_between(df['n'], df[col2], alpha=0.15, color="#a29bfe")
    ax.plot(df['n'], df[col2], color="#a29bfe", linewidth=2.5, marker='o', markersize=7,
            markerfacecolor="#ff6b6b", markeredgecolor="white", markeredgewidth=1.2,
            zorder=5, label=algo2)

    for x, y in zip(df['n'], df[col1]):
        ax.annotate(f"{int(y)}", xy=(x, y), xytext=(0, 10),
                    textcoords="offset points", ha='center', fontsize=6, color="#00d4ff")
    for x, y in zip(df['n'], df[col2]):
        ax.annotate(f"{int(y)}", xy=(x, y), xytext=(0, -10),
                    textcoords="offset points", ha='center', fontsize=6, color="#a29bfe")

    ax.grid(color="#ffffff", linestyle="--", linewidth=0.4, alpha=0.15)
    for spine in ax.spines.values():
        spine.set_edgecolor("#333355")
    ax.set_xlabel("Tamanho da entrada (n)", color="white", fontsize=11)
    ax.set_ylabel("Mediana (ms)", color="white", fontsize=11)
    ax.set_title(tipo, color="white", fontsize=13, pad=14)
    ax.tick_params(colors="white")
    ax.xaxis.set_major_formatter(ticker.FuncFormatter(lambda v, _: f"{int(v):,}".replace(",", ".")))
    ax.yaxis.set_major_formatter(ticker.FuncFormatter(lambda v, _: f"{int(v)}"))
    ax.legend(facecolor="#1a1a2e", edgecolor="#333355", labelcolor="white")
    plt.tight_layout()

    os.makedirs(path_saida, exist_ok=True)
    plt.savefig(os.path.join(path_saida, tipo.replace(' ', '_') + ".png"), facecolor="#0f0f0f")
    plt.close(fig)


if __name__ == "__main__":
    benchmark(
        caminho_csv=sys.argv[1],
        tipo=sys.argv[2],
        path_saida=sys.argv[3] if len(sys.argv) > 3 else "resultados"
    )
# Para rodar pelo terminal:
# python scripts/benchmark.py "Nome_CSV" "Legenda_de_tipo" "pasta_saida"