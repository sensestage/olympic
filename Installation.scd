( // defining the quarks path:
~olympic = Quarks(
	reposPath: "",
	// adapt this local path to your path:
	//localPath: "/home/nescivi/svn/schwelle/trunk/supercollider/quarks"
	localPath: Document.current.dir +/+ "quarks"
	//localPath: "/Users/schwelle/svn/schwelle_hex/trunk/supercollider/quarks"
);
);
// install from GUI:
~olympic.gui;

// install from code:
~olympic.install( "Olympic" )


// only if you don't have a very recent version of SC, and want to be the host (trunk rev. 10094; April 20)
~olympic.install( "OlympicExtra" )

// From the main quarks repository (svn revision 1477 or higher):
Quarks.install( "Republic" );