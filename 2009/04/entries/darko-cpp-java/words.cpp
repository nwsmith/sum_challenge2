#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <iostream>
#include <set>
#include <tr1/unordered_map>

#define NN (1<<16)
#define oo (1<<25)

using namespace std;

typedef set<int>::const_iterator sit;

char words[NN][16];
char buf[1<<20];
int len, sz, q[NN], p[NN];
tr1::unordered_map< int , set < int > > map1, map2;

void print(int i){
	int j;
	if(i==oo) return;
	print(p[i]);
	for(j=0;j<len;j++)buf[sz++]=words[i][j];
	buf[sz++]='\n';
}

int get_hash(char*str){
	int h=5381;
	while(*str) h=73*h+*str++;
	return h;
}

int main(int argc, char* argv[]){
	int n,s,t,qs,qe,i,u,v;
	memset(p,-1,sizeof(p));
	if(argc != 4){
		printf("Usage: ./a.out <word1> <word2> <file_name>\n");
		return -1;
	}
	else{
		len=0;
		while(argv[1][len]>0)len++;
		i=0;
		while(argv[2][i]>0)i++;
		if(i!=len){
			printf("words [%s] and [%s] have different lengths\n", argv[1], argv[2]);
			return -1;
		}
	}
	freopen(argv[3],"r",stdin);
	s=t=-1;
	n=0;
	v=read(0,buf,sizeof(buf));
	u=0;
	while(1){
		while(u < v && buf[u]<=' '&&buf[u]>=0){
			u++;
		}
		if(u>=v)break;
		i=0;
		while(buf[u]>32)words[n][i++]=buf[u++];
		if(i==len) {
			words[n][i]='\0';
			for(i=0;i<len;i++) if(argv[1][i]!=words[n][i]) break;
			if(i==len)s=n;
			for(i=0;i<len;i++) if(argv[2][i]!=words[n][i]) break;
			if(i==len)t=n;
			for (int kk = 0; kk < len; kk++) {
				char tc=words[n][kk];
		    		words[n][kk] = '\\';
		    		int hash = get_hash(words[n]);
		    		words[n][kk] = tc;
		    		map1[hash].insert(n);
		    		map2[n].insert(hash);
			}
			n++;
		}
	}
	// printf("map size = %d\n", (int)map1.size());
	if(s<0||t<0){
        	if(s<0) printf("[%s] not found in the dictionary\n", argv[1]);
        	if(t<0) printf("[%s] not found in the dictionary\n", argv[2]);
        	return -1;
	}

	qs=qe=0;
	q[qe++]=s;
	p[s]=oo;
        while(qs<qe)	if(q[qs]==t){sz=0;print(q[qs]);write(1,buf,sz);return 0;}
        		else for (sit iter1=map2[u=q[qs++]].begin(); iter1 != map2[u].end(); ++iter1)
				for (sit iter2=map1[*iter1].begin(); iter2 != map1[*iter1].end(); ++iter2)
		    			if (p[*iter2]<0) p[q[qe++]=*iter2]=u;
        printf("Could not find a chain from [%s] to [%s]\n", argv[1],argv[2]);
	return 0;
}
