import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;


public class XMLParser {
    final String fileString;
    private String[] tokens;
    Map<String, Object> mapRepresentation;

    XMLParser(String fileString) {
        this.fileString = fileString;

        this.tokens = this.getTokens().toArray(new String[0]);
        System.out.println("\n----------------------------------------------------- \n");
        System.out.println("map representation of the XML file: \n");
        this.loadDocument();
        System.out.println("\n----------------------------------------------------- \n");

    }


    //    get the read file: String
    public ArrayList<String> getTokens() {
        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder strVal = new StringBuilder();

        for (int i = 0; i < fileString.length(); i++) {
            // if the character is the opening angle brackets for a tag;
            // stop the stringBuilder and add a token to the arraylist
            if (fileString.charAt(i) == '<') {
                // add the constructed string until now and create a new string builder
                tokens.add(strVal.toString());
                strVal = new StringBuilder();

                int tempI = i;
                while (fileString.charAt(tempI) != '>') {
                    strVal.append(fileString.charAt(tempI));
                    tempI++;
                }
                // add the last character of a tag; the ">"
                strVal.append(fileString.charAt(tempI));
                // add the new token; a tag
                tokens.add(strVal.toString());
                strVal = new StringBuilder();
                i = tempI;
            } else {
                strVal.append(fileString.charAt(i));
            }
        }


        tokens = (ArrayList<String>) tokens.stream()
                .filter(str -> !str.isEmpty())
                .collect(Collectors.toList());

        return tokens;
    }

    public ArrayList<String> getTags() {
        ArrayList<String> tags = new ArrayList<>();
        for (int i = 0; i < this.tokens.length; i++) {
            System.out.println("is tag:::   " + this.isTag(this.tokens[i]));

        }

        return tags;
    }

    public boolean isTag(String str) {
        return str.charAt(0) == '<' && str.charAt(str.length() - 1) == '>';
    }

    public boolean isSelfClosingTag(String str) {
        return this.isTag(str) && str.charAt(str.length() - 2) == '/';
    }

    public boolean isClosingTag(String str) {
        return this.isTag(str) && str.charAt(1) == '/';
    }

    public boolean isOpeningTag(String str) {
        return this.isTag(str) && !this.isSelfClosingTag(str) && !this.isClosingTag(str);
    }

    public String getTagName(String str) {
        if (this.isTag(str)) {
            return str.replace("<", "")
                    .replace(">", "")
                    .replace("/", "");
        } else {
            return null;
        }

    }


    public void loadDocument() {
        XMLNode root = new XMLNode(this.getTagName(this.tokens[0]));
        XMLDocumentTree XMLDocument = new XMLDocumentTree(root);
        Stack<XMLNode> XMLNodeStack = new Stack<>();
        XMLNodeStack.push(root);
        Map<String, Object> dynamicMap = new HashMap<>();


        for (int i = 1; i < this.tokens.length; i++) {
            // if the current token is an opening tag or a self-closing tag, we create a node and push it
            if (this.isOpeningTag(this.tokens[i]) || this.isSelfClosingTag(this.tokens[i])) {

                XMLNode node = new XMLNode(this.getTagName(this.tokens[i]));

                //only if a tag is an opening tag that we push it to either of the stacks
                if (this.isOpeningTag(this.tokens[i])) {
                    XMLNodeStack.push(node);
                }

            }
            // if the current token is not a tag; then it's the value of the current tag and node
            else if (!this.isTag(this.tokens[i])) {
                XMLNodeStack.peek().setText(this.tokens[i]);
            }

            // and if the current token is a closing tage; we pop from the two stacks
            else if (this.isClosingTag(this.tokens[i])) {
                dynamicMap.put(XMLNodeStack.peek().getName(), XMLNodeStack.peek().getText());
                XMLNode lastNode = XMLNodeStack.pop();
                if (!XMLNodeStack.isEmpty()) {
                    XMLNodeStack.peek().appendChild(lastNode);
                }
            }
        }
        printTree(root);

    }

    public Map<String, Object> getDocument(XMLNode root) {
        Map<String, Object> dynamicMap = new HashMap<>();
        dynamicMap.put(root.getName(), root.getText());

        if (!root.getChildren().isEmpty()) {
            for (XMLNode child : root.getChildren()) {
                dynamicMap.put(child.getName(), getDocument(child));
            }
        }
        return dynamicMap;
    }

    public static void printTree(XMLNode root) {
        printNode(root, "", true);
    }

    private static void printNode(XMLNode node, String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + node.getName()+": "+node.getText());

        ArrayList<XMLNode> children = node.getChildren();
        for (int i = 0; i < children.size() - 1; i++) {
            printNode(children.get(i), prefix + (isTail ? "    " : "│   "), false);
        }

        if (!children.isEmpty()) {
            printNode(children.getLast(), prefix + (isTail ? "    " : "│   "), true);
        }
    }

}


