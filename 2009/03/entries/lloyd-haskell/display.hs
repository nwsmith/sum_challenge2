import List (transpose)
import System (getArgs)

digits = [[" - ",   
           "| |",  
           "   ",
           "| |",  
           " - "],
          ["   ",
           "  |",
           "   ",
           "  |",
           "   "],
          [" - ", 
           "  |",     
           " - ",
           "|  ",
           " - "],   
          [" - ",
           "  |",
           " - ",
           "  |",
           " - "],
          ["   ",
           "| |",
           " - ", 
           "  |",
           "   "], 
          [" - ",
           "|  ",
           " - ",
           "  |",
           " - "],
          [" - ",
           "|  ",
           " - ", 
           "| |",
           " - "],
          [" - ",
           "| |",
           "   ", 
           "  |",
           "   "], 
          [" - ",
           "| |",
           " - ", 
           "| |",
           " - "], 
          [" - ",
           "| |",
           " - ", 
           "  |",
           " - "]]

scaleLine s (' ' : '-': ' ' : []) = (' ' : replicate s '-' ++ " ") : []
scaleLine s (' ' : ' ': ' ' : []) = (' ' : replicate s ' ' ++ " ") : []
scaleLine s ('|' : ' ': '|' : []) = replicate s ('|' : replicate s ' ' ++ "|")
scaleLine s ('|' : ' ': ' ' : []) = replicate s ('|' : replicate s ' ' ++ " ")
scaleLine s (' ' : ' ': '|' : []) = replicate s (' ' : replicate s ' ' ++ "|")

scale s xs = concat (map (scaleLine s) xs)

showNum val = unlines (map unwords $ List.transpose (val))

display n s = putStr $ showNum sval
    where ds = reverse $ intToDigits n
          val = map (\x -> digits !! x) ds
          sval = map (scale s) val 
          
intToDigits n | n < 10 = [n]
              | otherwise = n `rem` 10 : intToDigits (n `quot` 10) 

main = do
  args <- System.getArgs
  case args of
    (x:x1:[]) -> display n s
        where n = read x ::Int
              s = read x1 ::Int                 
    (x:[]) -> display n 2
        where n = read x ::Int


