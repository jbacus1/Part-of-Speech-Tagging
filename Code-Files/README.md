## Files

### POS Tagging files
- ConsoleViterbi.java: Provides a console-based interface for the POS tagger. It allows users to input sentences, trains the Viterbi model on provided files, and outputs the predicted tags for the input sentence.
 
- FileViterbi.java: Evaluates the POS tagger by comparing predicted tags to given tags from test files. It processes sentence and tag files, calculates accuracy, and outputs the total number of correct predictions.
 
- Viterbi.java: Implements the Viterbi algorithm for part-of-speech tagging using dynamic programming. It builds transition and emission probability matrices from training data, handles unseen words with a penalty, and uses backpointers to reconstruct the optimal sequence of tags.

 ### Sentence and Tag files
 - Simple-train: two files one with sentences, one with tags to train the model.
  
 - Simple-test: two files that have not been trained on that can be used to test the model.
  
 - Brown-train: a robust set of sentences and tags for training.
  
 - Brown-test: a large set of test sentences and tags.
