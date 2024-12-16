import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileViterbi {

    /**
     * Takes sentence file and tag file and uses Viterbi to attempt to put POS to the sentence file
     * @throws Exception throws if there is a file reading error
     */
    public static void main(String[] args) throws Exception {
        Viterbi myViterbi = new Viterbi();
        String trainTag = "brown-train-tags.txt";
        String trainWord = "brown-train-sentences.txt";
        myViterbi.train(trainTag, trainWord);

        //the file with sentences
        String readPath = "brown-test-sentences.txt";

        //the file with tags
        String resultPath = "brown-test-tags.txt";

        BufferedReader viterbiInput = null;
        BufferedReader results = null;
        int correct = 0;
        int runs = 0;
        try {
            viterbiInput = new BufferedReader(new FileReader(readPath));
            results = new BufferedReader(new FileReader(resultPath));

            String input;
            String tags;
            while (((input = viterbiInput.readLine()) != null) && ((tags = results.readLine()) != null)) {
                String[] inputArray = input.split(" ");
                String[] tagsArray = tags.split(" ");
                List<String> tagsList = new ArrayList<>(Arrays.asList(tagsArray));
                List<String> inputList = new ArrayList<>(Arrays.asList(inputArray));
                List<String> resultList = myViterbi.decodePOS(inputList);

                for (int i = 0; i < tagsList.size(); i++) {
                    if (Objects.equals(tagsList.get(i), resultList.get(i))) {
                        correct++;
                    }
                    runs++;
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            if (viterbiInput != null) {
                viterbiInput.close();
            }
            if (results != null) {
                results.close();
            }
        }

        System.out.println("Total tags checked: " + runs);
        System.out.println(" ");
        System.out.println("Total tags correct: " + correct);
    }
}
