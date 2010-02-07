#!/usr/bin/env python

import sys
import string
from finder import Finder

def main():
    word1 = sys.argv[1]
    word2 = sys.argv[2]
    size = len(word1)+1 # include the new line

    finder = Finder(size, sys.argv[3])
    # flip it since we backtrack
    if finder.find(word2, word1):
        print word2
    else:
        print "no match"

def profile():
    import cProfile
    cProfile.run('[main() for x in xrange(1000)]', sort="time") # 16.586
        
def timer():
    import time
    s = time.time()
    [main() for x in xrange(1)] # 0.0260
    print time.time() - s

if __name__ == '__main__':
    # assumptions:
    # 1: lowercase ascii (0-127)
    # 2: full words only

    #timer()
    #profile()
    main()
