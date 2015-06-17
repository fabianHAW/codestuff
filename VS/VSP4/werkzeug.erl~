-module(werkzeug).
-export([get_config_value/2,logging/2,logstop/0,openSe/2,openSeA/2,openRec/3,openRecA/3,createBinaryS/1,createBinaryD/1,createBinaryT/1,createBinaryNS/1,concatBinary/4,message_to_string/1,
		 shuffle/1,timeMilliSecond/0,reset_timer/3,compareNow/2,getUTC/0,compareUTC/2,now2UTC/1,
		 type_is/1,to_String/1,
		 bestimme_mis/2,testeMI/2]).
-define(ZERO, integer_to_list(0)).
-define(TAUS, 1000).
-define(MILL, 1000000).

-define(TTL, 1).

%% -------------------------------------------
% Werkzeug
%% -------------------------------------------
%%
%% -------------------------------------------
%%
% Sucht aus einer Config-Liste die gewünschten Einträge
% Beispielaufruf: 	{ok, ConfigListe} = file:consult("server.cfg"),
%                  	{ok, Lifetime} = get_config_value(lifetime, ConfigListe),
%
get_config_value(Key, []) ->
	{nok, Key};
get_config_value(Key, [{Key, Value} | _ConfigT]) ->
	{ok, Value};
get_config_value(Key, [{_OKey, _Value} | ConfigT]) ->
	get_config_value(Key, ConfigT).

%% -------------------------------------------
% Schreibt auf den Bildschirm und in eine Datei
% nebenläufig zur Beschleunigung
% Beispielaufruf: logging('FileName.log',"Textinhalt"),
%
logging(Datei,Inhalt) -> Known = erlang:whereis(logklc),
						case Known of
						undefined -> PIDlogklc = spawn(fun() -> logloop(0) end),
								 erlang:register(logklc,PIDlogklc);
								_NotUndef -> ok
						end,
						logklc ! {Datei,Inhalt},
						ok.

logstop( ) -> 	Known = erlang:whereis(logklc),
				case Known of
					undefined -> false;
					_NotUndef -> logklc ! kill, true
				end.
					
logloop(Y) -> 	receive
					{Datei,Inhalt} -> io:format(Inhalt),
									  file:write_file(Datei,Inhalt,[append]),
									  logloop(Y+1);
					kill -> true
				end.

%% -------------------------------------------
%%
% Unterbricht den aktuellen Timer
% und erstellt einen neuen und gibt ihn zurück
%%
reset_timer(Timer,Sekunden,Message) ->
	case timer:cancel(Timer) of
		{error, _Reason} ->
				neu;
		{ok, cancel} ->
				alt
 	end,
	{ok,TimerNeu} = timer:send_after(Sekunden*1000,Message),
	TimerNeu.
	
%% Zeitstempel: 'MM.DD HH:MM:SS,SSS'
% Beispielaufruf: Text = lists:concat([Clientname," Startzeit: ",timeMilliSecond()]),
%
timeMilliSecond() ->
	{_Year, Month, Day} = date(),
	{Hour, Minute, Second} = time(),
	Tag = lists:concat([klebe(Day,""),".",klebe(Month,"")," ",klebe(Hour,""),":"]),
	{_, _, MicroSecs} = now(),
	Tag ++ concat([Minute,Second],":") ++ "," ++ toMilliSeconds(MicroSecs)++"|".
toMilliSeconds(MicroSecs) ->
	Seconds = MicroSecs / ?MILL,
	%% Korrektur, da string:substr( float_to_list(0.234567), 3, 3). 345 ergibt
	if (Seconds < 1) -> CorSeconds = Seconds + 1;
	   (Seconds >= 1) -> CorSeconds = Seconds
	end,
	string:substr( float_to_list(CorSeconds), 3, 3).

getUTC() ->
	{MegaSecs, Secs, MicroSecs} = erlang:now(),
	% MicroSecs ist eine Zahl der Form ***000, deshalb div 1000
	((((MegaSecs * ?MILL) + Secs) * ?TAUS) + (MicroSecs div ?TAUS)).

compareUTC(UTC1,UTC2) ->
	case (UTC1 - UTC2) of
		X when X > 0 -> afterw;
		X when X < 0 -> before;
		X when X == 0 -> concurrent
	end.

now2UTC({MegaSecs, Secs, MicroSecs}) -> 	
	((((MegaSecs * ?MILL) + Secs) * ?TAUS) + (MicroSecs div ?TAUS));
now2UTC(UTCtimestamp) ->
	UTCtimestamp.
	
compareNow({MegaSecs1,Secs1,MicroSecs1},{MegaSecs2,Secs2,MicroSecs2}) ->
	Val1 = MegaSecs1 - MegaSecs2,
	if Val1 > 0 -> afterw;
	   Val1 < 0 -> before;
	   Val1 == 0 -> 
			Val2 = Secs1 - Secs2,
			if Val2 > 0	-> afterw;
			   Val2 < 0 -> before;
			   Val2 == 0 -> 
					Val3 = MicroSecs1 - MicroSecs2,
					if Val3 > 0 -> afterw;
					   Val3 < 0 -> before;
					   Val3 == 0 -> concurrent
					end
			end
	end.
	
concat(List, Between) -> concat(List, Between, "").
concat([], _, Text) -> Text;
concat([First|[]], _, Text) ->
	concat([],"",klebe(First,Text));
concat([First|List], Between, Text) ->
	concat(List, Between, string:concat(klebe(First,Text), Between)).
klebe(First,Text) -> 	
	NumberList = integer_to_list(First),
	string:concat(Text,minTwo(NumberList)).	
minTwo(List) ->
	case {length(List)} of
		{0} -> ?ZERO ++ ?ZERO;
		{1} -> ?ZERO ++ List;
		_ -> List
	end.

%% -------------------------------------------
% Ermittelt den Typ
% Beispielaufruf: type_is(Something),
%
type_is(Something) ->
    if is_atom(Something) -> atom;
	   is_binary(Something) -> binary;
	   is_bitstring(Something) -> bitstring;
	   is_boolean(Something) -> boolean;
	   is_float(Something) -> float;
	   is_function(Something) -> function;
	   is_integer(Something) -> integer;
	   is_list(Something) -> list;
	   is_number(Something) -> number;
	   is_pid(Something) -> pid;
	   is_port(Something) -> port;
	   is_reference(Something) -> reference;
	   is_tuple(Something) -> tuple
	end.
	
% Wandelt in eine Zeichenkette um
% Beispielaufruf: to_String(Something),
%
to_String(Etwas) ->
	lists:flatten(io_lib:format("~p", [Etwas])).	

% Oeffnen von UDP Sockets, zum Senden und Empfangen 
% Schliessen nicht vergessen: timer:apply_after(?LIFETIME, gen_udp, close, [Socket]),

% openSe(IP,Port) -> Socket
% diesen Prozess PidSend (als Nebenläufigenprozess gestartet) bekannt geben mit
%  gen_udp:controlling_process(Socket, PidSend),
% senden  mit gen_udp:send(Socket, Addr, Port, <MESSAGE>)
openSe(Addr, Port) ->
  io:format("~nAddr: ~p~nPort: ~p~n", [Addr, Port]),
  {ok, Socket} = gen_udp:open(Port, [binary, 	{active, false}, {reuseaddr, true}, {ip, Addr}, {multicast_ttl, ?TTL}, inet, 
												{multicast_loop, true}, {multicast_if, Addr}]),
  Socket.

% openRec(IP,Port) -> Socket
% diesen Prozess PidRec (als Nebenläufigenprozess gestartet) bekannt geben mit
%  gen_udp:controlling_process(Socket, PidRec),
% aktives Abholen mit   {ok, {Address, Port, Packet}} = gen_udp:recv(Socket, 0),
openRec(MultiCast, Addr, Port) ->
  io:format("~nMultiCast: ~p~nAddr: ~p~nPort: ~p~n", [MultiCast, Addr, Port]),
  {ok, Socket} = gen_udp:open(Port, [binary, 	{active, false}, {reuseaddr, true}, {multicast_if, Addr}, inet, 
												{multicast_ttl, ?TTL}, {multicast_loop, true}, {add_membership, {MultiCast, Addr}}]),
  Socket.

  % Aktives UDP-Socket:
% openSe(IP,Port) -> Socket
% diesen Prozess PidSend (als Nebenläufigenprozess gestartet) bekannt geben mit
%  gen_udp:controlling_process(Socket, PidSend),
% senden  mit gen_udp:send(Socket, Addr, Port, <MESSAGE>)
openSeA(Addr, Port) ->
  io:format("~nAddr: ~p~nPort: ~p~n", [Addr, Port]),
  {ok, Socket} = gen_udp:open(Port, [binary, 	{active, true}, {ip, Addr}, inet, 
												{multicast_loop, false}, {multicast_if, Addr}]),
  Socket.
 
% openRec(IP,Port) -> Socket
% diesen Prozess PidRec (als Nebenläufigenprozess gestartet) bekannt geben mit
%  gen_udp:controlling_process(Socket, PidRec),
% passives Empfangen mit   receive	{udp, ReceiveSocket, IP, InPortNo, Packet} -> ... end
openRecA(MultiCast, Addr, Port) ->
  io:format("~nMultiCast: ~p~nAddr: ~p~nPort: ~p~n", [MultiCast, Addr, Port]),
  {ok, Socket} = gen_udp:open(Port, [binary, 	{active, true}, {multicast_if, Addr}, inet, 
												{multicast_ttl, ?TTL}, {multicast_loop, false}, {add_membership, {MultiCast, Addr}}]),
  Socket.

% Nachrichtenpaket fertig stellen
createBinaryS(Station) ->
    % 1 Byte for Stationtype  
%	<<(list_to_binary(Station)):8/binary>>.
	<<(list_to_binary(Station))/binary>>.
createBinaryD(Data) ->
    % 24 Byte for Payload  
%	<<(list_to_binary(Data)):192/binary>>.
	<<(list_to_binary(Data))/binary>>.
createBinaryNS(NextSlot) ->
    % 1 Byte for NextSlot
%    <<NextSlot:8/integer>>.
    <<NextSlot>>.
createBinaryT(Timestamp) ->    
    % 8 Byte for Time  
    <<(Timestamp):64/big-unsigned-integer>>.	
concatBinary(BinStation,BinData,BinNextSlot,BinTime) ->         
    % Konkatenieren der Binaries: Nachrichtenformat pruefen!             
    <<BinStation/binary, BinData/binary,BinNextSlot/binary,BinTime/binary>>.

message_to_string(Packet)	->
%	Packet= <<BinStationTyp:8/binary,BinNutzdaten:192/binary,Slot:8/integer,Timestamp:64/integer>>
	StationTyp = erlang:binary_to_list(Packet,0,0),
    Nutzdaten= erlang:binary_to_list(Packet,1,24),
	Slot = erlang:binary_to_list(Packet,25,25),
	Timestamp = erlang:binary_to_list(Packet,26,33),
    {StationTyp,Nutzdaten,Slot,Timestamp}.
	
	
	
%% -------------------------------------------
%% Mischt eine Liste
% Beispielaufruf: NeueListe = shuffle([a,b,c]),
%
shuffle(List) -> shuffle(List, []).
shuffle([], Acc) -> Acc;
shuffle(List, Acc) ->
    {Leading, [H | T]} = lists:split(random:uniform(length(List)) - 1, List),
    shuffle(Leading ++ T, [H | Acc]).

	
%% -------------------------------------------
%
% initialisiert die Mi der ggT-Prozesse, um den
% gewünschten ggT zu erhalten.
% Beispielaufruf: bestimme_mis(42,88),
% 42: gewünschter ggT
% 88: Anzahl benötigter Zahlen
% 
%%
bestimme_mis(WggT,GGTsCount) -> bestimme_mis(WggT,GGTsCount,[]).
bestimme_mis(_WggT,0,Mis) -> Mis;
bestimme_mis(WggT,GGTs,Mis) -> 
	Mi = einmi([2,3,5,7,11,13,17],WggT),
	Enthalten = lists:member(Mi,Mis), 
	if 	Enthalten -> bestimme_mis(WggT,GGTs,Mis);
		true ->	bestimme_mis(WggT,GGTs-1,[Mi|Mis])
	end.	
% berechnet ein Mi
einmi([],Akku) -> Akku;	
einmi([Prim|Prims],Akku) ->
	Expo = random:uniform(3)-1, % 0 soll möglich sein!
	AkkuNeu = trunc(Akku * math:pow(Prim,Expo)), % trunc erzeugt integer, was für rem wichtig ist
	einmi(Prims,AkkuNeu).	

testeMI(WggT,GGTsCount) ->
		testeMis(bestimme_mis(WggT,GGTsCount),WggT).

testeMis([],_WggT) -> true;
testeMis([Num1|Rest],WggT) ->
	Val = Num1 rem WggT,
	case Val of
		0 -> testeMis(Rest,WggT);
		_X -> io:format("Zahl ~p Rest ~p\n",[Num1,Val]),testeMis(Rest,WggT)
	end.
		
		