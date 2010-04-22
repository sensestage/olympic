+ Symbol {

	asTarget{
		var republic = republic ? Republic.default;
		if(republic.notNil) { 
			^republic.servers.at( this );
		}
		^Server.default;
	}
	
}