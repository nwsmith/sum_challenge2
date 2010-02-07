h3. Fastest times so far:


h4. (java->ruby):   0.0030 seconds


h4. (print->cramp): 0.0044 seconds

Given two words of equal length, build a chain of words connecting the first to the second. Each word in the chain must be in the dictionary and every step along the chain changes exactly one letter from the previous word. Words may not be repeated. In order to keep the playing field even, your solution will be run with [the attached dictionary|^words.zip].

The result should be a word chain starting with the first word and ending with the last, and showing an error if no chain can be found.

Here's an example for duck->ruby.&nbsp; Note that there are many different valid chains.

{code}duck
ruck
rusk
ruse
rube
ruby
{code}Programs must be compilable by me (makefiles are fine if you need 'em) - no binaries accepted.  Files must be runnable with three args:

{code}./program <start> <end> <dictionary>

# eg
./program duck ruby ./words.txt
{code}Scoring this month will be calculated as follows:

# There will be a number of test cases (i.e. a chain from "duck \-> ruby" would be one test case)
# For *each* test case, programs will be run twice, a) is for the fastest generation of any arbitrary chain, b) for the fastest generation of the longest chain.&nbsp; The following points will be awarded:
## The fastest execution time will be awarded the same number of points as there are entries, with a minimum of 10.  The next fastest will get 9, then 8, etc.
## The fastest execution time for finding the longest chain will be awarded (number of entries - 5) bonus points, with a minimum of 5.  The next fastest will get 4, then 3, etc.  This means not all entries will get bonus points, even if they find the longest chain.

*Note:* The longest chain bonus is biased toward length.&nbsp; If entry 1 has a 14 word chain that takes 5 seconds and entry 2 has a 13 word chain that takes .5 seconds, entry 1 would get the highest bonus.


----
h3. Example:

There are 14 entries.
Entry 1 is the second fastest, and fastest longest chain
Entry 2 is the fastest, and third fastest longest chain
Entry 3 is the slowest and thirteenth fastest longest chain
Entry 4 is the fourth fastest and second fastest longest chain

The base score is 14, the base bonus is 9

Entry 1 gets 13 points (second) + 9 (first longest) = 22
Entry 2 gets 14 points (fastest) + 7 (third longest) = 21
Entry 3 gets 1 points (last) + 0 (thirteenth longest) = 1
Entry 4 gets 11 points (fourth) + 8 (second longest) = 20

In this case, the ranking for this test case is 1, 2, 4, 3
----
The totals for each test case will be aggregated and the winner will be the one with the highest total score.

You can [download the timing script|^timer.rb] that will be used.

(1) It is fine to have separate programs for fastest and longest chain.
