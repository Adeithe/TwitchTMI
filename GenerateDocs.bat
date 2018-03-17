@ECHO Off
RMDIR /S /Q docs 2>nul
MKDIR docs
CALL mvn javadoc:javadoc
MOVE %~dp0\target\site\apidocs\* %~dp0\docs
PAUSE