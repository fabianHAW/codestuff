-module(koordinator).
-import(werkzeug, [get_config_value/2, logging/2, timeMilliSecond/0, shuffle/1]).
-export([start/0]).
-define(LOGFILE, lists:flatten(io_lib:format("~p.log", [node()]))).

%Umsetzung der Anforderungen "Modul: Koordinator" Anf.-Nr. 1
start() ->
	logging(?LOGFILE, lists:flatten(io_lib:format("~p: Startzeit ~p | mit PID ~p. ~n", [node(), timeMilliSecond(), self()]))),
	{{ok, Arbeitszeit}, 
	{ok, Termzeit}, 
	{ok, Ggtprozessnummer}, 
	{ok, Nameservicenode}, 
	{ok, Nameservicename}, 
	{ok, Koordinatorname}, 
	{ok, Quote}, 
	{ok, Korrigieren}} = readConfig(), %Config-Werte auslesen.
	Pang = net_adm:ping(nameservices),  %Ping an Erlang-Node.
	
	%Pang erhalten? Loggen!
	case Pang == pang of
		true ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: Ping erfolgreich. ~n", [])));
		false ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: Ping gescheitert!!!. ~n", [])))
	end,
	
	PIDns = global:whereis_name(Nameservicename), %Nameservice PID erfragen.
	
	%Nameservice PID erhalten? Loggen!
	case PIDns == undefined of
		true ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: Nameservice bind gescheitert!!! ~n", [])));
		false ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: Nameservice PID empfangen: ~p ~n", [PIDns])))
	end,
	
	timer:sleep(2*1000), %2 Sekunden warten um Nameservice Zeit zu geben.
	true = register(Koordinatorname, self()), %Bei Erlang-Node registrieren.
	logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: Erfolgreich registriert. ~n", []))),
	PIDns ! {self(), {rebind, Koordinatorname, node()}}, %An Nameservice binden.
	
	receive
		ok ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: Erfolgreich an Nameservice gebunden. ~n", [])))
	end,
	
	loop(Arbeitszeit, Termzeit, Ggtprozessnummer, Nameservicenode, Nameservicename, Koordinatorname, Quote, Korrigieren, PIDns, [], undef)
	.
%AZ := Arbeitszeit; TZ := Termzeit; GGTPNr := GGT-Prozessnummer; NameSno := NameserviceNode
%NameSna := NameserviceName; KN := Koordinatorname; QUO := Quote; KOR := Korrigieren
%PIDns := PID-Nameservice; CMD := Command; GGTL := GGT-Prozessliste
loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, CMD) when CMD /= step ->
	receive
	%%%%********************************************************************************************************************%%%%
	%%%%*****************************************Initialiserungsphase*******************************************************%%%%
	%%%%********************************************************************************************************************%%%%
		{StarterPID, getsteeringval} ->
			Quota = round(QUO / 100) * 1, %ToDo: Anzahl erwarteter Prozesse statt 1
			StarterPID ! {steeringval, AZ, TZ, Quota, GGTPNr},
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, CMD);
		{hello, GGTName} ->
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL ++ GGTName, CMD);
		{step} ->
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, createRing(GGTL, PIDns), step);
	%%%%********************************************************************************************************************%%%%
	%%%%*****************************************Arbeitsphase***************************************************************%%%%
	%%%%********************************************************************************************************************%%%%
	
	%%%%********************************************************************************************************************%%%%
	%%%%*****************************************Beendigungsphase***********************************************************%%%%
	%%%%********************************************************************************************************************%%%%
		{reset} ->
			print("reset nicht implementiert ~n");
		{prompt} ->
			print("prompt nicht implementiert ~n");
		{nudge} ->
			print("nudge nicht implementiert ~n");
		{toggle} ->
			print("toggle nicht implementiert ~n");
		{kill} ->
			print("kill nicht implementiert ~n")
	end	
	.
	
createRing(GGTL, PIDns) ->
	createRing(werkzeug:shuffle(GGTL), 1, PIDns),
	GGTL.
createRing(GGTL, Idx, PIDns) ->
	PIDns ! {self(), {lookup, lists:nth(length(GGTL))}},
	receive
		{pin, {GGTName, GGTNode}} ->
			{GGTName, GGTNode} ! {setneighbors,getLneighbor(Idx, PIDns, GGTL), getRneighbor(Idx, PIDns, GGTL)},
			createRing(GGTL, Idx + 1, PIDns)
	end
	.

getRneighbor(Idx, PIDns, GGTL) when Idx == length(GGTL) ->
	PIDns ! {self(), {lookup, lists:nth(1, GGTL)}},
	receive
		{pin, {GGTName, GGTNode}} ->
		Rneighbor = {GGTName, GGTNode}
	end,
	Rneighbor
	;
getRneighbor(Idx, PIDns, GGTL) ->
	PIDns ! {self(), {lookup, lists:nth(Idx + 1, GGTL)}},
	receive
		{pin, {GGTName, GGTNode}} ->
		Rneighbor = {GGTName, GGTNode}
	end,
	Rneighbor
	.
	
getLneighbor(1, PIDns, GGTL) when  length(GGTL) > 1 ->
	PIDns ! {self(), {lookup, lists:nth(length(GGTL), GGTL)}},
	receive
		{pin, {GGTName, GGTNode}} ->
		Lneighbor = {GGTName, GGTNode}
	end,
	Lneighbor
	;
getLneighbor(Idx, PIDns, GGTL) ->
	PIDns ! {self(), {lookup, lists:nth(Idx - 1, GGTL)}},
	receive
		{pin, {GGTName, GGTNode}} ->
		Lneighbor = {GGTName, GGTNode}
	end,
	Lneighbor
	.
	
readConfig() ->
	{ok, CfgList} = file:consult("koordinator.cfg"),
	{get_config_value(arbeitszeit, CfgList),
	get_config_value(termzeit, CfgList),
	get_config_value(ggtprozessnummer, CfgList),
	get_config_value(nameservicenode, CfgList),
	get_config_value(nameservicename, CfgList),
	get_config_value(koordinatorname, CfgList),
	get_config_value(quote, CfgList),
	get_config_value(korrigieren, CfgList)}
	.
	
print(String) ->
	_ok = io:format(String)
	.
