#import <Cocoa/Cocoa.h>


@interface Word : NSObject {
	
	NSString * word;
	Word * nextWord;
	Word * previousWord;
	
}

- (NSString *)word;
- (void)setWord:(NSString *)newWord;
- (Word *)nextWord;
- (void)setNextWord:(Word *)newWord;
- (Word *)previousWord;
- (void)setPreviousWord:(Word *)newWord;

@end
