#!/usr/bin/env python

"""this interpreter is actually slow as anythin and the bracket balancing is broken"""

"""A Brainfuck interpreter"""
__author__ = "Andrew Pennebaker (andrew.pennebaker@gmail.com)"
__date__ = "18 Nov 2005 - 27 Feb 2006"
__copyright__ = "Copyright 2006 Andrew Pennebaker"
__license__ = "GPL"
__version__ = "0.5"
__URL__ = "http://snippets.dzone.com/posts/show/3536"



import sys


class BF(object):
    def __init__(self):
        self.tape = [0] * 100
        self.address = 0
        self.numinstructions = 0
        self.output = []
        
    def sublevel(self, toplevel):
        i = 0

        # until a balanced-bracket code block is found, add a character
        if toplevel.count("[") != toplevel.count("]"):
            raise RuntimeError("Unbalanced brackets")

        while toplevel[0:i + 1].count("[") != toplevel[0:i + 1].count("]") or i > len(toplevel): i += 1

        return toplevel[1:i]

    def run(self, instructions, stdin=sys.stdin):

        if len(instructions) == 0:
             raise RuntimeError("Infinite loop")

        position = 0
        if position >= len(instructions):
            self.numinstructions += 1
        if self.numinstructions > 1000:
            raise RuntimeError("Too many operations, killed")
        while position < len(instructions):
            self.numinstructions += 1
            cmd = instructions[position]

            if cmd == "<": self.address -= 1
            elif cmd == ">": self.address += 1
            elif cmd == "+": self.tape[self.address] += 1
            elif cmd == "-": self.tape[self.address] -= 1
            elif cmd == ".":
                self.output.append(self.tape[self.address])
            elif cmd == ",":
                try: self.tape[self.address] = ord(stdin.read(1))
                except: self.tape[self.address] = 0
            elif cmd == "[":
                level = self.sublevel(instructions[position:])
                while self.tape[self.address] != -1: self.run(level,stdin)
                position += len(level) + 1

            position += 1
