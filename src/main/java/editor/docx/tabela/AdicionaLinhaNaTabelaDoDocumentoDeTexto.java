package editor.docx.tabela;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class AdicionaLinhaNaTabelaDoDocumentoDeTexto {

    private static final Integer PRIMEIRA_POSICAO = 0;
    private static final Integer UM = 1;

    private final List<Tabela> tabelasParaDocumentoDeTextos;
    private final List<FormatacaoDaTabela> formatacoesDasTabelas;

    private AdicionaLinhaNaTabelaDoDocumentoDeTexto(List<Tabela> tabelasParaDocumentoDeTextos, List<FormatacaoDaTabela> formatacaoDasTabelas) {
        this.tabelasParaDocumentoDeTextos = tabelasParaDocumentoDeTextos;
        this.formatacoesDasTabelas = formatacaoDasTabelas;
    }

    public static AdicionaLinhaNaTabelaDoDocumentoDeTexto comTabelas(List<Tabela> tabelasParaDocumentoDeTextos, FormatacaoDaTabela... formatacaoDaTabela) {
        return new AdicionaLinhaNaTabelaDoDocumentoDeTexto(tabelasParaDocumentoDeTextos, Arrays.asList(formatacaoDaTabela));
    }

    public static AdicionaLinhaNaTabelaDoDocumentoDeTexto comTabelas(List<Tabela> tabelasParaDocumentoDeTextos, List<FormatacaoDaTabela> formatacaoDaTabela) {
        return new AdicionaLinhaNaTabelaDoDocumentoDeTexto(tabelasParaDocumentoDeTextos, formatacaoDaTabela);
    }

    public static AdicionaLinhaNaTabelaDoDocumentoDeTexto comTabelas(Tabela tabelasParaDocumentoDeTextos, FormatacaoDaTabela... formatacaoDaTabela) {
        return new AdicionaLinhaNaTabelaDoDocumentoDeTexto(Collections.singletonList(tabelasParaDocumentoDeTextos), Arrays.asList(formatacaoDaTabela));
    }

    public AdicionaLinhaNaTabelaDoDocumentoDeTexto adicionarConteudoNasTabelasDoDocumentoDeTexto(List<XWPFTable> xwpfTables) {
        List<XWPFTable> xwpfTablesDocument = xwpfTables.stream().filter(xwpfTable -> tabelaDoDocumentoPossuiApenasOCabecalho(xwpfTable)).collect(Collectors.toList());
        if (!xwpfTablesDocument.isEmpty()) {
            for (Tabela tabelaParaDocumentoDeTexto : tabelasParaDocumentoDeTextos) {
                Integer posicaoDaTabelaNoDocumento = tabelasParaDocumentoDeTextos.indexOf(tabelaParaDocumentoDeTexto);
                Optional<FormatacaoDaTabela> formatacaoDaTabela = !formatacoesDasTabelas.isEmpty() ? obterFormatacaoDaTabela(posicaoDaTabelaNoDocumento) : Optional.empty();
                editarTabelaDoDocumento(xwpfTablesDocument.get(posicaoDaTabelaNoDocumento), tabelasParaDocumentoDeTextos.get(posicaoDaTabelaNoDocumento), formatacaoDaTabela);
            }
        }
        return this;
    }

    private boolean tabelaDoDocumentoPossuiApenasOCabecalho(XWPFTable xwpfTable) {
        return xwpfTable.getRows().size() == UM;
    }

    private Optional<FormatacaoDaTabela> obterFormatacaoDaTabela(Integer posicao) {
        return formatacoesDasTabelas.size() >= UM ? Optional.of(formatacoesDasTabelas.get(posicao)) : Optional.of(formatacoesDasTabelas.get(PRIMEIRA_POSICAO));
    }

    private void editarTabelaDoDocumento(XWPFTable xwpfTable, Tabela tabelaParaDocumentoDeTexto, Optional<FormatacaoDaTabela> formatacaoDaTabela) {
        if (tabelaParaDocumentoDeTexto != null) {
            adicionarLinhasNaTabela(xwpfTable, tabelaParaDocumentoDeTexto.getLinhas(), formatacaoDaTabela);
        }
    }

    private void adicionarLinhasNaTabela(XWPFTable xwpfTable, List<Linha> linhasDaTabela, Optional<FormatacaoDaTabela> formatacaoDaTabela) {
        linhasDaTabela.forEach(linhaDaTabela -> adicionarLinhaNaTabela(xwpfTable, linhasDaTabela, linhaDaTabela, formatacaoDaTabela));
    }

    private void adicionarLinhaNaTabela(XWPFTable xwpfTable, List<Linha> linhasDaTabela, Linha linhaDaTabela, Optional<FormatacaoDaTabela> formatacaoDaTabela) {
        xwpfTable.createRow();
        Integer posicaoDaLinhaDaTabela = linhasDaTabela.indexOf(linhaDaTabela) + UM;
        XWPFTableRow xwpfTableRow = xwpfTable.getRow(posicaoDaLinhaDaTabela);
        adicionarCelulasNaLinhaDaTabela(linhaDaTabela, xwpfTableRow, formatacaoDaTabela);
    }

    private void adicionarCelulasNaLinhaDaTabela(Linha linhaDaTabela, XWPFTableRow xwpfTableRow, Optional<FormatacaoDaTabela> formatacaoDaTabela) {
        List<Celula> celulasDaLinha = linhaDaTabela.getCelulas();
        celulasDaLinha.forEach(celulaDaLinha -> adicionarCelulaNaLinhaDaTabela(xwpfTableRow, celulasDaLinha, celulaDaLinha, formatacaoDaTabela));
    }

    private void adicionarCelulaNaLinhaDaTabela(XWPFTableRow xwpfTableRow, List<Celula> celulasDaLinha, Celula celulaDaLinha, Optional<FormatacaoDaTabela> formatacaoDaTabela) {
        Integer posicaoDaCelulaDaTabela = celulasDaLinha.indexOf(celulaDaLinha);
        XWPFTableCell cell = xwpfTableRow.getCell(posicaoDaCelulaDaTabela.intValue());
        if (cell != null) {
            XWPFRun xwpfRun = cell.addParagraph().createRun();
            xwpfRun.setFontFamily(this.definirFonteDaCelulaNaLinhaDaTabela(formatacaoDaTabela));
            xwpfRun.setFontSize(this.definirTamanhoDaFonteDaCelulaNaLinhaDaTabela(formatacaoDaTabela).intValue());
            xwpfRun.setText(celulaDaLinha.getValorDaCelula());
        }
    }

    private String definirFonteDaCelulaNaLinhaDaTabela(Optional<FormatacaoDaTabela> formatacaoDaTabela) {
        return formatacaoDaTabela.isPresent() ? formatacaoDaTabela.get().getFonte() : FormatacaoDaTabela.getFontePadrao();
    }

    private Integer definirTamanhoDaFonteDaCelulaNaLinhaDaTabela(Optional<FormatacaoDaTabela> formatacaoDaTabela) {
        return formatacaoDaTabela.isPresent() ? formatacaoDaTabela.get().getTamanhoDaFonte() : FormatacaoDaTabela.getTamanhoDaFontePadrao();
    }
}
