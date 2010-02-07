nlign=left,width=400!
To help get the rust out of the hinges after going so long with the July (/August/September/half of October) competition, we're going to do a quick and easy one to finish out October.  The due date for this one, therefore is only a couple weeks away, on *November 2, 2009*

Morse code is a way to encode telegraphic messages in a series of long and short sounds or visual signals. During transmission, pauses are used to group letters and words, but in written form the code can be ambiguous.

For example, using the typical dot (.) and dash ( - ) for a written representation of the code, the word
{code}...---..-....-
{code}in Morse code could be an encoding of the names Sofia or Eugenia depending on where you break up the letters:

{code}...|---|..-.|..|.-    Sofia
.|..-|--.|.|-.|..|.-  Eugenia
{code}
This week's quiz is to write program that displays all possible translations for ambiguous words provided in code.

Your program will be passed a word of Morse code on STDIN. Your program should print all possible translations of the code to STDOUT, one translation per line. Your code should print gibberish translations in case they have some meaning for the reader, but indicating which translations are in the standard linux dictionary will be worth a single bonus point (i.e. you will be working from a base of 3 points, with the possibility for 6 if you get first place).

We will only focus on the alphabet for this quiz to keep things simple. Here are the encodings for each letter:

{code}A .-            N -.
B -...          O ---
C -.-.          P .--.
D -..           Q --.-
E .             R .-.
F ..-.          S ...
G --.           T -
H ....          U ..-
I ..            V ...-
J .---          W .--
K -.-           X -..-
L .-..          Y -.--
M --            Z --..
{code}
This contest will follow "standard" rules, meaning you can write it in any language that reads/writes STDIN/STDOUT and will run in linux, with the emphasis on creativity.

Have fun\!
