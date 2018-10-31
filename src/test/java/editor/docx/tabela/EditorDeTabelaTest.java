package editor.docx.tabela;

import editor.docx.paragrafo.EditorDeParagrafo;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EditorDeTabelaTest {

	@Test
	public void deveEditarOConteudoDeUmTabela() throws Exception {
		String conteudoDoTestoEsperado = "João do Pé de Feijão";
		String atributoQueSeraAlteradoNoDocumento = "nomeDoFuncionario";
		Map<String, Object> mapaDeAtributos = montarMapaDeAtributos(atributoQueSeraAlteradoNoDocumento,
				conteudoDoTestoEsperado);

		EditorDeParagrafo editorDeParagrafo = EditorDeParagrafo.comMapaDeAtributos(mapaDeAtributos);
		XWPFDocument documentoDeTexto = new XWPFDocument();
		XWPFTable tabelaDoDocumentoDeTexto = documentoDeTexto.createTable();
		XWPFTableRow linhaDaTabelaDoDocumentoDeTexto = tabelaDoDocumentoDeTexto.getRow(0);
		XWPFRun linha = linhaDaTabelaDoDocumentoDeTexto.getCell(0).addParagraph().createRun();
		linha.setText(atributoQueSeraAlteradoNoDocumento);

		EditorDeTabela.comEditorDeParagrafo(editorDeParagrafo).editarConteudoDasTabelas(documentoDeTexto.getTables());
		String textoAdicionadoNoDocumento = buscarPalavraNoDocumento(documentoDeTexto, conteudoDoTestoEsperado);

		Assert.assertEquals(conteudoDoTestoEsperado, textoAdicionadoNoDocumento);
	}

	private Map<String, Object> montarMapaDeAtributos(String tagNoDocumento, String conteudoDoTestoEsperado) {
		Map<String, Object> mapaDeAtributos = new HashMap<>();
		mapaDeAtributos.put(tagNoDocumento, conteudoDoTestoEsperado);
		return mapaDeAtributos;
	}

	private String buscarPalavraNoDocumento(XWPFDocument documentoDeTexto, String conteudoDoTextoEsperado){
		List<XWPFTableRow> linhas = documentoDeTexto.getTables().stream().flatMap(tabela -> tabela.getRows().stream()).collect(Collectors.toList());
		List<XWPFParagraph> paragrafos = linhas.stream().flatMap(linha -> linha.getTableCells().stream().flatMap(celula -> celula.getParagraphs().stream())).collect(Collectors.toList());
		return paragrafos.stream().filter(paragrafo -> paragrafo.getText().equals(conteudoDoTextoEsperado)).findFirst().get().getText();
	}
}
