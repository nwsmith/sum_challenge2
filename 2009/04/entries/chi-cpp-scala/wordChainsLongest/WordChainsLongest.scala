object WordChainsLongest {
  def distance(strA: String, strB: String):Int = {
    (0 until strA.length).foldLeft(0){(cnt, A) => cnt+(if (strA(A) == strB(A)) 0 else 1)}
  }

  def main(args: Array[String]) {
    if ((args.length != 3) || (args(0).trim.length != args(1).trim.length)) {
      println("Usage: scala -classpath . WordChainsLongest duck ruby ./words")
      exit(0)
    }
    val start=args(0).trim
    val goal=args(1).trim

    var bestLengthSoFar = 0;

    type Chain = List[String]
    implicit def chainToOrdered(thisChain: Chain): Ordered[Chain] = new Ordered[Chain] {  
      def compare(otherChain: Chain): Int = {
        if ((thisChain.length < bestLengthSoFar) && (otherChain.length < bestLengthSoFar)) {
          thisChain.size compare otherChain.size
        } else {
          val ret = distance(otherChain.head, goal) compare distance(thisChain.head, goal)
          if (ret != 0) {
            ret
          } else {
            otherChain.size compare thisChain.size
          }
        }
      }
    } 

    var longestChainSoFar : Chain= List();

    val wordFile = scala.io.Source.fromFile(args(2), "ISO-8859-1").getLines
    val words = wordFile.map(_.trim).filter(_.length == goal.length).toList

    val pq = new scala.collection.mutable.PriorityQueue[Chain] 
    pq+=List(start)

    var loopCount=0;
    while ((!pq.isEmpty) && (loopCount < 4000000)) {
      val currChain = pq.dequeue
      if (currChain.head == goal) {
        if (currChain.length > longestChainSoFar.length) {
          //println("Chain["+currChain.length+", "+loopCount+"] found: "+currChain.reverse.mkString(",")+"\n")
          longestChainSoFar = currChain
          bestLengthSoFar=longestChainSoFar.length
        }
      } else {
        val nextWords = words.filter(distance(_, currChain.head) == 1).filter(!currChain.contains(_))
        nextWords.foreach(currWord => {pq += (currWord :: currChain)})
      }

      if (pq.size > 4000) {
        pq.reduceToSize(4000)
      }
      //println(pq.map(A => (A.toString + " " +distance(A.head, goal))).mkString("\n"))
      loopCount=loopCount+1;
    }

    if (pq.isEmpty) {
      println("No chain found")
    } else {
      println(longestChainSoFar.reverse.mkString("\n"))
    }    
  }
}
