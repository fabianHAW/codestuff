-module(hbq).

start() ->
	loop().
	
loop() ->
	receive 
		{Pid, {request, initHBQ}} ->
			io:format("HBQ init"),
			loop([[]], init);
	end
.

loop(MSGs, init) ->
	Pid ! {reply, ok},
	loop(MSGs, running);
loop(MSGs, running) ->
	receive
		{Pid, {request,pushHBQ,[NNr,Msg,TSclientout]}} ->
			MSGsN = pushHBQ(MSGs, [NNr, Msg, TSclientout]),
			Pid ! {reply, ok},
			loop(MSGsN, running);
		{Pid, {request,deliverMSG,NNr,ToClient}} ->
			%ToDo: Check MSG in HBQ or DLQ??
			Pid ! {reply, SendNNr},
			loop(MSGs, running);
		{Pid, {request,dellHBQ}} ->
			io:format("HBQ: Goodnight!")
	end
.

pushHBQ(MSGs, []) ->
	MSGs;
