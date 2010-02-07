#!/usr/bin/ruby

#Static Strategies
strat_0 = ["R","P","S"];
strat_1 = ["R","R","R"];
strat_2 = ["S","S","S"];
strat_3 = ["P","P","P"];
strat_4 = ["S","R","P"];
strat_5 = ["R","P","R","S"];
strat_6 = ["P","P","R","S"];
strat_7 = ["S","P","S","R"];
strat_8 = ["R","P","R","P"];
strat_9 = ["S","R","R","S"];	
strat_10 = ["S","P","R","S","P"];
strat_11 = ["P","P","S","S","R"];
strat_12 = ["R","P","P","S","R"];
strat_13 = ["R","P","S","S","R"];

strats = [strat_0,strat_1,strat_2,strat_3,strat_4,strat_5,strat_6,strat_7,strat_8,strat_9,strat_10,strat_11,strat_12,strat_13];

opponent_name = ARGV[0]
command = ARGV[1]
opponent_last_move = ARGV[2]

case command
     when "start"
	 # set up all needed files
	 move_list = File.new(Dir.pwd + "/movelist","w")
	 move_list.puts strats[0] + strats[0];
	 strat_number = File.new(Dir.pwd + "/strat","w")
         strat_number.puts "1";
	 win_ratio = File.new(Dir.pwd + "/winratio","w")
         win_ratio.puts "0";
	 

	 #start with scissors 
	 this_move = File.new(Dir.pwd + "/thismove", "w")
	 this_move.puts "S";
	 puts "S" 
     when "turn"
	 #lets see if we won or lost
	 last_move = File.new(Dir.pwd + "/thismove", "r")
	 last_move = last_move.gets;
	 
	 score = 0;
	 if((opponent_last_move == "R" && last_move =~ /.*S.*/)||
	     (opponent_last_move == "P" && last_move =~ /.*R.*/) ||
		(opponent_last_move == "S" && last_move =~ /.*P.*/))
		 	score = -1;
	 elsif((opponent_last_move == "R" && last_move =~ /.*P.*/)||
	       (opponent_last_move == "P" && last_move =~ /.*S.*/) ||
		(opponent_last_move == "S" && last_move =~ /.*R.*/))
			score = 1;
         end
	 #Adjust the win ratio
	 win_ratio = File.new(Dir.pwd + "/winratio", "r")
	 current_win_ratio = win_ratio.gets
         win_ratio.close
         current_win_ratio = Integer(current_win_ratio) + score;
         win_ratio = File.new(Dir.pwd + "/winratio","w")
	 win_ratio.puts current_win_ratio
	 win_ratio.close
	 
	#Get the next move from the move list. if nil we deal with it	 	     	
	 move_list = File.new(Dir.pwd + "/movelist", "r")
	 current_move = move_list.gets;
	 
	if (current_move != nil) 
	    remaining = Array.new
	    while(line = move_list.gets)
	 	       remaining << line
	    end
            move_list.close
	    new_move_list = File.new(Dir.pwd + "/movelist", "w")
	    new_move_list.puts remaining;
	    new_move_list.close
            this_move = File.new(Dir.pwd + "/thismove", "w")
	    this_move.puts current_move;
	    this_move.close
	    puts current_move
	 else
	   # decide if it was a good strategy
	   win_ratio = File.new(Dir.pwd + "/winratio", "r")
	   current_win_ratio = win_ratio.gets
           current_win_ratio = Integer(current_win_ratio) 
	   win_ratio.close;
	   adder = 0;
	              
	   # if it wasn't winning pick a new one
	   if(current_win_ratio < 1)	
		adder = 1;
                new_win_ratio = File.new(Dir.pwd + "/winratio","w")
         	new_win_ratio.puts "0";               
   	   end
	   strat_number_file = File.new(Dir.pwd + "/strat", "r")
	   current_strat = strat_number_file.gets
	   strat_number_file.close
	   current_strat =Integer(current_strat) + adder;
	   new_strat_number_file = File.new(Dir.pwd + "/strat","w")
	   new_strat_number_file.puts current_strat
	   new_strat_number_file.close
	   move_list = File.new(Dir.pwd + "/movelist", "w")
	   move_list.puts strats[(current_strat-1) % strats.length] + strats[(current_strat-1) % strats.length]
	   move_list.close
	
	   # get the first one off the list
	   move_list = File.new(Dir.pwd + "/movelist", "r")
	   current_move = move_list.gets;
	   remaining = Array.new
	   while(line = move_list.gets)
	 	       remaining << line
	   end
	   move_list.close
	   new_move_list = File.new(Dir.pwd + "/movelist", "w")
	   new_move_list.puts remaining;
	   new_move_list.close
           this_move = File.new(Dir.pwd + "/thismove", "w")
	   this_move.puts current_move;
	   this_move.close
	   puts current_move
	end
end

