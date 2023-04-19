package utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PdfUtilsTest {

    private static final String PATH_RESOURCES_DOCUMENTOS_DE_TESTE = "src/test/resources/";
    private static final String EXTENSAO_DO_ARQUIVO_ESPERADA = "application/pdf";
    private File arquivoDeSaida = new File("");

    @Test
    public void deveConverterUmArquivoComExtensaoDocxParaPdf() throws Exception {
        Path pathDoRelatorio = Paths.get(PATH_RESOURCES_DOCUMENTOS_DE_TESTE.concat("documentoDeTeste.docx"));
        File arquivoDocx = new File(pathDoRelatorio.toString());

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
    public void deveUnirVariosArquivosPdfEmUmUnicoRelatorio() throws Exception {
        Path pathDoRelatorio = Paths.get(PATH_RESOURCES_DOCUMENTOS_DE_TESTE.concat("documentoDeTeste.pdf"));
        File primeiroArquivoPDF = new File(pathDoRelatorio.toString());
        File segundoArquivoPDF = new File(pathDoRelatorio.toString());
        Integer quantidadeDePaginasEsperada = calcularQuantidadeDePaginasDeUmOuMaisDocumentos(primeiroArquivoPDF,segundoArquivoPDF);

        String nomeDoArquivoDeSaida = "arquivoDeSaida";
        arquivoDeSaida = PdfUtils.unirArquivosPdf(nomeDoArquivoDeSaida, primeiroArquivoPDF, segundoArquivoPDF);
        String extensaoDoArquivoConvertido = obterExtensaoDoAquivo(arquivoDeSaida);
        Integer quantidadeDePaginasDoArquivoDeSaida = calcularQuantidadeDePaginasDeUmOuMaisDocumentos(arquivoDeSaida);

        assertNotNull(arquivoDeSaida);
        assertEquals(quantidadeDePaginasEsperada, quantidadeDePaginasDoArquivoDeSaida);
        assertEquals(EXTENSAO_DO_ARQUIVO_ESPERADA, extensaoDoArquivoConvertido);
    }

    private Integer calcularQuantidadeDePaginasDeUmOuMaisDocumentos(File... documentos) throws IOException {
        int quantidadeDePaginasDoArquivoDeSaida = 0;
        PDDocument leitorDePaginas;
        for (File documento : documentos) {
            byte[] bytesDoDocumento = Files.readAllBytes(documento.toPath());
            leitorDePaginas = PDDocument.load(bytesDoDocumento);
            quantidadeDePaginasDoArquivoDeSaida += leitorDePaginas.getNumberOfPages();
            leitorDePaginas.close();
        }
        return quantidadeDePaginasDoArquivoDeSaida;
    }

    private String obterExtensaoDoAquivo(File arquivoDeSaida){
        return URLConnection.guessContentTypeFromName(arquivoDeSaida.getName());
    }

    @After
    public void end() {
        arquivoDeSaida.delete();
    }
}
