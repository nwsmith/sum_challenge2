val worldFile = scala.io.Source.fromFile(args(0).trim, "ISO-8859-1").getLines.toList
- Read in the worldfile into a List of Strings

var world=worldFile.zipWithIndex.flatMap(y => y._1.toList.zipWithIndex.flatMap(x => if (x._1 == '+') List((x._2, y._2)) else Nil))
- Convert the List of Strings into a List of (x,y) tuples containing the coordinates of land.  For example, the file
+--
-+-
--+
Converts into List((0,0), (1,1), (2,2))


def getContinent(seed : List[Tuple2[Int,Int]]) : List[Tuple2[Int,Int]] = {
  val expanded = seed.flatMap(p => for (x <- -1 to 1; y <- -1 to 1) yield((p._1+x, p._2+y))).removeDuplicates.intersect(world);
  if (expanded == seed) seed else getContinent(expanded)
}
- The core algorithm.  Starting with a seed, which is a list of (x,y) tuples, map each tuple into the coordinates of the 9-square around it
- List((x,y)) maps into List((x-1,y-1), (x-1, y), (x-1, y+1), (x,y-1), (x, y), (x, y+1), (x+1,y-1), (x+1, y), (x+1, y+1)) 
- Similarily, a list of 10 tuples maps into a list of 90 tuples
- Once you have this expanded list, you can just remove duplicates (of which there will be many), and then do an intersect(world) to restrict it down to land coordinates
- If this expanded list is the same as the seed list, that means the seed list consists of the entire continent.  If not, recurse until you do find the entire continent


val continentList = new scala.collection.mutable.ArrayBuffer[List[Tuple2[Int,Int]]];
while (!world.isEmpty) {
  var continent = getContinent(world.take(1))
  world = world--continent
  continentList+=continent
}
- The main loop.  In each loop iteration
  - take the first (x,y) land coordinate as the seed
  - find its continent
  - add the continent to a list of continents
  - remove the continent from the world
  - Repeat until the world is empty
