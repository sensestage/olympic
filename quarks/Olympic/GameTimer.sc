GameTimer {
	classvar <w, <bounds, <doc, pop, startbut;
	classvar <all;
	classvar <uniqueID;
	var <task, <curtime,<name,<key,<endtime,<>post;
	classvar <sendTask, <sendIPs, <>sendOSC;

	*initClass { 
		all = IdentityDictionary.new;
		uniqueID = 0;
	}

	*at { arg key;
		^all.at(key);
	}

	*put { arg key, pattern;
		uniqueID = uniqueID + 1;
		all.put(key, pattern);
	}

	*remove{ arg key;
		all.removeAt( key ).task.stop;
	}

	*new{ |name,end,step=1,post=false|
		^super.new.init( name,end,step ).post_( post );
	}

	init{ |nm,end,step|
		var ns = (end/step).round(1);
		endtime = end;
		curtime = endtime;
		name = nm;
		key = uniqueID.asSymbol;
		//		key = (name+Date.localtime.stamp).asSymbol;
		task = Task{ 
			ns.do{
				if ( this.post, { 
				(name+"--"+curtime+"--"+
					this.class.formatTime(curtime)+"--"+this.class.formatTime(endtime)).postln; });
				 step.wait; curtime = curtime - step;
				 };
			this.class.remove( key ) 
			}.play;
		this.class.put( key, this );
	}

	*lineShorts{
		^all.collect{ |it,key| (""+key + " | " +
			this.formatTime(it.curtime)+" | " + this.formatTime(it.endtime) + " | " + it.name) }.asArray.sort.reverse;
	}

	*getTime{ |key=\last|
		if ( key == \last ){
			key = uniqueID - 1;
		};
		if ( all[key.asSymbol].notNil ){
			^this.formatTime( all[key.asSymbol].curtime );
		};
		^0
	}

	*formatTime { arg val;
			var h, m, s;
			h = val div: (60 * 60);
			val = val - (h * 60 * 60);
			m = val div: 60;
			val = val - (m * 60);
			s = val;
			^"%:%:%".format(h, m, s.round(0.01))
	}

	*makeWin { 
		var closebut, v, f, font;
		bounds = Rect(0, 0, 300, 200); 
		font = GUI.font.new( "Helvetica", 16 );
		w = GUI.window.new("Game Timers", bounds).front;	////
		
		v = GUI.listView.new(w,bounds.moveTo(0, 20))	////
			.items_([])
			.resize_(5)							////
			.background_(Color.grey(0.62))
		.font_( font );
		
		SkipJack({ 
			v.items_(this.lineShorts).value_(0);
		}, 1, { w.isClosed }, "GameTimerWin");
	^w;
	}
	
	*makeOSC { |ips|
		sendOSC = true;
		sendIPs = ips;
		sendTask = SkipJack({
			this.all.keysValuesDo{ |key,timer|
				sendIPs.do{ |it|
					it.sendMsg( "/game/timer", key, 
						GameTimer.getTime( key )
						//	(""+this.formatTime(timer.curtime)+" | " + this.formatTime(timer.endtime) + " | " + timer.name)
					);
				};
			};
		}, 1, { this.sendOSC.not }, "GameTimerOSC" );	
	}
}

