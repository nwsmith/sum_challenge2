OK, here is another boring submission... but in a weird language.

Flavor of the month is... J:
http://www.jsoftware.com/

Download and install the "IDE" from:
http://www.jsoftware.com/stable.htm

To run the program:

1. start JWD ("IDE"), I have it in /home/darko/j602/bin
2. load the .ijs file (attached) (safest bet - put it into j602 directory and type
   load 'map.ijs' (or you can use full path, note that single quotes are important)

3. if there are more than 200(?) continents, output will get truncated, so - Edit->Configure->max lines after, set to something larger than needed
4. run my program

  solve 'map01' (if map files are in j602, if not, use the full path)

I was hoping I was going to do it in J, but it more like C, translated into J. I just couldn't figure out how to do it J-like.

And - it is really slow. On 1024x1024 grid it takes maybe 10-12 minutes (Java program takes 0.3 secs)
512x512 takes some 30 secs. There, you've been warned.

I tried to comment it just to make it a bit more readable if anyone actually takes a look at it. Not sure how helpful it is.
