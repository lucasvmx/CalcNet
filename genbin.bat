:: Este arquivo faz parte do Calcnet
:: Script para unir todos os binarios em uma unica pasta
:: Autor: Lucas Vieira de Jesus

@echo off
mkdir bin
copy /y client\android\app\release\app-release.apk bin\calcnet.apk>nul
copy /y client\android\app\release\output.json bin\apk_output.json>nul
copy /y server\windows\calcnetserver\bin\release\*.exe bin\>nul
copy /y server\windows\calcnetserver\bin\release\*.dll bin\>nul