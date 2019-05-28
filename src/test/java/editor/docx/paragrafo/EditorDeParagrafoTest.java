package editor.docx.paragrafo;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Assert;
import org.junit.Ignore;
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
        XWPFParagraph paragrafoDoDocumento = criarParagrafoNoDocumento(documento, atributoQueSeraAlteradoNoDocumento);

        EditorDeParagrafo.comMapaDeAtributos(mapaDeAtributos).editarParagrafosDoDocumento(paragrafoDoDocumento);
        String textoAdicionadoNoDocumento = buscarPalavraNoArquivo(documento, conteudoDoTextoEsperado);

        Assert.assertEquals(conteudoDoTextoEsperado, textoAdicionadoNoDocumento);
    }

    @Ignore
    @Test
    public void deveEditarUmParagrafoNoTextoUsandoTagComDelimitadores() throws Exception {
        String conteudoDoTextoEsperado = "Marquinhos DJ";
        String atributoQueSeraSubstituido = "nomeDoDJDaFesta";
        String tagQueSeraAlteradaNoDocumento = "${" + atributoQueSeraSubstituido + "}";
        Map<String, Object> mapaDeAtributos = montarMapaDeAtributos(atributoQueSeraSubstituido,
                conteudoDoTextoEsperado);
        XWPFDocument documento = new XWPFDocument();
        XWPFParagraph paragrafoDoDocumento = criarParagrafoNoDocumento(documento, tagQueSeraAlteradaNoDocumento);

        EditorDeParagrafo.comMapaDeAtributos(mapaDeAtributos).editarParagrafosDoDocumento(paragrafoDoDocumento);
        String textoAdicionadoNoDocumento = buscarPalavraNoArquivo(documento, conteudoDoTextoEsperado);

        Assert.assertEquals(conteudoDoTextoEsperado, textoAdicionadoNoDocumento);
    }

    private Map<String, Object> montarMapaDeAtributos(String tagNoDocumento, String conteudoDoTestoEsperado) {
        Map<String, Object> mapaDeAtributos = new HashMap<>();
        mapaDeAtributos.put(tagNoDocumento, conteudoDoTestoEsperado);
        return mapaDeAtributos;
    }

    private XWPFParagraph criarParagrafoNoDocumento(XWPFDocument documento, String atributoQueSeraAlteradoNoDocumento) {
        XWPFParagraph paragrafoDoDocumento = documento.createParagraph();
        XWPFRun linhaDoDocumento = paragrafoDoDocumento.createRun();
        linhaDoDocumento.setText(atributoQueSeraAlteradoNoDocumento);
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
