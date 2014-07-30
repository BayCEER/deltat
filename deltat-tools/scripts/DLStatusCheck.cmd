@ECHO OFF
REM usage: DLStatusCheck <device> <baudrate>
java -classpath delta-t-1.2.jar ui.DLStatusCheck %1 %2 
