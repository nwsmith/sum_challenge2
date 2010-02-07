In 1492 Columbus sailed the ocean blue

In the May programming challenge, we are back to creating the most creative solution you can come up with, with voting at the end of the month.  You mission, should you choose to accept it, is to travel the world and map all of it's continents.

The "world" will consist of a text file that maps a game world, where each location is either land or see.  Land points are +, sea points are -.  Maps will always be rectagular, but there is no way to determine the dimensions without reading the file itself.  You program must read this text file and determine:

how many continents are in the world +
a list of those continents +
the size of each continent.

A continent is defined as one or more connected locations where each location is a +.  Connected means that in a 3x3 grid around a +, if any of the 8 surrounding locations contins a + then the two are connected.  the followng are all "continents":

{code}
+
++
+
{code}

{code}
+
 +
  +
{code}

{code}
+++
   +
{code}

Sample output might look something like this:

There are 5 continents
Continent 1 has 200+
Continent 2 has 89+
Contenent 3 has ...

Yes, continents might contain lakes:

{code}
-----------
+++++++----
++---+++---
++---++++--
+++-+++++--
++++++-----
-----------
{code} 

So you can't just outline and assume, you'll need to explore!  So pick your favorite language and see what you come up with!  If you could send along a brief synopsis of what you actually did, it will really help people choose what to vote for.

Remember, above all keep it fun!

I've attached one sample file.
