[![Build Status](https://travis-ci.org/somosdigix/editordedocumentos.svg?branch=master)](https://travis-ci.org/somosdigix/editordedocumentos)
[![Coverage Status](https://coveralls.io/repos/github/somosdigix/editordedocumentos/badge.svg?branch=master)](https://coveralls.io/github/somosdigix/editordedocumentos?branch=master)

# Editor de Documento

Framework desenvolvido para gerar relatorios no formato docx com mais facilidade

para utilizar, adicione a dependencia abaixo ao seu pom

versões:
``` sh
<dependency>
   <groupId>br.com.digix</groupId>
   <artifactId>editordedocumento</artifactId>
   <version>1.1.8</version>
 </dependency> 
 ```
 ``` sh
<dependency>
  <groupId>br.com.digix</groupId>
  <artifactId>editordedocumento</artifactId>
  <version>2.0.0</version>
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
  tambem podemos passar o parameto de alinhamento da nota de rodapé:
  ``` sh
  AlinhamentoDaNotaDeRodape alinhamento = AlinhamentoDaNotaDeRodape.DIREITA;
  ByteBuffer byteBuffer = EditorDeArquivoDeTexto.editarArquivoDocx().comNotaDeRodape(notaDeRodape,alinhamento).editar(arquivoQueSeraEditado, dados);
  ```

- v2.2.0
Para facilitar a leitura e manutenção dos templates agora temos dois modos de atribuição das tags no template docx, por meio de expressão e por atribuição direta: 
   expressão: ${atributoQueSeraSubstituido}
   atribuição direta: atributoQueSeraSubstituido

https://search.maven.org/artifact/br.com.digix/editordedocumento

