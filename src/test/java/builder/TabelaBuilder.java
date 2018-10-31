package builder;

import java.util.Arrays;
import java.util.List;

import editor.docx.tabela.Linha;
import editor.docx.tabela.Tabela;

public class TabelaBuilder {

	private List<Linha> linhas;

	public TabelaBuilder() {
		this.linhas = Arrays.asList(new LinhaBuilder().criar());
	}

	public TabelaBuilder comLinhas(Linha... linhas) {
		this.linhas = Arrays.asList(linhas);
		return this;
	}

	public Tabela criar() {
		return Tabela.criar(linhas);
	}
}
