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
 - v2.2.4 (29/05/2019)
  ``` sh
<dependency>
  <groupId>br.com.digix</groupId>
  <artifactId>editordedocumento</artifactId>
  <version>2.2.4</version>
</dependency>
 ```
 - v2.2.6 (02/08/2021)
  ``` sh
<dependency>
  <groupId>br.com.digix</groupId>
  <artifactId>editordedocumento</artifactId>
  <version>2.2.6</version>
</dependency>
 ```
 - v2.2.7 (31/05/2023)
  ``` sh
<dependency>
  <groupId>br.com.digix</groupId>
  <artifactId>editordedocumento</artifactId>
  <version>2.2.7</version>
</dependency>
 ```
  última versão: 
 - v2.2.8 (04/07/2023)
   ``` sh
<dependency>
  <groupId>br.com.digix</groupId>
  <artifactId>editordedocumento</artifactId>
  <version>2.2.8</version>
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

- V2.2.5  
  Agora também é possível colocar mais de uma tabela em um mesmo documento passando uma lista de lista de atributos mapeados:
  ``` sh
  List<List<Map<String, Object>>> dadosDasTabelas = Arrays.asList(dadosDaPrimeiraTabela, dadosDaSegundaTabela);
  EditorDeArquivoDeTexto.editarArquivoDocx().docxComTabelas(dadosDasTabelas, formatacao).editar(arquivoQueSeraEditado, mapaDeDadosDoDocumento);
  ```
 
 - V2.2.6  
  Correções de bugs menores na substituição de tags.

 - V2.2.7  
  Remoção da lib iTextPDF em favor da lib PDFBox.
  
 - V2.2.8
  Inclusão de sobrecargas do método unirArquivosPdf da classe PdfUtils para lidar com ByteBuffer.

aqui você pode encontrar esse framework compilado para outras liguagens, como Scala, Kotlin, Groovy...
https://search.maven.org/artifact/br.com.digix/editordedocumento
