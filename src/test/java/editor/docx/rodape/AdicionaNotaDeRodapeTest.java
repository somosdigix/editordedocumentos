package editor.docx.rodape;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class AdicionaNotaDeRodapeTest {

    @Test
    public void deveAdicionarUmaNotaDeRodaPeNoDocumento() throws IOException {
        String notaParaAdicionar = "Gerado pelo editor de documentos.";
        AdicionaNotaDeRodape adicionaNotaDeRodape = AdicionaNotaDeRodape.comNotaDeRodape(notaParaAdicionar, Optional.empty());
        XWPFDocument documento = new XWPFDocument();
        XWPFFooter rodape = documento.createHeaderFooterPolicy().createFooter(XWPFHeaderFooterPolicy.DEFAULT);

        adicionaNotaDeRodape.adicionarNotaNosRodapes(Arrays.asList(rodape));
        String textoAdicionadoNoDocumento = buscarTextoDaNotaAdicionandaNoRodape(rodape);

        assertEquals(notaParaAdicionar, textoAdicionadoNoDocumento);
    }

    @Test
    public void deveAdicionarUmaNotaDeRodaPeNoDocumentoConfigurandoOAlinhamento() throws IOException {
        String notaParaAdicionar = "Gerado pelo editor de documentos.";
        Optional<AlinhamentoDaNotaDeRodape> alinhamentoDaNotaDeRodape = Optional.of(AlinhamentoDaNotaDeRodape.DIREITA);
        AdicionaNotaDeRodape adicionaNotaDeRodape = AdicionaNotaDeRodape.comNotaDeRodape(notaParaAdicionar, alinhamentoDaNotaDeRodape);
        XWPFDocument documento = new XWPFDocument();
        XWPFFooter rodape = documento.createHeaderFooterPolicy().createFooter(XWPFHeaderFooterPolicy.DEFAULT);

        adicionaNotaDeRodape.adicionarNotaNosRodapes(Arrays.asList(rodape));
        ParagraphAlignment alinhamentoDaNotaNoTexto = buscarAlinhamentoDoTextoDaNotaDeRodape(rodape);
        String textoAdicionadoNoDocumento = buscarTextoDaNotaAdicionandaNoRodape(rodape);

        assertEquals(notaParaAdicionar, textoAdicionadoNoDocumento);
        assertEquals(alinhamentoDaNotaDeRodape.get().getAlinhamento(), alinhamentoDaNotaNoTexto);
    }

    private String buscarTextoDaNotaAdicionandaNoRodape(XWPFFooter rodape) {
        return rodape.getListParagraph().get(1).getText().replace("\n", "");
    }

    private ParagraphAlignment buscarAlinhamentoDoTextoDaNotaDeRodape(XWPFFooter rodape) {
        return rodape.getListParagraph().get(1).getAlignment();
    }
}
