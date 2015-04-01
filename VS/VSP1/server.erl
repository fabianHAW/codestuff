-module(server).
-export([start/1]).

start(Timer) ->
	io:format("Server started"),
	
	loop(Timer).
	
    
loop(Timer) ->
	receive 
		{Pid, getmsgid} ->
			io:format("Client ~p will Nachrichtennr", [Pid]),
			lifetimeTimer:resetTimer(Timer),
			loop();
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
		
	
    
   

   	
   	
