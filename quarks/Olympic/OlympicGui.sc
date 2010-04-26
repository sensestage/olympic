OlympicGui{

	classvar <>xsize=1024;
	classvar <>ysize=800;

	classvar <docFlag = \sameDoc;
	classvar <useDocClass = false;

	var <>docTitle = "Oracle...", <>docHeight=120;
	var <doc, <oldDocs;

	var <w;
	var <ora;
	var <timer;
	var <pview;

	var <pviews;

	var <updater;
	var <game;

	var myXsize, myYsize, fontSize, bannerSize;

	*docFlag_{ |flag|
		docFlag = flag;
		OlympicAthleteGui.docFlag = flag;
	}
	*useDocClass_{ |flag|
		useDocClass = flag;
		OlympicAthleteGui.useDocClass = flag;
	}

	*new{ |game|
		^super.new.init(game);
	}

	init{ |g|

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
		ora.keyDownAction_({ |txvw, char, mod, uni, keycode|
			// char.postcs;
			if ([3, 13].includes(char.ascii)) {
				this.rip(txvw.string);
			};
		});

		// mid - the participants
		pview = CompositeView.new( w, Rect( 0, bannerSize, myXsize, myYsize-(2*bannerSize) ) ).resize_( 5 );
		pview.addFlowLayout(2@2,2@2);

		pviews = [];
		this.createParticipantView;

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

	createParticipantView{
		var pvs,pvx,pvy;
		var participants = game.participants;
		var noParticipants = participants.size;

		if ( noParticipants > pviews.size ){

			pview.removeAll;
			pviews = [];
			
			pvx = (myXsize - (noParticipants*2 + 2)) / noParticipants;
			pvy = myYsize - (2*bannerSize) - 4;
			
			pviews = participants.collect{ |key,val| 
				OlympicAthleteGui.new( key, pview, pvx@pvy, fontSize )
			};
		};

	}

	updateGui{
		defer{ 
			if ( game.type == \host ){
				timer.string_( GameTimer.getTime; );
			}{
				timer.string_( game.time );
			};
			ora.string_( game.prophecy );
			this.createParticipantView;
			if ( History.current.hasMovedOn ) {
				pviews.do{ |it| it.update };
				History.current.hasMovedOn = false;
			};
		}
	}

	rip { |string|
		this.findDoc;
		if ( useDocClass ){
			doc.string_( string );
		}{
			doc.view.children.first.string_(string);
		};
		doc.front;
	}
	
	findDoc {
		if (docFlag == \newDoc) { oldDocs = oldDocs.add(doc) };
		if ( useDocClass ){
			if (docFlag == \newDoc or: doc.isNil or: { Document.allDocuments.includes(doc).not }) {
				doc = Document.new(docTitle);
				if ( Platform.ideName == \scapp ){
					doc.bounds_( Rect(300, 500, 500, 100) );
				};
			};
			oldDocs = oldDocs.select {|d| d.notNil and: { d.dataptr.notNil } };
		}{
			if (docFlag == \newDoc or: doc.isNil or: { Window.allWindows.includes(doc).not }) {
				doc = Window(docTitle, Rect(300, 500, 500, 100));
				doc.addFlowLayout;
				TextView(doc, doc.bounds.resizeBy(-8, -8)).resize_(5);
			};
			oldDocs = oldDocs.select {|d| d.notNil and: { d.dataptr.notNil } };
		}
	}
}

OlympicAthleteGui{

	classvar <>docFlag = \sameDoc;
	classvar <>useDocClass = false;

	var <>docTitle = "Sharing from...", <>docHeight=120;


	var <>name,<>parent,<>bounds;

	var <view,<list;
	var <nameV,<latest;

	var lastLineSelected = 0, lastLinesShown;
	var selectedLine, linesToShow;

	var <doc, <oldDocs;

	*new{ |name,parent,bounds,fontSize|
		^super.new.name_(name).parent_(parent).bounds_(bounds).init( fontSize );
	}

	init{ |fontSize|
		
		docTitle = "Sharing from..."+name+"(Ctrl-Enter interprets)";

		view = CompositeView.new( parent,bounds );
		view.addFlowLayout( 0@0, 0@0 );

		nameV = StaticText.new( view, (bounds.x @ 22 ) ).font_( Font.new( "Helvetica", fontSize )).string_( name ).resize_(2).align_(\center);

		latest = TextView.new( view, (bounds.x @ (bounds.y-22/2) )).font_( Font.new( "Helvetica", fontSize )).resize_(2);
		latest.editable_(false);
		latest.keyDownAction_({ |txvw, char, mod, uni, keycode|
			// char.postcs;
			if ([3, 13].includes(char.ascii)) {
				this.rip(txvw.string);
			};
		});

		list = ListView.new( view, (bounds.x @ (bounds.y-22/2) ) ).font_( Font.new("Helvetica", fontSize ) );
		list.items_([])
			.resize_(5)
			.background_(Color.grey(0.8))
		.action_({ |lview|
			var index = lview.value;
			if (lview.items.isEmpty) {
				"no entries yet.".postln;
			} {
				lastLineSelected = lview.items[index];
				this.rip( lastLineSelected );
			}
		})
	}

	rip { |string|
		this.findDoc;
		if ( useDocClass ){
			doc.string_( string )
		}{
			doc.view.children.first.string_(string);
		};
		doc.front;	
	}
	
	findDoc {
		if (docFlag == \newDoc) { oldDocs = oldDocs.add(doc) };
		if ( useDocClass ){
			if (docFlag == \newDoc or: doc.isNil or: { Document.allDocuments.includes(doc).not }) {
				doc = Document.new(docTitle);
				if ( Platform.ideName == \scapp ){
					doc.bounds_( Rect(300, 500, 500, 100) );
				};
			};
			oldDocs = oldDocs.select {|d| d.notNil and: { d.dataptr.notNil } };
		}{
			if (docFlag == \newDoc or: doc.isNil or: { Window.allWindows.includes(doc).not }) {
				doc = Window(docTitle, Rect(300, 500, 500, 100));
				doc.addFlowLayout;
				TextView(doc, doc.bounds.resizeBy(-8, -8)).resize_(5);
			};
			oldDocs = oldDocs.select {|d| d.notNil and: { d.dataptr.notNil } };
		}
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