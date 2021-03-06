OlympicGames{

	var <>circle;
	var <histResp;

	var <>updateTime = 60;

	var <>oracle;
	var <>prophecy = "";
	var <>gui;

	var <>type; // \host, \player
	var <time = 0;

	var <timeResp,<oracleResp;

	var <broadcast;

	var s;

	*new{ |bcip, name|
		^super.new.init(bcip,name )
	}

	init{ |bcip, name|
		this.join(bcip,name);
		type = \player;
	}

	join{ |bcip, name|
		broadcast = NetAddrMP( bcip ).ports_( (57120..57124) );
		NetAddr.broadcastFlag = true;
		Republic.fixedLangPort = false;
		circle = Republic( broadcast ).makeDefault;
		circle.join( name );
	}

	s{
		^circle.s;
	}

	host{
		^circle.servers[\host];
	}

	me{
		^circle.servers[circle.nickname];
	}

	random{
		^circle.servers.choose;
	}

	asHost{
		type = \host;
	}

	/*
	makeDefault{
		Server.default = circle.s;
	}
	*/

	createListeners{
		timeResp = OSCresponder(nil, '/game/timer', {|t,r,msg|
			//	msg.postln;
			time = msg[2];
		}).add;
		oracleResp = OSCresponder(nil, '/oracle', {|t,r,msg|
			//	msg.postln;
			prophecy = msg[1];
		}).add;
	}

	startHistory{
		histResp = OSCresponder(nil, '/hist', {|t,r,msg| 
			//	("histResp"+msg).postln;
			//	msg.postcs;
			if ( msg[2].asString.size > 0 ){
				History.enter(msg[2].asString, msg[1]);
			};
		}).add; 	
	
		History.start;
		//		History.makeWin; // this will have to be our own gui!
		History.forwardFunc = { |code|
			//	("hist forward" + code).postln;
			circle.send(\all, '/hist', circle.nickname, code) 
		};
		History.localOff;
	}

	makeGui{
		EZRepublicGui(republic: circle);
	}

	makeGameGui{
		^OlympicGui.new( this )
	}

	participants{
		^circle.allIDs.keys.asArray;
	}

	startGames{
		if ( type == \host ){
			oracle = Oracle.new;
			GameTimer.makeOSC( broadcast );
			Tdef( \games,
				{
					loop{
						[\UGen, \Pattern].do{ |it|
							if ( prophecy.notNil ){
								//	History.enter( Oracle.asString( prophecy ), circle.nickname );
								//	prophecy.postln;
								circle.send(\all, '/hist', circle.nickname, 
									prophecy
								); 
							};
							GameTimer.new( "new oracle in..", updateTime.floor, 1 );
							prophecy = Oracle.asString( oracle.consult( it ) );
							broadcast.sendMsg( '/oracle', prophecy ); 
							updateTime.wait;
						};
					}
				}
			);

			Tdef( \games ).play;
		}{
			this.createListeners;
		}
	}

}