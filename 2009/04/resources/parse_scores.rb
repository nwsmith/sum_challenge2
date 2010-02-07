#!/usr/bin/ruby

class ScoreParser
  def initialize
    @files = Array.new
    Dir.glob("*.txt") {|file| @files << file}
  end

  def score
    totals = Hash.new
    totals["fastest"] = Hash.new
    totals["longest"] = Hash.new
    totals["total"] = Hash.new
    @files.each do |file|
      file =~ /\d_(\w+)-(\w+)\.txt/
      start = $1
      goal = $2  
      scores = score_file(file)
      scores["fastest"].each do |fast|
        name = fast[0]
        time = fast[1]
        score = fast[2]
        if (totals["fastest"][name].nil?) 
          totals["fastest"][name] = Hash.new
          totals["fastest"][name]["time"] = 0
          totals["fastest"][name]["score"] = 0

          totals["longest"][name] = Hash.new
          totals["longest"][name]["length"] = 0
          totals["longest"][name]["time"] = 0
          totals["longest"][name]["score"] = 0

          totals["total"][name] = Hash.new
          totals["total"][name]["score"] = 0
        end
        totals["fastest"][name]["time"] += time.to_f
        totals["fastest"][name]["score"] += score.to_f
        totals["total"][name]["score"] += score.to_f
      end
      scores["longest"].each do |long|
        name = long[0]
        length = long[1]
        time = long[2]
        score = long[3]
        totals["longest"][name]["length"] += length.to_f
        totals["longest"][name]["time"] += time.to_f
        totals["longest"][name]["score"] += score.to_f
        totals["total"][name]["score"] += score.to_f
      end
    end
    return totals
  end

  def score_file(file)
    res = Hash.new
    longest = Array.new
    fastest = Array.new
    totals = Array.new
    wrk_array = nil

    File.open(file).each do |line|
      line.strip!
      if line =~ /fastest/
        wrk_array = fastest
      elsif line =~ /longest/
        wrk_array = longest
      elsif line =~ /total/
        wrk_array = totals
      else
        pline = line.split("|")[1..-1];
        wrk_array << pline unless pline.nil?
      end
    end

    res["longest"] = longest
    res["fastest"] = fastest
    return res
  end
end

parser = ScoreParser.new
scores = parser.score
puts "|fastest|"
scores["fastest"].sort{|l, r| l[1]["score"]<=>r[1]["score"]}.reverse.each do |score|
  puts "|#{score[0]}|#{score[1]["score"]}|"
end
puts
puts "|longest|"
scores["longest"].sort{|l, r| l[1]["score"]<=>r[1]["score"]}.reverse.each do |score|
  puts "|#{score[0]}|#{score[1]["score"]}|"
end
puts
puts "|total|"
scores["total"].sort{|l, r| l[1]["score"]<=>r[1]["score"]}.reverse.each do |score|
  puts "|#{score[0]}|#{score[1]["score"]}|"
end
