package editor.docx.rodape;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;

public enum AlinhamentoDaNotaDeRodape {
    ESQUERDA(ParagraphAlignment.LEFT),
    DIREITA(ParagraphAlignment.RIGHT),
    CENTRO(ParagraphAlignment.CENTER);

    private final Integer TAMANHO_PADRAO = 10;
    private ParagraphAlignment alinhamento;
    private Integer tamanhoDaFonte;

    AlinhamentoDaNotaDeRodape(ParagraphAlignment alinhamento) {
        this.alinhamento = alinhamento;
        this.tamanhoDaFonte = TAMANHO_PADRAO;
    }

    public ParagraphAlignment getAlinhamento() {
        return alinhamento;
    }

    public void comTamanhoDaFonte(Integer tamanhoDaFonte) {
        this.tamanhoDaFonte = tamanhoDaFonte;
    }

    public int getTamanhoDaFonte() {
        return tamanhoDaFonte;
    }
}
