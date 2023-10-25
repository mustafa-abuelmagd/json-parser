import java.util.*;

public class CSVParser {
    final String fileString;
    private String[] tokens;
    private String[] colNames;
    private String[] entries;

    CSVParser(String fileString) {
        this.fileString = fileString;
        this.tokens = this.getTokens().toArray(new String[0]);
        this.getMappedEntries();
    }

    public ArrayList<String> getTokens() {
        ArrayList<String> tokens = new ArrayList<>();
        String[] lines = this.fileString.split("\n");
        this.colNames = lines[0].split(",");

        this.entries = Arrays.copyOfRange(lines, 1, lines.length);

////        for (int i = 1; i < lines.length; i++) {
////            tokens.addAll(List.of(lines[i].split(",")));
////        }
//
//        for (int i = 0; i < entries.length; i++) {
//            System.out.println(entries[i]);
//        }


        return tokens;
    }

    public ArrayList<Map<String, Object>> getMappedEntries() {
        ArrayList<Map<String, Object>> entries = new ArrayList<>();
        Map<String, Object> entry = new HashMap<>();
        for (int i = 0; i < this.entries.length; i++) {
            String[] values = this.entries[i].split(",");

//            Exception IllegalArgumentException = new Exception("lengths are not the same");
//            if (values.length != this.colNames.length) throw IllegalArgumentException;

            for (int j = 0; j < values.length; j++) {
                entry.put(this.colNames[j], values[j]);
            }
            entries.add(entry);
            entry= new HashMap<>();

        }

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


        return entries;
    }


}
