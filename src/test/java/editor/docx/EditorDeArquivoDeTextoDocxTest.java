package editor.docx;

import builder.RelatorioDeTesteBuilder;
import editor.EditorDeArquivoDeTexto;
import editor.docx.rodape.AlinhamentoDaNotaDeRodape;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EditorDeArquivoDeTextoDocxTest {

    private static final String PATH_DO_ARQUIVO_DE_TEXTO_DOCX_PARA_TESTE = "src/test/resources/documentoDeTeste.docx";

    @Test
    public void deveSubstituirAsTagsNoDocxPelosValoresInformadosNoObjeto() throws Exception {
        File arquivoQueSeraEditado = new File(PATH_DO_ARQUIVO_DE_TEXTO_DOCX_PARA_TESTE);
        String extensaoEsperada = ".docx";
        String tituloDoRelatorio = "Teste de cabecalho";
        RelatorioDeTesteBuilder relatorioDeTeste = criarRelatorioDeTesteBuilderComTitulo(tituloDoRelatorio);
        String nomeDoArquivoDeSaida = "arquivoDocxEditado";

        File arquivoEditado = EditorDeArquivoDeTexto.editarArquivoDocx().editar(arquivoQueSeraEditado, relatorioDeTeste, nomeDoArquivoDeSaida);
        arquivoEditado.deleteOnExit();

        String extensaoDoEditalGerado = obterExtensaoDoArquivo(arquivoEditado);
        assertTrue(buscarPalavraNoDocumento(arquivoEditado, tituloDoRelatorio));
        assertEquals(extensaoEsperada, extensaoDoEditalGerado);
    }

    @Test(expected = ErroAoEditarArquivoDeTexto.class)
    public void deveInformarQuandoEncontrarUmErroAoEditarODocumento() throws Exception {
        File arquivoQueSeraEditado = new File("");
        RelatorioDeTesteBuilder relatorioDeTeste = new RelatorioDeTesteBuilder().criar();
        String nomeDoArquivoDeSaida = "arquivoDocxEditado";

        EditorDeArquivoDeTexto.editarArquivoDocx().editar(arquivoQueSeraEditado, relatorioDeTeste, nomeDoArquivoDeSaida);
    }

    @Test
    public void deveGerarOArquivoNoFormatoOutputStream() throws Exception {
        Path path = Paths.get(PATH_DO_ARQUIVO_DE_TEXTO_DOCX_PARA_TESTE);
        byte[] bytesDoTemplate = Files.readAllBytes(path);
        InputStream arquivoQueSeraEditado = new ByteArrayInputStream(bytesDoTemplate);
        String tituloDoRelatorio = "Teste";
        RelatorioDeTesteBuilder relatorioDeTeste = criarRelatorioDeTesteBuilderComTitulo(tituloDoRelatorio);

        OutputStream outputStream = EditorDeArquivoDeTexto.editarArquivoDocx().editar(arquivoQueSeraEditado, relatorioDeTeste);

        assertTrue(buscarPalavraNoDocumento(outputStream, tituloDoRelatorio));
    }

    @Test
    public void deveGerarOArquivoNoFormatoByteBuffer() throws Exception {
        Path path = Paths.get(PATH_DO_ARQUIVO_DE_TEXTO_DOCX_PARA_TESTE);
        byte[] bytesDoTemplate = Files.readAllBytes(path);
        ByteBuffer arquivoQueSeraEditado = ByteBuffer.wrap(bytesDoTemplate);
        String tituloDoRelatorio = "Teste";
        RelatorioDeTesteBuilder relatorioDeTeste = criarRelatorioDeTesteBuilderComTitulo(tituloDoRelatorio);

        ByteBuffer byteBuffer = EditorDeArquivoDeTexto.editarArquivoDocx().editar(arquivoQueSeraEditado, relatorioDeTeste);

        assertTrue(buscarPalavraNoDocumento(byteBuffer, tituloDoRelatorio));
    }

    @Test
    public void deveGerarODocumentoRecebendoUmMapaComOsAtributos() throws Exception {
        Path path = Paths.get(PATH_DO_ARQUIVO_DE_TEXTO_DOCX_PARA_TESTE);
        byte[] bytesDoTemplate = Files.readAllBytes(path);
        ByteBuffer arquivoQueSeraEditado = ByteBuffer.wrap(bytesDoTemplate);
        String tituloDoRelatorio = "Teste";
        Map<String, Object> dados = new HashMap<>();
        dados.put("cabecalhoDoRelatorio", "Relatório de teste");
        dados.put("tituloDoRelatorio", tituloDoRelatorio);
        dados.put("tituloDaTabela", "Tabela para teste");
        dados.put("rodapeDoRelatorio", "Rodapé para teste");

        ByteBuffer byteBuffer = EditorDeArquivoDeTexto.editarArquivoDocx().editar(arquivoQueSeraEditado, dados);

        assertTrue(buscarPalavraNoDocumento(byteBuffer, tituloDoRelatorio));
    }

    @Test
    public void deveAdicionarUmaNotaDeRodapeNoDocumento() throws Exception {
        Path path = Paths.get(PATH_DO_ARQUIVO_DE_TEXTO_DOCX_PARA_TESTE);
        byte[] bytesDoTemplate = Files.readAllBytes(path);
        ByteBuffer arquivoQueSeraEditado = ByteBuffer.wrap(bytesDoTemplate);
        RelatorioDeTesteBuilder relatorioDeTeste = new RelatorioDeTesteBuilder().criar();
        String notaDeRodape = "Gerado pelo editor.";

        ByteBuffer byteBuffer = EditorDeArquivoDeTexto.editarArquivoDocx().comNotaDeRodape(notaDeRodape).editar(arquivoQueSeraEditado, relatorioDeTeste);

        String textoAdicionadoNaNotaDeRodape = obterTextoDaNotaDeRodapeAdicionadoNoDocumento(byteBuffer);
        assertEquals(textoAdicionadoNaNotaDeRodape, notaDeRodape);
    }

    @Test
    public void deveAdicionarUmaNotaDeRodapeComAlinhamentoNoDocumento() throws Exception {
        Path path = Paths.get(PATH_DO_ARQUIVO_DE_TEXTO_DOCX_PARA_TESTE);
        byte[] bytesDoTemplate = Files.readAllBytes(path);
        ByteBuffer arquivoQueSeraEditado = ByteBuffer.wrap(bytesDoTemplate);
        RelatorioDeTesteBuilder relatorioDeTeste = new RelatorioDeTesteBuilder().criar();
        String notaDeRodape = "Gerado pelo editor de documento.";
        AlinhamentoDaNotaDeRodape alinhamento = AlinhamentoDaNotaDeRodape.DIREITA;

        ByteBuffer byteBuffer = EditorDeArquivoDeTexto.editarArquivoDocx().comNotaDeRodape(notaDeRodape, alinhamento).editar(arquivoQueSeraEditado, relatorioDeTeste);

        String textoAdicionadoNaNotaDeRodape = obterTextoDaNotaDeRodapeAdicionadoNoDocumento(byteBuffer);
        assertEquals(textoAdicionadoNaNotaDeRodape, notaDeRodape);
    }

    @Test
    public void deveAdicionarUmaNotaDeRodapeEmUmDocumentoSemRodape() throws Exception {
        Path path = Paths.get("src/test/resources/documentoSemRodapeDeTeste.docx");
        byte[] bytesDoTemplate = Files.readAllBytes(path);
        ByteBuffer arquivoQueSeraEditado = ByteBuffer.wrap(bytesDoTemplate);
        RelatorioDeTesteBuilder relatorioDeTeste = new RelatorioDeTesteBuilder().criar();
        String notaDeRodape = "Gerado pelo editor de documento.";

        ByteBuffer byteBuffer = EditorDeArquivoDeTexto.editarArquivoDocx().comNotaDeRodape(notaDeRodape).editar(arquivoQueSeraEditado, relatorioDeTeste);

        String textoAdicionadoNaNotaDeRodape = obterTextoDaNotaDeRodapeAdicionadoNoDocumento(byteBuffer);
        assertEquals(textoAdicionadoNaNotaDeRodape, notaDeRodape);
    }

    private String obterTextoDaNotaDeRodapeAdicionadoNoDocumento(ByteBuffer byteBuffer) throws IOException {
        XWPFDocument xwpfDocument = obterDocument(byteBuffer);
        List<XWPFParagraph> paragrafosDoRodape = xwpfDocument.getFooterList().get(0).getListParagraph();
        return paragrafosDoRodape.get(1).getText().replace("\n", "");
    }

    private RelatorioDeTesteBuilder criarRelatorioDeTesteBuilderComTitulo(String tituloDoRelatorio) {
        RelatorioDeTesteBuilder relatorioDeTeste = new RelatorioDeTesteBuilder().criar();
        relatorioDeTeste.informarTituloDoRelatorio(tituloDoRelatorio);
        return relatorioDeTeste;
    }

    private boolean buscarPalavraNoDocumento(File documento, String tituloDoRelatorio) throws Exception {
        XWPFDocument xwpfDocument = obterDocument(documento);
        return buscarPalavraNoDocumento(xwpfDocument, tituloDoRelatorio);
    }

    private boolean buscarPalavraNoDocumento(OutputStream documento, String tituloDoRelatorio) throws Exception {
        XWPFDocument xwpfDocument = obterDocument(documento);
        return buscarPalavraNoDocumento(xwpfDocument, tituloDoRelatorio);
    }

    private boolean buscarPalavraNoDocumento(ByteBuffer documento, String tituloDoRelatorio) throws Exception {
        XWPFDocument xwpfDocument = obterDocument(documento);
        return buscarPalavraNoDocumento(xwpfDocument, tituloDoRelatorio);
    }

    private XWPFDocument obterDocument(File documento) throws IOException {
        InputStream inputStream = new FileInputStream(documento);
        return new XWPFDocument(inputStream);
    }

    private XWPFDocument obterDocument(OutputStream documento) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(((ByteArrayOutputStream) documento).toByteArray());
        return new XWPFDocument(inputStream);
    }

    private XWPFDocument obterDocument(ByteBuffer documento) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(documento.array());
        return new XWPFDocument(inputStream);
    }

    private boolean buscarPalavraNoDocumento(XWPFDocument documentoDocx, String palavraASerProcurada) throws Exception {
        boolean palavraEncontrada = documentoDocx.getParagraphs().stream().anyMatch(paragrafo -> buscarPalavraNoParagrafo(paragrafo, palavraASerProcurada));
        documentoDocx.close();
        return palavraEncontrada;
    }

    private boolean buscarPalavraNoParagrafo(XWPFParagraph paragrafo, String palavraASerProcurada) {
        return paragrafo.getRuns().stream().anyMatch(linhaDoParagrafo -> buscarPalavraNaLinha(linhaDoParagrafo, palavraASerProcurada));
    }

    private boolean buscarPalavraNaLinha(XWPFRun linhaDoParagrafo, String palavraASerProcurada) {
        Integer posicaoInicialDaPalavra = 1;
        String palavraDaLinha = linhaDoParagrafo.getText(posicaoInicialDaPalavra);
        return palavraDaLinha.equals(palavraASerProcurada);
    }

    public static String obterExtensaoDoArquivo(File arquivo) {
        String nomeDoArquivo = arquivo.getName();
        String pontoFinal = ".";
        return nomeDoArquivo.lastIndexOf(pontoFinal) != -1 ? pontoFinal.concat(nomeDoArquivo.substring(nomeDoArquivo.lastIndexOf(pontoFinal) + 1)) : "";
    }
}
