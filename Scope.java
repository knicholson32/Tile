import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

public class Scope {

    private Scope parent;
    private Map<String, TileValue> variables;
    private Map<String, TileValue> functions;
    private ParserRuleContext currentContext;
    private static final int MAX_DEPTH = 256;

    public Scope() {
        // only for the global scope, the parent is null
        this(null);

    }


    public void checkScope(){
      int d = getDepth();
      if(d > MAX_DEPTH){
        throw new RuntimeException("Function: Too many levels of recursion: " + d + " out of " + MAX_DEPTH);
      }
    }

    public int getDepth(){
      int out = 0;
      Scope s = this.parent();
      while(s != null){
        s = s.parent();
        out++;
      }

      return out;
    }

    public void setContext(ParserRuleContext c){
      currentContext = c;
    }
    public ParserRuleContext getContext(){
      if(currentContext != null){
        return currentContext;
      }else if(this.parent() != null){
        return this.parent().getContext();
      }else{
        return null;
      }
    }

    public Scope(Scope p) {
        parent = p;
        variables = new HashMap<String, TileValue>();
        functions = new HashMap<String, TileValue>();
    }

    public void integrateFunctions(List<FunctionPointer> functs){
      for(FunctionPointer funct: functs) {
        String name = funct.getID();
        functions.put(name, new TileValue(funct));
      }
    }

    //public void assignParam(String var, TileValue value) {
    	//variables.put(var, value);
    //}

    public void assign(String var, TileValue value) {
      //System.out.println("NAME: " + var);
      if(value != null && value.isFunction()){
        assignFunction(var, value);
        return;
      }

      //value.cleanNext();
      Scope p = parent;
      parent = null;
      if(resolve(var) != null) {
          // There is already such a variable, re-assign it
          this.reAssign(var, value, false);
      }
      else {
          // A newly declared variable
          variables.put(var, value);
      }
      parent = p;
    }

    public void assignFunction(String var, TileValue value) {
        //value.cleanNext();
        if(resolve(var, true) != null) {
            // There is already such a variable, re-assign it
            this.reAssign(var, value, true);
        }
        else {
            // A newly declared variable
            functions.put(var, value);
        }
    }

    public Scope copy() {
        // Create a shallow copy of this scope. Used in case functions are
        // are recursively called. If we wouldn't create a copy in such cases,
        // changing the variables would result in changes ro the Maps from
        // other "recursive scopes".
        Scope s = new Scope();
        s.variables = new HashMap<String, TileValue>(this.variables);
        s.functions = new HashMap<String, TileValue>(this.functions);
        s.parent = this.parent;
        return s;
    }

    public boolean isGlobalScope() {
        return parent == null;
    }

    public Scope parent() {
        return parent;
    }

    private void reAssign(String identifier, TileValue value, boolean useFunctions) {
      Map<String, TileValue> map;

      if(useFunctions){
        map = functions;
      }else{
        map = variables;
      }

      if(map.containsKey(identifier)) {
          // The variable is declared in this scope
          map.put(identifier, value);
      }
      else if(parent != null) {
          // The variable was not declared in this scope, so let
          // the parent scope re-assign it
          parent.reAssign(identifier, value, useFunctions);
      }
    }

    public TileValue resolve(String var){
      return resolve(var, false);
    }

    public TileValue resolveFunction(String var){
      return resolve(var, true);
    }

    public TileValue resolve(String var, boolean useFunctions) {
      //7scopeTree(-1);
        TileValue value;
        if(useFunctions){
          value = functions.get(var);
        }else{
          value = variables.get(var);
        }
        if(value != null) {
            // The variable resides in this scope
            return value;
        }
        else if(!isGlobalScope()) {
            // Let the parent scope look for the variable
            return parent.resolve(var, useFunctions);
        }
        else {
            // Unknown variable
            return null;
        }
    }

    public void processScopeTree(int level){
      String name = "Scope";
      Debug.state(">>>", name + " : " + System.identityHashCode(this) + " l:"+level, 'o', level);
      Debug.indent();
      String ind = "";
      for(int i = 0; i < level; i++){
        ind += "  ";
      }

      for(Map.Entry<String,TileValue> var: functions.entrySet()) {
        //if(!var.getValue().isClass() && !var.getValue().isFunction()){
        String valTypeName;
        if(var.getValue() == null){
          valTypeName = "NULL";
        }else{
          valTypeName = var.getValue().getTypeName();
        }
        Debug.println(Debug.getColor('r') + ind+var.getKey()+Debug.getColor(' ') +"->"+Debug.getColor('c') +var.toString()+Debug.getColor(' ') +" : " +   Debug.getColor('l') + valTypeName + /*(var.getValue().isStatic()?" : " + Debug.getColor('r') + "Static":"") +*/ Debug.getColor(' ') + ",",level);
      }

      Debug.println(Debug.getColor('r') + "---------------------" + Debug.getColor(' '),level);

      for(Map.Entry<String,TileValue> var: variables.entrySet()) {
        String valTypeName;
        if(var.getValue() == null){
          valTypeName = "NULL";
        }else{
          valTypeName = var.getValue().getTypeName();
        }
        Debug.println(Debug.getColor('r') + ind+var.getKey()+Debug.getColor(' ') +"->"+Debug.getColor('c') +var.toString()+Debug.getColor(' ') +" : " +   Debug.getColor('l') + valTypeName + /*(var.getValue().isStatic()?" : " + Debug.getColor('r') + "Static":"") +*/ Debug.getColor(' ') + ",",level);
        //}
      }
      if(this.parent()!=null){
        Debug.indent();
        parent().scopeTree(level);
        Debug.detent();
      }
      Debug.detent();
    }

    public void scopeTree(int level){
      if(!Debug.canDebug(level))
        return;
      if(this.parent()!=null){
        try{
          parent().parent().processScopeTree(level);
        }catch(Exception e){
          parent().processScopeTree(level);
        }
      }else{
        this.processScopeTree(level);
      }
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	for(Map.Entry<String,TileValue> var: variables.entrySet()) {
    		sb.append(var.getKey()+"->"+var.getValue()+",");
    	}
    	return sb.toString();
    }
}
