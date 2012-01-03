March 2010 - Run, Forrest, Encode 

!forrest.jpg|align=left,height=250,padding!

This month's challenge is to replicate the compress and expand programs described way back in _Software Tools in Pascal_, to provide run length encoding.

The compress program takes a text file as input and writes what is hopefully a smaller version of the text as output, while expand inverts the operation.

Compression involves replacing runs of four or more of the same character with a three-character code consisting for a tilde, a letter A through Z indicating 1 through 26 repetitions, and the character to be repeated.  Runs of longer than 26 characters require muliple encodings.  Literal tildes are represented as a run of length 1.

For exmaple, given this string:

{code}
ABBB~CDDDDDEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
{code}

is encoded to this string:

{code}
ABBB~A~C~ED~ZE~DE
{code}

Entries will be due April 16th - so go have fun! 
