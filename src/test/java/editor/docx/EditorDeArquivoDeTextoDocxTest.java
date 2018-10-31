package editor.docx;

import builder.RelatorioDeTesteBuilder;
import editor.EditorDeArquivoDeTexto;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EditorDeArquivoDeTextoDocxTest {

    private static final String NOME_DO_ARQUIVO_DE_TEXTO_DOCX_PARA_TESTE = "documentoDeTeste.docx";
    private static final String PATH_DO_ARQUIVO_DE_TEXTO_DOCX_PARA_TESTE = "src/test/resources/" + NOME_DO_ARQUIVO_DE_TEXTO_DOCX_PARA_TESTE;
    private static final String EXTENSAO_DO_ARQUIVO_ESPERADA = ".docx";
    private static final String NOME_DO_ARQUIVO_DE_SAIDA = "arquivoDocxEditado";
    private static final Integer UM = 1;
    private static final String PONTO = ".";

    @Test
    public void deveSubstituirAsTagsNoDocxPelosValoresInformadosNoObjeto() throws Exception {
        File arquivoQueSeraEditado = new File(PATH_DO_ARQUIVO_DE_TEXTO_DOCX_PARA_TESTE);
        String tituloDoRelatorio = "Teste de cabecalho";
        RelatorioDeTesteBuilder relatorioDeTeste = criarRelatorioDeTesteBuilderComTitulo(tituloDoRelatorio);

        File arquivoEditado = EditorDeArquivoDeTexto.editarArquivoDocx().editar(arquivoQueSeraEditado, relatorioDeTeste, NOME_DO_ARQUIVO_DE_SAIDA);
        arquivoEditado.deleteOnExit();

        String extensaoDoEditalGerado = obterExtensaoDoArquivo(arquivoEditado);
        assertTrue(buscarPalavraNoDocumento(arquivoEditado, tituloDoRelatorio));
        assertEquals(EXTENSAO_DO_ARQUIVO_ESPERADA, extensaoDoEditalGerado);
    }

    @Test(expected = ErroAoEditarArquivoDeTexto.class)
    public void deveInformarQuandoEncontrarUmErroAoEditarODocumento() throws Exception {
        File arquivoQueSeraEditado = new File("");
        RelatorioDeTesteBuilder relatorioDeTeste = new RelatorioDeTesteBuilder().criar();
        EditorDeArquivoDeTexto.editarArquivoDocx().editar(arquivoQueSeraEditado, relatorioDeTeste, NOME_DO_ARQUIVO_DE_SAIDA);
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

    private RelatorioDeTesteBuilder criarRelatorioDeTesteBuilderComTitulo(String tituloDoRelatorio) {
        RelatorioDeTesteBuilder relatorioDeTeste = new RelatorioDeTesteBuilder().criar();
        relatorioDeTeste.informarTituloDoRelatorio(tituloDoRelatorio);
        return relatorioDeTeste;
    }

    private boolean buscarPalavraNoDocumento(File documento, String tituloDoRelatorio) throws Exception {
        InputStream inputStream = new FileInputStream(documento);
        return buscarPalavraNoDocumento(inputStream, tituloDoRelatorio);
    }

    private boolean buscarPalavraNoDocumento(OutputStream documento, String tituloDoRelatorio) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(((ByteArrayOutputStream) documento).toByteArray());
        return buscarPalavraNoDocumento(inputStream, tituloDoRelatorio);
    }

    private boolean buscarPalavraNoDocumento(ByteBuffer documento, String tituloDoRelatorio) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(documento.array());
        return buscarPalavraNoDocumento(inputStream, tituloDoRelatorio);
    }

    private boolean buscarPalavraNoDocumento(InputStream arquivoEditado, String palavraASerProcurada) throws Exception {
        XWPFDocument documentoDocx = new XWPFDocument(arquivoEditado);
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
        return nomeDoArquivo.lastIndexOf(PONTO) != -1 ? PONTO.concat(nomeDoArquivo.substring(nomeDoArquivo.lastIndexOf(PONTO) + UM)) : "";
    }
}
