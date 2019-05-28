package editor.docx.rodape;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

public class AdicionaNotaDeRodape {

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
            XWPFParagraph paragraph = rodape.createParagraph();
            configurarAlinhamentoDaNota(paragraph);
            XWPFRun run = paragraph.createRun();
            run.addBreak();
            run.setText(this.notaParaAdicionar);
            configurarTamanhoDaNota(run);
        }
    }

    private void configurarTamanhoDaNota(XWPFRun run) {
        if (this.alinhamentoDaNotaDeRodape.isPresent()) {
            run.setFontSize(this.alinhamentoDaNotaDeRodape.get().getTamanhoDaFonte());
        } else {
            run.setFontSize(10);
        }
    }

    private void configurarAlinhamentoDaNota(XWPFParagraph paragraph) {
        if (this.alinhamentoDaNotaDeRodape.isPresent()) {
            paragraph.setAlignment(this.alinhamentoDaNotaDeRodape.get().getAlinhamento());
        }
    }
}
