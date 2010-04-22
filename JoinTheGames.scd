// check broadcast IPs
~ips = Republic.getBroadcastIPs.postcs;

a = OlympicGames.new( ~ips[0], "athlete" );
a.startHistory;

b= a.makeGameGui;

a.asHost;
a.startGames

GameTimer.getTime(\last)

GameTimer.makeWin;

(
SynthDef( \gray,
	{ |out=0|
		Out.ar( out, GrayNoise.ar( 0.4) )
	}
).share;
)

x = Synth.new( \gray, target: a.circle.servers.choose );

