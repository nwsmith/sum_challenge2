object FindContinents {
  def main(args: Array[String]) {
    if (args.length != 1) {
      println("Usage: scala -classpath . FindContinents map01")
      exit(0)
    }

    val worldFile = scala.io.Source.fromFile(args(0).trim, "ISO-8859-1").getLines.toList
    var world=worldFile.zipWithIndex.flatMap(y => y._1.toList.zipWithIndex.flatMap(x => if (x._1 == '+') List((x._2, y._2)) else Nil))

    def getContinent(seed : List[Tuple2[Int,Int]]) : List[Tuple2[Int,Int]] = {
      val expanded = seed.flatMap(p => for (x <- -1 to 1; y <- -1 to 1) yield((p._1+x, p._2+y))).removeDuplicates.intersect(world);
      if (expanded == seed) seed else getContinent(expanded)
    }

    val continentList = new scala.collection.mutable.ArrayBuffer[List[Tuple2[Int,Int]]];
    while (!world.isEmpty) {
      var continent = getContinent(world.take(1))
      world = world--continent
      continentList+=continent
    }
    println("There are "+continentList.size+" Continent(s)\n"+continentList.toList.zipWithIndex.map(z => "Continent "+(z._2+1)+" has "+(z._1.size)+"+").mkString("\n"));
  }
}

