
import java.util.ArrayList;


public class Variable {

    boolean isVariable;
    int value;
    IFunction function;
    int variableId;
    int modifier;

    public Variable(){}
    public Variable(int value, IFunction function){
        this.value = value;
        this.function = function;
    }
    public void printVariable() {
        
        if (isVariable) {
            System.out.print((char) (variableId + 97));
        }
    }
    
    public int getValue() {
        return value;
    }
    
    public boolean contains(Variable var) {
        return false;
    }
    
    public String getOp() {
        return "" + modifier;
    }
    
    public ArrayList getArgs() {
        return null;
    }
}
