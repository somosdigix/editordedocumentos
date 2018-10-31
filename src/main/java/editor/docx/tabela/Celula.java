package editor.docx.tabela;

import java.math.BigDecimal;
import java.util.Date;

import utils.StringUtils;

public class Celula {

	private final Object valorDaCelula;

	private Celula(Object valorDaCelula) {
		this.valorDaCelula = valorDaCelula;
	}

	public static Celula criar(Object valorDaCelula) {
		return new Celula(valorDaCelula);
	}

	public String getValorDaCelula() {
		return converterValorDaCelulaEmString();
	}

	private String converterValorDaCelulaEmString() {
		return valorDaCelula instanceof BigDecimal ? StringUtils.valorMonetarioFormatado((BigDecimal) valorDaCelula)
				: valorDaCelula instanceof Date ? StringUtils.dataFormatada((Date) valorDaCelula)
						: valorDaCelula instanceof Double ? StringUtils.valorFormatado((Double) valorDaCelula)
								: valorDaCelula.toString();
	}
}
