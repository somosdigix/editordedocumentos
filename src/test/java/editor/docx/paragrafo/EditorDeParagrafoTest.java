package editor.docx.paragrafo;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Assert;
import org.junit.Test;

import editor.docx.paragrafo.EditorDeParagrafo;

public class EditorDeParagrafoTest {

	@Test
	public void deveEditarUmParagrafoDoTexto() throws Exception {
		String conteudoDoTestoEsperado = "João do Pé de Feijão";
		String atributoQueSeraAlteradoNoDocumento = "nomeDoFuncionario";
		Map<String, Object> mapaDeAtributos = montarMapaDeAtributos(atributoQueSeraAlteradoNoDocumento,
				conteudoDoTestoEsperado);
		XWPFDocument documento = new XWPFDocument();
		XWPFParagraph paragrafoDoDocumento = criarParagrafoNoDocumento(documento, atributoQueSeraAlteradoNoDocumento);

		EditorDeParagrafo.comMapaDeAtributos(mapaDeAtributos).editarParagrafosDoDocumento(paragrafoDoDocumento);
		String textoAdicionadoNoDocumento = buscarPalavraNoArquivo(documento, conteudoDoTestoEsperado);

		Assert.assertEquals(conteudoDoTestoEsperado, textoAdicionadoNoDocumento);
	}

	private XWPFParagraph criarParagrafoNoDocumento(XWPFDocument documento, String atributoQueSeraAlteradoNoDocumento) {
		XWPFParagraph paragrafoDoDocumento = documento.createParagraph();
		XWPFRun linhaDoDocumento = paragrafoDoDocumento.createRun();
		linhaDoDocumento.setText(atributoQueSeraAlteradoNoDocumento);
		return paragrafoDoDocumento;
	}

	private Map<String, Object> montarMapaDeAtributos(String tagNoDocumento, String conteudoDoTestoEsperado) {
		Map<String, Object> mapaDeAtributos = new HashMap<>();
		mapaDeAtributos.put(tagNoDocumento, conteudoDoTestoEsperado);
		return mapaDeAtributos;
	}

	private String buscarPalavraNoArquivo(XWPFDocument documento, String conteudoParaBuscar) throws Exception {
		return documento.getParagraphs().stream()
				.filter(paragrafo -> verificarConteudoDoParagrafo(paragrafo, conteudoParaBuscar)).findFirst().get()
				.getText();
	}

	private boolean verificarConteudoDoParagrafo(XWPFParagraph paragrafo, String conteudoParaBuscar) {
		return paragrafo.getText().equals(conteudoParaBuscar);
	}
}
