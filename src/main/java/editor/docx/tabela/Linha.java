package editor.docx.tabela;

import java.util.List;

public class Linha {

	private final List<Celula> celulas;

	private Linha(List<Celula> celulas) {
		this.celulas = celulas;
	}

	public static Linha criar(List<Celula> celulas) {
		return new Linha(celulas);
	}

	public List<Celula> getCelulas() {
		return celulas;
	}
}
