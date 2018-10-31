package utils;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void deveConverterUmaDataEmStringNoFormatoDoPadraoBrasileiro() throws Exception {
		String dataEsperada = "12/05/2016";
		Date dataCriada = DateUtils.criarData(dataEsperada);

		String dataConvertida = StringUtils.dataFormatada(dataCriada);

		assertEquals(dataEsperada, dataConvertida);
	}

	@Test
	public void deveConverterUmBigDecimalParaStringNoFormatoDoPadraoBrasileiro() throws Exception {
		BigDecimal valorASerFormatado = BigDecimal.valueOf(1.99);

		String valorFormatado = StringUtils.valorMonetarioFormatado(valorASerFormatado);
		String valorEsperado = "R$ 1,99";

		assertEquals(valorEsperado, valorFormatado);
	}

	@Test
	public void deveConverterUmValorDoubleParaStringNoFormatoDoPadraoBrasileiro() throws Exception {
		Double valorASerFormatado = 2.49;

		String valorFormatado = StringUtils.valorFormatado(valorASerFormatado);
		String valorEsperado = "2,49";

		assertEquals(valorEsperado, valorFormatado);
	}
	
}
