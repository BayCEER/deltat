@ECHO OFF
REM usage: DLDump [device] [baudrate] [file] [new|full]  
java -classpath delta-t-1.2.jar ui.DLDump %1 %2 %3 %4 
