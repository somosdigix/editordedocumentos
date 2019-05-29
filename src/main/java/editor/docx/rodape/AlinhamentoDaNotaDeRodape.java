package editor.docx.rodape;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;

public enum AlinhamentoDaNotaDeRodape {
    ESQUERDA(ParagraphAlignment.LEFT),
    DIREITA(ParagraphAlignment.RIGHT),
    CENTRO(ParagraphAlignment.CENTER);

    private ParagraphAlignment alinhamento;

    AlinhamentoDaNotaDeRodape(ParagraphAlignment alinhamento) {
        this.alinhamento = alinhamento;
    }

    public ParagraphAlignment getAlinhamento() {
        return alinhamento;
    }
}
