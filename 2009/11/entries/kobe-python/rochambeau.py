import sys
import random
import csv
import os

def move(lastmove):
    if lastmove == "R":
        print "P"
    elif lastmove == "P":
        print "S"
    elif lastmove == "S":
        print "R"
    else:
        hand = "R","P","S"
        print random.choice(hand)

def start(opponent):
    moves = getmoves(opponent)
    firstmove = "?"
    if len(moves)>0:
        firstmove = moves[0]
    move(firstmove)
    
def turn(opponent, lastmove):
    writelastmove(opponent, lastmove)
    moves = getmoves(opponent)
    lastmove = "?"
    if len(moves)>0:
        lastmove = moves[len(moves)-1]
    move(lastmove)
    
def writelastmove(opponent, lastmove):
    moves = getmoves(opponent)
    moves.append(lastmove)
    csvWriter = csv.writer(open('kobe_'+opponent+'.csv', 'wb'))
    csvWriter.writerow(moves)


def getmoves(opponent):
    moves = []
    if os.path.exists('kobe_'+opponent+'.csv'):
        csvReader = csv.reader(open('kobe_'+opponent+'.csv', 'rb'))
        moves = csvReader.next()
    return moves
                 
def invalidcommand():
    sys.stderr.write("Invalid command: python " +  ' '.join(sys.argv) + "\n")
    sys.stderr.write("Expected: python rochambeay.py opponent start|turn|finish [last_move]\n")
    
if len(sys.argv) < 3:
    invalidcommand()
    sys.exit()

opponent = sys.argv[1]    
command = sys.argv[2]
lastmove = ""
if len(sys.argv) > 3:
    lastmove = sys.argv[3]
    
if command == "start":
    start(opponent)
elif command =="turn":
    turn(opponent,lastmove)
elif command == "finish":
    writelastmove(opponent,lastmove)
    print "Did we win?"
else:
    invalidcommand()


    

    
