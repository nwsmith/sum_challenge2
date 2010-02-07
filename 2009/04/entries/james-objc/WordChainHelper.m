#import "WordChainHelper.h"


@implementation WordChainHelper

+ (Word *) findPath:(NSString *) originWord destinationWord: (NSString *) destinationWord words: (NSHashTable *) words {
	Word *resultList = [[Word alloc] init];
	Word *resultListPointer = resultList;
	[resultListPointer setWord:originWord];
	[words removeObject:originWord];
	
	if ([self canMoveDirectlyToDestinationWord:originWord destinationWord:destinationWord]) {
		[self addNewWordToList:resultListPointer newWord:destinationWord];
		return resultList;
	} else {
		while (TRUE) {
		    NSString *nextWord = [self generateNextWord:[resultListPointer word] words:words];
			if ((nextWord == Nil) && ([[resultListPointer word] caseInsensitiveCompare:originWord] != NSOrderedSame)) {
				resultListPointer = [resultListPointer previousWord];
				[resultListPointer setNextWord:Nil];
			} else if (nextWord != Nil) {
				[nextWord retain];
				[self addNewWordToList:resultListPointer newWord:nextWord];
				resultListPointer = [resultListPointer nextWord];
				if ([self canMoveDirectlyToDestinationWord:[resultListPointer word] destinationWord:destinationWord]) {
					[self addNewWordToList:resultListPointer newWord:destinationWord];
					return resultList;
				}
				[words removeObject:nextWord];
			}
		}
	}
	return Nil;
}

+ (NSString *) generateNextWord:(NSString *) currentWord words: (NSHashTable *) words {	
	unichar charAry[[currentWord length]];
	[currentWord getCharacters:charAry];	
	for (int j = 0; j < [currentWord length]; j++) {
		for (int i = 97; i < 123; i++) {
			char prev = charAry[j];
			charAry[j] = i;
			NSString *new = [NSString stringWithCharacters:charAry length:[currentWord length]];
			if ([words containsObject:new]) {
				return new;	
			} else {
				charAry[j] = prev;	
			}
		}
	}
	return Nil;	
}

+ (BOOL) canMoveDirectlyToDestinationWord:(NSString *) currentWord destinationWord: (NSString *) destinationWord {
	int strikes = 0;
	for (int i = 0; i < destinationWord.length; i++) 
		if ([currentWord characterAtIndex:i] != [destinationWord characterAtIndex:i]) {
            strikes++;
		}	
	if (strikes == 1) {
	    return TRUE;
	}
	return FALSE;		
}

+ (void) addNewWordToList:(Word *) currentWordPointer newWord: (NSString *) newWord {
	Word *word = [[Word alloc] init];
	[word setWord:newWord]; 		
	[currentWordPointer setNextWord:word];
	[word setPreviousWord:currentWordPointer];	
}

+ (BOOL) listContainsWord:(Word *) words word: (NSString *) word {
	Word *resultListPointer = words;
	while (resultListPointer != Nil) {
		if ([[resultListPointer word] caseInsensitiveCompare:word] == NSOrderedSame) {
		    return TRUE;	
		} else {
		    resultListPointer = [resultListPointer nextWord];	
		}
	}
	return FALSE;
}

+ (NSHashTable *) getOptimizedDictionary:(NSArray *) dictionary wordSize : (int) wordSize {	
	NSHashTable *ret = [NSHashTable hashTableWithOptions:NSPointerFunctionsStrongMemory];
	for (NSString *str in dictionary) {
		if ([str length] == wordSize) {
			[ret addObject:str];
		}
	}
	return ret;	
}


@end
