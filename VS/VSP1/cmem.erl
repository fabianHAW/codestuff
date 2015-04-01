-module(cmem).
-export(lastVisit/2).


lastVisit([[]], Pid) ->
	{nok, Pid}.
lastVisit([[Pid, NNr, LastVisit] | Cmem], Pid) LastVisit -->
	
