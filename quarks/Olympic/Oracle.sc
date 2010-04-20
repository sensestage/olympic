// Oracle class
// inspired by an original function created by Alberto De Campo
// mechanisms borrowed from the Help class from the main distro

Oracle{

	var <tree;

	*new{
		^super.new.init;
	}

	init{
		this.buildTree;
	}

	consult{ |type|
		var oracle = List.new;
		if ( type.asSymbol == \Pattern ){
			["Event","List","Filter","Math"].do{ |it|
				oracle.add( this.choose( ['Streams-Patterns-Events',\Patterns,it.asSymbol]));
			}
		};
		if ( type.asSymbol == \UGen ){
			["Generators","Filters","InOut","Maths","Multichannel","Envelopes"].do{ |it|
				oracle.add( this.choose( ['UGens',it.asSymbol]));
			};
		};
		^oracle
	}

	buildTree{
		var classes;
		classes = Object.allSubclasses.reject({|c| c.asString.beginsWith("Meta_")});
		tree = Dictionary.new(8);

		classes.do( {|class| this.addCatsToTree(class) });
	}

	choose{ |cats|
		var node,res;
		node = tree;
		cats.do{ |it|
			node = node.at( it.asSymbol );
			[it,node].postln;
		};
		res = node.choose;
		res.postln;
		if ( res.isKindOf( Dictionary ) ){
			res = this.chooseFromDict( res );
		}
		^res;
	}

	chooseFromDict{ |dict|
		var res;
		res = dict.choose;
		res.postln;
		if ( res.isKindOf( Dictionary ) ){
			res = this.chooseFromDict( res );
		};
		res.postln;
		^res;
	}

	addCatsToTree { |class|
		var subc, node;
		if(class.categories.isNil.not, {
			class.categories.do({|cat|
				subc = cat.split($>).collect({|i| i });
				node = tree;
				// Crawl up the tree, creating hierarchy as needed
				subc.do({|catname|
					if(node[catname.asSymbol].isNil, {
						node[catname.asSymbol] = Dictionary.new(3);
					});
					node = node[catname.asSymbol];
				});
				node[class] = class.asSymbol ? "";
			});
		}); // end if
	}

}