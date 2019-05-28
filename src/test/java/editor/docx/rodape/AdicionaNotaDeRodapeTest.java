package editor.docx.rodape;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class AdicionaNotaDeRodapeTest {

    private XWPFDocument documento;

    @Before
    public void setup() throws IOException {
        Path path = Paths.get("src/test/resources/documentoDeTeste.docx");
        byte[] bytesDoTemplate = Files.readAllBytes(path);
        ByteBuffer arquivoQueSeraEditado = ByteBuffer.wrap(bytesDoTemplate);
        InputStream inputStream = new ByteArrayInputStream(arquivoQueSeraEditado.array());
        documento = new XWPFDocument(inputStream);
    }

    @Test
    public void deveAdicionarUmaNotaDeRodaPeNoDocumento() throws Exception {
        String notaParaAdicionar = "Gerado pelo editor de documentos.";
        AdicionaNotaDeRodape adicionaNotaDeRodape = AdicionaNotaDeRodape.comNotaDeRodape(notaParaAdicionar, Optional.empty());

        adicionaNotaDeRodape.adicionarNotaNosRodapes(documento);

        String textoAdicionadoNoDocumento = buscarTextoDaNotaAdicionandaNoRodape(documento.getFooterList().get(0));
        assertEquals(notaParaAdicionar, textoAdicionadoNoDocumento);
    }

    @Test
    public void deveAdicionarUmaNotaDeRodaPeNoDocumentoConfigurandoOAlinhamento() throws Exception {
        String notaParaAdicionar = "Gerado pelo editor de documentos.";
        Optional<AlinhamentoDaNotaDeRodape> alinhamentoDaNotaDeRodape = Optional.of(AlinhamentoDaNotaDeRodape.DIREITA);
        AdicionaNotaDeRodape adicionaNotaDeRodape = AdicionaNotaDeRodape.comNotaDeRodape(notaParaAdicionar, alinhamentoDaNotaDeRodape);

        adicionaNotaDeRodape.adicionarNotaNosRodapes(documento);

        ParagraphAlignment alinhamentoDaNotaNoTexto = buscarAlinhamentoDoTextoDaNotaDeRodape(documento.getFooterList().get(0));
        String textoAdicionadoNoDocumento = buscarTextoDaNotaAdicionandaNoRodape(documento.getFooterList().get(0));
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
