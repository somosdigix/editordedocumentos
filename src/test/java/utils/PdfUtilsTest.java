package utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PdfUtilsTest {

    private static final String PATH_RESOURCES_DOCUMENTOS_DE_TESTE = "src/test/resources/";
    private static final String EXTENSAO_DO_ARQUIVO_ESPERADA = "application/pdf";
    private File arquivoDeSaida = new File("");

    @Test
    public void deveConverterUmArquivoComExtensaoDocxParaPdf() throws Exception {
        Path pathDoArquivo = Paths.get(PATH_RESOURCES_DOCUMENTOS_DE_TESTE.concat("documentoDeTeste.docx"));
        File arquivoDocx = new File(pathDoArquivo.toString());

        String nomeDoArquivoDeSaida = "arquivoDeSaida";
        arquivoDeSaida = PdfUtils.converterDocxParaPdf(arquivoDocx, nomeDoArquivoDeSaida);
        String extensaoDoArquivoConvertido = obterExtensaoDoAquivo(arquivoDeSaida);

        assertNotNull(arquivoDeSaida);
        assertEquals(EXTENSAO_DO_ARQUIVO_ESPERADA, extensaoDoArquivoConvertido);
    }

    @Test(expected = ErroAoConverterDocxParaPdf.class)
    public void naoDeveConverterUmDocumentoVazio() throws Exception {
        File arquivoVazio = new File("");
        String nomeDoArquivo = "arquivoDeSaida";
        PdfUtils.converterDocxParaPdf(arquivoVazio, nomeDoArquivo);
    }

    @Test
    public void deveUnirVariosArquivosPdfEmUmUnicoArquivoComFiles() throws Exception {
        Path pathDoArquivo = Paths.get(PATH_RESOURCES_DOCUMENTOS_DE_TESTE.concat("documentoDeTeste.pdf"));
        File primeiroArquivoPDF = new File(pathDoArquivo.toString());
        File segundoArquivoPDF = new File(pathDoArquivo.toString());
        Integer quantidadeDePaginasEsperada = calcularQuantidadeDePaginasDeUmOuMaisDocumentos(primeiroArquivoPDF, segundoArquivoPDF);

        String nomeDoArquivoDeSaida = "arquivoDeSaida";
        arquivoDeSaida = PdfUtils.unirArquivosPdf(nomeDoArquivoDeSaida, primeiroArquivoPDF, segundoArquivoPDF);
        String extensaoDoArquivoConvertido = obterExtensaoDoAquivo(arquivoDeSaida);
        Integer quantidadeDePaginasDoArquivoDeSaida = calcularQuantidadeDePaginasDeUmOuMaisDocumentos(arquivoDeSaida);

        assertNotNull(arquivoDeSaida);
        assertEquals(quantidadeDePaginasEsperada, quantidadeDePaginasDoArquivoDeSaida);
        assertEquals(EXTENSAO_DO_ARQUIVO_ESPERADA, extensaoDoArquivoConvertido);
    }

    @Test
    public void deveUnirVariosArquivosPdfEmUmUnicoArquivoComByteArrays() throws Exception {
        Path pathDoArquivo = Paths.get(PATH_RESOURCES_DOCUMENTOS_DE_TESTE.concat("documentoDeTeste.pdf"));
        Integer quantidadeDePaginasEsperada = calcularQuantidadeDePaginasDeUmOuMaisDocumentos(new File(pathDoArquivo.toString()), new File(pathDoArquivo.toString()));
        byte[] primeiroArquivoPDF = Files.readAllBytes(pathDoArquivo);
        byte[] segundoArquivoPDF = Files.readAllBytes(pathDoArquivo);

        byte[] arquivoDeSaida = PdfUtils.unirArquivosPdf(primeiroArquivoPDF, segundoArquivoPDF);
        Integer quantidadeDePaginasDoArquivoDeSaida = calcularQuantidadeDePaginasDeUmOuMaisDocumentos(arquivoDeSaida);

        assertNotNull(arquivoDeSaida);
        assertEquals(quantidadeDePaginasEsperada, quantidadeDePaginasDoArquivoDeSaida);
    }

    @Test
    public void deveUnirVariosArquivosPdfEmUmUnicoArquivoComListaDeByteBuffers() throws Exception {
        Path pathDoArquivo = Paths.get(PATH_RESOURCES_DOCUMENTOS_DE_TESTE.concat("documentoDeTeste.pdf"));
        Integer quantidadeDePaginasEsperada = calcularQuantidadeDePaginasDeUmOuMaisDocumentos(new File(pathDoArquivo.toString()), new File(pathDoArquivo.toString()));
        ByteBuffer primeiroArquivoPDF = ByteBuffer.wrap(Files.readAllBytes(pathDoArquivo));
        ByteBuffer segundoArquivoPDF = ByteBuffer.wrap(Files.readAllBytes(pathDoArquivo));
        List<ByteBuffer> listaDeArquivosParaUnir = Arrays.asList(primeiroArquivoPDF, segundoArquivoPDF);

        ByteBuffer arquivoDeSaida = PdfUtils.unirArquivosPdf(listaDeArquivosParaUnir);
        Integer quantidadeDePaginasDoArquivoDeSaida = calcularQuantidadeDePaginasDeUmOuMaisDocumentos(arquivoDeSaida);

        assertNotNull(arquivoDeSaida);
        assertEquals(quantidadeDePaginasEsperada, quantidadeDePaginasDoArquivoDeSaida);
    }

    @Test
    public void deveUnirVariosArquivosPdfEmUmUnicoArquivoComByteBuffers() throws Exception {
        Path pathDoArquivo = Paths.get(PATH_RESOURCES_DOCUMENTOS_DE_TESTE.concat("documentoDeTeste.pdf"));
        Integer quantidadeDePaginasEsperada = calcularQuantidadeDePaginasDeUmOuMaisDocumentos(new File(pathDoArquivo.toString()), new File(pathDoArquivo.toString()));
        ByteBuffer primeiroArquivoPDF = ByteBuffer.wrap(Files.readAllBytes(pathDoArquivo));
        ByteBuffer segundoArquivoPDF = ByteBuffer.wrap(Files.readAllBytes(pathDoArquivo));

        ByteBuffer arquivoDeSaida = PdfUtils.unirArquivosPdf(primeiroArquivoPDF, segundoArquivoPDF);
        Integer quantidadeDePaginasDoArquivoDeSaida = calcularQuantidadeDePaginasDeUmOuMaisDocumentos(arquivoDeSaida);

        assertNotNull(arquivoDeSaida);
        assertEquals(quantidadeDePaginasEsperada, quantidadeDePaginasDoArquivoDeSaida);
    }

    private Integer calcularQuantidadeDePaginasDeUmOuMaisDocumentos(File... documentos) throws IOException {
        int quantidadeDePaginasDoArquivoDeSaida = 0;
        for (File documento : documentos) {
            byte[] bytesDoDocumento = Files.readAllBytes(documento.toPath());
            quantidadeDePaginasDoArquivoDeSaida += calcularQuantidadeDePaginasDeUmDocumento(bytesDoDocumento);
        }
        return quantidadeDePaginasDoArquivoDeSaida;
    }

    private static int calcularQuantidadeDePaginasDeUmDocumento(byte[] bytesDoDocumento) throws IOException {
        PDDocument leitorDePaginas = PDDocument.load(bytesDoDocumento);
        int quantidadeDePaginasDoArquivoDeSaida = leitorDePaginas.getNumberOfPages();
        leitorDePaginas.close();
        return quantidadeDePaginasDoArquivoDeSaida;
    }

    private Integer calcularQuantidadeDePaginasDeUmOuMaisDocumentos(byte[]... documentos) throws IOException {
        int quantidadeDePaginasDoArquivoDeSaida = 0;
        for (byte[] bytesDoDocumento : documentos) {
            quantidadeDePaginasDoArquivoDeSaida += calcularQuantidadeDePaginasDeUmDocumento(bytesDoDocumento);
        }
        return quantidadeDePaginasDoArquivoDeSaida;
    }

    private Integer calcularQuantidadeDePaginasDeUmOuMaisDocumentos(ByteBuffer... documentos) throws IOException {
        int quantidadeDePaginasDoArquivoDeSaida = 0;
        for (ByteBuffer documento : documentos) {
            quantidadeDePaginasDoArquivoDeSaida += calcularQuantidadeDePaginasDeUmDocumento(documento.array());
        }
        return quantidadeDePaginasDoArquivoDeSaida;
    }

    private String obterExtensaoDoAquivo(File arquivoDeSaida) {
        return URLConnection.guessContentTypeFromName(arquivoDeSaida.getName());
    }

    @After
    public void end() {
        arquivoDeSaida.delete();
    }
}
