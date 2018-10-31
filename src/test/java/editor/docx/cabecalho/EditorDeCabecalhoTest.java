package editor.docx.cabecalho;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Assert;
import org.junit.Test;

import editor.docx.cabecalho.EditorDeCabecalho;
import editor.docx.paragrafo.EditorDeParagrafo;
import editor.docx.tabela.EditorDeTabela;

public class EditorDeCabecalhoTest {
	
	@Test
	public void deveEditarOConteudoDeUmCabecalho() throws Exception{
		String conteudoEsperado = "Escola Maria";
		String atributoQueSeraAlterado = "nomeDaEscola";
		Map<String, Object> mapaDeAtributos = montarMapaDeAtributos(atributoQueSeraAlterado,
				conteudoEsperado);

		EditorDeParagrafo editorDeParagrafo = EditorDeParagrafo.comMapaDeAtributos(mapaDeAtributos);
		EditorDeTabela editorDeTabela = EditorDeTabela.comEditorDeParagrafo(editorDeParagrafo);
		EditorDeCabecalho editorDeCabecalho = EditorDeCabecalho.comEditorDeParagrafoETabela(editorDeParagrafo, editorDeTabela);
		XWPFHeader cabecalho = criarCabelcalhoComAtributoQueSeraAlterado(atributoQueSeraAlterado);

		editorDeCabecalho.editarConteudoDosCabecalhosDoDocumento(Arrays.asList(cabecalho));
		String textoAdicionadoNoDocumento = buscarPalavraNoDocumento(cabecalho, conteudoEsperado);

		Assert.assertEquals(conteudoEsperado, textoAdicionadoNoDocumento);
	}
	
	@SuppressWarnings("resource")
	private XWPFHeader criarCabelcalhoComAtributoQueSeraAlterado(String atributoQueSeraAlteradoNoDocumento) throws IOException {
		XWPFHeader cabecalho = new XWPFDocument().createHeaderFooterPolicy().createHeader(XWPFHeaderFooterPolicy.DEFAULT);
		XWPFParagraph paragrafo = cabecalho.createParagraph();
		XWPFRun linha = paragrafo.createRun();
		linha.setText(atributoQueSeraAlteradoNoDocumento);
		return cabecalho;
	}
	
	private Map<String, Object> montarMapaDeAtributos(String tagNoDocumento, String conteudoDoTestoEsperado) {
		Map<String, Object> mapaDeAtributos = new HashMap<>();
		mapaDeAtributos.put(tagNoDocumento, conteudoDoTestoEsperado);
		return mapaDeAtributos;
	}
	
	private String buscarPalavraNoDocumento(XWPFHeader cabecalho, String conteudoDoTextoEsperado) {
		List<XWPFParagraph> paragrafos = cabecalho.getListParagraph();
		return paragrafos.stream().filter(paragrafo -> paragrafo.getText().equals(conteudoDoTextoEsperado)).findFirst()
				.get().getText();
	}

}
