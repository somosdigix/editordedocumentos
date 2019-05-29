package editor.docx.tabela;

public class FormatacaoDaTabela {

	private static final Integer TAMANHO_DA_FONTE_PADRAO = 10;
	private static final String FONTE_PADRAO = "Arial";

	private String fonte;
	private Integer tamanhoDaFonte;

	private FormatacaoDaTabela() {
		this.tamanhoDaFonte = TAMANHO_DA_FONTE_PADRAO;
		this.fonte = FONTE_PADRAO;
	}
	
	public FormatacaoDaTabela comFonte(String fonte) {
		this.fonte = fonte;
		return this;
	}

	public FormatacaoDaTabela comTamanhoDaFonte(Integer tamanhoDaFonte) {
		this.tamanhoDaFonte = tamanhoDaFonte;
		return this;
	}

	public String getFonte() {
		return fonte;
	}

	public Integer getTamanhoDaFonte() {
		return tamanhoDaFonte;
	}

	public static Integer getTamanhoDaFontePadrao() {
		return TAMANHO_DA_FONTE_PADRAO;
	}

	public static String getFontePadrao() {
		return FONTE_PADRAO;
	}
}
