-module(koordinator).
-import(werkzeug, [get_config_value/2, logging/2, timeMilliSecond/0]).
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
	
	loop(Arbeitszeit, Termzeit, Ggtprozessnummer, Nameservicenode, Nameservicename, Koordinatorname, Quote, Korrigieren, PIDns)
	.
		
loop(AZ, TZ, GGTPNr, NameSn, Nameservicename, Koordinatorname, Quote, Korrigieren, PIDns) ->
	print("loop Noch nicht implementiert! ~n")
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
