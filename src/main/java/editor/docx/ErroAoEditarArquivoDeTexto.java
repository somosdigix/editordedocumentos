package editor.docx;

public class ErroAoEditarArquivoDeTexto extends Exception {

	private static final long serialVersionUID = 1L;

	public ErroAoEditarArquivoDeTexto(Exception exception) {
		super("Erro ao editar arquivo de texto" + "\n" + exception.getMessage());
	}
}
