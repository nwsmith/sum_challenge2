import sys
import random
import bf
import cStringIO

class Organism:
    soup = []
    minsize = 0
    maxsize = 0
    def __init__(self, parent = None):
        if parent:
            self.genes = parent.genes
        else:
            size = random.randint(Organism.minsize, Organism.maxsize)
            self.genes = [random.choice(Organism.soup) for _ in xrange(size)]
            
        self.fitness = None
        self.age = 0

    def mutate(self):
        if random.random() < 0.02:
            geneid = random.randint(0,len(self.genes)-1)
            self.genes[geneid] = random.choice(Organism.soup)
            self.age = 0
             
    def subtract(self):
        if random.random() < 0.5 and len(self.genes) > Organism.minsize:
            geneid = random.randint(0,len(self.genes)-1)
            self.genes.pop(geneid)
            self.age = 0

    def add(self):
        if random.random() < 0.5 and len(self.genes) < Organism.maxsize:
            geneid = random.randint(0,len(self.genes)-1)
            self.genes.insert(geneid,random.choice(Organism.soup))
            self.age = 0

    def breed(self, mate):
        crosspointone = random.randint(0,len(self.genes)-1)
        if random.randint(0,1):
            self.genes = self.genes[:crosspointone] + mate.genes[crosspointone:]
        else:
            self.genes = mate.genes[:crosspointone] + self.genes[crosspointone:]
        self.genes = self.genes[:Organism.maxsize]
        self.age = 0

    def __cmp__(self, other):
        # sort descending
        r = cmp(other.fitness, self.fitness)
        if r == 0:
            r = cmp(len(self.genes), len(other.genes))
        return r

    def __str__(self):
        return str(self.fitness) + ' ' + str(self.genes)

    def __repr__(self):
        return str(self.fitness) + ' ' + repr(self.genes)

class Population:
    def __init__(self, population_size = 40):
        self.size = population_size
        self.organisms = []

        for x in range(population_size):
            self.organisms.append(Organism())

    # crossover and mutate
    def breed(self):
        survivors = self.organisms

        required = self.size - len(self.organisms)
        for _ in range(required):
            organism = Organism(random.choice(survivors))
            mate = None
            if random.random() < 0.1:
                mate = Organism()
            else:
                oneid = int(random.expovariate(1))
                if oneid >= len(survivors):
                    oneid = len(survivors) - 1
                mate = survivors[oneid]
            organism.breed(mate)
            self.organisms.append(organism)

        # fancy - mutate is an instance method
        map(Organism.mutate, self.organisms[1:])

        # fancy - subtract is an instance method
        map(Organism.subtract, self.organisms[1:])

        # fancy - add is an instance method
        map(Organism.add, self.organisms[1:])
        for o in self.organisms:
            o.age += 1

    def cull(self, keep):
        self.organisms = [self.organisms[0]] + [o for o in self.organisms[1:keep] if o.age < 50]
        



class Evolver(object):
    def __init__(self, logfile):
        if (logfile):
            self.log = logfile
        else:
            self.log = open('output.log', 'w')
        
    def populate(self):
        self.population = Population()        

    def survive(self):
        map(self.applyfitness, self.population.organisms)
            
        self.population.organisms.sort()
        return self.population.organisms[0]

    # Organisms are judged on fitness
    # the weak die
    # Survivors breed
    # ad nauseum
    def evolve(self, threshold):            
        best = self.survive()
        print >>self.log, 'Generation', 0, 'Fitness', best.fitness, "genelen", len(best.genes), "resultlen", len(best.result), 'age', best.age, 'Result\n', ''.join([chr(r) for r in best.result if r > 0 and r < 128])
        print >>self.log, ''.join(best.genes)
        print >>self.log 

        self.population.cull(keep = 5)
        i = 0
        while best.fitness != threshold:
            self.population.breed()

            best = self.survive()
            self.population.cull(keep = 5)
            i += 1

            if i % 10 == 0 and best.result:
                print >>self.log, 'Generation', i, 'Fitness', best.fitness, "genelen", len(best.genes), "resultlen", len(best.result), 'age', best.age, 'Result\n', ''.join([chr(r) for r in best.result if r > 0 and r < 128])
                print >>self.log, ''.join(best.genes)
                print >>self.log                

        print >>self.log, 'Generation', i, 'Fitness', best.fitness, "genelen", len(best.genes), "resultlen", len(best.result),'Result\n', ''.join([chr(r) for r in best.result])
        print >>self.log, ''.join(best.genes)
        print >>self.log
        
        return ''.join([chr(r) for r in best.result]), ''.join(best.genes)

    @classmethod
    def get_differences(clz, expected, actual):
        ealist = map(None, expected, actual)
        for i, (e,a) in enumerate(ealist):
            x0 = 255
            x1 = -255
            if e != None:
                x0 = ord(e)
            if a != None:
                x1 = a
            delta = abs(x1 - x0)

            # find first different char, the closer to start of string, the more it is penalized.
            # the delta determines how much it should be penalized
            if delta != 0:          
                return ((len(ealist)-i) * 512) + delta

        return 0

    def get_similar(self, expected, actual):
        cnt = 0
        for i in xrange(len(expected)):
            if len(actual) > i and expected[i] == actual[i]:
                cnt += 1

        return cnt + (abs(len(expected) - len(actual))*10)

class PatternEvolver(Evolver):
    def __init__(self, genesoup, expected):
        Organism.soup = genesoup
        Organism.minsize = len(expected)
        Organism.maxsize = len(expected)

        self.expected = expected
        self.populate()

    def applyfitness(self, organism):

        diffs = self.get_differences(self.expected, organism.genes)
        organism.fitness = diffs
        return diffs


class BFEvolver(Evolver):
    def __init__(self, genesoup, input, expected, logfile=None):
        Evolver.__init__(self, logfile)
        Organism.soup = genesoup

        Organism.minsize = 1
        Organism.maxsize = 1000*len(input)

        self.expected = expected
        self.expectedarray = [ord(c) for c in self.expected]
        self.input = input
        self.populate()


    def applyfitness(self, organism):
        fitness = -1
        result = None
        genes = r''  + ''.join(organism.genes)

        organism.result = result
        try:
            b = bf.BF()
            stdin = cStringIO.StringIO(self.input)
            b.run(genes, stdin)
            result =  b.output

        except IndexError:
           organism.fitness = -100000000
           return
        except RuntimeError:
           organism.fitness = -100000000
           return
            
        fitness -= self.get_differences(self.expected, result)

        if result == self.expectedarray:
            fitness = sys.maxint

        organism.fitness = fitness
        organism.result = result




