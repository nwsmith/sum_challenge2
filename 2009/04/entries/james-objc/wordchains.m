#include "Word.h"
#include "WordChainHelper.h"
#import <Foundation/Foundation.h>

int main(int argc, char *argv[]) {
	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
	
	NSUserDefaults *args = [NSUserDefaults standardUserDefaults];
	
	NSString *originWord = [args stringForKey:@"originWord"];	
	NSString *filename = [args stringForKey:@"filename"];	
	NSString *destinationWord = [args stringForKey:@"destinationWord"];
	NSString *fileString = [NSString stringWithContentsOfFile:filename];
	NSArray *words = [fileString componentsSeparatedByString:@"\n"];
	NSHashTable *table = [WordChainHelper getOptimizedDictionary:words wordSize:[originWord length]];	
	Word *chain = [WordChainHelper findPath:originWord destinationWord:destinationWord words:table];
	Word *word = chain;
	while (word != Nil) {
	    printf("%s\n", [[word word] cStringUsingEncoding:NSUTF8StringEncoding]);
		word = [word nextWord];
	}
	
	
	[pool release];
	return 0;
	
}

