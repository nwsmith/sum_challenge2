#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <string>
#include <vector>
#include <fstream>
#include <iostream>
#include <algorithm>
#include <queue>
#include <list>
#include <ext/hash_set>

using namespace std;
using namespace __gnu_cxx;

//lame boilerplate needed to be able to hash strings
namespace __gnu_cxx {
  template<> struct hash<std::string> {
    size_t operator()(const std::string& x) const {
      return hash<const char*>()( x.c_str() );
    }
  };
}
 
vector<string> dict;
string start;
string goal;
int goalLength;

int distance(string* strA, string* strB) {
  int count = 0;
  string::iterator itA = strA->begin();
  string::iterator itB = strB->begin();
  for (int i=0;i<goalLength;i++) {
    if (*itA != *itB) {      
      count++;
    }
    itA++; itB++;
  }
  return count;
}

//the early exit makes it faster than (distance(strA, strB) == 1)
//can be optimized to use SSE instructions
bool isDistanceEqualToOne(string* strA, string* strB) {
  int count = 0;
  string::iterator itA = strA->begin();
  string::iterator itB = strB->begin();
  for (int i=0;i<goalLength;i++) {
    if (*itA != *itB) {      
      count++;
      if (count>1) {
        break;
      }
    }
    itA++; itB++;
  }
  return (count==1);
}

class WordChain {
  public:
    WordChain* _prior;
    string* _lastWord;
    int _size;

    WordChain(string* lastWord) {
      _prior = NULL;
      _lastWord = lastWord;
      _size = 1;
    }  

    WordChain(WordChain* w, string* lastWord) {
      _prior = w;
      _lastWord = lastWord;
      _size = w->_size+1;
    }  

    string mkString(string delim) {
      string ret = "";
      
      WordChain* curr = this;
      while (curr != NULL) {
        if (ret.size() == 0) {
          ret = *(curr->_lastWord);
        } else {
          ret = *(curr->_lastWord) + delim + ret;
        }      

        curr = curr->_prior;
      } 
      return ret;
    }
};


struct WordChain_compareFastest: binary_function<WordChain*, WordChain*, bool> {
  bool operator()(const WordChain* wc1, const WordChain* wc2) const {
    int wc1Distance = distance(wc1->_lastWord, &goal);
    int wc2Distance = distance(wc2->_lastWord, &goal);
    if (wc1Distance != wc2Distance) { 
      return wc2Distance < wc1Distance;
    } else {
      return wc2->_size < wc1->_size;
    }
  }
}; 


void getDictFromFile(char* dictFileName, vector<string>* vect, int wordLen) {
    char* dictMapped;
    int dictFd = open(dictFileName, O_RDONLY);
    if (dictFd == -1) {
      cout << "error in open" << endl;
      exit(1);
    }

    struct stat dictStat;
    if (stat(dictFileName, &dictStat) == -1) {
      cout << "error in stat" << endl;
      exit(1);
    }
    int dictFileSize = dictStat.st_size;

    dictMapped = (char*)mmap(0, dictFileSize, PROT_READ, MAP_SHARED, dictFd, 0);
    if (dictMapped == MAP_FAILED) {
        cout << "error in close" << endl;
        exit(1);
    }

    char* lineStart = dictMapped;
    char* dictMappedEnd = dictMapped+dictFileSize;
    char* currPos;
    for(currPos=dictMapped;currPos != dictMappedEnd;currPos++) {
      char currChar = *currPos;
      if (currChar == '\n' || currChar=='\r') {  
        int len = (currPos-lineStart);
        if (len == wordLen) {
          vect->push_back(string(lineStart, len));
        }
        lineStart = currPos+1;
      }
    }  
    int len = (currPos-lineStart);
    if (len == wordLen) { 
      vect->push_back(string(lineStart, len));
    }
    
    if (munmap(dictMapped, dictFileSize) == -1) {
        cout << "error unmapping" << endl;
        exit(1);
    }
    close(dictFd);
}


int main(int argc, char* argv[]) {
  if (argc < 4) {
     cout << "Usage: " << argv[0] << " duck ruby /usr/share/dict/words" << endl;
     return 1; 
  }

  start = argv[1];
  goal = argv[2];
  goalLength = goal.length();

  vector<string> dict;
  dict.reserve(100000);
  getDictFromFile(argv[3], &dict,goalLength);

  hash_set<string, hash<string> > visited;

  priority_queue<WordChain*, std::vector<WordChain*>, WordChain_compareFastest> pq;
  WordChain* chainStart = new WordChain(&start);
  pq.push(chainStart); 

  while (!pq.empty() && ((*pq.top()->_lastWord) != goal)) {
    WordChain* currChain = pq.top();
    pq.pop();
    string* currChainEnd = currChain->_lastWord;
    //cout<<"Checking: ["<< (currChain->mkString(", ")) <<"]" << endl;
	
    vector<string*> nextWords;
    for (vector<string>::iterator currWord = dict.begin(); currWord != dict.end(); currWord++) {    
      string* currDictWordPtr = &(*currWord);
      if (isDistanceEqualToOne(currChainEnd, currDictWordPtr)) {
        if (visited.find(*currDictWordPtr) == visited.end()) {
          nextWords.push_back(currDictWordPtr);
        }
      }
    }

    for (vector<string*>::iterator currWord = nextWords.begin(); currWord != nextWords.end(); currWord++) {    
      visited.insert(**currWord);
      pq.push(new WordChain(currChain, *currWord));
      //cout<<"Adding: "<<distance(*currWord, &goal)<<"["<< (**currWord) <<"]" << endl;
    }
  }

  if (pq.empty()) {
    cout << "No chain found" << endl;
  } else {
    cout << pq.top()->mkString("\n") << endl;
  }

  return 0;
}

