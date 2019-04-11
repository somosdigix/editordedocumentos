package editor.docx;

import editor.EditorDeArquivoDeTexto;
import editor.docx.cabecalho.EditorDeCabecalho;
import editor.docx.paragrafo.EditorDeParagrafo;
import editor.docx.rodape.AdicionaNotaDeRodape;
import editor.docx.rodape.AlinhamentoDaNotaDeRodape;
import editor.docx.rodape.EditorDeRodape;
import editor.docx.tabela.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import utils.ReflectionUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

public class EditorDeArquivoDeTextoDocx extends EditorDeArquivoDeTexto {

    private static final String EXTENSAO_DO_ARQUIVO = ".docx";
    private File arquivoEditado;
    private EditorDeParagrafo editorDeParagrafo;
    private EditorDeTabela editorDeTabela;
    private EditorDeRodape editorDeRodape;
    private EditorDeCabecalho editorDeCabecalho;
    private AdicionaLinhaNaTabelaDoDocumentoDeTexto adicionaLinhaNaTabelaDoDocumentoDeTexto;
    private AdicionaNotaDeRodape adicionaNotaDeRodape;

    public EditorDeArquivoDeTexto docxComTabelas(Object[] dadosParaMontarAsTabelasDoDocumento, FormatacaoDaTabela... formatacaoDaTabelas) {
        if (Objects.nonNull(dadosParaMontarAsTabelasDoDocumento)) {
            List<Tabela> tabelasParaDocumentoDeTextos = MontadorDeTabelas.montar(dadosParaMontarAsTabelasDoDocumento);
            this.adicionaLinhaNaTabelaDoDocumentoDeTexto = AdicionaLinhaNaTabelaDoDocumentoDeTexto.comTabelas(tabelasParaDocumentoDeTextos, formatacaoDaTabelas);
        }
        return this;
    }

    public EditorDeArquivoDeTexto docxComTabelas(List<Map<String, Object>> dadosParaMontarAsTabelasDoDocumento, FormatacaoDaTabela... formatacaoDaTabelas) {
        if (Objects.nonNull(dadosParaMontarAsTabelasDoDocumento)) {
            Tabela tabelasParaDocumentoDeTextos = MontadorDeTabelas.montarTabela(dadosParaMontarAsTabelasDoDocumento);
            this.adicionaLinhaNaTabelaDoDocumentoDeTexto = AdicionaLinhaNaTabelaDoDocumentoDeTexto.comTabelas(tabelasParaDocumentoDeTextos, formatacaoDaTabelas);
        }
        return this;
    }

    public EditorDeArquivoDeTexto comNotaDeRodape(String notaDeRodape, AlinhamentoDaNotaDeRodape... alinhamentoDaNotaDeRodape) {
        if (Objects.nonNull(notaDeRodape)) {
            Optional<AlinhamentoDaNotaDeRodape> alinhamentoDaNota = alinhamentoDaNotaDeRodape.length > 0 ? Optional.of(alinhamentoDaNotaDeRodape[0]) : Optional.empty();
            this.adicionaNotaDeRodape = AdicionaNotaDeRodape.comNotaDeRodape(notaDeRodape, alinhamentoDaNota);
        }
        return this;
    }

    @Override
    protected void editarArquivo(File arquivoQueSeraEditado, Object object, String nomeDoArquivoDeSaida) throws ErroAoEditarArquivoDeTexto {
        try {
            this.arquivoEditado = new File(nomeDoArquivoDeSaida + EXTENSAO_DO_ARQUIVO);
            instanciarEditores(object);
            XWPFDocument arquivoEditado = editarDocumento(arquivoQueSeraEditado);
            salvarNovoArquivo(arquivoEditado);
        } catch (Exception exception) {
            throw new ErroAoEditarArquivoDeTexto(exception);
        }
    }

    @Override
    protected OutputStream editarArquivo(InputStream arquivoQueSeraEditado, Object dados) throws ErroAoEditarArquivoDeTexto {
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            instanciarEditores(dados);
            XWPFDocument documentoDoWordEditadoNoApachePoi = editarDocumento(arquivoQueSeraEditado);
            documentoDoWordEditadoNoApachePoi.write(outputStream);
        } catch (Exception exception) {
            throw new ErroAoEditarArquivoDeTexto(exception);
        }
        return outputStream;
    }

    @Override
    protected ByteBuffer editarArquivo(ByteBuffer arquivoQueSeraEditado, Object dados) throws ErroAoEditarArquivoDeTexto {
        ByteBuffer arquivoEditado;
        try {
            InputStream inputStream = new ByteArrayInputStream(arquivoQueSeraEditado.array());
            OutputStream outputStream = editarArquivo(inputStream, dados);

            arquivoEditado = ByteBuffer.wrap(((ByteArrayOutputStream) outputStream).toByteArray());

            outputStream.close();
            inputStream.close();
        } catch (Exception exception) {
            throw new ErroAoEditarArquivoDeTexto(exception);
        }
        return arquivoEditado;
    }

    @Override
    protected ByteBuffer editarArquivo(ByteBuffer arquivoQueSeraEditado, Map<String, Object> dados) throws ErroAoEditarArquivoDeTexto {
        ByteBuffer arquivoEditado;
        try {
            instanciarEditores(dados);
            OutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = new ByteArrayInputStream(arquivoQueSeraEditado.array());
            XWPFDocument documentoDoWordEditadoNoApachePoi = editarDocumento(inputStream);
            documentoDoWordEditadoNoApachePoi.write(outputStream);

            arquivoEditado = ByteBuffer.wrap(((ByteArrayOutputStream) outputStream).toByteArray());

            outputStream.close();
            inputStream.close();
        } catch (Exception exception) {
            throw new ErroAoEditarArquivoDeTexto(exception);
        }
        return arquivoEditado;
    }

    private void instanciarEditores(Map<String, Object> dados) {
        this.editorDeParagrafo = EditorDeParagrafo.comMapaDeAtributos(dados);
        this.editorDeTabela = EditorDeTabela.comEditorDeParagrafo(editorDeParagrafo);
        this.editorDeRodape = EditorDeRodape.comEditorDeParagrafoETabela(editorDeParagrafo, editorDeTabela);
        this.editorDeCabecalho = EditorDeCabecalho.comEditorDeParagrafoETabela(editorDeParagrafo, editorDeTabela);
    }

    @Override
    protected File getArquivoEditado() {
        return this.arquivoEditado;
    }

    private void salvarNovoArquivo(XWPFDocument documentoDocx) throws IOException {
        documentoDocx.write(new FileOutputStream(arquivoEditado));
    }

    private void instanciarEditores(Object object) {
        this.editorDeParagrafo = EditorDeParagrafo.comMapaDeAtributos(ReflectionUtils.getFieldsAndValues(object));
        this.editorDeTabela = EditorDeTabela.comEditorDeParagrafo(editorDeParagrafo);
        this.editorDeRodape = EditorDeRodape.comEditorDeParagrafoETabela(editorDeParagrafo, editorDeTabela);
        this.editorDeCabecalho = EditorDeCabecalho.comEditorDeParagrafoETabela(editorDeParagrafo, editorDeTabela);
    }

    private XWPFDocument editarDocumento(File arquivoQueSeraEditado) throws InvalidFormatException, IOException {
        XWPFDocument documentoDocx = new XWPFDocument(OPCPackage.open(arquivoQueSeraEditado));
        return editarDocumento(documentoDocx);
    }

    private XWPFDocument editarDocumento(InputStream arquivoQueSeraEditado) throws InvalidFormatException, IOException {
        XWPFDocument documentoDocx = new XWPFDocument(arquivoQueSeraEditado);
        return editarDocumento(documentoDocx);
    }

    private XWPFDocument editarDocumento(XWPFDocument documentoDocx) throws InvalidFormatException, IOException {
        editorDeCabecalho.editarConteudoDosCabecalhosDoDocumento(documentoDocx.getHeaderList());
        editorDeParagrafo.editarParagrafosDoDocumento(documentoDocx.getParagraphs());
        editorDeTabela.editarConteudoDasTabelas(documentoDocx.getTables());
        adicionarConteudoNasTabelasDoDocumentoDeTexto(documentoDocx);
        editorDeRodape.editarConteudoDosRodapesDoDocumento(documentoDocx.getFooterList());
        adicionarNotaDeRodape(documentoDocx.getFooterList());
        return documentoDocx;
    }

    private void adicionarConteudoNasTabelasDoDocumentoDeTexto(XWPFDocument documentoDocx) {
        if (Objects.nonNull(adicionaLinhaNaTabelaDoDocumentoDeTexto)) {
            adicionaLinhaNaTabelaDoDocumentoDeTexto.adicionarConteudoNasTabelasDoDocumentoDeTexto(documentoDocx.getTables());
        }
    }

    private void adicionarNotaDeRodape(List<XWPFFooter> rodapesDoDocumento) {
        if (Objects.nonNull(adicionaNotaDeRodape)) {
            adicionaNotaDeRodape.adicionarNotaNosRodapes(rodapesDoDocumento);
        }
    }
}
