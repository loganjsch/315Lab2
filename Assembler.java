import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

public class Assembler {
    
    public static void main(String[] args){

        // Initialize
        File infile = new File("test1.asm");
        StringBuilder output = new StringBuilder();
        File outfile = new File("output.txt");
        HashMap<String, Integer> labelMap = new HashMap<>();
        int lineCount = 0;

        // first pass to find labels, save name and line number into table 
        try {
            Scanner scanner = new Scanner(infile);
            while (scanner.hasNextLine()) {

                String line = scanner.nextLine().trim(); // removes whitespace

                if (line.startsWith("#")){
                    continue;
                }

                int colonIndex = line.indexOf(":"); // looks for colon

                // extracts the data from before the colon and then puts it into hashmap with lineCount and label
                if (colonIndex >= 0) {
                    String label = line.substring(0, colonIndex);
                    labelMap.put(label, lineCount);
                    lineCount++;
                } else {
                    int commentIndex = line.indexOf("#");
                    if (commentIndex >= 0) {
                        line = line.split("#")[0].trim(); // remove any text after the comment
                    }
                    if (!line.isEmpty() || line.startsWith("#")) { // check if line is not empty
                        lineCount++;
                    }
                }
            }
            scanner.close();    
            // Output the label table
            printLabelTable(labelMap);
        }
        catch (FileNotFoundException e) {
            System.out.println("Error.");
            e.printStackTrace();
        }

        // second pass 
        try {
            Scanner scanner = new Scanner(infile); 
            PrintWriter writer = new PrintWriter(outfile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("#")){
                    continue;
                }

                boolean inlinecomment = false;
                boolean leadingwhitespace = true;

                int colonIndex = line.indexOf(":");
                if (colonIndex >= 0) {
                    // Skip the label in the code
                    line = line.substring(colonIndex + 1).trim();
                }
                // If the line is a comment, dont include
                for (int i = 0; i < line.length(); i++){
                    char c = line.charAt(i);
                    if (c == '#') {
                        inlinecomment = true;
                    }
                    if (Character.isWhitespace(c)) {
                        if (leadingwhitespace){
                            // ignore whitespace
                        }
                        else {
                            writer.print(' ');
                            output.append(' ');
                            leadingwhitespace = true;
                        }
                    } else if (c == ',' || c == '(' || c == ')') {
                        output.append(' ');
                        writer.print(' ');
                    } else if (c == '$') {
                            output.append(' ');
                            output.append(c);
                            writer.print(' ');
                            writer.print(c);
                            leadingwhitespace = false;
                    } else if (!inlinecomment) {
                        output.append(c);
                        writer.print(c);
                        leadingwhitespace = false;
                    }
                }
                // Newline
                writer.println();
                output.append('\n');
            }
            scanner.close();
            writer.close();

        }
        catch (FileNotFoundException e) {
            System.out.println("Error.");
            e.printStackTrace();
        }


        // building and prepping the output array
        String[] outputArray = output.toString().split("\\r?\\n");
        outputArray = Arrays.stream(outputArray).filter(s -> !s.trim().isEmpty()).toArray(String[]::new);

        // print label table 
        printLabelTable(labelMap);
        // print the array being set to convert to binary 
        printArray(outputArray);
        // Convert outputArray to Binary
        stringToBinary(outputArray, labelMap);


    }



    public static void printArray(String[] outputArray){
        for (String line : outputArray){
            System.out.println(line);
        }
    }

    public static void printLabelTable(HashMap<String, Integer> labelMap){
        System.out.println("Label Table:");
        for (Entry<String, Integer> entry : labelMap.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }

    public static void stringToBinary(String[] outputArray, HashMap<String, Integer> labelMap){
        int linecount = 0;
        for (String line : outputArray) {

            
            System.out.println(line);
            instruction instr = new instruction(line);
            System.out.print(instr.returnBinary());


            String[] words = line.split("\\s+");
            if (words[0].equals("beq") || words[0].equals("bne") ) {
                int offset = labelMap.get(words[3]) - (linecount + 1);
                String binOffset = Integer.toBinaryString(offset);
                String paddedBinOffset = String.format("%16s", binOffset).replace(' ', '0');
                paddedBinOffset = paddedBinOffset.substring(paddedBinOffset.length() - 16);
                System.out.print(paddedBinOffset);
            }
            if (words[0].equals("j") || words[0].equals("jal") ) {
                int offset = labelMap.get(words[1]) - (linecount + 1);
                String binOffset = Integer.toBinaryString(offset);
                String paddedBinOffset = String.format("%16s", binOffset).replace(' ', '0');
                paddedBinOffset = paddedBinOffset.substring(paddedBinOffset.length() - 16);
                System.out.print(paddedBinOffset);
            }
            if (instr.returnBinary().split("\\s+")[0].equals("invalid")) {
                System.exit(0);
            }
            linecount ++;
            System.out.println();
        }

    }

}