*************Wichtige Info************
You need to create an log/-dir, for the log-files to start the application!

************start stations************

$ ./startStations.sh <interface> <multicast-address> <receive-port> <from-station-index> <to-station-index> <station-class> [ <UTC-offset-(ms)> ]

e.g.:
$ ./startStations.sh lo 225.10.1.2 15001 1 5 A 0

***************************************

*******normal close the stations*******

At the moment the receiver-process not terminate! Take the other way (via rpc) to close the application right.

(w)erl -(s)name close -setcookie kekse
1> make:all(). 
2> send_kill:start(From, To)

From := index of first station
To := index of last station
The two indices must be same you gave to startStations.sh above!!

***************************************

**close via rpc**

If there occures an exception, or the kill-command not close all processes, you can kill the processes like:

$ ./killStations.sh <from-station-index> <to-station-index> 
The two indices must be same you gave to startStations.sh above!!

e.g.:
$ ./killStations.sh 1 5

***************************************
