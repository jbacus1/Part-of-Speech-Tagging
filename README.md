# Part-of-Speech (POS) Tagger

## Overview

This project is a Part-of-Speech (POS) tagger that assigns grammatical tags (e.g., nouns, verbs, adjectives) to words in a sentence using a Hidden Markov Model (HMM). The tagger employs the Viterbi algorithm to efficiently determine the most probable sequence of tags. 

---

## Key Features

### Training
- **Transition Probabilities:** Learn how often one part-of-speech tag transitions to another.
- **Emission Probabilities:** Learn how likely a word is to appear for a specific tag.
- **Logarithmic Scoring:** Uses log-space for numerical stability when handling probabilities.
- **Unseen Word Penalty:** Assigns a default penalty for words not encountered in the training data.

### Tagging
- **Dynamic Programming:** Uses the Viterbi algorithm to compute the most probable tag sequence for a given sentence.
- **Backpointer System:** Tracks the best previous tag for each word to reconstruct the optimal tag path.
- **Real-Time Predictions:** Provides both console-based and file-based interfaces for tagging.

---

## System Components

### Viterbi Class
- **train():** Reads training data to compute transition and emission probabilities.
- **decodePOS():** Implements the Viterbi algorithm to tag words in a sentence.

### ConsoleViterbi
- Allows users to input sentences directly in the console.
- Outputs predicted POS tags in real-time.

### FileViterbi
- Processes sentences from a test file.
- Compares predicted tags with ground truth labels to calculate accuracy.
- Outputs evaluation metrics, including the number of correct and total tags.

---

## How It Works

1. **Training:**
   - Input: Tagged sentences (e.g., `brown-train-tags.txt`) and corresponding words (e.g., `brown-train-sentences.txt`).
   - Output: Probabilistic models for tag transitions and word emissions.

2. **Console Tagging:**
   - Enter a sentence directly via the console.
   - View the POS tags for the sentence in real-time.

3. **File Evaluation:**
   - Input: Test sentences (e.g., `brown-test-sentences.txt`) and reference tags (e.g., `brown-test-tags.txt`).
   - Output: Accuracy metrics and detailed comparison of predicted vs. actual tags.

---

## Enjoy!

---

## Acknowledgments

Developed as part of the Dartmouth CS curriculum, Winter 2024.

---

## License

This project is distributed under the MIT License. Refer to the LICENSE file for detailed terms and conditions.

