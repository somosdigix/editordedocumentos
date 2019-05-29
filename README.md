[![Build Status](https://travis-ci.org/somosdigix/editordedocumentos.svg?branch=master)](https://travis-ci.org/somosdigix/editordedocumentos)
[![Coverage Status](https://coveralls.io/repos/github/somosdigix/editordedocumentos/badge.svg?branch=master)](https://coveralls.io/github/somosdigix/editordedocumentos?branch=master)

# Editor de Documento

Framework desenvolvido para gerar relatorios no formato docx com mais facilidade

para utilizar, adicione a dependencia abaixo ao seu pom

versões:
- v1.1.8 (14/11/2018)
``` sh
<dependency>
   <groupId>br.com.digix</groupId>
   <artifactId>editordedocumento</artifactId>
   <version>1.1.8</version>
 </dependency> 
 ```
 - v2.0.0 (11/04/2019)
 ``` sh
<dependency>
  <groupId>br.com.digix</groupId>
  <artifactId>editordedocumento</artifactId>
  <version>2.0.0</version>
</dependency>
 ```
 ultima versão: 
 - v2.2.4 (29/05/2019)
  ``` sh
<dependency>
  <groupId>br.com.digix</groupId>
  <artifactId>editordedocumento</artifactId>
  <version>2.2.4</version>
</dependency>
 ```
 
 Novidades:
  
  - v1.1.8
   Agora é possivel enviar um mapa de atributos para o editor, veja abaixo como é simples: 
   ``` sh
   byte[] bytesDoTemplate = Files.readAllBytes("src/test/resources/documento.docx");
   ByteBuffer arquivoQueSeraEditado = ByteBuffer.wrap(bytesDoTemplate);
   Map<String, Object> dados = new HashMap<>();
   dados.put("cabecalhoDoRelatorio", "Relatório de teste");
   ByteBuffer byteBuffer = EditorDeArquivoDeTexto.editarArquivoDocx().editar(arquivoQueSeraEditado, dados);
  ```
 - v2.0.0
 Agora é possivel inserir uma nota de rodapé no arquivo, veja abaixo como é simples:
  ``` sh
  byte[] bytesDoTemplate = Files.readAllBytes("src/test/resources/documento.docx");
  ByteBuffer arquivoQueSeraEditado = ByteBuffer.wrap(bytesDoTemplate);
  Map<String, Object> dados = new HashMap<>();
  dados.put("cabecalhoDoRelatorio", "Relatório de teste");
  String notaDeRodape = "Gerado pelo editor de documento.";
  ByteBuffer byteBuffer = EditorDeArquivoDeTexto.editarArquivoDocx().comNotaDeRodape(notaDeRodape).editar(arquivoQueSeraEditado, dados);
   ```
 - V2.2.4  
  Agora também podemos passar a formatação da nota de rodapé:
  ``` sh
  FormatacaoDaNotaDeRodape formatacaoDaNotaDeRodape = new FormatacaoDaNotaDeRodape().comTamanhoDaFonte(8).comFonte("Arial").comAlinhamento(AlinhamentoDaNotaDeRodape.DIREITA);
  ByteBuffer byteBuffer = EditorDeArquivoDeTexto.editarArquivoDocx().comNotaDeRodape(notaDeRodape,formatacaoDaNotaDeRodape).editar(arquivoQueSeraEditado, dados);
  ```

aqui você pode encontrar esse framework compilado para outras liguagens, como Scala, Kotlin, Groovy...
https://search.maven.org/artifact/br.com.digix/editordedocumento

