package editor.docx.cabecalho;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFHeader;

import editor.docx.paragrafo.EditorDeParagrafo;
import editor.docx.tabela.EditorDeTabela;

public class EditorDeCabecalho {

	private final EditorDeParagrafo editorDeParagrafo;
	private final EditorDeTabela editorDeTabela;

	private EditorDeCabecalho(EditorDeParagrafo editorDeParagrafo, EditorDeTabela editorDeTabela) {
		this.editorDeParagrafo = editorDeParagrafo;
		this.editorDeTabela = editorDeTabela;
	}

	public static EditorDeCabecalho comEditorDeParagrafoETabela(EditorDeParagrafo editorDeParagrafo,
			EditorDeTabela editorDeTabela) {
		return new EditorDeCabecalho(editorDeParagrafo, editorDeTabela);
	}

	public void editarConteudoDosCabecalhosDoDocumento(List<XWPFHeader> cabecalhos) {
		cabecalhos.forEach(cabecalhoDoDocumento -> editarCabecalho(cabecalhoDoDocumento));		
	}
	
	private void editarCabecalho(XWPFHeader cabecalhoDoDocumento) {
		editorDeTabela.editarConteudoDasTabelas(cabecalhoDoDocumento.getTables());
		if (cabecalhoDoDocumento.getListParagraph() != null) {
			cabecalhoDoDocumento.getListParagraph().forEach(paragrafoDoCabecalho -> editorDeParagrafo.editarParagrafosDoDocumento(paragrafoDoCabecalho));
		}
	}

}
