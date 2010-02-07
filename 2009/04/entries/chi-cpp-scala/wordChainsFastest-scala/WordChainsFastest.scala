object WordChainsFastest {
  def distance(strA: String, strB: String):Int = {
    (0 until strA.length).foldLeft(0){(cnt, A) => cnt+(if (strA(A) == strB(A)) 0 else 1)}
  }

  def main(args: Array[String]) {
    if ((args.length != 3) || (args(0).trim.length != args(1).trim.length)) {
      println("Usage: scala -classpath . WordChainsFastest duck ruby ./words")
      exit(0)
    }
    val start=args(0).trim
    val goal=args(1).trim

    type Chain = List[String]
    implicit def chainToOrdered(thisChain: Chain): Ordered[Chain] = new Ordered[Chain] {  
      def compare(otherChain: Chain): Int = {
        val ret = distance(otherChain.head, goal) compare distance(thisChain.head, goal)
        if (ret != 0) {
          ret
        } else {
          otherChain.size compare thisChain.size
        }
      }
    } 

    val wordFile = scala.io.Source.fromFile(args(2), "ISO-8859-1").getLines
    val words = wordFile.map(_.trim).filter(_.length == goal.length).toList

    val visited = new scala.collection.mutable.HashSet[String]
    val pq = new scala.collection.mutable.PriorityQueue[Chain] 
    pq+=List(start)

    while((!pq.isEmpty) && (pq.max.head != goal)) {
      val currChain = pq.dequeue
      val nextWords = words.filter(distance(_, currChain.head) == 1).filter(!visited.contains(_))
      nextWords.foreach(currWord => {visited += currWord; pq += (currWord :: currChain)})
      //println(pq.map(A => (A.toString + " " +distance(A.head, goal))).mkString("\n"))
    }

    if (pq.isEmpty) {
      println("No chain found")
    } else {
      println(pq.max.reverse.mkString("\n"))
    }    
  }
}
