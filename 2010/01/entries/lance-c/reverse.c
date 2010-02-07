#include <stdio.h>
#include <string.h>
char* reverse(char* inputString);

int main(int argc, char *argv[])
{ 
  int i;
  if(argc < 2) {
    printf("usage: %s <string to reverse>\n\n", argv[0]);
    return -1;
  }
  for(i=argc-1; i > 0; i--) {
    char* revString = reverse(argv[i]);
    printf("%s ",revString);
  }
  printf("\n");
  return 0;
}

char* reverse(char* inputString) {
  int length = strlen(inputString);
  char* revString = (inputString+length-1);
  int fwdIndex;
  int revIndex=0;
  for(fwdIndex=length-1; fwdIndex >= 0; fwdIndex--,revIndex++) {
    *(revString+revIndex) = *(inputString+fwdIndex);
  }
  *(revString+revIndex) = '\0';
  return revString;
}
