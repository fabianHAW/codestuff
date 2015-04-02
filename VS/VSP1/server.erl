-module(server).
-export([start/1]).

start(Timer) ->
	io:format("Server started"),
	
	loop(Timer, 1).
	
    
loop(Timer, INNR) ->
	receive 
		{Pid, getmsgid} ->
			io:format("Client ~p will Nachrichtennr", [Pid]),
			Pid ! {nid, INNR},
			loop(lifetimeTimer:resetTimer(Timer), INNR + 1);
		{dropmessage, [INNR, Msg, TSclientout]} ->
			io:format("Client sendet Nachricht"),
			lifetimeTimer:resetTimer(Timer),
			loop();
		{Pid, getmessages} ->
			io:format("Client ~p will Nachricht", [Pid]),
			lifetimeTimer:resetTimer(Timer),
			loop();
		{srvtimeout} ->
			io:format("Server Timeout!")
	end
.
		
	
    
   

   	
   	
