#import "Word.h"

@implementation Word

- (NSString *)word {
    return word;	
}

- (void)setWord:(NSString *)newWord {
	[newWord retain];
	[word release];
	word = newWord;
}

- (Word *)nextWord {
    return nextWord;
}

- (void)setNextWord:(Word *)newWord {
	[newWord retain];
	[nextWord release];
	nextWord = newWord;	
}

- (Word *)previousWord {
    return previousWord;	
}

- (void)setPreviousWord:(Word *)newWord {
	[newWord retain];
	[previousWord release];
	previousWord = newWord;	
}

@end
