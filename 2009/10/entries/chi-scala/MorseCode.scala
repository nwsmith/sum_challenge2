object MorseCode {
  def main(args: Array[String]) {
    if (args.length != 2) {
      println("Usage: scala -classpath . MorseCode /usr/share/dict/words -.-.......")
      exit(0)
    }

    val morse=Map(
      'A'->".-",
      'B'->"-...",
      'C'->"-.-.",
      'D'->"-..",
      'E'->".",
      'F'->"..-.",
      'G'->"--.",
      'H'->"....",
      'I'->"..",
      'J'->".---",
      'K'->"-.-",
      'L'->".-..",
      'M'->"--",
      'N'->"-.",
      'O'->"---",
      'P'->".--.",
      'Q'->"--.-",
      'R'->".-.",
      'S'->"...",
      'T'->"-",
      'U'->"..-",
      'V'->"...-",
      'W'->".--",
      'X'->"-..-",
      'Y'->"-.--",
      'Z'->"--..")

    def decode(s :String): Iterable[String]={
      if(s.isEmpty) 
        List("") 
      else 
        morse.flatMap(e => if(s.startsWith(e._2)) decode(s.substring(e._2.length)).map(z=>e._1+z) else Nil)
    }

    val dict = scala.io.Source.fromFile(args(0),"ISO-8859-1").getLines.toList.map(_.trim.toUpperCase)
    val decoded = decode(args(1)).toList.sort(_<_)
    println("All words: \n"+decoded.mkString("\n")+"\n")
    println("Words from dictionary: \n"+decoded.intersect(dict).mkString("\n"))
  }
}

