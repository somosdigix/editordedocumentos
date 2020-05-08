package editor.docx;

import builder.RelatorioDeTesteBuilder;
import editor.EditorDeArquivoDeTexto;
import editor.docx.rodape.FormatacaoDaNotaDeRodape;
import editor.docx.tabela.FormatacaoDaTabela;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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
        FormatacaoDaNotaDeRodape formatacaoDaNotaDeRodape = new FormatacaoDaNotaDeRodape();

        ByteBuffer byteBuffer = EditorDeArquivoDeTexto.editarArquivoDocx().comNotaDeRodape(notaDeRodape, formatacaoDaNotaDeRodape).editar(arquivoQueSeraEditado, relatorioDeTeste);

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

    @Test
    public void deveGerarDocumentoComUmaTabela() throws Exception {
        byte[] bytesDoTemplate = Files.readAllBytes(Paths.get("src/test/resources/documentoComUmaTabelaTeste.docx"));
        ByteBuffer arquivoQueSeraEditado = ByteBuffer.wrap(bytesDoTemplate);
        int numeroDeColunasDaTabela = 3;
        int numeroDeRegistrosDaTabela = 2;
        Integer numeroDaTabela = 1;
        List<Map<String, Object>> dadosDaTabela = montarDadosDeTabelaComNumeroDeColunasERegistros(numeroDaTabela, numeroDeColunasDaTabela, numeroDeRegistrosDaTabela);
        Map<String, Object> mapaDeDadosDoDocumento = new HashMap<>();
        mapaDeDadosDoDocumento.put("cabecalhoDoRelatorio", "Tabelas");
        mapaDeDadosDoDocumento.put("TabelaTitulo", "Pessoas");
        List<Object> dadosDoDocumentoEsperado = new ArrayList<>(mapaDeDadosDoDocumento.values());
        List<Object> dadosDaTabelaEsperado = dadosDaTabela.stream().flatMap(mapa -> mapa.values().stream()).collect(Collectors.toList());
        dadosDoDocumentoEsperado.addAll(dadosDaTabelaEsperado);

        ByteBuffer byteBuffer = EditorDeArquivoDeTexto.editarArquivoDocx().docxComTabela(dadosDaTabela).editar(arquivoQueSeraEditado, mapaDeDadosDoDocumento);

        assertTrue(possuiPalavrasNoDocumento(byteBuffer, dadosDoDocumentoEsperado));
    }

    @Test
    public void deveGerarDocumentoComMaisDeUmaTabela() throws Exception {
        byte[] bytesDoTemplate = Files.readAllBytes(Paths.get("src/test/resources/documentoComTabelasTeste.docx"));
        ByteBuffer arquivoQueSeraEditado = ByteBuffer.wrap(bytesDoTemplate);
        int numeroDeColunasDaPrimeiraTabela = 4;
        int numeroDeRegistrosDaPrimeiraTabela = 3;
        int numeroDeColunasDaSegundaTabela = 3;
        int numeroDeRegistrosDaSegundaTabela = 2;
        Integer numeroDaPrimeiraTabela = 1;
        Integer numeroDaSegundaTabela = 2;
        List<Map<String, Object>> dadosDaPrimeiraTabela = montarDadosDeTabelaComNumeroDeColunasERegistros(numeroDaPrimeiraTabela, numeroDeColunasDaPrimeiraTabela, numeroDeRegistrosDaPrimeiraTabela);
        List<Map<String, Object>> dadosDaSegundaTabela = montarDadosDeTabelaComNumeroDeColunasERegistros(numeroDaSegundaTabela, numeroDeColunasDaSegundaTabela, numeroDeRegistrosDaSegundaTabela);
        List<List<Map<String, Object>>> dadosDasTabelas = Arrays.asList(dadosDaPrimeiraTabela, dadosDaSegundaTabela);
        Map<String, Object> mapaDeDadosDoDocumento = new HashMap<>();
        mapaDeDadosDoDocumento.put("cabecalhoDoRelatorio", "Tabelas");
        mapaDeDadosDoDocumento.put("PrimeiraTabelaTitulo", "Produtos");
        mapaDeDadosDoDocumento.put("SegundaTabelaTitulo", "Pessoas");
        List<Object> dadosDoDocumentoEsperado = new ArrayList<>(mapaDeDadosDoDocumento.values());
        List<Object> dadosDaPrimeiraTabelaEsperado = dadosDaPrimeiraTabela.stream().flatMap(mapa -> mapa.values().stream()).collect(Collectors.toList());
        List<Object> dadosDaSegundaTabelaEsperado = dadosDaSegundaTabela.stream().flatMap(mapa -> mapa.values().stream()).collect(Collectors.toList());
        dadosDoDocumentoEsperado.addAll(dadosDaPrimeiraTabelaEsperado);
        dadosDoDocumentoEsperado.addAll(dadosDaSegundaTabelaEsperado);
        List<FormatacaoDaTabela> formatacao = Collections.emptyList();

        ByteBuffer byteBuffer = EditorDeArquivoDeTexto.editarArquivoDocx().docxComTabelas(dadosDasTabelas, formatacao).editar(arquivoQueSeraEditado, mapaDeDadosDoDocumento);

        assertTrue(possuiPalavrasNoDocumento(byteBuffer, dadosDoDocumentoEsperado));
    }

    private List<Map<String, Object>> montarDadosDeTabelaComNumeroDeColunasERegistros(Integer numeroDaTabela,
                                                                                      Integer numeroDeColunas,
                                                                                      Integer numeroDeRegistros) {
        List<Map<String, Object>> dadosDaTabela = new ArrayList<>();
        for (int i = 0; i < numeroDeRegistros; i++) {
            Map<String, Object> dadoDaTabela = new LinkedHashMap<>();
            for (int j = 0; j < numeroDeColunas; j++) {
                dadoDaTabela.put("coluna" + j, "tab." + numeroDaTabela + " reg." + i + " col." + j);
            }
            dadosDaTabela.add(dadoDaTabela);
        }
        return dadosDaTabela;
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

    private boolean possuiPalavrasNoDocumento(ByteBuffer documento, List<Object> palavras) throws Exception {
        String textoDoDocumento = extrairTexto(documento);
        return palavras.stream()
                .map(palavra -> (String) palavra)
                .allMatch(textoDoDocumento::contains);
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

    public static String extrairTexto(ByteBuffer bytesDoDocx) throws IOException, InvalidFormatException {
        XWPFDocument xwpfDocument = converterBytesEmXWPFDocument(bytesDoDocx);
        return new XWPFWordExtractor(xwpfDocument).getText();
    }

    private static XWPFDocument converterBytesEmXWPFDocument(ByteBuffer bytesDoArquivoParaConcatenar) {
        InputStream inputStream = new ByteArrayInputStream(bytesDoArquivoParaConcatenar.array());
        OPCPackage opcPackage = null;
        try {
            opcPackage = OPCPackage.open(inputStream);
            XWPFDocument arquivo = new XWPFDocument(opcPackage);
            inputStream.close();
            return arquivo;
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu erro ao converter documento.", e);
        }
    }
}
