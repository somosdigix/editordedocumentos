package editor.docx.tabela;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import builder.CelulaBuilder;
import builder.LinhaBuilder;
import editor.docx.tabela.Celula;
import editor.docx.tabela.Linha;

public class LinhaTest {

	@Test
	public void deveCriarUmaLinhaDaTabelaInformandoAsCelulas() throws Exception {
		Celula celula = new CelulaBuilder().criar();
		
		Linha linha = new LinhaBuilder().comCelulas(celula).criar();
		
		assertTrue(linha.getCelulas().contains(celula));
	}
}
