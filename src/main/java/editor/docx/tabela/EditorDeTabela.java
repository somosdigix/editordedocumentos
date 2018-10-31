package editor.docx.tabela;

import editor.docx.paragrafo.EditorDeParagrafo;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.List;

public class EditorDeTabela {

    private final EditorDeParagrafo editorDeParagrafo;

    private EditorDeTabela(EditorDeParagrafo editorDeParagrafo) {
        this.editorDeParagrafo = editorDeParagrafo;
    }

    public static EditorDeTabela comEditorDeParagrafo(EditorDeParagrafo editorDeParagrafo) {
        return new EditorDeTabela(editorDeParagrafo);
    }

    public EditorDeTabela editarConteudoDasTabelas(List<XWPFTable> tabelas) {
        if (tabelas != null) {
            tabelas.forEach(tabela -> editarTabela(tabela));
        }
        return this;
    }

    private void editarTabela(XWPFTable tabela) {
        if (tabela.getRows() != null) {
            tabela.getRows().forEach(linhaDaTabela -> editarLinhaDaTabela(linhaDaTabela));
        }
    }

    private void editarLinhaDaTabela(XWPFTableRow linhaDaTabela) {
        if (linhaDaTabela.getTableCells() != null) {
            linhaDaTabela.getTableCells()
                    .forEach(celulaDaLinhaDaTabela -> editarCelulaDaLinhaDaTabela(celulaDaLinhaDaTabela));
        }
    }

    private void editarCelulaDaLinhaDaTabela(XWPFTableCell celulaDaLinhaDaTabela) {
        celulaDaLinhaDaTabela.getParagraphs().forEach(paragrafo -> editarParagrafo(paragrafo));
        if (celulaDaLinhaDaTabela.getTables() != null) {
            celulaDaLinhaDaTabela.getTables().forEach(tabela -> editarTabela(tabela));
        }
    }

    private void editarParagrafo(XWPFParagraph paragrafo) {
        this.editorDeParagrafo.editarParagrafosDoDocumento(paragrafo);
    }
}
