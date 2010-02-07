#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define FILE_BUF_SZ ((1<<20) * sizeof(char))
#define FILE_BUF_SZ ((1<<5) * sizeof(char))

int affinity(char *f, char *s);
int legal_move(char *f, char *s);

int main(int argc, char **argv) {
    int length, can_abort, file_sz;
    int word_count, chain_idx, s_found, g_found, best_affinity, word_idx;
    char *dict_file, **dictionary, **chain;

    if (argc < 4) {
      printf("Usage: a.out <start> <goal> <path to dictionary>\n");
      return -1;
    }
    length = strlen(argv[1]);
    if (length != strlen(argv[2])) {
      printf("Error: words must be of same length\n");
      return -1;
    }

    word_count = chain_idx = 0;
    s_found = g_found = 0;

    /* Giant blob for our file */
    dict_file = (char*) malloc(FILE_BUF_SZ);
    /* Giant blob for our chain */
    chain = (char**)malloc(FILE_BUF_SZ);
    /* Giant blob for our working set */
    dictionary = (char**)malloc(FILE_BUF_SZ);

    FILE *f = fopen(argv[3], "r");
    file_sz = fread(dict_file, 1, FILE_BUF_SZ, f);

    chain[chain_idx] = argv[1];
    /* We can cheat on processing in this special case...*/
    can_abort = legal_move(argv[1], argv[2]);
    
    int i, j;
    j = 0;
    
    while(*dict_file) {
      char *q = dict_file;
      while (*dict_file != '\n') {
        ++dict_file;
        ++j;
      }
      /* replace newline with string terminator */
      *dict_file = 0;
      ++dict_file;

      if (j == length) {
        if (!s_found && *q == argv[1][0] && !strcmp(q, argv[1])) {
          ++s_found;
        } else {
          if (!g_found && *q == argv[2][0] && !strcmp(q, argv[2])) {
            ++g_found;
          }
          dictionary[word_count++] = q;
        }
      }
      /* start->goal is a chain and they're in the dictionary, so bail */
      if (can_abort && s_found && g_found) {
        goto shortcut; /* <-- some would consider this harmful... */
      }
      j = 0;
    }

    if (!(s_found || g_found)) {
      printf("%s or %s is not in dictionary\n", argv[1], argv[2]);
      return -1;
    }

    do {
      word_idx = best_affinity = -1;

      for (i = 0; i < word_count; ++i) {
        if (legal_move(chain[chain_idx], dictionary[i])) {
          int curr_affinity = affinity(dictionary[i], argv[2]);
          if (curr_affinity == length) {
            goto shortcut;
          } 

          if (curr_affinity > best_affinity) {
            word_idx = i;
            best_affinity = curr_affinity;
          }
        } 
      }
      /* dead end - reject word and keep trying */
      if (best_affinity < 0) {
        --chain_idx;
        /* backtracked to nothing, so no chain */
        if (chain_idx < 0) {
          printf("No chain found\n");
          return -1;
        }
      } else {
        chain[++chain_idx] = dictionary[word_idx];
        /* skip words already in the chain */
        dictionary[word_idx] = "\0"; 
      }
    } while(1); /* Not quite infinite */

shortcut:
      for (j = 0; j <= chain_idx; ++j) {
        printf("%s\n", chain[j]);
      }
      printf("%s\n", argv[2]);

    return 0;
}

/* How many characters do 2 words have in common? */
int affinity(char *f, char*s) {
  int affinity = 0;
  do {
    if (*f++ == *s++) ++affinity; 
  } while (*f != '\0');
  return affinity;
}

int legal_move(char *f, char *s) {
  int legal = 0;
  do {
    if (*f++ != *s++ && legal++ == 1) return 0; 
  } while (*f != '\0');
  return 1;
}
