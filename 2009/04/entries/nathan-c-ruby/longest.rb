#!/usr/bin/ruby
class String
  # Calculate the distance between all the letters in the two words
  # to get an estimate of how "far apart" the words are
  def distance(s)
    (0..length-1).inject(0) {|t, i| (self[i] - s[i]).abs}
  end

  def link?(s)
    (0..length).inject(0) {|t, i| t + (self[i] != s[i] ? 1 : 0)} == 1
  end
end

class Array
  # easy way to get 0,1, 1,2, 2,3, etc
  def each_index_pair
    (0..length-2).each{|i| yield i, i+1}
  end

  def insert_chain(i, chain)
    insert(i, chain[1..-2]).flatten! if chain.size() > 2
  end
end

class Hash
  def initialize(file, len)
    File.open(file).each do |word|
      word.strip!
      self[word] = word if word.length == len
    end
  end

  def <<(array)
    array.each {|word| self[word] = word}
  end
end

class ChainBuilder
  def initialize(dict_file, len)
    @unused = Array.new
    @dict = Hash.new(dict_file, len)
  end

  def repopulate
    @dict << @unused
    @unused.clear
  end

  def longest_chain(start, goal)
    lengthen(build_chain(start, goal))
  end

  def build_chain(start, goal)
    @dict.delete(start)
    chain = [start]
    loop do
      found, max_distance, candidate_word = false, -1, nil
      @dict.each_key do |word|
        if word.link?(chain[-1])
          if (word == goal)
            @dict.delete(word)
            return chain << word          
          elsif (word.link?(goal))
            # Our crappy heuristic (see below) actually leads us away
            # from our goal, this tries to grab it if it gets the chance            
            @dict.delete(word)
            @dict.delete(goal)
            return chain << word << goal
          else
            curr_distance = word.distance(goal)
            # Essentially take the word furthest away from our goal
            # Not a brilliant heuristic, but it seems to do the job
            found, max_distance, candidate_word = true, curr_distance, word  if curr_distance > max_distance     
          end
        end
      end

      if found
        chain << @dict.delete(candidate_word)
      else  
        # dead end, back track
        bad_word = chain.pop
        # this might come in handy later...
        @unused << bad_word unless bad_word == start
        # back tracked all the way...it's over
        return chain if chain.empty?
      end
    end
  end

  def lengthen(chain)
    loop do
      # to see if we get any longer (get your mind out of the gutter!)
      sz = chain.size

      chain.each_index_pair do |x, y|
        # some of those words we didn't use might be useful now
        repopulate
        chain.insert_chain(y, build_chain(chain[x], chain[y]))
      end

      # Our lengthening journey is over
      return chain if chain.size == sz
    end
  end
end

start = ARGV[0]
goal = ARGV[1]
dict_file = ARGV[2]

puts ChainBuilder.new(dict_file, start.length).longest_chain(start, goal)