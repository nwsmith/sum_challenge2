#!/usr/bin/ruby

def read_history(file_name)
  history = Array.new
  if File.exists?(file_name)
    File.readlines(file_name).each do |line|
      entry = line.split("=")
      history.push([entry[0],entry[1].to_i()])
    end
  end
  return history
end

def write_history(file_name, history)
  f = File.open(file_name, "w")
  history.each_index do |i|
    entry = history[i][0] + "=" + history[i][1].to_s() + "\n"
    f.write(entry)
  end
  f.close
end

def guess_opponent_next_move(myLastMove, history)
  opponentMove = nil
  count = 0

  history.each_index do |i|
    combination = history[i][0]
    combinationCount =  history[i][1]
    if combination[0,1] == myLastMove and combinationCount > count
      opponentMove = combination[1,1]
    end
  end

  return opponentMove
end

def get_winning_move(opponentMove)

  case opponentMove
  when 'R' then move = 'P'
  when 'S' then move = 'R'
  when 'P' then move = 'S'
  else move = 'S'
  end

  return move
end

def update_history(history, opponentLastMove, myLastLastMove)

  if opponentLastMove != nil and myLastLastMove != nil
    combination = myLastLastMove + opponentLastMove
    found = false
    history.each_index do |i|
      if history[i][0] == combination
        history[i][1] = history[i][1] + 1
        found = true
        break
      end
    end
    if not found
      history.push([combination, 1])
    end
  end
  
end

def pop_my_move(history, count)
  myLastMove = nil
  entry = history.last
  
  if entry != nil and entry[1] == count
    myLastMove = entry[0]
    history.pop
  end
  
  return myLastMove;
end



opponent = ARGV[0]
command = ARGV[1]
opponentLastMove = ARGV[2]

fileName = "aplayer_" + opponent + ".tmp"
history = Array.new

myLastMove = nil

history = read_history(fileName)

#if history.length > 0

  myLastMove = pop_my_move(history, 0)
  myLastLastMove = pop_my_move(history, -1)

  update_history(history, opponentLastMove, myLastLastMove)

  if myLastMove != nil
    history.push([myLastMove, -1])
  end

#end

move = nil

case command
when 'start'
  move = 'R'
when 'turn'
  opponentMove = guess_opponent_next_move(myLastMove, history)
  move = get_winning_move(opponentMove)
when 'finish'
end

if move != nil
  history.push([move,0])
  puts move
end

write_history(fileName, history)