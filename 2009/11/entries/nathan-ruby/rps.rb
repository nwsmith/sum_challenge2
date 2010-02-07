#!/usr/bin/ruby
MAX_HISTORY = 340
STAT_DIR = '/tmp/nathanrps/'
WEIGHTS = {
2 => 0.3, 
3 => 0.4, 
5 => 0.6, 
7 => 0.7, 
9 => 0.8, 
11 => 0.9, 
13 => 1.0, 
15 => 1.1, 
17 => 1.2, 
19 => 1.3, 
21 => 1.4, 
23 => 1.6}
MIN_PRC_DIFF = 1 
MIN_PRC = -4.0
ME_FACTOR = 0.83
MAX_LOSE_STREAK = 5
MAX_STRATEGY_LOSE_STREAK = 2
MIN_STRATEGY_WIN_STREAK = 10

class Move
  attr_reader :name
 
  def self.get_move(name)
    case name
      when 'R'
        return Rock.new
      when 'S'
        return Scissors.new
      when 'P'
        return Paper.new
    end
  end

  def initialize(name)
    @name = name
  end

  def to_s
    return @name
  end

  def eql?(move)
    return @name == move.name
  end

  def <(move)
    return move.eql?(self.get_superior)
  end

  def >(move)
    return move.eql?(self.get_inferior)
  end

  def ==(move)
    return move.eql?(self.get_equal)
  end

  def result(move)
    if self < move
      return -1
    elsif self > move
      return 1
    else
      return 0
    end

  end
end

class Rock < Move
  def initialize
    super("R")
  end

  def judge(move)
    case move
      when Rock
        return 0
      when Paper
        return -1
      when Scissors
        return 1
    end
  end

  def hash
    1
  end

  def get_superior
    return Paper.new
  end

  def get_equal
    return Rock.new
  end

  def get_inferior
    return Scissors.new
  end
end

class Paper < Move
  def initialize
    super("P")
  end

  def judge(move)
    case move
      when Rock
        return 1
      when Paper
        return 0
      when Scissors
        return -1
    end
  end

  def hash
    2
  end

  def get_superior
    return Scissors.new
  end

  def get_equal
    return Paper.new
  end

  def get_inferior
    return Rock.new
  end
end

class Scissors < Move
  def initialize
    super("S")
  end

  def hash
    3
  end

  def judge(move)
    case move
      when Rock
        return -1
      when Paper
        return 1
      when Scissors
        return 0
    end
  end

  def get_superior
    return Rock.new
  end

  def get_equal
    return Scissors.new
  end

  def get_inferior
    return Paper.new
  end
end

THEM = "them"
ME = "me"

class Array
  def each_window(size)
    i = 0
    while (i + size < self.length)
      values = self.slice(i, size)
      next_value = self[i + size]
      yield Window.new(values, next_value)
      i += 1
    end
  end
end

class Window
  attr_accessor :values, :next_value

  def initialize(values, next_value)
    @values = values
    @next_value = next_value
  end

  def matches? (pattern, type = THEM)
    return false unless @values.length == pattern.length
    @values.zip(pattern) {|x, y| return false unless x.send(type).eql?(y.send(type))}
    true
  end
end

class Turn
  attr_accessor :me, :them, :result, :strategy

  def initialize(me, them = nil, result = nil, strategy = nil)
    @me = me
    @them = them
    @result = result
    @strategy = strategy
  end

  def to_s
    return @me.to_s + " " + @them.to_s + " " + @result.to_s + " " + @strategy.to_s
  end
end

class Streak
  attr_accessor :count, :result
  def initialize(result, count) 
    @result = result
    @count = count
  end
end

class Stats
  attr_accessor :turns

  def initialize(bad_guy, use_history = true)
    @turns = Array.new
    if (use_history)
      Dir.mkdir(STAT_DIR) unless File.exist?(STAT_DIR)
      @filename = STAT_DIR + bad_guy
      File.open(@filename, 'w') unless File.exist?(@filename)
      File.open(@filename).each do |line|
        values = line.chomp!.split(" ")
        @turns.push(Turn.new(Move.get_move(values[0]),
                             Move.get_move(values[1]),
                             values[2], 
                             values[3]))
      end
    end
  end

  def write
    File.open(@filename, 'w') do |file|
      turns.each {|turn| file.write(turn.to_s + "\n")}
    end
  end
 
  def truncate
    File.truncate(@filename, 0)
  end

  def pop
    return turns.pop
  end

  def push(turn)
    turns.push(turn)
  end

  def get_pattern(size)
    return @turns.reverse.slice(0, size).reverse
  end

#  def get_win_percentage(strategy) 
#    count = 0
#    wins = 0
#   
#    @turns.each do |turn|
#      if (turn.strategy.include?(strategy))
#        count += 1
#        if (turn.result.to_i == 1) 
#          wins += 1
#        end
#      end
#    end
#    return count == 0 ? 0 : (wins/count.to_f)
#  end

  def get_win_percentage(strategy)
    score = 0
    @turns.each do |t|
      score += t.result.to_i if t.strategy.include?(strategy)
    end
    score
  end

  def get_streak(strategy)
    set = @turns.reverse
    #set.slice!(set.length-MAX_HISTORY..-1) if set.length > MAX_HISTORY
    i = 0
    s = 99
    while (i < set.length && s == 99) 
      if (set[i].strategy.include?(strategy)) 
        s = set[i].result.to_i
      else
        i += 1
      end
    end
    
    l = 0
    while (i < set.length && set[i].result.to_i == s)
      i += 1
      l += 1
    end

    return Streak.new(s == 99 ? 0 : s, l)
  end

  def get_current_streak
    set = @turns.reverse
    s = set[0].result
    i = 1 
    l = 1
    while (i < set.length && set[i].result.to_i == s)
      i += 1
      l += 1
    end
    return Streak.new(s, l)
  end
end

class Pattern
  attr_accessor :window, :values

  def initialize(window, values = Array.new)
    @window = window
    @values = values
  end

  def populate(stats, start = 1)
    r_stat = stats.turns.reverse
    start.upto(window) do |i|
      @values.push(r_stat[i])
    end
  end
end

class MoveCount
  attr_accessor :move, :count
  def initialize(move, count)
    @move = move
    @count = count
  end
end

class MoveFrequency
  attr_accessor :frequency

  def initialize
    @frequency = Hash.new
    @frequency[Rock.new] = 0.to_f
    @frequency[Paper.new] = 0.to_f
    @frequency[Scissors.new] = 0.to_f
  end

  def inc(move, factor = 1)
    @frequency[move] += (1 * factor)
  end

  def get_frequency(move)
    return @frequency[move]
  end

  def most_frequent
    max_count, most_frequent_move = 0, nil
    @frequency.each_pair do |move, count|
      if count > max_count
        max_count = count
        most_frequent_move = move
      end
    end
    max_count > 0 ? MoveCount.new(most_frequent_move, max_count) : nil
  end
end

class PatternAnalysis
  attr_accessor :window_size, :windows, :matches, :frequency, :pattern

  def get_move
    return @frequency.most_frequent
  end
end

class PatternAnalyzer
  def initialize(type = THEM)
    @type = type
  end

  def analyze(stats, size = 2)
    matches = 0
    frequency = MoveFrequency.new
    pattern = stats.get_pattern(size)
    set = stats.turns.length > MAX_HISTORY ? (stats.turns.slice((stats.turns.length - MAX_HISTORY), (stats.turns.length-1))) : stats.turns

    set.each_window(size) do |window|
      if window.matches?(pattern, @type)
        frequency.inc(window.next_value.send(@type))
        matches += 1
      end
    end

    analysis = PatternAnalysis.new
    analysis.pattern = pattern
    analysis.window_size = size
    analysis.frequency = frequency
    analysis.matches = matches
    analysis.windows = (stats.turns.length/size).to_i

    return analysis
  end
end

class WindowScan
  attr_accessor :analysis

  def initialize
    @analysis = Hash.new
  end

  def add_pattern_analysis(pattern_analysis) 
    @analysis[pattern_analysis.window_size] = pattern_analysis if pattern_analysis.matches > 0  
  end

  def sizes 
    @analysis.each_key.inject('') {|s, k| s += k.to_s + ","}
  end

  def most_frequent
    frequency = MoveFrequency.new
    @analysis.each_pair do |size, analysis|
      frequency.inc(Rock.new, analysis.frequency.get_frequency(Rock.new) * WEIGHTS[size]) 
      frequency.inc(Scissors.new, analysis.frequency.get_frequency(Scissors.new) * WEIGHTS[size]) 
      frequency.inc(Paper.new, analysis.frequency.get_frequency(Paper.new) * WEIGHTS[size]) 
    end
    return frequency.most_frequent
  end
end

class WindowScanner
  def initialize(type = THEM)
    @type = type
  end

  def scan(stats)
    scan = WindowScan.new
    pattern_analyzer = PatternAnalyzer.new(@type)
    WEIGHTS.each_key do |size|
      if ((stats.turns.length > size) && (stats.turns.length < size * 50))
        pattern_result = pattern_analyzer.analyze(stats, size)
        scan.add_pattern_analysis(pattern_result)
      end
    end
    return scan
  end
end

class MovePick
  attr_accessor :move, :strategy
  def initialize(move, strategy)
    @move = move
    @strategy = strategy
  end
end

class MovePicker
  def get_fallback
    n = rand(3)
    m = nil
    case n
      when 0
        m = Rock.new
      when 1
        m = Scissors.new
      when 2
        m = Paper.new
    end
    return m
  end

  def pick_move(stats)
    them_scanner = WindowScanner.new(THEM)
    me_scanner = WindowScanner.new(ME)

    them_scan = them_scanner.scan(stats)
    me_scan = me_scanner.scan(stats)
 
    them_based_move = them_scan.most_frequent
    me_based_move = me_scan.most_frequent

    my_move = nil
   
    if (!them_based_move.nil? || !me_based_move.nil?) 
      them_count = them_based_move.nil? ? 0 : them_based_move.count
      me_count = (me_based_move.nil? ? 0 : me_based_move.count) * ME_FACTOR
      suffix = "/" + them_count.to_s + ":" + them_scan.sizes +  "/" + me_count.to_s + ":" + me_scan.sizes + "/"
      current_streak = stats.get_current_streak
      if (current_streak.result.to_i == -1 && current_streak.count >= MAX_LOSE_STREAK)
        m = get_fallback
        if (current_streak.result.to_i == -1 && current_streak.count >= MAX_LOSE_STREAK) 
          str = "streak"
        else 
          str = "fallback"
        end
        my_move = MovePick.new(m, str)
      elsif (!them_based_move.nil? && them_count >= (me_count))
        win_pct_1 = stats.get_win_percentage("them-1-").to_f
        win_pct_2 = stats.get_win_percentage("them-2-").to_f
        pct_suffix = "(#{win_pct_1}=#{win_pct_2})"
        streak_1 = stats.get_streak("them-1-")
        streak_2 = stats.get_streak("them-2-")
        win_pct_1 += 2*(streak_1.result.to_i*streak_1.count)
        win_pct_2 += 2*(streak_2.result.to_i*streak_2.count)
        str_suffix = "(s:#{streak_1.result}|#{streak_1.count}-s:#{streak_2.result}|#{streak_2.count})"
        if (win_pct_1 >= MIN_PRC || win_pct_2 >= MIN_PRC) 
          if (win_pct_1 > win_pct_2 && ((win_pct_1 - win_pct_2) > MIN_PRC_DIFF))
            my_move = MovePick.new(them_based_move.move.get_superior, THEM + "-1-" + pct_suffix + suffix + "1>2")
          elsif (win_pct_2 > win_pct_1 && ((win_pct_2 - win_pct_1) > MIN_PRC_DIFF))
            my_move = MovePick.new(them_based_move.move.get_equal, THEM + "-2-" + pct_suffix + suffix + "2>1")
          elsif (win_pct_1 >= MIN_PRC)
            my_move = MovePick.new(them_based_move.move.get_superior, THEM + "-1-" + pct_suffix + suffix + "1>=0")
          elsif (win_pct_2 >= MIN_PRC)
            my_move = MovePick.new(them_based_move.move.get_equal, THEM + "-2-" + pct_suffix + suffix + "2>=0")
          end
        elsif (streak_1.result.to_i == -1 && streak_1.count >= MAX_STRATEGY_LOSE_STREAK  && streak_2.result.to_i != -1)
          my_move = MovePick.new(them_based_move.move.get_equal, THEM + "-2-" + str_suffix + suffix)
        elsif (streak_2.result.to_i == -1 && streak_2.count >= MAX_STRATEGY_LOSE_STREAK && streak_1.result.to_i != -1) 
          my_move = MovePick.new(them_based_move.move.get_superior, THEM + "-1-" + str_suffix + suffix)
        elsif (streak_1.result.to_i == 1 && streak_1.count > MIN_STRATEGY_WIN_STREAK) 
          my_move = MovePick.new(them_based_move.move.get_superior, THEM + "-1-" + str_suffix + suffix)
        elsif (streak_2.result.to_i == 1 && streak_2.count > MIN_STRATEGY_WIN_STREAK)
          my_move = MovePick.new(them_based_move.move.get_equal, THEM + "-2-" + str_suffix + suffix)
        else
          # cause we gotta do something
          fallback_1_win = stats.get_win_percentage("them-1-fallback").to_f
          lr_suffix = "[#{win_pct_1},#{win_pct_2},#{fallback_1_win}]"
          
          case [win_pct_1, win_pct_2, fallback_1_win].max
            when win_pct_1
              my_move = MovePick.new(them_based_move.move.get_superior, THEM + "-1-" + lr_suffix + suffix)
            when win_pct_2
              my_move = MovePick.new(them_based_move.move.get_equal, THEM + "-2-" + lr_suffix + suffix)
            when fallback_1_win
              my_move = MovePick.new(get_fallback, THEM + "-1-" + "fallback" + lr_suffix + suffix)
          end
        end
      else
        my_win_pct_1 = stats.get_win_percentage("me-1-").to_f
        my_win_pct_2 = stats.get_win_percentage("me-2-").to_f
        pct_suffix = "(#{my_win_pct_1}=#{my_win_pct_2}"
        my_streak_1 = stats.get_streak("me-1-")
        my_streak_2 = stats.get_streak("me-2-")
        my_win_pct_1 += 2*(my_streak_1.result.to_i*my_streak_1.count)
        my_win_pct_2 += 2*(my_streak_2.result.to_i*my_streak_2.count)
        str_suffix = "(s:#{my_streak_1.result}|#{my_streak_1.count}-s#{my_streak_2.result}|#{my_streak_2.result}"
       
        if (my_win_pct_1 >= MIN_PRC || my_win_pct_2 >= MIN_PRC) 
          if (my_win_pct_1 > my_win_pct_2 && ((my_win_pct_1 - my_win_pct_2) > MIN_PRC_DIFF))
            my_move = MovePick.new(me_based_move.move.get_inferior, ME + "-1-" + pct_suffix + suffix + "1>2")
          elsif (my_win_pct_2 > my_win_pct_1 && ((my_win_pct_2 - my_win_pct_1) > MIN_PRC_DIFF))
            my_move = MovePick.new(me_based_move.move.get_superior, ME + "-2-" + pct_suffix + suffix + "2>1")
          elsif my_win_pct_1 >= MIN_PRC 
            my_move = MovePick.new(me_based_move.move.get_inferior, ME + "-1-" + pct_suffix + suffix + "1>=0")
          elsif (my_win_pct_2 >= MIN_PRC)
            my_move = MovePick.new(me_based_move.move.get_superior, ME + "-2-" + pct_suffix + suffix + "2>=0")
          end
        elsif (my_streak_1.result.to_i == -1 && my_streak_1.count >= MAX_STRATEGY_LOSE_STREAK && my_streak_2.result.to_i != -1)
          my_move = MovePick.new(me_based_move.move.get_superior, ME + "-2-" + str_suffix + suffix)
        elsif (my_streak_2.result.to_i == -1 && my_streak_2.count >= MAX_STRATEGY_LOSE_STREAK && my_streak_1.result.to_i != -1)
          my_move = MovePick.new(me_based_move.move.get_inferior, ME + "-1-" + str_suffix + suffix)
        elsif (my_streak_1.result.to_i == 1 && my_streak_1.count > MIN_STRATEGY_WIN_STREAK)
          my_move = MovePick.new(me_based_move.move.get_inferior, ME + "-1-" + str_suffix + suffix)
        elsif (my_streak_2.result.to_i == 1 && my_streak_2.count > MIN_STRATEGY_WIN_STREAK) 
          my_move = MovePick.new(me_based_move.move.get_superior, ME + "-2-" + str_suffix + suffix)
        else
          # cause we gotta do something
          my_fallback_1_win = stats.get_win_percentage("me-1-fallback").to_f
          lr_suffix = "[#{my_win_pct_1},#{my_win_pct_2},#{my_fallback_1_win}]"
          
          case [my_win_pct_1, my_win_pct_2, my_fallback_1_win].max
            when my_win_pct_1
              my_move = MovePick.new(me_based_move.move.get_inferior, ME + "-1-" + lr_suffix + suffix)
            when my_win_pct_2
              my_move = MovePick.new(me_based_move.move.get_superior, ME + "-2-" + lr_suffix + suffix)
            when my_fallback_1_win
              my_move = MovePick.new(get_fallback, ME + "-1-" + "fallback" + lr_suffix + suffix)
          end
        end
      end
      puts "FUCK!!!!" if my_move.nil?
      return my_move
    else
      frequency = MoveFrequency.new
      stats.turns.each {|turn| frequency.inc(turn.them) unless turn.them.nil?}
      return MovePick.new(frequency.most_frequent.move.get_superior, "freq")
    end
  end
end

bad_guy = ARGV[0]
instruction = ARGV[1]
their_move = instruction == 'turn' ? Move.get_move(ARGV[2]) : ''

stats = Stats.new(bad_guy)

case instruction
  when 'start'
    stats.truncate
    picker = MovePicker.new 
    my_move = MovePick.new(picker.get_fallback, "start")
    if (stats.turns.length > 1)
      my_move = picker.pick_move(stats)
    end
    stats.turns.push(Turn.new(my_move.move, 'D', '9', my_move.strategy))
    puts my_move.move
  when 'turn'
    # grab my last move
    last_turn = stats.turns.pop
    last_turn.them = their_move
    r = last_turn.me.result(last_turn.them)
    last_turn.result = r
    stats.push(last_turn)
    picker = MovePicker.new
    my_move = picker.pick_move(stats)
    stats.push(Turn.new(my_move.move, 'D', '9', my_move.strategy))
    puts my_move.move
  when 'finish'
    # we always put our last move, but we don't need it
    stats.turns.pop
    puts "It's over"
end
stats.write
