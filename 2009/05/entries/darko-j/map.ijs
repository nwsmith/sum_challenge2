NB. Count the sizes of connected components in a grid using a DFS
NB. Had to implement a stack because it seems J does not have much of a space reserved for anything

load 'printf'
confusing_part=: 3 : '0,(((1+i.#y){(2 2+$y)$0)+(i.#y){|:(0,(|:y),0)),0'
NB. Just padding the 2D array with zeros
NB. Argument in a single-argument function ('monadic verb') is usually y for some reason
NB. i.#y means "generate the list of integers [0..size_of_y)
NB. so for each row we...
NB. |: transposes the array, so second part (after '+') is - transpose row, add zeros (left and right), transpose
NB. (2 2 + $y) $ 0 - $y is size of y (say (m n)), so this creates 2D array of zeros of size (m+2 n+2)
NB. add that 'old' array to the one we read in
NB. add columns of zeros (left and right)
NB. If you think that was fun, here is the quicksort:
NB. quicksort=: (($:@(<#[) , (=#[) , $:@(>#[)) ({~ ?@#)) ^: (1<#)
NB. how about a suffix array of string S?
NB. sort|.each;:\|.S

solve=:verb define				NB. functions are called 'verbs' in J
di=. _1 _1 0 1 1  1  0 _1			NB. 8 neighbours
dj=. 0   1 1 1 0 _1 _1 _1
g=.confusing_part '+'=>cutopen toJ 1!:1<y	NB. read the file into a 2D boolean array, pad it with zeros
r=.''						NB. r will hold sizes of the continents 
n=.0						NB. number of continents
s=.''						NB. stack
for_ii. i.(_1+#g)				NB. for(ii=0;ii<g.length;ii++) // i and j not used as variables in J
  do.
   for_jj. i.(_1+#|:g)				NB. for(jj=0;jj<g_transposed.length;jj++)
     do.					NB. I just realized I am trying to access the array out of boundaries
						NB. below. Seems that it wraps around ?!? (which is ok, because of my padding)
      if. (jj{ii{g)				NB. if(g[ii][jj])
       do.
        n=.>:n					NB. this is n++ in C... 
	c=.0					NB. c will hold the count of the cells in the continent
	]s=.s(,~)ii				NB. push(ii);
	]s=.s(,~)jj				NB. push(jj);
        g =. 0 ((<ii;jj)}) g			NB. g[ii][jj]=0;
	while. ((#s)>0)				NB. #s is size of the stack		
	  do.
          c=.>:c				NB. c++... but they have some weird stuff shortened to a single character
	  'jjj s'=.({. ; }.) s			NB. jjj=pop();
	  'iii s'=.({. ; }.) s			NB. iii=pop();
	  ti=.iii +/ di				NB. get the list of neighbours
	  tj=.jjj +/ dj
	  for_kk. i.#di				NB. for each neighbour
  	    do.
   	     iii=.kk{ti				
   	     jjj=.kk{tj
   	     if. (jjj{iii{g)			NB. if(g[iii][jjj])
    		do.
		  g =. 0 ((<iii;jjj)}) g        NB. g[iii][jjj]=0;
		  s=.s(,~)iii			NB. push(iii);
	          s=.s(,~)jjj			NB. push(jjj);
             end.
   	   end.
	end.
        r =. r, c				NB. add the size of the island to the list
      end.  
   end.
end.
ostr=.'%d continents'				NB. printf is, well, printf
if.(n=1)do.ostr=.'%d continent' end.
ostr printf n
,.r						NB. print the list in a column (gets truncated?)
)




