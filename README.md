# IS-TFIDF

This program is calculating the TF-IDF score according to the basic method shown here: https://en.wikipedia.org/wiki/Tf%E2%80%93idf.

In order to build this project, use maven with the pom.xml provided with the project.

The seperation of words is the sequences of word-chracters, hence for example, the word U.S. is treated as U & S.

Running the program with no prameters will use a test file included in the project (under test directory), using 10 threads for fetching the data (descriptions of Itunes app store http://itunes.apple.com/lookup?id=ID ) and printing the top 10 words with their TF-IDF score.

The optional parameters are:
args[0] - local file path contating of app ids
args[1] - amount of executors to use (values hieger than 10 will be ignored, and the program will use 10 executors)
args[2] - amount of top resutls to retrieve
