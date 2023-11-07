import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String csvFileName = "test.CSV";
        String jsonFileName = "test.json";
        String yamlFileName = "test.yaml";
        String xmlFileName = "test.xml";

        String csvString = readFileCSV(csvFileName);
        String jsonString = readFile(jsonFileName);
        String yamlString = readFileYaml(yamlFileName);
        String xmlString = readFileXML(xmlFileName);


        System.out.println("JSONString  " + jsonString + "\n\n\n\n\nCSVString::::::\n" + csvString + "\n\n\n\n\nXMLString:::::::\n" + xmlString + "\n\n\n\n\nYAMLString::::\n" + yamlString);

        //parsing logic for csv
        CSVParser csvParser = new CSVParser(csvString);

//parsing logic for xml
        XMLParser xmlParse = new XMLParser(xmlString);

//        parsing logic for json
        ArrayList<String> jsonTokens = tokenize(jsonString);
        composeMap(jsonTokens);

//        parsing logic for yaml
        Map<String, Object> yamlMap = parseYaml(yamlString);

    }


    // all the file readers are practically the same.
    public static @NotNull String readFile(String fileName) throws FileNotFoundException {
        try {
            // construct the file path
            String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + fileName;
            File testJson = new File(filePath);
            StringBuilder jsonString = new StringBuilder();
            Scanner fileScanner = new Scanner(testJson);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                jsonString.append(line);
            }
            String jsonStringString = jsonString.toString();
            jsonStringString = jsonStringString.replaceAll("\\s", "");
            return jsonStringString;

        } catch (FileNotFoundException excecption) {
            throw excecption;
        }
    }

    public static @NotNull String readFileYaml(String fileName) throws FileNotFoundException {
        try {
            String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + fileName;
            File testJson = new File(filePath);
            StringBuilder jsonString = new StringBuilder();
            Scanner fileScanner = new Scanner(testJson);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();

                jsonString.append(line);
                jsonString.append("\n");

            }
            String jsonStringString = jsonString.toString();
//            jsonStringString = jsonStringString.replaceAll("\\s", "");
            return jsonStringString;

        } catch (FileNotFoundException excecption) {
            throw excecption;
        }
    }

    public static @NotNull String readFileXML(String fileName) throws FileNotFoundException {
        try {
            String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + fileName;
            File testJson = new File(filePath);
            StringBuilder jsonString = new StringBuilder();
            Scanner fileScanner = new Scanner(testJson);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();

                jsonString.append(line);
                jsonString.append("\n");

            }
            String jsonStringString = jsonString.toString();
            jsonStringString = jsonStringString.replaceAll("\\s", "");
            return jsonStringString;

        } catch (FileNotFoundException excecption) {
            throw excecption;
        }
    }


    public static @NotNull String readFileCSV(String fileName) throws FileNotFoundException {
        String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + fileName;
        File testJson = new File(filePath);
        StringBuilder jsonString = new StringBuilder();
        Scanner fileScanner = new Scanner(testJson);
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();

            jsonString.append(line);
            jsonString.append("\n");

        }
        String jsonStringString = jsonString.toString();
        return jsonStringString;

    }


    public static ArrayList<String> tokenize(String jsonString) {
        //get word tokens
        String[] wordTokens = getWordTokens(jsonString);
        //get special character tokens
        String[] characterTokens = getCharacterTokens(jsonString);


        //define the arraylist for the final tokens and iterate over the arrays to sort properly
        ArrayList<String> tokens = new ArrayList<>();
        int wordTokensItr = 0;
        int characterTokensItr = 0;
        int totalArrayLength = wordTokens.length + characterTokens.length;
        try {
            for (int i = 0; i < totalArrayLength; i++) {
                tokens.add(characterTokens[characterTokensItr]);
                characterTokensItr++;

                tokens.add(wordTokens[wordTokensItr]);
                wordTokensItr++;

            }
        } catch (ArrayIndexOutOfBoundsException exception) {
//            System.out.println("exception " + exception);
        }
        ArrayList<String> tokensSplitSpecialCharacters = splitSpecialCharacters(tokens);
        ArrayList<String> tokensRemoveQotations = removeQotations(tokensSplitSpecialCharacters);


        return tokensRemoveQotations;

    }

    public static String[] getWordTokens(@NotNull String jsonString) {
        //        regular expression for splitting text
        String regex = "[{}\\[\\],:'\"]";
        String[] wordTokens = jsonString.split(regex);
        // Filter out empty tokens (due to regex splitting)
        wordTokens = Arrays.stream(wordTokens)
                .filter(token -> !token.isEmpty())
                .toArray(String[]::new);
        return wordTokens;
    }

    public static String[] getCharacterTokens(@NotNull String jsonString) {
        //        regular expression for splitting text
        String pattern = "\\w+";

        return jsonString.split(pattern);
    }

    public static ArrayList<String> splitSpecialCharacters(ArrayList<String> tokens) {
        ArrayList<String> characterSplits = new ArrayList<>();
        String pattern = "\\w+";
        for (int i = 0; i < tokens.size(); i++) {
            if (!tokens.get(i).matches(pattern) && tokens.get(i).split("").length > 1) {
                String[] multipleCharToken = tokens.get(i).split("");
                characterSplits.addAll(Arrays.asList(multipleCharToken));
            } else {
                characterSplits.add(tokens.get(i));
            }
        }
        return characterSplits;

    }

    public static ArrayList<String> removeQotations(ArrayList<String> tokens) {
        ArrayList<String> characterSplits = new ArrayList<>();
        String pattern = "\"";
        String pattern2 = ",";
        String pattern3 = ":";

        for (int i = 0; i < tokens.size(); i++) {
            if (!tokens.get(i).matches(pattern) && !tokens.get(i).matches(pattern2) && !tokens.get(i).matches(pattern3)) {
                characterSplits.add(tokens.get(i));
            }
        }
        return characterSplits;
    }

    public static Map<String, Object> composeMap(ArrayList<String> data) {
        System.out.println("\n----------------------------------------------------- \n");
        System.out.println("map representation of the JSON file: \n");
        Map<String, Object> dynamicMap = new HashMap<>();

        for (int i = 1; i < data.size() - 1; i++) {
//            System.out.println("i   " + data.get(i));
            if (Objects.equals(data.get(i + 1), "[")) {

                int tempI = i;
                int arrItr = i + 2;
                ArrayList<String> values = new ArrayList<>();

                while (!Objects.equals(data.get(arrItr), "]")) {
                    values.add(data.get(arrItr));
                    arrItr++;
                }
                dynamicMap.put(data.get(tempI), values);
                i = arrItr + 1;

            } else {
                System.out.println("          " + data.get(i) + "  " + data.get(i + 1));
                dynamicMap.put(data.get(i), data.get(i + 1));
                i++;
            }

        }
        System.out.println("-------------------------------------------------------   ");
        return dynamicMap;

    }

    //create me a new class to parse yaml files
    public static Map<String, Object> parseYaml(String yamlString) {  //yamlString is the string that we get from the file        //create a new map to store the key value pairs
        Map<String, Object> dynamicMap = new HashMap<>();
        //split the string into lines
        String[] lines = yamlString.split("\n");
        //iterate over the lines
        try {
            for (int i = 0; i < lines.length; i++) {
                //split the line into key and value
                String[] keyValue = lines[i].split(":");
                //add the key and value to the map
                if (keyValue.length == 2) {
                    dynamicMap.put(keyValue[0], keyValue[1]);
                }
//            modify the above code to account for arrays
                if (keyValue.length < 2) {
                    int tempI = i + 1;

                    ArrayList<String> values = new ArrayList<>();

                    while (tempI < lines.length && Objects.equals(lines[tempI].replaceAll("\\s", "").charAt(0), '-')) {
                        values.add(lines[tempI].replaceAll("\\s", "").replaceAll("-", ""));
                        tempI++;
                    }
                    dynamicMap.put(lines[i], values);

                    i = tempI + 1;
                }

            }
        } catch (ArrayIndexOutOfBoundsException exception) {
        }
        System.out.println("\n----------------------------------------------------- \n");
        System.out.println("map representation of the yaml file: \n");

        for (Map.Entry<String, Object> entry : dynamicMap.entrySet()) {
            String key = entry.getKey();
            System.out.print(key + "::: ");
//            for (String value : values) {
            System.out.print(entry.getValue() + ", ");
//            }
            System.out.println(); // Move to the next line for the next key
        }
        System.out.println("\n----------------------------------------------------- \n");

        return dynamicMap;
    }


}

