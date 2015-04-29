-module(starter).
-import(werkzeug, [get_config_value/2, logging/2, timeMilliSecond/0]).
-export([start/1]).
-define(LOGFILE, lists:flatten(io_lib:format("~p.log", [node()]))).

start(StarterNummer) ->
	{{ok, PraktikumsGruppe},
	{ok, TeamNummer},
	{ok, NameserviceNode},
	{ok, NameserviceName},
	{ok, KoordinatorName}} = readConfig(),
	
	logging(?LOGFILE, lists:flatten(io_lib:format("Starter: ggt gelesen... ~n", []))),
	
	Pong = net_adm:ping(NameserviceNode),  %Ping an Erlang-Node.
	timer:sleep(1000),
	%Pang erhalten? Loggen!
	case Pong == pong of
		true ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Starter: Ping erfolgreich. ~n", [])));
		false ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Starter: Ping gescheitert!!!. ~n", [])))
	end,
	
	PIDns = global:whereis_name(NameserviceName), %Nameservice PID erfragen.
	
	%Nameservice PID erhalten? Loggen!
	case PIDns == undefined of
		true ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Starter: Nameservice bind gescheitert!!! ~n", [])));
		false ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Starter: Nameservice gebunden... ~n", [])))
	end,

	{NameserviceName, NameserviceNode} ! {self(), {lookup, KoordinatorName}},
	
	receive
		{pin, {KoNa, KoNo}} ->
			KN = {KoNa, KoNo}
	end,
	
	logging(?LOGFILE, lists:flatten(io_lib:format("Starter: Koordinator ~p gebunden... ~n", [KoNa]))),
	
	register('1', self()),	
	
	KN ! {self(), getsteeringval},
	
    receive 
			{steeringval, AZ, TZ, Quota, GGTPNr} -> 
				logging(?LOGFILE, lists:flatten(io_lib:format("Starter: getsteeringval: ~p Arbeitszeit ggT; ~p Wartezeit Terminierung ggT; ~p Abstimmungsquote ggT; ~p-te GGT Prozess.", [AZ, TZ, Quota, GGTPNr]))),
				startGGTs(AZ, TZ, GGTPNr, StarterNummer, PraktikumsGruppe,  TeamNummer, NameserviceName, NameserviceNode, KoordinatorName, Quota, 1)
	end,
 
	logging(?LOGFILE, lists:flatten(io_lib:format("Starter: ggt gelesen... ~n", [])))
	.

startGGTs(AZ, TZ, GGTPNr, StarterNummer, PraktikumsGruppe,  TeamNummer, NameserviceName, NameserviceNode, KoordinatorName, Quota, ProzessNummer) when ProzessNummer =< GGTPNr ->
	spawn(ggt, start, [AZ, TZ, ProzessNummer, StarterNummer, PraktikumsGruppe,  TeamNummer, NameserviceName, NameserviceNode, KoordinatorName, Quota]),
	%startGGTs(AZ, TZ, GGTPNr - 1, StarterNummer, PraktikumsGruppe,  TeamNummer, NameserviceName, NameserviceNode, KoordinatorName, Quota, ProzessNummer + 1) 
	startGGTs(AZ, TZ, GGTPNr, StarterNummer, PraktikumsGruppe,  TeamNummer, NameserviceName, NameserviceNode, KoordinatorName, Quota, ProzessNummer + 1) 
	;
startGGTs(_AZ, _TZ, _GGTPNr, _StarterNummer, _PraktikumsGruppe,  _TeamNummer, _NameserviceName, _NameserviceNode, _KoordinatorName, _Quota, _ProzessNummer) ->
	ok
	.

readConfig() ->
	{ok, CfgList} = file:consult("ggt.cfg"),
	logging(?LOGFILE, lists:flatten(io_lib:format("Starter: ggt geoeffnet... ~n", []))),
	{get_config_value(praktikumsgruppe, CfgList),
	get_config_value(teamnummer, CfgList),
	get_config_value(nameservicenode, CfgList),
	get_config_value(nameservicename, CfgList),
	get_config_value(koordinatorname, CfgList)}
	.
