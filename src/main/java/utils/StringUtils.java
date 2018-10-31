package utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtils {

	private static final Locale LOCAL_BRASIL = new Locale("pt", "BR");
	private static final String FORMATO_BRASILEIRO_PARA_DATA = "dd/MM/yyy";

	public static String valorMonetarioFormatado(BigDecimal valorASerFormatado) {
		return NumberFormat.getCurrencyInstance(LOCAL_BRASIL).format(valorASerFormatado);
	}

	public static String dataFormatada(Date dataASerFormatada) {
		return new SimpleDateFormat(FORMATO_BRASILEIRO_PARA_DATA).format(dataASerFormatada);
	}

	public static String valorFormatado(Double valorASerFormatado) {
		return valorASerFormatado.toString().replace(".", ",");
	}
}
