#!/usr/bin/env python

import sys
import string

class Finder(object):
    def __init__(self, wordlen, filename):

        wordfile = open(filename)

        # list comprehension is faster but len is slow
        #(when profiled, fast when timed - yay, and yay for heisenberg)
        self.trie = set([line[:-1] for line in wordfile if len(line) == wordlen])

        self.checked = set()

        self.alphabet = string.ascii_lowercase

    def find(self, word, target):
        self.checked = set()
        self.checked.add(word)
        self.target = target
        self.letters = list(enumerate(zip(word, self.target)))
        if not target in self.trie:
            return False
        return self._find(word)

    def _find(self, word):
        if word == self.target:
            return True


        # very similar code, but do you really want to push extra methods on to
        # the stack in a recursive function?
        for i, (letter1, letter2) in self.letters:
            if letter1 != letter2:
                prefix = word[:i]
                suffix = word[i+1:]
                if letter2 == letter1:
                    continue
                curr = prefix + letter2 + suffix
                if curr in self.trie and not curr in self.checked:
                    self.checked.add(curr)
                    newfound = self._find(curr)
                    if newfound:
                       print curr
                       return newfound

        for i, (letter1, letter2) in self.letters:
            if letter1 != letter2:
                prefix = word[:i]
                suffix = word[i+1:]
                for x in self.alphabet:
                    if x == letter1:
                        continue
                    curr = prefix + x + suffix
                    if curr in self.trie and not curr in self.checked:
                        self.checked.add(curr)
                        if self._find(curr):
                           print curr
                           return True

