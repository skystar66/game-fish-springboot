@echo off
setlocal enabledelayedexpansion

echo ��ʼ����proto����...
echo.

FOR %%p in (*.proto) do (����
	set proto=!proto!%%p 
)

echo %proto%
protoc --java_out E:/Project/game-server2/game-server/game-message/src/main/java ./%proto%

::����protobuf��game-manage��̨
xcopy *.proto E:\Project\game-server2\game-server\game-manage\src\main\webapp\assets\proto /s /h

echo.
echo ִ�����...
echo.
PAUSE 