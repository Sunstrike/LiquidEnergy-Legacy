ECHO OFF
ECHO (Windows CMD) Linking source + APIs with MKLINK /J...
MKLINK /J build\forge\mcp\src\minecraft\io common\io
MKLINK /J build\forge\mcp\src\minecraft\buildcraft common\buildcraft
MKLINK /J build\forge\mcp\src\minecraft\ic2 common\ic2
ECHO ================== WARNING! ==================
ECHO REMOVE THE JUNCTIONS MANUALLY BEFORE REBUILD!
ECHO Failure to do so will wipe your source tree.
ECHO ==============================================