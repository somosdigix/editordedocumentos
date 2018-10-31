package editor.docx.rodape;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import editor.docx.paragrafo.EditorDeParagrafo;
import editor.docx.tabela.EditorDeTabela;

public class EditorDeRodape {

    private final EditorDeParagrafo editorDeParagrafo;
    private final EditorDeTabela editorDeTabela;

    private EditorDeRodape(EditorDeParagrafo editorDeParagrafo, EditorDeTabela editorDeTabela) {
        this.editorDeParagrafo = editorDeParagrafo;
        this.editorDeTabela = editorDeTabela;
    }

    public static EditorDeRodape comEditorDeParagrafoETabela(EditorDeParagrafo editorDeParagrafo,
                                                             EditorDeTabela editorDeTabela) {
        return new EditorDeRodape(editorDeParagrafo, editorDeTabela);
    }

    public EditorDeRodape editarConteudoDosRodapesDoDocumento(List<XWPFFooter> rodapesDoDocumento) {
        rodapesDoDocumento.forEach(rodapeDoDocumento -> editarRodape(rodapeDoDocumento));
        return this;
    }

    private void editarRodape(XWPFFooter rodape) {
        editarConteudoDasTabelasDoRodape(rodape.getTables());
        if (rodape.getListParagraph() != null) {
            rodape.getListParagraph().forEach(paragrafo -> editarParagrafo(paragrafo));
        }
    }

    private void editarConteudoDasTabelasDoRodape(List<XWPFTable> tabelasDoRodape) {
        if (tabelasDoRodape != null) {
            editorDeTabela.editarConteudoDasTabelas(tabelasDoRodape);
        }
    }

    private void editarParagrafo(XWPFParagraph paragrafo) {
        editorDeParagrafo.editarParagrafosDoDocumento(paragrafo);
    }
}
