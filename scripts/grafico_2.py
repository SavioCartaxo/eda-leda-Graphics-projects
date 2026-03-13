import os
import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
import sys

# gera um grafico comparativo dos 3 algoritmos
def benchmark(df, tipo: str, path_saida: str = "resultados") -> None:
    """
    Plota o gráfico comparativo de desempenho entre N algoritmos a partir de um DataFrame.
    Salva o gráfico como imagem PNG na pasta especificada.
    :param df: DataFrame com colunas n, arestas e *_ms para cada algoritmo
    :type df: pd.DataFrame
    :param tipo: descrição do cenário testado, usada no título e no nome do arquivo
    :type tipo: str
    :param path_saida: caminho da pasta onde o gráfico será salvo
    :type path_saida: str
    :return: None
    """
    colunas_ms = [col for col in df.columns if col.endswith("_ms")]
    cores = ["#00d4ff", "#a29bfe", "#ff6b6b", "#55efc4"]
    offsets = [10, -10, 18, -18]

    fig, ax = plt.subplots(figsize=(10, 5))
    fig.patch.set_facecolor("#0f0f0f")
    ax.set_facecolor("#1a1a2e")

    for i, col in enumerate(colunas_ms):
        cor = cores[i % len(cores)]
        label = col.replace("_ms", "")
        ax.fill_between(df['n'], df[col], alpha=0.15, color=cor)
        ax.plot(df['n'], df[col], color=cor, linewidth=2.5, marker='o', markersize=7,
                markerfacecolor="#ff6b6b", markeredgecolor="white", markeredgewidth=1.2,
                zorder=5, label=label)
        for x, y in zip(df['n'], df[col]):
            ax.annotate(f"{int(y)}", xy=(x, y), xytext=(0, offsets[i % len(offsets)]),
                        textcoords="offset points", ha='center', fontsize=6, color=cor)

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
    df = pd.read_csv(sys.argv[1])
    benchmark(
        df=df,
        tipo=sys.argv[2],
        path_saida=sys.argv[3] if len(sys.argv) > 3 else "resultados"
    )