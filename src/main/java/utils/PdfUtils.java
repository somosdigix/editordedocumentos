package utils;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class PdfUtils {

	private static final String EXTENSAO_DO_ARQUIVO = ".pdf";

	public static File converterDocxParaPdf(File arquivoDocx, String nomeDoArquivoDeSaida, PdfOptions... opcoes)
			throws ErroAoConverterDocxParaPdf {
		try {
			String nomeDoArquivo = nomeDoArquivoDeSaida.concat(EXTENSAO_DO_ARQUIVO);
			File arquivoConvertido = new File(nomeDoArquivo);
			InputStream inputStream = new FileInputStream(arquivoDocx);
			XWPFDocument docx = new XWPFDocument(inputStream);
			PdfOptions options = null;
			if (opcoes != null && opcoes.length > 0) {
				options = opcoes[0];
			}
			OutputStream pdfDeSaidaOutputStream = new FileOutputStream(arquivoConvertido);
			PdfConverter.getInstance().convert(docx, pdfDeSaidaOutputStream, options);
			return arquivoConvertido;
		} catch (Exception exception) {
			throw new ErroAoConverterDocxParaPdf();
		}
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

	// TODO avaliar opção de uso de memória para os dois métodos de união de arquivo, talvez receber como parâmetro
	//  pode ser mais performático em algumas situações usando arquivos temporários
	public static File unirArquivosPdf(String nomeDoArquivoDeSaida, File... arquivosPdf) throws IOException {
		PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
		String nomeDoArquivoDeSaidaComExtensao = nomeDoArquivoDeSaida + EXTENSAO_DO_ARQUIVO;
		pdfMergerUtility.setDestinationFileName(nomeDoArquivoDeSaidaComExtensao);
		for (File arquivo : arquivosPdf) {
			pdfMergerUtility.addSource(arquivo);
		}
		pdfMergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
		return new File(nomeDoArquivoDeSaidaComExtensao);
	}

	public static byte[] unirArquivosPdf(byte[]... arquivosPdf) throws IOException {
		Stream<byte[]> streamDosBytesDosArquivosPdf = Arrays.stream(arquivosPdf);
		return unirArquivosPdf(streamDosBytesDosArquivosPdf);
	}

	private static byte[] unirArquivosPdf(Stream<byte[]> streamDosBytesDosArquivosPdf) throws IOException {
		PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
		streamDosBytesDosArquivosPdf.map(ByteArrayInputStream::new)
				.forEachOrdered(pdfMergerUtility::addSource);
		ByteArrayOutputStream pdfDeSaidaOutputStream = new ByteArrayOutputStream();
		pdfMergerUtility.setDestinationStream(pdfDeSaidaOutputStream);
		pdfMergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
		return pdfDeSaidaOutputStream.toByteArray();
	}

	public static ByteBuffer unirArquivosPdf(List<ByteBuffer> arquivosPdf) throws IOException {
		Stream<byte[]> streamDosBytesDosArquivosPdf = arquivosPdf.stream().map(ByteBuffer::array);
		byte[] bytesDoArquivoUnificado = unirArquivosPdf(streamDosBytesDosArquivosPdf);
		return ByteBuffer.wrap(bytesDoArquivoUnificado);
	}

	public static ByteBuffer unirArquivosPdf(ByteBuffer... arquivosPdf) throws IOException {
		return unirArquivosPdf(Arrays.asList(arquivosPdf));
	}

	public static int obterQuantidadeDePaginas(byte[] bytesDoArquivoPdf) throws IOException {
		PDDocument leitorDePaginas = PDDocument.load(bytesDoArquivoPdf);
		int quantidadeDePaginas = leitorDePaginas.getNumberOfPages();
		leitorDePaginas.close();
		return quantidadeDePaginas;
	}
}
