// check broadcast IPs
~ips = Republic.getBroadcastIPs.postcs;

a = OlympicGames.new( ~ips[0], "host" );
a.startHistory;

// only the host should be the host:
// 
a.asHost;
a.startGames

b= a.makeGameGui;

(
SynthDef( \gray,
	{ |out=0,amp=0.1|
		Out.ar( out, RLPF.ar( GrayNoise.ar( amp ), 200, 0.1) );
	}
).share;
)


x = Synth.new( \gray, target: a.host; );

x.free;

x = Synth.new( \gray, target: a.me );

x.free;


// e.g. share a SynthDef with all participants
(
SynthDef(\xxy, {|out, sustain = 1, freq = 440|
	var env = Line.kr(0.1, 0, sustain, doneAction: 2);
	var son = SinOsc.ar(freq * [1, 1.2, 1.5, 1.7]).sum;
	Out.ar(out, son * env);
}).share;
)


// and send a synth to everyone:
c = (server: a.host, instrument: \xxy, freq: exprand(300, 1000)).play;


// or to a random member:
(server: a.random, instrument: \xxy, freq: exprand(300, 1000)).play;

// or to a specific member (replace \XXX with someone's nickname)
(server: a.s, where: \host, instrument: \xxy, freq: exprand(300, 1000)).play;

// or to a specific member (replace \XXX with someone's nickname)
(server: a.s, where: \XXX, instrument: \xxy, freq: exprand(300, 1000)).play;

(server: a.s, instrument: \xxy, where: \XXX, freq: exprand(300, 1000)).play;

(
Pdef(\x,
	Pbind(
		\instrument, \xxy,
		\server, a.s,
		\where, Prand([\host], 8), // 
		\dur, 1,
		\legato, 0.3, 
		\freq, Pwhite().linexp(0, 1, 500, 2000)
	).trace
).play;
)
