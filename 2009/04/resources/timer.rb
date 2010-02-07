#!/usr/bin/ruby

CHECK_CHAIN = true;
RUN_COUNT = 500;
OUTPUT_CHAIN = true;

class Timer
  attr_accessor :cmd, :program, :start, :goal, :dictionary_hash

  def initialize(program, start, goal, dictionary, opt) 
    @start = start
    @goal = goal
    @dictionary_hash = Hash.new
    @cmd = "#{program} #{start} #{goal} #{dictionary}"
    @cmd << " #{opt}" if opt


    print("Loading Dictionary...")
    File.open(dictionary).each do |word|
      dictionary_hash[word.strip] = nil
    end
    puts("finished.")
  end

  def run   
    times = Array.new
    longest = 0
    1.upto(RUN_COUNT) do 
      t0 = Time.now.to_f
      raw_chain = %x[#{@cmd}]
      t1 = Time.now.to_f
      td = t1 - t0 
      chain = raw_chain.split 
      longest = chain.length if chain.length > longest 
      if (CHECK_CHAIN && !valid_chain?(chain))
        puts "Bad Chain."
        exit
      end  
      puts raw_chain if OUTPUT_CHAIN
      times.push(td);
      print "."
      STDOUT.flush
    end
    total_seconds = times.inject(0) {|total, curr| total + curr.to_f}
    official_time = times.sort[0]
    puts
    puts "Longest Chain: #{longest} words"
    puts "Total    Time: #{total_seconds} seconds"
    puts "Official Time: #{official_time} seconds" 
  end

  def valid_chain?(chain) 
    return false if chain.empty?
    len = chain[0].length
    if chain[0] != @start
      puts "First word of chain should be #{@start} not #{chain[0]}"
      return false
    end
    if chain[-1] != @goal
      puts "Last word of chain should be #{@goal} not #{chain[-1]}"
      return false
    end
    return false unless chain[0] == @start
    return false unless chain[-1] == @goal
    seen = Hash.new
    chain.each_index do |i|
      chain[i].strip!
      if chain[i].length != chain[0].length
        puts "#{chain[i]} is too long (should be #{chain[0].length}"
        return false
      end
      if !@dictionary_hash.include?(chain[i])
        puts "#{chain[i]} not in dictionary"
        return false
      end
      if seen.include?(chain[i]) 
        puts "#{chain[i]} has been repeated"
        return false
      end
      if i < (chain.length - 1) && !words_have_one_letter_different(chain[i], chain[i+1])
        puts "#{chain[i]} and #{chain[i+1]} are not valid links."
        return false
      end
      seen[chain[i]] = nil
    end
    return true
  end

  def words_have_one_letter_different(first, second)
    found_diff = false
    (0..first.length).each do |i|
      if first[i] != second[i] 
        if found_diff 
          return false
        else
          found_diff = true
        end
      end
    end
    return found_diff
  end
end

program = ARGV[0]
start = ARGV[1]
goal = ARGV[2]
dictionary = ARGV[3]
opt = ARGV[4]

Timer.new(program, start, goal, dictionary, opt).run
