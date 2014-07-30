#!/bin/bash
echo -e "btg5x3\t$1\t$2\t"$3"\n" | /usr/bin/send_nsca -H 132.180.14.1 -c send_nsca.cfg
