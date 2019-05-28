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

    private static final Integer TAMANHO_PADRAO = 10;

    private String notaParaAdicionar;
    private Optional<AlinhamentoDaNotaDeRodape> alinhamentoDaNotaDeRodape;

    private AdicionaNotaDeRodape(String notaParaAdicionar, Optional<AlinhamentoDaNotaDeRodape> alinhamentoDaNotaDeRodape) {
        this.notaParaAdicionar = notaParaAdicionar;
        this.alinhamentoDaNotaDeRodape = alinhamentoDaNotaDeRodape;
    }

    public static AdicionaNotaDeRodape comNotaDeRodape(String notaParaAdicionar, Optional<AlinhamentoDaNotaDeRodape> alinhamentoDaNotaDeRodape) {
        return new AdicionaNotaDeRodape(notaParaAdicionar, alinhamentoDaNotaDeRodape);
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
        }
    }

    private void configurarTamanhoDaFonteDaNota(XWPFRun linha) {
        if (this.alinhamentoDaNotaDeRodape.isPresent()) {
            linha.setFontSize(this.alinhamentoDaNotaDeRodape.get().getTamanhoDaFonte());
        } else {
            linha.setFontSize(TAMANHO_PADRAO);
        }
    }

    private void configurarAlinhamentoDaNota(XWPFParagraph paragrafo) {
        if (this.alinhamentoDaNotaDeRodape.isPresent()) {
            paragrafo.setAlignment(this.alinhamentoDaNotaDeRodape.get().getAlinhamento());
        }
    }
}
