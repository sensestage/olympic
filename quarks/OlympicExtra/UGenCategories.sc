/*
Categorisation of SC UGens, created by Dan Stowell, 2007.
Addition by Marije Baalman, 2010
*/
+ ClearBuf           { *categories { ^ #["UGens>Buffer"] } }
+ LFGauss        { *categories { ^ #["UGens>Generators>Stochastic"] } }
+ LocalBuf           { *categories { ^ #["UGens>Buffer"] } }
+ MaxLocalBufs { *categories { ^ #["UGens>Buffer>Info"] } }
+ PartConv   { *categories { ^ #["UGens>FFT"] } }
+ Peak    { *categories { ^ #["UGens>Analysis>Amplitude"] } }
+ RunningMin      { *categories { ^ #["UGens>Maths"] } }
+ RunningMax      { *categories { ^ #["UGens>Maths"] } }
+ SetBuf           { *categories { ^ #["UGens>Buffer"] } }
+ VDiskIn          { *categories { ^ #["UGens>InOut", "UGens>Buffer"] } }
+ Vibrato         { *categories { ^ #["UGens>Filters>Pitch"] } }
+ Control { *categories { ^#["UGens>Synth control"]}}
+ AudioControl { *categories { ^#["UGens>Synth control"]}}
+ LagControl { *categories { ^#["UGens>Synth control"]}}
+ TrigControl { *categories { ^#["UGens>Synth control"]}}