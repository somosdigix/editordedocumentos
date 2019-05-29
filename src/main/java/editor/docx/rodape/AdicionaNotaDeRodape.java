package editor.docx.rodape;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AdicionaNotaDeRodape {

    private String notaParaAdicionar;
    private Optional<FormatacaoDaNotaDeRodape> formatacaoDaNotaDeRodape;

    private AdicionaNotaDeRodape(String notaParaAdicionar, Optional<FormatacaoDaNotaDeRodape> formatacaoDaNotaDeRodape) {
        this.notaParaAdicionar = notaParaAdicionar;
        this.formatacaoDaNotaDeRodape = formatacaoDaNotaDeRodape;
    }

    public static AdicionaNotaDeRodape comNotaDeRodape(String notaParaAdicionar, Optional<FormatacaoDaNotaDeRodape> formatacaoDaNotaDeRodape) {
        return new AdicionaNotaDeRodape(notaParaAdicionar, formatacaoDaNotaDeRodape);
    }

    public void adicionarNotaNosRodapes(XWPFDocument documentoDocx) throws IOException {
        List<XWPFFooter> rodapesDoDocumento = obterRodapesDoDocumento(documentoDocx);
        rodapesDoDocumento.forEach(rodape -> adicionarNotaRodape(rodape));
    }

    private List<XWPFFooter> obterRodapesDoDocumento(XWPFDocument documentoDocx) throws IOException {
        List<XWPFFooter> rodapesDoDocumento = documentoDocx.getFooterList();
        if (rodapesDoDocumento.isEmpty()) {
            rodapesDoDocumento = Collections.singletonList(documentoDocx.createHeaderFooterPolicy().createFooter(XWPFHeaderFooterPolicy.DEFAULT));
        }
        return rodapesDoDocumento;
    }

    private void adicionarNotaRodape(XWPFFooter rodape) {
        if (rodape.getListParagraph() != null) {
            XWPFParagraph paragrafo = rodape.createParagraph();
            configurarAlinhamentoDaNota(paragrafo);
            XWPFRun linha = paragrafo.createRun();
            linha.addBreak();
            linha.setText(this.notaParaAdicionar);
            configurarTamanhoDaFonteDaNota(linha);
            configurarFonteDaNota(linha);
        }
    }

    private void configurarAlinhamentoDaNota(XWPFParagraph paragrafo) {
        if (this.formatacaoDaNotaDeRodape.isPresent()) {
            paragrafo.setAlignment(this.formatacaoDaNotaDeRodape.get().getAlinhamento());
        }else{
            paragrafo.setAlignment(FormatacaoDaNotaDeRodape.getAlinhamentoPadrao());
        }
    }

    private void configurarTamanhoDaFonteDaNota(XWPFRun linha) {
        if (this.formatacaoDaNotaDeRodape.isPresent()) {
            linha.setFontSize(this.formatacaoDaNotaDeRodape.get().getTamanhoDaFonte());
        } else {
            linha.setFontSize(FormatacaoDaNotaDeRodape.getTamanhoDaFontePadrao());
        }
    }

    private void configurarFonteDaNota(XWPFRun linha) {
        if (this.formatacaoDaNotaDeRodape.isPresent()) {
            linha.setFontFamily(this.formatacaoDaNotaDeRodape.get().getFonte());
        } else {
            linha.setFontFamily(FormatacaoDaNotaDeRodape.getFontePadrao());
        }
    }
}
