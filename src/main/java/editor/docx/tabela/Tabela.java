package editor.docx.tabela;

import java.util.List;

public class Tabela {

	private final List<Linha> linhas;

	private Tabela(List<Linha> linhas) {
		this.linhas = linhas;
	}

	public static Tabela criar(List<Linha> linhas) {
		return new Tabela(linhas);
	}

	public List<Linha> getLinhas() {
		return linhas;
	}
}
