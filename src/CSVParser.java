import java.util.*;

public class CSVParser {
    final String fileString;
    private String[] colNames;
    private String[] entries;

    CSVParser(String fileString) {
        this.fileString = fileString;
        this.getTokens();
        this.getMappedEntries();
    }

    public void getTokens() {
        String[] lines = this.fileString.split("\n");
//      getting the column names; which are the first line of the csv file

        this.colNames = lines[0].split(",");
//      getting the entries; which are the rest of the lines of the csv file
        this.entries = Arrays.copyOfRange(lines, 1, lines.length);
    }

    public ArrayList<Map<String, Object>> getMappedEntries() {
        ArrayList<Map<String, Object>> entries = new ArrayList<>();
        Map<String, Object> entry = new HashMap<>();
        for (int i = 0; i < this.entries.length; i++) {
            String[] values = this.entries[i].split(",");

            for (int j = 0; j < values.length; j++) {
                entry.put(this.colNames[j], values[j]);
            }
            entries.add(entry);
            entry= new HashMap<>();

        }
        System.out.println("\n----------------------------------------------------- \n");

        System.out.println("map representation of the csv file: \n");
        System.out.print("[\n");
        for (Map<String, Object> tempEntry : entries) {
            System.out.print("{\n");
            for (Map.Entry<String, Object> InnerEntry : tempEntry.entrySet()) {
                String key = InnerEntry.getKey();
                System.out.print(key + ": ");
                System.out.print(InnerEntry.getValue() + ", ");
                System.out.println(); // Move to the next line for the next key
            }
            System.out.print("\n},");
        }
        System.out.print("\n]");
        System.out.println("\n----------------------------------------------------- \n");

        return entries;
    }


}
