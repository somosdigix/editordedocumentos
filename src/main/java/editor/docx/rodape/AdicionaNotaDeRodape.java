package editor.docx.rodape;

import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

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

    public void adicionarNotaNosRodapes(List<XWPFFooter> rodapesDoDocumento) {
        rodapesDoDocumento.forEach(rodape -> adicionarNotaRodape(rodape));
    }

    private void adicionarNotaRodape(XWPFFooter rodape) {
        if (rodape.getListParagraph() != null) {
            XWPFParagraph paragraph = rodape.createParagraph();
            configurarAlinhamentoDaNota(paragraph);
            XWPFRun run = paragraph.createRun();
            run.addBreak();
            run.setText(this.notaParaAdicionar);
        }
    }

    private void configurarAlinhamentoDaNota(XWPFParagraph paragraph) {
        if (this.alinhamentoDaNotaDeRodape.isPresent()) {
            paragraph.setAlignment(this.alinhamentoDaNotaDeRodape.get().getAlinhamento());
        }
    }
}
