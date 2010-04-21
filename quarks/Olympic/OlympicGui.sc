OlympicGui{

	classvar <>xsize=1024;
	classvar <>ysize=800;

	var <w;
	var <ora;
	var <timer;
	var <pview;

	var <pviews;

	var <updater;
	var <game;

	*new{ |participants,game|
		^super.new.init(participants,game);
	}

	init{ |participants,g|
		var pvs,pvx,pvy;
		var noParticipants = participants.size;

		game = g;
		w = Window.new("Olympic Games", Rect(0,0,xsize,ysize));
		
		// top - the oracle
		ora = TextView.new( w, Rect( 0,0, xsize, ysize/4)).background_( Color.white ).align_( 'center' ).resize_( 2 ).string_( "Oracle" ).font_( Font( "Helvetica", 40) );

		// mid - the participants
		pview = CompositeView.new( w, Rect( 0, ysize/4, xsize, ysize/2 )).resize_( 5 );
		pview.addFlowLayout(2@2,2@2);
		
		// initialise array with 0's
		//		pvs = Array.fill( noParticipants, 0 );
		pvx = (xsize - ((noParticipants / 2)*2 + 4))/(noParticipants / 2);
		pvy = (ysize/4) - 4;
		
		pviews = participants.collect{ |key,val| 
			OlympicAthleteGui.new( key, pview, pvx@pvy )
		};

		// bottom - some kind of timer
		timer = StaticText.new( w, Rect( 0,3*ysize/4, xsize, ysize/4)).background_( Color.white ).align_('center').resize_( 8 ).string_( "TIMER").font_( Font( "Helvetica", 120) );	
		w.front;

		updater = SkipJack.new(
			{ this.updateGui },
			0.5,
			{ w.isClosed },
			"OlympicGuiUpdater"
		);
		History.current.hasMovedOn = true;
	}

	updateGui{
		var pstring = "";
		game.prophecy.do{ |it|
			if ( it.isKindOf( Array )){
				pstring = pstring + "\n";
				it.do{ |jt|
					pstring = pstring + jt + "\t";
				};
			}{
				pstring = pstring + it + "\t";

			}
		};
		ora.string_( pstring );
		timer.string_( GameTimer.getTime; );
			//			GameTimer.lineShorts.asString );

		if ( History.current.hasMovedOn ) {
			pviews.do{ |it| it.update };
			History.current.hasMovedOn = false;
		};
	}
}

OlympicAthleteGui{

	var <>name,<>parent,<>bounds;

	var <view,<list;

	var lastLineSelected = 0, lastLinesShown;
	var selectedLine, linesToShow;


	*new{ |name,parent,bounds|
		^super.new.name_(name).parent_(parent).bounds_(bounds).init;
	}

	init{
		view = CompositeView.new( parent,bounds );
		view.addFlowLayout( 0@0, 0@0 );
		
		list = ListView.new( view, bounds ).font_( Font.new("Helvetica",20 ) );
		list.items_([])
			.resize_(5)
			.background_(Color.grey(0.62))
		/*
			.action_({ |lview|
				var index = lview.value;
				if (lview.items.isEmpty) {
					"no entries yet.".postln;
				} {
					lastLineSelected = list.items[index];
					//	this.postInlined(index)
				}
			})
			.enterKeyAction_({ |lview|
				var index = lview.value;
				if (filtering) { index = filteredIndices[index] };
				try {
					History.current.lines[index][2].postln.interpret.postln;
				//	"did execute.".postln;
				} {
					"execute line from history failed.".postln;
				};
			});
		*/
	}

	update{
		var newIndex;
		selectedLine = (lastLinesShown ? [])[list.value];
		linesToShow = History.current.lines.at( 
			History.current.indicesFor( name );
		);
		if (linesToShow != lastLinesShown) {
			//	"or updating listview here?".postln;
			list.items_(linesToShow.collect{ |it| it[2] });
			lastLinesShown = linesToShow;
		};
		newIndex = if (selectedLine.isNil) 
		{ 0 }
		{ linesToShow.indexOf(selectedLine) };
			
		list.value_(newIndex ? 0);
	}
}