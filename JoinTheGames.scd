// check broadcast IPs
~ips = Republic.getBroadcastIPs.postcs;

a = OlympicGames.new( ~ips[0], "host" );
a.startHistory;

// only the host should be the host:
// a.asHost;
a.startGames

// Two ways to grab code from the GUI
// - by opening a Window with a TextView (gui method)
// - by posting the code into a new document (only scapp and scel at the moment)
OlympicGui.useDocClass = true; // by default it is false, so the GUI method

// create the game gui:
b= a.makeGameGui;


// In the backend of the games the Republic is used, so also take a look at the helpfiles of that quark to learn about coding in the republic environment

// this sets up the Republic style GUI:
a.makeGui


// creating a synthdef and sharing it with others
(
SynthDef( \gray,
	{ |out=0,amp=0.1|
		Out.ar( out, RLPF.ar( GrayNoise.ar( amp ), 200, 0.1) );
	}
).share;
)

// playing it at the host:
x = Synth.new( \gray, target: a.host; );

x.free;

// playing it locally:
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

// using a pattern:
(
Pdef(\x,
	Pbind(
		\instrument, \xxy,
		\server, a.s, // a.s chooses the republic server
		\where, Prand([\host], 8), // where controls the targer
		\dur, 1,
		\legato, 0.3, 
		\freq, Pwhite().linexp(0, 1, 500, 2000)
	).trace
).play;
)
