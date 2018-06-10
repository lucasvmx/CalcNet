:: Este arquivo faz parte do Calcnet
:: Script para unir todos os binarios em uma pasta só
:: Autor: Lucas Vieira de Jesus

@echo off
copy /y client\app\release\app-release.apk bin\calcnet.apk>nul
copy /y client\app\release\output.json bin\apk_output.json>nul
copy /y server\calcnetserver\bin\release\*.exe bin\>nul
copy /y server\calcnetserver\bin\release\*.dll bin\>nul