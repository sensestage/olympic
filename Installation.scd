(
~olympic = Quarks(
	reposPath: "",
	// adapt this local path to your path:
	//localPath: "/home/nescivi/svn/schwelle/trunk/supercollider/quarks"
	localPath: Document.current.dir +/+ "quarks"
	//localPath: "/Users/schwelle/svn/schwelle_hex/trunk/supercollider/quarks"
);


//~olympic.gui;

~olympic.install( "Olympic" )
)