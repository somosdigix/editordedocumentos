package editor.docx.rodape;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;

public class FormatacaoDaNotaDeRodape {

    private static final Integer TAMANHO_DA_FONTE_PADRAO = 8;
    private static final String FONTE_PADRAO = "Arial";
    private static final AlinhamentoDaNotaDeRodape ALINHAMENTO_PADRAO = AlinhamentoDaNotaDeRodape.CENTRO;

    private String fonte;
    private Integer tamanhoDaFonte;
    private AlinhamentoDaNotaDeRodape alinhamentoDaNota;

    public FormatacaoDaNotaDeRodape() {
        this.fonte = FONTE_PADRAO;
        this.tamanhoDaFonte = TAMANHO_DA_FONTE_PADRAO;
        this.alinhamentoDaNota = ALINHAMENTO_PADRAO;
    }

    public FormatacaoDaNotaDeRodape comFonte(String fonte) {
        this.fonte = fonte;
        return this;
    }

    public FormatacaoDaNotaDeRodape comTamanhoDaFonte(Integer tamanhoDaFonte) {
        this.tamanhoDaFonte = tamanhoDaFonte;
        return this;
    }

    public FormatacaoDaNotaDeRodape comAlinhamento(AlinhamentoDaNotaDeRodape alinhamento) {
        this.alinhamentoDaNota = alinhamento;
        return this;
    }

    public String getFonte() {
        return fonte;
    }

    public Integer getTamanhoDaFonte() {
        return tamanhoDaFonte;
    }

    public ParagraphAlignment getAlinhamento() {
        return alinhamentoDaNota.getAlinhamento();
    }

    public static Integer getTamanhoDaFontePadrao() {
        return TAMANHO_DA_FONTE_PADRAO;
    }

    public static String getFontePadrao() {
        return FONTE_PADRAO;
    }

    public static ParagraphAlignment getAlinhamentoPadrao() {
        return ALINHAMENTO_PADRAO.getAlinhamento();
    }
}
