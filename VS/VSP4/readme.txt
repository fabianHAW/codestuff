************start stations************

$ ./startStations.sh <interface> <multicast-address> <receive-port> <from-station-index> <to-station-index> <station-class> [ <UTC-offset-(ms)> ]

e.g.:
$ ./startStations.sh wlan0 225.10.1.2 15001 1 5 A 1000

***************************************

*******normal close the stations*******

(w)erl -(s)name close -setcookie kekse
1> make:all(). 
2> send_kill:start(From, To)

From := index of first station
To := index of last station
The two indices must be same you gave to startStations.sh above!!

***************************************

*******hard close the stations********

If there occures an exception, which we don't think, you can kill the processes with a hard kill-script:

$ ./killStations.sh <from-station-index> <to-station-index>
The two indices must be same you gave to startStations.sh above!!

e.g.:
$ ./killStations.sh 1 5

***************************************
