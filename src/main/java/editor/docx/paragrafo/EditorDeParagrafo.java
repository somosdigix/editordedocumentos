package editor.docx.paragrafo;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            paragrafoDocumento.getRuns().forEach(this::editarTagDoDocumento);
        }
    }

    private void editarTagDoDocumento(XWPFRun trechoDeParagrafo) {
        String conteudoDoTrechoDeParagrafo = trechoDeParagrafo.getText(INICIO_DA_TAG_NO_TEXTO);
        List<String> chavesParaSubstituir = mapaDeAtributos.keySet().stream()
                .filter(chaveParaSubstituicao -> verificarSeChaveEstaContidaNoTexto(chaveParaSubstituicao, conteudoDoTrechoDeParagrafo)).collect(Collectors.toList());
        if (Objects.nonNull(conteudoDoTrechoDeParagrafo) && !chavesParaSubstituir.isEmpty()) {
            String conteudoDoTrechoDeParagrafoComTagsSubstituidas = obterTrechoDeParagrafoComTagsSubstituidas(conteudoDoTrechoDeParagrafo);
            trechoDeParagrafo.setText(VAZIO, INICIO_DA_TAG_NO_TEXTO);
            String[] linhas = conteudoDoTrechoDeParagrafoComTagsSubstituidas.split(PULA_LINHA);
            Arrays.stream(linhas).forEach(linha -> formatarLinha(trechoDeParagrafo, conteudoDoTrechoDeParagrafoComTagsSubstituidas, linha));
        }
    }

    private String obterTrechoDeParagrafoComTagsSubstituidas(String textoDaRun) {
        return mapaDeAtributos.entrySet().stream().map(chaveEValor ->
                        (Function<String, String>) textoParaSubstituicao ->
                                textoParaSubstituicao.replace(chaveEValor.getKey(), obterValorParaSubstituicao(chaveEValor.getKey())))
                .reduce(Function.identity(), Function::andThen)
                .apply(textoDaRun);
    }

    private boolean verificarSeChaveEstaContidaNoTexto(String chaveParaSubstituicao, String conteudoDoTrechoDeParagrafo) {
        return Objects.nonNull(conteudoDoTrechoDeParagrafo) && conteudoDoTrechoDeParagrafo.contains(chaveParaSubstituicao);
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
