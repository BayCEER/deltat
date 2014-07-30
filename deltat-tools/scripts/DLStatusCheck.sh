#!/bin/bash
# usage: DLStatusCheck <device> <baudrate>
java -cp delta-t-1.0.jar:log4j-1.2.15.jar:serial-1.0.jar:RXTXcomm.jar ui.DLStatusCheck $1 $2 
