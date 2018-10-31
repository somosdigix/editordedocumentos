package editor.docx.paragrafo;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EditorDeParagrafo {

    private static final Integer INICIO_DA_TAG_NO_TEXTO = 0;
    private static final String PULA_LINHA = "\n";
    private static final String TAB = "\t";
    private static final String VAZIO = "";
    private final Map<String, Object> mapaDeAtributos;

    private EditorDeParagrafo(Map<String, Object> mapaDeAtributos) {
        this.mapaDeAtributos = mapaDeAtributos;
    }

    public static EditorDeParagrafo comMapaDeAtributos(Map<String, Object> mapaDeAtributos) {
        return new EditorDeParagrafo(mapaDeAtributos);
    }

    public EditorDeParagrafo editarParagrafosDoDocumento(XWPFParagraph... paragrafosDoDocumento) {
        editarParagrafos(Arrays.asList(paragrafosDoDocumento));
        return this;
    }

    public EditorDeParagrafo editarParagrafosDoDocumento(List<XWPFParagraph> paragrafosDoDocumento) {
        editarParagrafos(paragrafosDoDocumento);
        return this;
    }

    private void editarParagrafos(List<XWPFParagraph> listaDeParagrafos) {
        listaDeParagrafos.forEach(paragrafoDoDocumento -> editarUmParagrafo(paragrafoDoDocumento));
    }

    private void editarUmParagrafo(XWPFParagraph paragrafoDocumento) {
        if (Objects.nonNull(paragrafoDocumento.getRuns())) {
            paragrafoDocumento.getRuns().forEach(linhaDoParagrado -> editarTagDoDocumento(linhaDoParagrado));
        }
    }

    private void editarTagDoDocumento(XWPFRun linhaDoDocumento) {
        String chaveParaSubstituicao = linhaDoDocumento.getText(INICIO_DA_TAG_NO_TEXTO);
        if (Objects.nonNull(chaveParaSubstituicao) && mapaDeAtributos.containsKey(chaveParaSubstituicao)) {
            String valorParaSubstituir = obterValorParaSubstituicao(chaveParaSubstituicao);
            linhaDoDocumento.setText(VAZIO, INICIO_DA_TAG_NO_TEXTO);
            String[] linhas = valorParaSubstituir.split(PULA_LINHA);
            Arrays.stream(linhas).forEach(linha -> formatarLinha(linhaDoDocumento, valorParaSubstituir, linha));
        }
    }

    private String obterValorParaSubstituicao(String atributoDoDocumento) {
        return Objects.nonNull(mapaDeAtributos.get(atributoDoDocumento)) ? mapaDeAtributos.get(atributoDoDocumento).toString() : atributoDoDocumento;
    }

    private void formatarLinha(XWPFRun linhaDoDocumento, String valorParaSubstituir, String linha) {
        if (valorParaSubstituir.contains(PULA_LINHA)) {
            inserirTextoComTabulacaoSeNecessario(linhaDoDocumento, linha);
            linhaDoDocumento.addCarriageReturn();
        } else {
            inserirTextoComTabulacaoSeNecessario(linhaDoDocumento, valorParaSubstituir);
        }
    }

    private void inserirTextoComTabulacaoSeNecessario(XWPFRun linhaDoDocumento, String paragrafo) {
        if (paragrafo.contains(TAB)) {
            String[] textoSeparadoPorTabulacao = paragrafo.split(TAB);
            int ultimaPalavra = textoSeparadoPorTabulacao.length - 1;
            for (int posicaoDoTexto = 0; posicaoDoTexto < textoSeparadoPorTabulacao.length; posicaoDoTexto++) {
                linhaDoDocumento.setText(textoSeparadoPorTabulacao[posicaoDoTexto]);
                if (posicaoDoTexto < ultimaPalavra) {
                    linhaDoDocumento.getCTR().addNewTab();
                }
            }
        } else {
            linhaDoDocumento.setText(paragrafo);
        }
    }
}
