object Days {
  def main(args: Array[String]) {
    if (args.length != 1) {
      println("Usage: scala -classpath . Days 1,3,4,5,6")
      exit(0)
    }

    val dayOfWeek=Map(
      1 -> "Mon",
      2 -> "Tue",
      3 -> "Wed",
      4 -> "Thu",
      5 -> "Fri",
      6 -> "Sat",
      7 -> "Sun")

    val in = args(0).split(",").map(_.trim.toInt).toList.sort(_<_)
    val o = in.foldRight(List[Tuple2[Int,Int]]()) {(n,l) => if (!l.isEmpty && l.first._1 == (n+1)) (n,l.first._2)::l.tail else (n,n)::l}
    println(o.map(z => {if (z._1==z._2) dayOfWeek(z._1) else if (z._1+1==z._2) dayOfWeek(z._1)+","+dayOfWeek(z._2) else dayOfWeek(z._1)+"-"+dayOfWeek(z._2)}).mkString(","))
  }
}

