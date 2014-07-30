#!/bin/bash
# Dump logger device into a random file
# usage: DLDumpDev <device> <baudrate> <new|full>
# Oliver Archner 
# 22.04.2009
DEST="/var/local/export"
YEAR=$(date +"%Y")
DEV=${1:5:${#1}}
FILE="$DEST/oebg.logger_$DEV.$$.$YEAR.bin"
logger "Dumping Logger $1 into $FILE"
./DLDump.sh $1 $2 $FILE $3



