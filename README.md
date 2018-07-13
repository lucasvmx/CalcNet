[![Licence](https://img.shields.io/badge/license-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0.en.html)

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

### Softwares úteis para o desenvolvimento
* **Microsoft Visual** Studio: https://www.visualstudio.com/pt-br/downloads
* **Android Studio**: https://developer.android.com/studio/?hl=pt-br
* **Git**: https://git-scm.com/downloads
* **Microsoft Visio**: https://products.office.com/pt-br/visio/flowchart-software?tab=tabs-1

### Como compilar
* **Cliente**: 
 * Baixe o Android Studio e abra o projeto.
 * Execute o script autorevision.bat
 * Volte ao Android Studio e clique em **Build**.
* **Servidor**:
 * Baixe o Microsoft Visual Studio (2017 de preferência)
 * Abra o projeto e depois clique em **Build Project**

### Como contribuir
* 1 - Faça um fork do projeto.
* 2 - Realize as suas alterações.
* 3 - Nos envie um pull request, comentado da forma mais detalhada possível (em português ou inglês).
* 4 - Pull requests só começarão a ser aceitos a partir do dia 08/07/2018.

### Detalhes de funcionamento:
* Primeiramente, os usuários devem se conectar a um roteador propriamente configurado para o CalcNet.
* Quando os alunos abrirem a calculadora, os seguintes dados serão solicitados: nome de usuário, endereço IPV4 e porta de conexão. O IP e a porta serão fornecidas pelo aplicador de prova.
* Ao se conectar com o roteador, haverá uma comunicação entre o computador do aplicador de prova e a calculadora do aluno. Nessa conexão inicial, serão enviados ao servidor o número de série do dispositivo móvel utilizado pelo aluno, para identificar cada dispositivo na rede.
* O CalcNet fará no celular do aluno uma coleta periódica sobre os seguintes dados:
  * Número de série.
  * Status do seu bluetooth (on/off).
  * Status do modo avião (on/off).
* Esses dados serão coletados e enviados ao servidor periodicamente, com o intuito de constatar alguma fraude, durante a prova.
* Caso seja detectada alguma fraude pelo CalcNet, as medidas cabíveis devem ser tomadas pelo aplicador de prova.

### Características gerais
* Dispensa cadastro de usuário, e banco de dados.
* Intuitivo.
* Baixo custo de implantação e manutenção.

### Desenvolvedores:

##### Implementação e documentação:
* Lucas Vieira de Jesus -> <lucas.engen.cc@gmail.com>.

##### Pesquisa de campo e documentação:
* Marcelo José Gomes Leitão -> <marcelomjgl@hotmail.com>
* Renan Cristyan Araújo Pinheiro -> <rcristyan9@gmail.com>

### Referências
<http://www.wolframalpha.com/> - Expansão em série de potência.
.
