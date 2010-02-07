h1. March 2009 - Seven Segment Numbers

This month's solium programming challenge is to write a program that displays LCD style numbers at adjustable sizes.

The digits to be displayed will be passed as an argument to the program. You must provide some way to pass the size into the program, where size is a positive integer with a default of two.

For example, if your program is called with input 012345:

The correct display is:

{code} --       --  --        --
|  |   |    |   | |  | |
|  |   |    |   | |  | |
          --  --   --   --
|  |   | |      |    |    |
|  |   | |      |    |    |
 --       --  --        --
{code}
And for input 26789 and size of 1:

The correct display is:

{code} -   -  -   -   -
  | |    | | | | |
 -   -      -   -
|   | |  | | |   |
 -   -      -   -
{code}
Note the single column of space between digits in both examples. For other values of size, simply lengthen the - and \| bars.

Email your solutions to nathan.smith@solium.com and have fun\!
