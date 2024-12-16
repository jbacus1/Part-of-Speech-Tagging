import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConsoleViterbi {

    /**
     * Takes a sentence as input and prints its POS tags
     * @throws Exception if something fails with file reading
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Enter a sentence with each word and punctuation separated by spaces: ");
        System.out.println(" ");
        Viterbi myViterbi = new Viterbi();
        Scanner myScanner = new Scanner(System.in);
        String tag = "brown-train-tags.txt";
        String word = "brown-train-sentences.txt";
        myViterbi.train(tag, word);


        String input = myScanner.nextLine();
        input = input.toLowerCase();
        String[] inputArray = input.split(" ");
        List<String> posInput = new ArrayList<>(Arrays.asList(inputArray));


        System.out.println(myViterbi.decodePOS(posInput));
    }
}
