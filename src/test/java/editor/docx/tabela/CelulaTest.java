package editor.docx.tabela;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import builder.CelulaBuilder;
import editor.docx.tabela.Celula;
import utils.DateUtils;

public class CelulaTest {

	@Test
	public void deveCriarUmaCelulaDaLinhaDaTabelaInformandoUmValor() throws Exception {
		String valorDaCelula = "Alcir Junior";

		Celula celula = new CelulaBuilder().comValorDaCelula(valorDaCelula).criar();

		assertNotNull(celula);
	}

	@Test
	public void deveConverterOValorDaCelulaEmString() throws Exception {
		String valorDaCelula = "Relatório";

		Celula celula = new CelulaBuilder().comValorDaCelula(valorDaCelula).criar();

		assertTrue(celula.getValorDaCelula() instanceof String);
	}

	@Test
	public void deveFormatarUmValorMonetario() throws Exception {
		BigDecimal valorDaCelula = BigDecimal.valueOf(10.30);

		Celula celula = new CelulaBuilder().comValorDaCelula(valorDaCelula).criar();
		String valorEsperado = "R$ 10,30";

		assertEquals(valorEsperado, celula.getValorDaCelula());
	}

	@Test
	public void deveFormatarUmValorNumerico() throws Exception {
		Double valorDaCelula = 1.99;

		Celula celula = new CelulaBuilder().comValorDaCelula(valorDaCelula).criar();
		String valorEsperado = "1,99";

		assertEquals(valorEsperado, celula.getValorDaCelula());
	}

	@Test
	public void deveFormatarUmValorDoTipoData() throws Exception {
		String dataEsperada = "01/05/2016";
		Date valorDaCelula = DateUtils.criarData(dataEsperada);
		
		Celula celula = new CelulaBuilder().comValorDaCelula(valorDaCelula).criar();
		
		assertEquals(dataEsperada, celula.getValorDaCelula());
	}
}
