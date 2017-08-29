import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.ParserRuleContext;

public class FunctionPointer {
  public final String id;
  private List<Function> functions = new ArrayList<Function>();

  public FunctionPointer(String id){
    this.id = id;
  }

  public FunctionPointer(String id, List<Function> functions){
    this(id);
    this.functions = functions;
  }

  public String getID(){
    return this.id;
  }

  public void addFunctionReferece(Function f){
    //System.out.println(f.getParamIDs());
    if(!checkExists(f)){
      this.functions.add(f);
    }else{
      throw new RuntimeException("Function with those parameters already exists with the name \'" + id + "\'."); //TODO: Make this error more comprehensive
    }
  }

  public TileValue invoke(List<TileParser.ExpressionContext> params, Map<String, Struct> structs, Scope scope){
    EvalVisitor evalVisitor = new EvalVisitor(scope, /*functions,*/ structs, false);
    List<Type> inputParams = new ArrayList<Type>(params.size());
    ArrayList<TileValue> paramInputs = new ArrayList<TileValue>();
    for (int i = 0; i < params.size(); i++) {
        TileValue value = evalVisitor.visit(params.get(i));
        inputParams.add(value.getType());
        //System.out.println(value);
        paramInputs.add(value);
    }
    for (Function f: functions) {
      if(f.checkValid(inputParams)){
        scope = new Scope(scope); // create function scope
    		scope.checkScope();

    		//Generate a visitor that has local scope
        TileValue t = f.invoke(paramInputs, inputParams, structs, scope);;
        return t;
      }
    }
    throw new RuntimeException("Illegal Function call: Function with those params not found"); //TODO: Make this error more comprehensive
  }

  public boolean checkExists(Function func){
    for (Function f: functions) {
      if(f.checkValid(func.getParamIDs())){
        return true;
      }
    }
    return false;
  }

  public String toString(){
    String params = "";
    for(Function f : functions){
      params = params + f.getParamIDs() + ", ";
    }
    params = params.substring(0,params.length() - 2);
    return params;
  }
}
