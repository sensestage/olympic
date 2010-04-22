OlympicGames{

	var <>circle;
	var <histResp;

	var <>updateTime = 60;

	var <>oracle;
	var <>prophecy;
	var <>gui;

	var <>type; // \host, \player
	var <time;

	var <timeResp,<oracleResp;

	var <broadcast;

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

	asHost{
		type = \host;
	}

	createListeners{
		timeResp = OSCresponder(nil, '/game/timer', {|t,r,msg|
			msg.postln;
			time = msg[2];
		}).add;
		oracleResp = OSCresponder(nil, '/oracle', {|t,r,msg|
			msg.postln;
			oracle = msg[2];
		}).add;
	}

	startHistory{
		histResp = OSCresponder(nil, '/hist', {|t,r,msg| 
			History.enter(msg[2].asString, msg[1]) 
		}).add; 	
	
		History.start;
		//		History.makeWin; // this will have to be our own gui!
		History.forwardFunc = { |code|
			circle.send(\all, '/hist', circle.nickname, code) 
		};
		History.localOff;
	}

	makeGui{
		EZRepublicGui(republic: circle);
	}

	makeGameGui{
		^OlympicGui.new( circle.allIDs.keys.asArray, this )
	}

	startGames{
		if ( type == \host ){
			oracle = Oracle.new;
			GameTimer.makeOSC( broadcast );
			Tdef( \games,
				{
					loop{
						GameTimer.new( "new oracle in..", updateTime.floor, 1 );
						prophecy = oracle.consult( \UGen );
						circle.send(\all, '/oracle', circle.nickname, prophecy); 
						updateTime.wait;
						GameTimer.new( "new oracle in..", updateTime.floor, 1 );
						prophecy = oracle.consult( \Pattern );
						circle.send(\all, '/oracle', circle.nickname, prophecy); 
						updateTime.wait;
					}
				}
			);

			Tdef( \games ).play;
		}{
			this.createListeners;
		}
	}

}