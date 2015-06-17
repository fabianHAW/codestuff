#!/bin/bash
# 
# This programme is provided 'As-is', without any guarantee of any kind, implied or otherwise and is wholly unsupported.
# You may use and modify it as long as you state the above copyright.
#
# Hard-Kill script for n stations.
#
# Parameters:
#              index of first station
#              index of last station
#
# Example:  killStations.sh 2 11
# 
#           will kill ten stations 2 to 11.
#
# To use this script assign the appropriate values to the variables below.
#
#

firstIndex=$1
lastIndex=$2


printUsage() {
	echo "Usage: $0 <from-station-index> <to-station-index>"
	echo "index must be same you gave to startStations.sh"
	echo "Example: $0 1 10"
}


if [ $# -gt 1 ]
then
	if [ $firstIndex == ${firstIndex//[^0-9]/} -a $lastIndex == ${lastIndex//[^0-9]/} ] 
	then
	
		if [ $firstIndex -le $lastIndex ]
		then
			for i in `seq $firstIndex $lastIndex`
			do
				erl -sname kill_nodes$i -setcookie kekse -noshell -s kill_nodes start $i &
			done
			rc_status=0
		else
			echo "First index must not be greater than last index"
			printUsage
			rc_status=1
		fi
		
	else
		echo "Indexes must be integers."
		printUsage
		rc_status=1
	fi
	
else
	echo "Not enough parameters specified."
	printUsage
	rc_status=1
fi

exit $rc_status
