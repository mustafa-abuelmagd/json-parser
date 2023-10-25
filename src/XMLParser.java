import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;


public class XMLParser {
    final String fileString;
    private String[] tokens;

    XMLParser(String fileString) {
        this.fileString = fileString;
        this.tokens = this.getTokens().toArray(new String[0]);
//        getTags();
        this.loadDocument();
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
//
//        for (int i = 0; i < tokens.size(); i++) {
//            System.out.println("I  " + i + ":  " + tokens.get(i));
//        }

        return tokens;
    }

//    get the tags
    //    tags begin with "<" and end with either ">", "/>"
    //    open tags being with "</"
    //    self-closing tags end with "/>"

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
            return str.replace("<","")
                    .replace(">","")
                    .replace("/","");
        } else {
            return null;
        }

    }


    public void loadDocument() {
        Stack<String> stack = new Stack<>();
        Stack<XMLNode> XMLNodeStack = new Stack<>();
        Map<String, Object> dynamicMap = new HashMap<>();
        ArrayList<XMLNode> nodeList = new ArrayList<>();

        // using the stack to form the XML document by creating the nodes based
        // on the first element in the stack
        // 1- if it's  a self-closing tag; it's easy, create a node with that, self-closing tags do not have children
        // 2- if it's an opening tag; push the opening tag to the stack and create a node with that name
        // the value of the current opening tag will be the next non tag token
        // if it's a closing tag; pop the first element from the stack
        // if there's a current opening tag and the nxt token is another opening tag; then it's a child


        for (int i = 0; i < this.tokens.length; i++) {
            System.out.println("ITR::  "+ this.tokens[i]);
            // if the current token is an opening tag or a self-closing tag, we create a node and push it
            if (this.isOpeningTag(this.tokens[i]) || this.isSelfClosingTag(this.tokens[i])) {
                System.out.println("opening or self opening::  "+ this.tokens[i]);

                XMLNode node = new XMLNode(this.getTagName(this.tokens[i]));
                if (!stack.isEmpty() &&this.isOpeningTag(stack.peek())) {
                    System.out.println("setting children::  "+XMLNodeStack.peek().getName() );

                    XMLNodeStack.peek().children.add(node);
                    node.setParent(XMLNodeStack.peek());


                    //TODO we need a way to implemnt the array elements and
                    // add them. may be if the previouse element in the
                    // stack has the same name then it's an array;
                    // but elements are poped. think or a way

                }
                //only if a tag is an opening tag that we push it to either of the stacks
                if (this.isOpeningTag(this.tokens[i])){
                    XMLNodeStack.push(node);
                    stack.push(this.tokens[i]);
                }

            }
            // if the current token is not a tag; then it's the value of the current tag and node
            else if (!this.isTag(this.tokens[i])) {
                System.out.println("setting tag text::  "+XMLNodeStack.peek().getName());

                XMLNodeStack.peek().setText(this.tokens[i]);
            }
            // and if the current token is a closing tage; we pop from the two stacks
            else if (this.isClosingTag(this.tokens[i])) {
                System.out.println("putting in dectionary::  "+XMLNodeStack.peek().getName());

                dynamicMap.put(XMLNodeStack.peek().getName(),XMLNodeStack.peek().getText() );
                if(Objects.equals(XMLNodeStack.peek().getName(), this.getTagName(this.tokens[i]) )){
                    System.out.println("poping::  "+XMLNodeStack.peek().getName());

                    stack.pop();
                    XMLNodeStack.pop();
                }
            }
        }
        for (Map.Entry<String, Object> entry : dynamicMap.entrySet()) {
            String key = entry.getKey();
            System.out.print(key + "::: ");
//            for (String value : values) {
            System.out.print(entry.getValue() + ", ");
//            }
            System.out.println(); // Move to the next line for the next key
        }


    }

//    get the nodes

//    get the node children

//    get node attributes

//    get node text
}


