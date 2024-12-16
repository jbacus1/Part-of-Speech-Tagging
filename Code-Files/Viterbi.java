import java.io.*;
import java.util.*;

public class Viterbi {

    //penalty for a word not being observed in a state before
    private int unseen = -100;

    //how often one state goes to another state
    private HashMap<String, HashMap<String, Double>> transitionValues = null;

    //how often a word is observed in a state
    private HashMap<String, HashMap<String, Double>> observationValues = null;

    /**
     * Gets transitionValues map
     * @return map of transition values
     */
    public HashMap<String, HashMap<String, Double>> getTransitionValues (){
        return transitionValues;
    }

    /**
     * Gets observationValues map
     * @return map of observation values
     */
    public HashMap<String, HashMap<String, Double>> getObservationValues (){
        return observationValues;
    }

    /**
     * uses transitionValues and observationValues to determine what POS words in a list of words are
     * @param input string of words being decoded
     * @return list of POS corresponding to input
     * @throws Exception throws if there is no training
     */
    public List<String> decodePOS(List<String> input) throws Exception {
        if (observationValues == null || transitionValues == null) {
            throw new Exception("No training data");
        }
        List<String> res =  new ArrayList<>();
        Map<Integer, Map<String, String>> backPointer = null;
        Map<String, Double> nextTraversal = null;

        for (int i = 0; i < input.size(); i++) {

            String currWord = input.get(i);

            //If this is the first word
            if (i == 0) {
                backPointer = new HashMap<>();
                backPointer.put(i, new HashMap<>());
                nextTraversal = new HashMap<>();

                for (String transitionNext : transitionValues.get("#").keySet()) {
                    Double currScore = 0.0;
                    if (!observationValues.containsKey(transitionNext)) {
                        if (!observationValues.get(transitionNext).containsKey(currWord)) {
                            currScore += unseen;
                        } else {
                            currScore += observationValues.get(transitionNext).get(currWord);
                        }
                    } else {
                        currScore += unseen;
                    }

                    //calculates score, saves score for the next traversal
                    //stores backpointer for all combinations based on index
                    currScore += transitionValues.get("#").get(transitionNext);
                    backPointer.get(i).put(transitionNext, "#");
                    nextTraversal.put(transitionNext, currScore);
                }
            } else {
                Map<String, Double> currTraversal = nextTraversal;
                nextTraversal = new HashMap<>();
                backPointer.put(i, new HashMap<>());

                for (String currTag : currTraversal.keySet()) {
                    if (Objects.equals(currTag, ".")) {
                        continue;
                    }

                    //checks combinations of currTag and nextTag
                    for (String nextTag : transitionValues.get(currTag).keySet()) {
                        Double currScore = 0.0;
                        currScore += currTraversal.get(currTag);
                        currScore += transitionValues.get(currTag).get(nextTag);
                        if (!observationValues.containsKey(nextTag)) {
                            currScore += unseen;
                        } else if (!observationValues.get(nextTag).containsKey(currWord)) {
                            currScore += unseen;
                        } else {
                            currScore += observationValues.get(nextTag).get(currWord);
                        }

                        //only includes highest scoring combination
                        if (!nextTraversal.containsKey(nextTag)) {
                            nextTraversal.put(nextTag, currScore);
                            backPointer.get(i).put(nextTag, currTag);

                        } else if (nextTraversal.get(nextTag) < currScore) {
                            nextTraversal.replace(nextTag, currScore);
                            backPointer.get(i).put(nextTag, currTag);
                        }
                    }

                }

            }
        }

        //finds the best scoring tag at the end on the traversal
        String bestTag = null;
        for (String tag : nextTraversal.keySet()) {
            if (bestTag == null) {
                bestTag = tag;
            } else if (nextTraversal.get(bestTag) < nextTraversal.get(tag)) {
                bestTag = tag;
            }
        }

        //traverses through backpointer and returns result
        for (int i = input.size() - 1; i > -1; i--) {
            res.add(0, bestTag);
            bestTag = backPointer.get(i).get(bestTag);
        }

        return res;
    }

    /**
     * Generates maps to represent a graph of how POS relate to each other
     * @param tagFilePath the file path to training tags
     * @param sentenceFilePath the file path to training sentences
     * @throws Exception throws if there is an error with file reading
     */
    public void train(String tagFilePath, String sentenceFilePath) throws Exception {
        HashMap<String, HashMap<String, Double>> observations = new HashMap<>();
        HashMap<String, HashMap<String, Double>> transitions = new HashMap<>();
        BufferedReader tagInput = null;
        BufferedReader sentenceInput = null;
        try {
            tagInput = new BufferedReader(new FileReader(tagFilePath));
            sentenceInput = new BufferedReader(new FileReader(sentenceFilePath));

            String tags;
            String sentences;

            while((tags = tagInput.readLine()) != null && (sentences = sentenceInput.readLine()) != null) {
                String[] tagsList = tags.split(" ");
                String[] wordsList = sentences.split(" ");

                //Counts how many times a word occurs as a particular POS
                for (int i = 0; i < tagsList.length; i++) {
                    if (!observations.containsKey(tagsList[i])) {
                        observations.put(tagsList[i], new HashMap<>());
                        observations.get(tagsList[i]).put(wordsList[i], 1.0);
                    } else {
                        HashMap<String, Double> currentTag = observations.get(tagsList[i]);
                        if (!currentTag.containsKey(wordsList[i])) {
                            currentTag.put(wordsList[i], 1.0);
                        } else {
                            currentTag.replace(wordsList[i], currentTag.get(wordsList[i]) + 1);
                        }
                    }
                }

                //Counts how many times one POS transitions to another
                for (int i = 0; i < tagsList.length; i++) {
                    if (i == 0) {
                        if (!transitions.containsKey("#")) {
                            transitions.put("#", new HashMap<>());
                            transitions.get("#").put(tagsList[i], 1.0);
                        } else {
                            if (!transitions.get("#").containsKey(tagsList[i])) {
                                transitions.get("#").put(tagsList[i], 1.0);
                            } else {
                                transitions.get("#").replace(tagsList[i], transitions.get("#").get(tagsList[i]) + 1);
                            }
                        }
                    } else {
                        if(!transitions.containsKey(tagsList[i-1])) {
                            transitions.put(tagsList[i-1], new HashMap<>());
                            transitions.get(tagsList[i-1]).put(tagsList[i], 1.0);
                        } else {
                            if (!transitions.get(tagsList[i-1]).containsKey(tagsList[i])) {
                                transitions.get(tagsList[i-1]).put(tagsList[i], 1.0);
                            } else {
                                transitions.get(tagsList[i-1]).replace(tagsList[i], transitions.get(tagsList[i-1]).get(tagsList[i]) + 1);
                            }
                        }
                    }
                }
            }
            //converts values into logs
            for (String tag : observations.keySet()) {
                double currTotal = 0.0;
                for (String word : observations.get(tag).keySet()) {
                    currTotal = currTotal + observations.get(tag).get(word);
                }
                for (String word : observations.get(tag).keySet()) {
                    observations.get(tag).replace(word, Math.log(observations.get(tag).get(word)/currTotal));
                }
            }
            //converts values into logs
            for (String tag : transitions.keySet()) {
                double currTotal = 0.0;
                for (String next : transitions.get(tag).keySet()) {
                    currTotal = currTotal + transitions.get(tag).get(next);
                }
                for (String next : transitions.get(tag).keySet()) {
                    transitions.get(tag).replace(next, Math.log(transitions.get(tag).get(next)/currTotal));
                }
            }

        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            if (tagInput != null) {
                tagInput.close();
            }
            if (sentenceInput != null) {
                sentenceInput.close();
            }
        }
        transitionValues = transitions;
        observationValues = observations;
    }

}
