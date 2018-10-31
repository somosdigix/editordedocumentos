package builder;

import editor.docx.tabela.Celula;

public class CelulaBuilder {

	private Object valorDaCelula;

	public CelulaBuilder() {
		this.valorDaCelula = "Alcir Junior";
	}

	public CelulaBuilder comValorDaCelula(Object valorDaCelula) {
		this.valorDaCelula = valorDaCelula;
		return this;
	}

	public Celula criar() {
		return Celula.criar(valorDaCelula);
	}
}
