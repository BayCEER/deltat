#!/bin/bash
# Usage: DLtoNagios <device> <baudrate> <name>
java -cp delta-t-1.0.jar:log4j-1.2.15.jar:serial-1.0.jar:RXTXcomm.jar ui.DLtoNagios $1 $2 $3  
