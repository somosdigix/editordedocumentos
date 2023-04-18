package utils;

import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class PdfUtils {

	private static final String EXTENSAO_DO_ARQUIVO = ".pdf";
	private static File arquivoConvertido;

	public static File converterDocxParaPdf(File arquivoDocx, String nomeDoArquivoDeSaida, PdfOptions... opcoes)
			throws ErroAoConverterDocxParaPdf {
		try {
			String nomeDoArquivo = nomeDoArquivoDeSaida.concat(EXTENSAO_DO_ARQUIVO);
			arquivoConvertido = new File(nomeDoArquivo);
			InputStream inputStream = new FileInputStream(arquivoDocx);
			XWPFDocument docx = new XWPFDocument(inputStream);
			PdfOptions options = null;
			if (opcoes != null && opcoes.length > 0) {
				options = opcoes[0];
			}
			OutputStream pdfDeSaidaOutputStream = new FileOutputStream(arquivoConvertido);
			PdfConverter.getInstance().convert(docx, pdfDeSaidaOutputStream, options);
		} catch (Exception exception) {
			throw new ErroAoConverterDocxParaPdf();
		}
		return arquivoConvertido;
	}

	public static byte[] converterDocxParaPdf(byte[] dados) throws ErroAoConverterDocxParaPdf {
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dados);
			XWPFDocument docx = new XWPFDocument(byteArrayInputStream);
			ByteArrayOutputStream pdfDeSaidaOutputStream = new ByteArrayOutputStream();
			PdfConverter.getInstance().convert(docx, pdfDeSaidaOutputStream, PdfOptions.create());
			return pdfDeSaidaOutputStream.toByteArray();
		} catch (Exception exception) {
			throw new ErroAoConverterDocxParaPdf();
		}
	}

	public static File unirArquivosPdf(String nomeDoArquivoDeSaida, File... arquivosPdf) throws IOException {
		PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
		String nomeDoArquivoDeSaidaComExtensao = nomeDoArquivoDeSaida + EXTENSAO_DO_ARQUIVO;
		pdfMergerUtility.setDestinationFileName(nomeDoArquivoDeSaidaComExtensao);
		for (File arquivo : arquivosPdf) {
			pdfMergerUtility.addSource(arquivo);
		}
		// TODO avaliar opção de uso de memória, talvez receber como parâmetro
		pdfMergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
		return new File(nomeDoArquivoDeSaidaComExtensao);
	}

	private static List<byte[]> lerBytesDosArquivos(List<File> arquivos) throws IOException {
		return arquivos.stream().filter(file -> file != null).map(file -> adicionarBytesDosArquivos(file)).collect(Collectors.toList());
	}

	private static byte[] adicionarBytesDosArquivos(File file) {
		try {
			return Files.readAllBytes(file.toPath());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void concatenarDocumento(PdfCopy copiadorDeDocumento, byte[] documento) {
		try {
			PdfReader leitorDePaginas = new PdfReader(documento);
			for (int paginaAtualDoDocumento = 1; paginaAtualDoDocumento <= leitorDePaginas.getNumberOfPages(); paginaAtualDoDocumento++) {
				PdfImportedPage primeiraPagina = copiadorDeDocumento.getImportedPage(leitorDePaginas, paginaAtualDoDocumento);
				copiadorDeDocumento.addPage(primeiraPagina);
			}
			copiadorDeDocumento.freeReader(leitorDePaginas);
			leitorDePaginas.close();
		} catch (Exception excecao) {
			throw new RuntimeException(excecao);
		}
	}
}
