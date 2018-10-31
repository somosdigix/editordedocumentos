package editor.docx.tabela;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import builder.LinhaBuilder;
import builder.TabelaBuilder;
import editor.docx.tabela.Linha;
import editor.docx.tabela.Tabela;

public class TabelaTest {

	@Test
	public void deveCriarUmaTabelaComLinhas() throws Exception {
		Linha linha = new LinhaBuilder().criar();
		
		Tabela tabela = new TabelaBuilder().comLinhas(linha).criar();

		assertTrue(tabela.getLinhas().contains(linha));
	}
}
