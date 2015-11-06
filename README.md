# InsightData  
*******************************************

This package consists of an custom built JSON
parser to read and clean tweets as well as
create a dynamically updated hashtag graph.
The program is capable of reading JSON tweets
from a dynamically updated "tweets.txt" file 
from the "tweet_input" directory and outputs 
cleaned tweets to the "ft1.txt" and the rolling 
degree of the hastag graph to the "ft2.txt" 
files in the "tweet_output" directory. 
****************************
# File Details

Insight_Data.java - Main file for reading input, process the data and write the output to file.

Parser.java - Parses the JSON string to the required format

hastagGraph.java - Create the graph and update it

Edge.java - Create the edge objects of the graph


********************************
# Troubleshooting: 

(i) The output file gets appended when the program is re-run with a different input file.

Solution: Delete output files. The program waits for a dynamically updated input file and hence the program assumes the new input file to be part of a dynamically updated file






