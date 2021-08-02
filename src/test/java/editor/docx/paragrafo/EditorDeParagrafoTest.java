package editor.docx.paragrafo;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EditorDeParagrafoTest {

    @Test
    public void deveEditarUmParagrafoDoTexto() throws Exception {
        String conteudoDoTextoEsperado = "João do Pé de Feijão";
        String atributoQueSeraAlteradoNoDocumento = "nomeDoFuncionario";
        Map<String, Object> mapaDeAtributos = montarMapaDeAtributos(atributoQueSeraAlteradoNoDocumento,
                conteudoDoTextoEsperado);
        XWPFDocument documento = new XWPFDocument();
        XWPFParagraph paragrafoDoDocumento = criarParagrafoNoDocumentoComApenasUmTrecho(documento, atributoQueSeraAlteradoNoDocumento);

        EditorDeParagrafo.comMapaDeAtributos(mapaDeAtributos).editarParagrafosDoDocumento(paragrafoDoDocumento);
        String textoAdicionadoNoDocumento = buscarPalavraNoArquivo(documento, conteudoDoTextoEsperado);

        Assert.assertEquals(conteudoDoTextoEsperado, textoAdicionadoNoDocumento);
    }

    @Test
    public void deveEditarUmParagrafoQuandoOTrechoDoParagrafoTiverMaisDeUmaPalavra() throws Exception {
        String conteudoDoTextoQueDeveSerSubstituido = "Carlos Silva";
        String conteudoDoTextoEsperado = "e assina também Carlos Silva doravante citado como Contratado";
        String textoOriginalDoDocumentoComAsTags = "e assina também nomeDoFuncionario doravante citado como Contratado";
        Map<String, Object> mapaDeAtributos = new HashMap<>();
        mapaDeAtributos.put("nomeDoFuncionario", conteudoDoTextoQueDeveSerSubstituido);
        XWPFDocument documento = new XWPFDocument();
        XWPFParagraph paragrafoDoDocumento = criarParagrafoNoDocumentoComApenasUmTrecho(documento, textoOriginalDoDocumentoComAsTags);

        EditorDeParagrafo.comMapaDeAtributos(mapaDeAtributos).editarParagrafosDoDocumento(paragrafoDoDocumento);

        String textoAdicionadoNoDocumento = buscarPalavraNoArquivo(documento, conteudoDoTextoEsperado);
        Assert.assertEquals(conteudoDoTextoEsperado, textoAdicionadoNoDocumento);
    }

    @Test
    public void deveEditarUmParagrafoQuandoOTrechoDoParagrafoTiverChavesParaSubstituirSemEspaçoEntreElas() throws Exception {
        String conteudoDoTextoEsperado = "na cidade de Campo Grande/MS";
        String textoOriginalDoDocumentoComAsTags = "na cidade de nomeDaCidade/siglaDoUF";
        Map<String, Object> mapaDeAtributos = new HashMap<>();
        mapaDeAtributos.put("nomeDaCidade", "Campo Grande");
        mapaDeAtributos.put("siglaDoUF", "MS");
        XWPFDocument documento = new XWPFDocument();
        XWPFParagraph paragrafoDoDocumento = criarParagrafoNoDocumentoComApenasUmTrecho(documento, textoOriginalDoDocumentoComAsTags);

        EditorDeParagrafo.comMapaDeAtributos(mapaDeAtributos).editarParagrafosDoDocumento(paragrafoDoDocumento);

        String textoAdicionadoNoDocumento = buscarPalavraNoArquivo(documento, conteudoDoTextoEsperado);
        Assert.assertEquals(conteudoDoTextoEsperado, textoAdicionadoNoDocumento);
    }

    private Map<String, Object> montarMapaDeAtributos(String tagNoDocumento, String conteudoDoTestoEsperado) {
        Map<String, Object> mapaDeAtributos = new HashMap<>();
        mapaDeAtributos.put(tagNoDocumento, conteudoDoTestoEsperado);
        return mapaDeAtributos;
    }

    private XWPFParagraph criarParagrafoNoDocumentoComApenasUmTrecho(XWPFDocument documento, String conteudoDoTrechoDoParagrafo) {
        XWPFParagraph paragrafoDoDocumento = documento.createParagraph();
        XWPFRun linhaDoDocumento = paragrafoDoDocumento.createRun();
        linhaDoDocumento.setText(conteudoDoTrechoDoParagrafo);
        return paragrafoDoDocumento;
    }

    private String buscarPalavraNoArquivo(XWPFDocument documento, String conteudoParaBuscar) throws Exception {
        return documento.getParagraphs().stream()
                .filter(paragrafo -> verificarConteudoDoParagrafo(paragrafo, conteudoParaBuscar)).findFirst().get()
                .getText();
    }

    private boolean verificarConteudoDoParagrafo(XWPFParagraph paragrafo, String conteudoParaBuscar) {
        return paragrafo.getText().equals(conteudoParaBuscar);
    }
}
