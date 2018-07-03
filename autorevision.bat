:: Este script faz parte do projeto CalcNet
:: Script para gerar automaticamente o número de versão do git
:: Autor: Lucas Vieira de Jesus <lucas.engen.cc@gmail.com>

:: 					***** ATENÇÃO *****
:: Este script tende a falhar quando executado diretamente do Microsoft Visual Studio 2017 em computadores AMD64
:: Ao invés de procurar o git na pasta %programfiles% ele procura o git na pasta %programfiles(x86)% mesmo que você
:: escreva %programfiles% corretamente.

:: É importante criar a variável de ambiente com o nome 'bash' e colocar nela o caminho do executável.
:: exemplo: C:\Program Files\Git\bin\bash.exe

@echo off
set saida_cs="%cd%\Server\Windows\CalcNetServer\Properties\Autorevision.cs"
set saida_java="%cd%\Client\app\src\main\java\unb\fga\calcnet\autorevision.java"
set argumento=%~1

if [%argumento%]==[] (
echo.
echo Uso: %~0 [tipo]
echo.
echo Onde 'tipo' deve ser um dos seguintes parametros:
echo cliente_servidor
echo cliente
echo servidor
echo.
goto end
)

echo Pasta atual: %cd%
echo Local do bash: %bash%
path

if /I %argumento%==cliente_servidor ( 
	"%bash%" ./autorevision -t csharp > %saida_cs%
	"%bash%" ./autorevision -t java > %saida_java%
	goto end
)

if /I %argumento%==cliente ( 
	"%bash%" ./autorevision -t java > %saida_java%
	goto end
)

if /I %argumento%==servidor (
	"%bash%" ./autorevision -t csharp > %saida_cs%
	goto end
)

:end
color
echo Tarefas finalizadas
