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
			TextView.new( pview, pvx@pvy ).background_( Color.magenta ).string_( key ).resize_( 5 );
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
	}

	updateGui{
		ora.string_( game.prophecy.asString );
		timer.string_( GameTimer.getTime; );
			//			GameTimer.lineShorts.asString );
	}
	

}