// check broadcast IPs
~ips = Republic.getBroadcastIPs.postcs;


a = OlympicGames.new( ~ips[0], "athlete" );
a.startHistory;

b= a.makeGameGui;
b.updateGui

NetAddr.langPort

a.startGames

(
SynthDef( \gray,
	{ |out=0|
		Out.ar( out, GrayNoise.ar( 0.4) )
	}
).share;
)

x = Synth.new( \gray, target: a.circle.servers.choose );

a.circle.servers

Pdef(\gray,
	Pmono(
		\gray,
		
	)
)

b.pviews[\athlete].;

a.histResp
History.makeWin;

History.current.hasMovedOn

b.pviews.first.update

b.participants

a.participants

Republic.default.allIDs.keys

{ SinOsc.ar( 200, 0, 0.2) }.play(\all);

a.makeGui;

History.current.indicesFor( \athlete )

History.current.lines.at( History.current.indicesFor( \athlete ) )

a.prophecy


Republic.default.allIDs;

a = OlympicGui.new( Republic.default.allIDs );

a.pviews.do{ |it| it.resize_( 9 )}


Help.gui;

OlympicGames.prophecy.asString

GameTimer.makeWin

TdefAllGui.new;

a.updateGui;

(
var xsize = 1024,ysize=800;
var w;
var ora;
var timer;
var pview;
var pviews;

var pvs,pvx,pvy;
var noParticipants = 8;
w = Window.new("Olympic Games", Rect(0,0,xsize,ysize));
		
// top - the oracle
ora = TextView( w, Rect( 0,0, xsize, ysize/4)).background_( Color.white ).align_( 'center' ).resize_( 2 );

// mid - the participants
pview = CompositeView( w, Rect( 0, ysize/4, xsize, ysize/2 )).resize_( 5 );
pview.addFlowLayout(2@2,2@2);
		
// initialise array with 0's
//		pvs = Array.fill( noParticipants, 0 );
pvx = (xsize - ((noParticipants / 2)*2 + 4))/(noParticipants / 2);
pvy = (ysize/4) - 4;

pviews = 8.collect{ |key,val| 
	TextView( pview, pvx@pvy ).background_( Color.magenta ).string_( key ).resize_( 5 );
};

// bottom - some kind of timer
timer = TextView( w, Rect( 0,3*ysize/4, xsize, ysize/4)).background_( Color.white ).align_('center').resize_( 8 );	
w.front;
);

.unixCmdGetStdOut