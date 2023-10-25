import java.util.ArrayList;

public class XMLNode {
    String text;
    String name;
    XMLNode parent;
    boolean hasChildren;
    ArrayList<XMLNode> children;
    //create me the constructor and the getters and setters
    public XMLNode(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }
//    create me the getters and setters

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public void setParent(XMLNode parent) {
        this.parent = parent;
    }
    public XMLNode getParent() {
        return parent;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setChildren(ArrayList<XMLNode> children) {
        this.children = children;
    }

    public void appendChild(XMLNode child){
        this.children.add(child);
    }
    public ArrayList<XMLNode> getChildren() {
        return children;
    }









}
