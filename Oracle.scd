(
o = { |nUgens=2, nUn=2, nBin=2|
        var ugens, unaries, binaries; 
        ugens = UGen.allSubclasses.reject({ |cl| 
                                // filter unusable UGen classes
                (cl.superclasses ++ cl).includesAny(
                        [ BufInfoUGenBase, InfoUGenBase ]
                );
        }); 
        
        unaries = AbstractFunction.methods.select { |l| 
                l.findReferences(\composeUnaryOp).notNil
        };
        
        binaries = AbstractFunction.methods.select { |l| 
                l.findReferences(\composeBinaryOp).notNil
        };
        
        ({ ugens.choose.name } ! nUgens)
        ++      ({ unaries.choose.name } ! nUn)
        ++      ({ binaries.choose.name } ! nBin);
};
)
o.value; 

UGen.allSubclasses.postcs

~categoriesSkipThese = [Filter, BufInfoUGenBase, InfoUGenBase, MulAdd, BinaryOpUGen,UnaryOpUGen, BasicOpUGen, MultiOutUGen, ChaosGen, OutputProxy, AbstractOut, AbstractIn, Object, Class, UGen];

(
~classes = Object.allSubclasses.reject({|c| c.asString.beginsWith("Meta_")});
//.difference(categoriesSkipThese);
~fileslist = IdentityDictionary.new;
(
~tree = Dictionary.new(8);
~addCatsToTree = { |class, fileslist|
	var subc, node;
	if(class.categories.isNil.not, {
		class.categories.do({|cat|
			subc = cat.split($>).collect({|i| i });
			node = ~tree;
			// Crawl up the tree, creating hierarchy as needed
			subc.do({|catname|
				if(node[catname].isNil, {
					node[catname] = Dictionary.new(3);
				});
				node = node[catname];
			});
			// "node" should now be the tiniest branch
			//	node[class] = fileslist[class.asSymbol] ? "";
		});
		
		// Class has been added to list so we're OK
		//	fileslist.removeAt(class.asSymbol);
	}); // end if
}
);
~classes.do( {|class| ~addCatsToTree.value(class, ~fileslist) });
);



(
o.tree.keysValuesDo{ |key,v|
	key.postcs;
	v.keysValuesDo{ |key,v| "\t".post; key.postcs;
		v.keysValuesDo{ |key,v| "\t\t".post; key.postcs;
			if ( v.isKindOf( Dictionary) ){
				v.keysValuesDo{ |key,v|
					"\t\t\t".post; key.postcs;
				}
			}
		}
	}
}
)


~fileslist
~tree.do{ |it| it.postcs}

~tree["UGens"].keys

Help.gui;
Help.rebuildTree;

o = Oracle.new;

o.consult( \UGen )

o.consult( \Pattern )

o.tree.keys

o.tree.at('Streams-Patterns-Events').at('Patterns').keys.postcs

o.choose( ["UGens"] )
o.choose( ["UGens","Synth control"] )
o.choose( ["UGens","Multichannel"] )

Env.methods.postcs

1.choose

Help.gui;

interesting mixes:

Basic ingredients:
*Envelopes
*Filters
*Generators
*InOut
*Math
*MultiChannel

Random choice:
*FFT *Buffer *Trigger *Delays *Unclassified


~tree["UGens"]