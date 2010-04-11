OlympicGames{

	classvar <>circle;
	classvar <histResp;

	*join{ |bcip, name|
		NetAddr.broadcastFlag = true;
		Republic.fixedLangPort = false;
		circle = Republic(NetAddrMP( bcip ).ports_( (57120..57124) ) ).makeDefault;
		circle.join( name );
	}

	*startHistory{
		histResp = OSCresponder(nil, '/hist', {|t,r,msg| 
			History.enter(msg[2].asString, msg[1]) 
		}).add; 	
	
		History.start;
		History.makeWin; // this will have to be our own gui!
		History.forwardFunc = { |code|
			circle.send(\all, '/hist', circle.nickname, code) 
		};
		History.localOff;
	}

	*makeGui{
		EZRepublicGui(republic: circle);
	}

}