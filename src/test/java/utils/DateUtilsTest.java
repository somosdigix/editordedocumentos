package utils;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class DateUtilsTest {

	private static final String FORMATO_BRASILEIRO_PARA_DATA = "dd/MM/yyy";

	@Test
	public void deveConverterUmaDataDoFormatoStringParaOFormatoDate() throws Exception {
		String data = "11/05/2016";

		Date dataCriada = DateUtils.criarData(data);

		Date dataEsperada = (Date) new SimpleDateFormat(FORMATO_BRASILEIRO_PARA_DATA).parse(data);

		assertTrue(dataCriada instanceof Date);
		assertEquals(dataEsperada, dataCriada);
	}
}
