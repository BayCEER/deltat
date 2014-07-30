#!/bin/bash
# usage: DLDump [device] [baudrate] [file] [new|full]  
java -cp delta-t-1.0.jar:log4j-1.2.15.jar:serial-1.0.jar:RXTXcomm.jar ui.DLDump $1 $2 $3 $4 
