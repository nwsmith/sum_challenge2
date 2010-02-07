#/usr/bin/env python

import sys
import lookup
import genetic
import bf
import cStringIO

def getExpectedString(numbers, width):
    result = ""

    # get the 1 width numbers expected (as 5 row arrays)
    segNumbers = []
    for number in numbers:
        segNumbers.append(lookup.segmenttable[number])

    # zip the rows
    # [(segNumber[0][0],segNumber[1][0]),(segNumber[0][1],segNumber[1][1]) ...etc... ]
    zipped = zip(*segNumbers)

    # go through the zipped rows and join the zips with a space
    for i, row in enumerate(zipped):
        joinedRow = ' '.join(row)

        rowStr = ""
        for j, char in enumerate(joinedRow):
            rowStr += char
            # expand every 4 char starting at 1
            # _x_ _x_ -> _xxxx_ _xxxx_
            if ((j - 1) % 4) == 0:
                rowStr += (char*(width-1))

        result += rowStr + '\n'
        # rows 1 and 3 need to be expanded vertically
        if i == 1 or i == 3:
            for x in range(width-1):
                result += rowStr + '\n'
    return result

def main():
    width = int(sys.argv[2])

    numbers = []
    for char in sys.argv[1]:
        numbers.append(int(char))

    # ok so maybe I could be done at this point
    expected = getExpectedString(numbers, width)

    # lets mix it up

    # his noodly appendage has graced us with intelligent design
    genesoup = [c for c in '><+-.,']
    output = []
    genes = []

    e = genetic.BFEvolver(genesoup, sys.argv[1] + ' ' + sys.argv[2], expected)#, logfile = sys.stdout)
    result, gene = e.evolve(sys.maxint)
    print gene
    print result


if __name__ == '__main__':
    main()
