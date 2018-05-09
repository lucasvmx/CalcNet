# CalcNet

### Do que se trata?
* Trata-se de uma calculadora científica que poderá ser utilizada em sala de aula durante as provas.
* Com ela, o aplicador de prova pode monitorar os alunos para ver se eles estão realmente utilizando a calculadora.
* Se o aluno utilizar qualquer outro software que não seja a calculadora, o aplicador de prova será notificado em tempo real.

### Público-alvo
* Estudantes de nível superior.

#### Plataformas suportadas:
* A calculadora em si suporta apenas a seguinte plataforma:
  * Android 5.0 ou versões posteriores.
* O Servidor do CalcNet irá suportar as seguintes plataformas:
  * Windows - x86 e x64
  * Linux - x86 e x64
  * macOS - não testado *

### Como compilar
* Cliente: baixe o Android Studio, abra o projeto e depois clique em 'build'.
* Servidor: baixe o Microsoft Visual Studio (2017 de preferência) e clique em 'build project'.

### Como contribuir
* 1 - Faça um fork do projeto.
* 2 - Realize as suas alterações.
* 3 - Nos envie um pull request, comentado da forma mais detalhada possível.
* 4 - Pull requests só começarão a ser aceitos a partir do dia 08/07/2018.

### Detalhes de funcionamento:
* Primeiramente, os usuários devem se conectar a um roteador propriamente configurado para o CalcNet.
* Quando os alunos abrirem a calculadora, os seguintes dados serão solicitados: endereço ipv4 e porta de conexão. Essas informações deverão ser fornecidas pelo aplicador de prova.
* Ao se conectar com o roteador, haverá uma comunicação entre o computador do aplicador de prova e a calculadora do aluno. Nessa conexão inicial, serão enviados ao servidor o endereço MAC do dispositivo móvel utilizado pelo aluno, para identificar cada dispositivo na rede.
* O CalcNet fará no celular do aluno uma coleta periódica sobre os seguintes dados:
  * Nome do aplicativo em primeiro plano.
  * Status da sua conexão de dados móveis.
  * Status do seu bluetooth (on/off).
  * Status do modo avião (on/off).
* Esses dados serão coletados e enviados ao servidor periodicamente, com o intuito de constatar alguma fraude, durante a prova.
* Caso seja detectada alguma fraude pelo CalcNet, as medidas cabíveis devem ser tomadas pelo aplicador de prova.


### Características gerais
* Dispensa cadastro de usuário, e banco de dados.
* Intuitivo
* Baixo custo de implementação

### Desenvolvedores:

##### Implementação e documentação:
* Lucas Vieira de Jesus -> <lucas.engen.cc@gmail.com>.
* Christopher Lucas Israel dos Santos -> <dantecreedtutoriais@gmail.com>
##### Pesquisa de campo e documentação:
* Andressa de Fátima Araújo Miranda -> <andressaaraujo0505@gmail.com>
* Marcelo José Gomes Leitão -> <marcelomjgl@hotmail.com>
* Renan Cristyan Araújo Pinheiro -> <rcristyan9@gmail.com>

### Referências
<http://www.wolframalpha.com/> - Expansão em série de potência
