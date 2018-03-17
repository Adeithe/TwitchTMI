@ECHO Off
RMDIR /S /Q %~dp0docs 2>nul
MKDIR %~dp0docs
CALL mvn javadoc:javadoc
ROBOCOPY %~dp0target\site\apidocs %~dp0docs /S /MOV
PAUSE