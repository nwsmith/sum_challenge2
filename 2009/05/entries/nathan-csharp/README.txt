You might have to install mono, but if not, just make sure you have the generics C# compiler:

sudo apt-get install mono-gmcs

Then run:

./run.sh <data file>

A note on process.

The process here is essentially a brute force check - the algorithm sweeps the world, and creates a new name for every new piece of land (i.e. if the current piece is not connected to an existing piece).  In order to catch pieces with "interesting" connections, it explores the area around each to see if we can connect something thought discrete back to the new continent.

Two "C#" specific features that are used are rectangular arrays (i.e. true multi-dimensional array as opposed to an array of arrays) and the ability to create enumerable collections on the fly using "yield" (which also exists in Ruby and Javascript, among other languages)
