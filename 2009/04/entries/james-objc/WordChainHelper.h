#import <Cocoa/Cocoa.h>
#import "Word.h"

@interface WordChainHelper : NSObject {
	
}

+ (NSString *) generateNextWord:(NSString *) currentWord words: (NSHashTable *) words;
+ (Word *) findPath:(NSString *) originWord destinationWord: (NSString *) destinationWord words: (NSHashTable *) words;
+ (NSHashTable *) getOptimizedDictionary:(NSArray *) dictionary wordSize : (int) wordSize;
+ (BOOL) canMoveDirectlyToDestinationWord:(NSString *) currentWord destinationWord: (NSString *) destinationWord;
+ (void) addNewWordToList:(Word *) currentWordPointer newWord: (NSString *) newWord;
+ (BOOL) listContainsWord:(Word *) words word: (NSString *) word;

@end
