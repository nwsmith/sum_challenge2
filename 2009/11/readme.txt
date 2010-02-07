This time it's WAR!

This month we're going to do something different - we aren't going to find a winner through voting.  We aren't going to find a winner through timing.  We're going to find a winner through conquering!

That's right, this time it's war, rock-paper-scissor style.  Your challenge this month is to create a program that will destroy all comers in a game of rock-paper-scissors.  The rules are simple:

No random bots (that takes all the fun out of it!) - you must have some kind of strategy, even if it's just "always rock"

Your program must accept that a new game has started as follows:

<program-name> <opponent-name> start

and output either "R", "P", or "S"

Then, for the number of turns in the game - 2, you will receive the following:

<program-name> <opponent-name> turn <last-move>

This will consist of the opponent name, the word "turn" and the last-move that the opponent made - the letter "R", "P", or "S".  Finally you will get the message:

<program-name> <opponent-name> finish <last-move>

Programs are allowed to write any files to disk *in the working directory*, this will allow you to keep statistics on the match and use it (or not use it - the choice is yours!) to help hone your strategy.  These files will persist throughout the tournament, so that if you face the same opponent more than once, the second time you will (potentially) have gathered game winning information. 

As with all other challenges, the only limit is a language that can read command line arguments and ouput to stdout.  I've provided the scoring program so you can tell me if you think it's cheating or not!

Feel free to direct any questions my way.
