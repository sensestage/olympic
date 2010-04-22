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
		var myXsize, myYsize, fontSize, bannerSize;
		var noParticipants = participants.size;

		game = g;

		if ( game.type == \host ){
			myXsize = xsize;
			myYsize = ysize;
			fontSize = 20;
			bannerSize = 170;
		}{
			myXsize = 1000;
			myYsize = 400;
			fontSize = 12;
			bannerSize = 60;
		};

		w = Window.new("Olympic Games", Rect(0,0, myXsize, myYsize));
		
		// top - the oracle
		ora = TextView.new( w, Rect( 0,0, myXsize, bannerSize )).background_( Color.white ).align_( 'center' ).resize_( 2 ).string_( "Oracle" ).font_( Font( "Helvetica", fontSize*1.5) );

		// mid - the participants
		pview = CompositeView.new( w, Rect( 0, bannerSize, myXsize, myYsize-(2*bannerSize) ) ).resize_( 5 );
		pview.addFlowLayout(2@2,2@2);
		
		// initialise array with 0's
		//		pvs = Array.fill( noParticipants, 0 );
		pvx = (myXsize - (noParticipants*2 + 2)) / noParticipants;
		pvy = myYsize - (2*bannerSize) - 4;
		
		pviews = participants.collect{ |key,val| 
			OlympicAthleteGui.new( key, pview, pvx@pvy, fontSize )
		};

		// bottom - some kind of timer
		timer = StaticText.new( w, Rect( 0,myYsize-bannerSize, myXsize, bannerSize)).background_( Color.white ).align_('center').resize_( 8 ).string_( "TIMER").font_( Font( "Helvetica", fontSize*6) );	
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
		if ( game.type == \host ){
			timer.string_( GameTimer.getTime; );
		}{
			timer.string_( game.time );
		};
		ora.string_( game.prophecy );
		if ( History.current.hasMovedOn ) {
			pviews.do{ |it| it.update };
			History.current.hasMovedOn = false;
		};
	}
}

OlympicAthleteGui{

	var <>name,<>parent,<>bounds;

	var <view,<list;
	var <nameV,<latest;

	var lastLineSelected = 0, lastLinesShown;
	var selectedLine, linesToShow;

	*new{ |name,parent,bounds,fontSize|
		^super.new.name_(name).parent_(parent).bounds_(bounds).init( fontSize );
	}

	init{ |fontSize|

		view = CompositeView.new( parent,bounds );
		view.addFlowLayout( 0@0, 0@0 );

		nameV = StaticText.new( view, (bounds.x @ 22 ) ).font_( Font.new( "Helvetica", fontSize )).string_( name ).resize_(2).align_(\center);

		latest = TextView.new( view, (bounds.x @ (bounds.y-22/2) )).font_( Font.new( "Helvetica", fontSize )).resize_(2);

		list = ListView.new( view, (bounds.x @ (bounds.y-22/2) ) ).font_( Font.new("Helvetica", fontSize ) );
		list.items_([])
			.resize_(5)
			.background_(Color.grey(0.8))
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
			if ( linesToShow.first.notNil ){
				//	linesToShow.first.postln;
				latest.string = linesToShow.first.at( 2 );
			};
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