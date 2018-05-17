:: Este script faz parte do projeto CalcNet
:: Script para gerar automaticamente o número de versão do git
:: Autor: Lucas Vieira de Jesus <lucas.engen.cc@gmail.com>

:: 										***** ATENÇÃO *****
:: Este script tende a falhar quando executado diretamente do Microsoft Visual Studio 2017 em computadores AMD64
:: Ao invés de procurar o git na pasta %programfiles% ele procura o git na pasta %programfiles(x86)% mesmo que você
:: escreve %programfiles% corretamente.

@echo off
set saida_cs="%cd%\Server\CalcNetServer\Properties\Autorevision.cs"
set saida_java="%cd%\Client\app\src\main\java\unb\fga\calcnet\autorevision.java"
set caminho_base="%programfiles%\Git\bin\bash.exe"
set caminho_correto="%homedrive%\Program Files\Git\bin\bash.exe"

echo Pasta atual: %cd%
echo Procurando o bash em: %caminho_correto%

set var=%caminho_correto%

if not exist %caminho_correto% (
	echo Falha ao encontrar o bash em: %caminho_correto%
	echo Procurando o bash em: %caminho_base%
	if not exist %caminho_base% (
		echo Por favor, instale o git.
		color c
		goto end
	)
set var=%caminho_base%
)

echo bash encontrado em: %var%
color a
path %var%;%path%
%var% ./autorevision -t csharp > %saida_cs%
%var% ./autorevision -t java > %saida_java%
:end
color
echo Tarefas finalizadas
