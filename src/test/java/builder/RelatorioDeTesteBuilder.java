package builder;

public class RelatorioDeTesteBuilder {

	private String cabecalhoDoRelatorio;
	private String tituloDoRelatorio;
	private String tituloDaTabela;
	private String rodapeDoRelatorio;

	public RelatorioDeTesteBuilder() {
		this.cabecalhoDoRelatorio = "Relatório de teste";
		this.tituloDoRelatorio = "Titulo de teste";
		this.tituloDaTabela = "Tabela para teste";
		this.rodapeDoRelatorio = "Rodapé para teste";
	}

	public void informarTituloDoRelatorio(String tituloDoRelatorio) {
		this.tituloDoRelatorio = tituloDoRelatorio;
	}

	public String getCabecalhoDoRelatorio() {
		return cabecalhoDoRelatorio;
	}

	public String getTituloDoRelatorio() {
		return tituloDoRelatorio;
	}

	public String getTituloDaTabela() {
		return tituloDaTabela;
	}

	public String getRodapeDoRelatorio() {
		return rodapeDoRelatorio;
	}
	
	public RelatorioDeTesteBuilder criar() {
		return new RelatorioDeTesteBuilder();
	}
}
