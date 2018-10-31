package editor.docx.tabela;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import utils.ReflectionUtils;

public class MontadorDeTabelas {

    public static List<Tabela> montar(Object[] objects) {
        List<?> listasDeObjetosParaAsTabelas = Arrays.asList(objects);
        return listasDeObjetosParaAsTabelas.stream().map(listaDeObjetos -> montarTabelaParaDocumentoDeTexto((List<?>) listaDeObjetos)).collect(Collectors.toList());
    }

    private static Tabela montarTabelaParaDocumentoDeTexto(List<?> listaDeObjetos) {
        List<Linha> linhasDaTabelaParaDocumentoDeTexto = listaDeObjetos.stream().map(objeto -> montarLinhaDaTabela(objeto)).collect(Collectors.toList());
        return Tabela.criar(linhasDaTabelaParaDocumentoDeTexto);
    }

    private static Linha montarLinhaDaTabela(Object objeto) {
        List<Celula> celulasDaLinha = montarCelulasDaLinha(objeto);
        return Linha.criar(celulasDaLinha);
    }

    private static List<Celula> montarCelulasDaLinha(Object objeto) {
        Map<String, Object> fieldsEValoresDoObjeto = montarMapDeObjetos(objeto);
        return fieldsEValoresDoObjeto.entrySet().stream().map(objetoDoMapa -> montarCelulaDaLinha(objetoDoMapa)).collect(Collectors.toList());
    }

    public static Tabela montarTabela(List<Map<String, Object>> objetos) {
        List<Linha> linhasDaTabelaParaDocumentoDeTexto = objetos.stream().map(objeto -> montarLinhaDaTabela(objeto)).collect(Collectors.toList());
        return Tabela.criar(linhasDaTabelaParaDocumentoDeTexto);
    }

    private static Linha montarLinhaDaTabela(Map<String, Object> objeto) {
        List<Celula> celulasDaLinha = objeto.entrySet().stream().map(objetoDoMapa -> montarCelulaDaLinha(objetoDoMapa)).collect(Collectors.toList());
        return Linha.criar(celulasDaLinha);
    }


    private static Map<String, Object> montarMapDeObjetos(Object objeto) {
        return ReflectionUtils.getFieldsAndValues(objeto);
    }

    private static Celula montarCelulaDaLinha(Entry<String, Object> objetoDoMap) {
        return Celula.criar(objetoDoMap.getValue());
    }
}
