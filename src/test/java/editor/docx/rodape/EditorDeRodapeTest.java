package editor.docx.rodape;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Assert;
import org.junit.Test;

import editor.docx.paragrafo.EditorDeParagrafo;
import editor.docx.rodape.EditorDeRodape;
import editor.docx.tabela.EditorDeTabela;

public class EditorDeRodapeTest {

	@Test
	public void deveEditarOConteudoDoRodape() throws Exception {
		String conteudoDoTestoEsperado = "João";
		String atributoQueSeraAlterado = "nomeDoFuncionario";
		Map<String, Object> mapaDeAtributos = montarMapaDeAtributos(atributoQueSeraAlterado,
				conteudoDoTestoEsperado);

		EditorDeParagrafo editorDeParagrafo = EditorDeParagrafo.comMapaDeAtributos(mapaDeAtributos);
		EditorDeTabela editorDeTabela = EditorDeTabela.comEditorDeParagrafo(editorDeParagrafo);
		EditorDeRodape editorDeRodape = EditorDeRodape.comEditorDeParagrafoETabela(editorDeParagrafo, editorDeTabela);
		XWPFFooter rodape = criarRodapeComAtributoQueSeraAlterado(atributoQueSeraAlterado);

		editorDeRodape.editarConteudoDosRodapesDoDocumento(Arrays.asList(rodape));
		String textoAdicionadoNoDocumento = buscarPalavraNoDocumento(rodape, conteudoDoTestoEsperado);

		Assert.assertEquals(conteudoDoTestoEsperado, textoAdicionadoNoDocumento);
	}

	@SuppressWarnings("resource")
	private XWPFFooter criarRodapeComAtributoQueSeraAlterado(String atributoQueSeraAlteradoNoDocumento) throws IOException {
		XWPFDocument documentoDeTexto = new XWPFDocument();
		XWPFFooter rodape = documentoDeTexto.createHeaderFooterPolicy().createFooter(XWPFHeaderFooterPolicy.DEFAULT);
		XWPFParagraph paragrafo = rodape.createParagraph();
		XWPFRun linha = paragrafo.createRun();
		linha.setText(atributoQueSeraAlteradoNoDocumento);
		return rodape;
	}

	private Map<String, Object> montarMapaDeAtributos(String tagNoDocumento, String conteudoDoTestoEsperado) {
		Map<String, Object> mapaDeAtributos = new HashMap<>();
		mapaDeAtributos.put(tagNoDocumento, conteudoDoTestoEsperado);
		return mapaDeAtributos;
	}

	private String buscarPalavraNoDocumento(XWPFFooter rodape, String conteudoDoTextoEsperado) {
		List<XWPFParagraph> paragrafos = rodape.getListParagraph();
		return paragrafos.stream().filter(paragrafo -> paragrafo.getText().equals(conteudoDoTextoEsperado)).findFirst()
				.get().getText();
	}
}
