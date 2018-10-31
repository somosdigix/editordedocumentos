package editor.docx.tabela;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.junit.Assert;
import org.junit.Test;

import builder.CelulaBuilder;
import builder.LinhaBuilder;
import builder.TabelaBuilder;

public class AdicionaLinhaNaTabelaDoDocumentoDeTextoTest {

    @Test
    public void deveAdicionarAsLinhasEmUmaTabelaQuePossuaApenasOCabecalho() {
        String conteudoDaTabela = "conteúdo da tabela";
        Tabela tabela = montarTabela(conteudoDaTabela);

        AdicionaLinhaNaTabelaDoDocumentoDeTexto adicionaLinhasNaTabelaDoDocumentoDeTexto = AdicionaLinhaNaTabelaDoDocumentoDeTexto.comTabelas(Arrays.asList(tabela));
        XWPFDocument documentoDeTexto = new XWPFDocument();
        XWPFTable tabelaDoDocumentoDeTexto = documentoDeTexto.createTable();

        adicionaLinhasNaTabelaDoDocumentoDeTexto.adicionarConteudoNasTabelasDoDocumentoDeTexto(Arrays.asList(tabelaDoDocumentoDeTexto));
        Boolean conteudoFoiAdicionadoNaTabela= exiteOConteudoNaTabela(documentoDeTexto, conteudoDaTabela);

        Assert.assertTrue(conteudoFoiAdicionadoNaTabela);
    }

    @Test
    public void NaodeveAdicionarLinhasAUmaTabelaQuePossuaConteudoAlemDoCabecalho() {
        String conteudoDaTabela = "conteúdo da tabela";
        Tabela tabela = montarTabela(conteudoDaTabela);

        AdicionaLinhaNaTabelaDoDocumentoDeTexto adicionaLinhasNaTabelaDoDocumentoDeTexto = AdicionaLinhaNaTabelaDoDocumentoDeTexto.comTabelas(Arrays.asList(tabela));
        XWPFDocument documentoDeTexto = new XWPFDocument();
        XWPFTable tabelaDoDocumentoDeTexto = documentoDeTexto.createTable();
        tabelaDoDocumentoDeTexto.createRow();

        adicionaLinhasNaTabelaDoDocumentoDeTexto.adicionarConteudoNasTabelasDoDocumentoDeTexto(Arrays.asList(tabelaDoDocumentoDeTexto));
        Boolean conteudoFoiAdicionadoNaTabela= exiteOConteudoNaTabela(documentoDeTexto, conteudoDaTabela);

        Assert.assertFalse(conteudoFoiAdicionadoNaTabela);
    }

    private Tabela montarTabela(String conteudoDoTestoEsperado) {
        Celula celula = new CelulaBuilder().comValorDaCelula(conteudoDoTestoEsperado).criar();
        Linha linha = new LinhaBuilder().comCelulas(celula).criar();
        Tabela tabela = new TabelaBuilder().comLinhas(linha).criar();
        return tabela;
    }

    private Boolean exiteOConteudoNaTabela(XWPFDocument documentoDeTexto, String conteudoDoTextoEsperado) {
        List<XWPFTableRow> linhas = obterLinhasDaTabela(documentoDeTexto);
        List<XWPFParagraph> paragrafos = obterParagrafosDasLinhas(linhas);
        return paragrafos.stream().anyMatch(paragrafo -> paragrafo.getText().equals(conteudoDoTextoEsperado));
    }

    private List<XWPFTableRow> obterLinhasDaTabela(XWPFDocument documentoDeTexto) {
        return documentoDeTexto.getTables().stream().flatMap(tabela -> tabela.getRows().stream()).collect(Collectors.toList());
    }

    private List<XWPFParagraph> obterParagrafosDasLinhas(List<XWPFTableRow> linhas) {
        return linhas.stream().flatMap(linha -> linha.getTableCells().stream().flatMap(celula -> celula.getParagraphs().stream()))
                .collect(Collectors.toList());
    }
}
