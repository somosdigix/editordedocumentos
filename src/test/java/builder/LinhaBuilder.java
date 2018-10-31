package builder;

import java.util.Arrays;
import java.util.List;

import editor.docx.tabela.Celula;
import editor.docx.tabela.Linha;

public class LinhaBuilder {

	private List<Celula> celulas;

	public LinhaBuilder() {
		this.celulas = Arrays.asList(new CelulaBuilder().criar());
	}

	public LinhaBuilder comCelulas(Celula... celulas) {
		this.celulas = Arrays.asList(celulas);
		return this;
	}

	public Linha criar() {
		return Linha.criar(celulas);
	}
}
