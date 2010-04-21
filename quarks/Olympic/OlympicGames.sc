OlympicGames{

	var <>circle;
	var <histResp;

	var <>updateTime = 60;

	var <>oracle;
	var <>prophecy;
	var <>gui;

	*new{ |bcip, name|
		^super.new.init(bcip,name )
	}

	init{ |bcip, name|
		this.join(bcip,name)
	}

	join{ |bcip, name|
		NetAddr.broadcastFlag = true;
		Republic.fixedLangPort = false;
		circle = Republic(NetAddrMP( bcip ).ports_( (57120..57124) ) ).makeDefault;
		circle.join( name );
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
		^OlympicGui.new( Republic.default.allIDs.keys.asArray, this )
	}

	startGames{
		oracle = Oracle.new;
		Tdef( \games,
			{
				loop{
					GameTimer.new( "new oracle in..", 60, 1 );
					prophecy = oracle.consult( \UGen );
					updateTime.wait;
					GameTimer.new( "new oracle in..", 60, 1 );
					prophecy = oracle.consult( \Pattern );
					updateTime.wait;
				}
			}
		);

		Tdef( \games ).play;
		
	}

}