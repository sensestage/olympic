OlympicGames{

	classvar <>circle;

	*join{ |bcip, name|
		NetAddr.broadcastFlag = true;
		Republic.fixedLangPort = false;
		circle = Republic(NetAddrMP( bcip ).ports_( (57120..57124) ) ).makeDefault;
		circle.join( name );
	}

	*makeGui{
		EZRepublicGui(republic: circle);
	}

}