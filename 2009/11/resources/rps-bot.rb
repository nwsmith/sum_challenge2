#!/usr/bin/ruby
def score_turn(move_one, move_two)
  case move_one
    when 'R'
      case move_two
        when 'S' then
          return -1
        when 'R' then
          return 0
        when 'P' then
          return 1
      end
    when 'S'
      case move_two
        when 'P' then
          return -1
        when 'S' then
          return 0
        when 'R' then
          return 1
      end
    when 'P'
      case move_two
        when 'R' then
          return -1
        when 'P' then
          return 0
        when 'S' then
          return 1
      end
  end
end


num_turns = ARGV[0].to_i
player_one = ARGV[1]
program_one = ARGV[2]
player_two = ARGV[3]
program_two = ARGV[4]

if num_turns < 2
  puts "Must have at least two turns"
end

player_one_moves = Array.new
player_two_moves = Array.new


player_one_moves.push(%x[#{program_one} #{player_two} start].chomp)
player_two_moves.push(%x[#{program_two} #{player_one} start].chomp)

(num_turns-1).downto(1) do |turn|
  player_one_move = %x[#{program_one} #{player_two} turn #{player_two_moves.last}].chomp
  player_two_move = %x[#{program_two} #{player_one} turn #{player_one_moves.last}].chomp

  player_one_moves.push(player_one_move)
  player_two_moves.push(player_two_move)
end

%x[#{program_one} #{player_two} finish #{player_two_moves.last}]
%x[#{program_two} #{player_one} finish #{player_one_moves.last}]

overall = 0

puts "#{player_one} vs #{player_two}"
player_one_moves.each_index do |i|
  score = score_turn(player_one_moves[i], player_two_moves[i])
  puts "#{player_one_moves[i]} #{player_two_moves[i]} #{score}"
  overall += score
end

puts overall

