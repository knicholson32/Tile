import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.ParserRuleContext;

public class Function {
  private List<TerminalNode> params;
  private ParseTree block;
	private List<Type> paramsID;


  public Function(List<Type> paramsID, ParseTree block, List<TerminalNode> params) {
    this.paramsID = paramsID;
    this.block = block;
		this.params = params;
  }
	//public enum Type { STRING, NUMBER, LIST, NULL, STRUCT, ANY, STRING_NUMBER, BOOL, FUNCTION, VOID};
	/*type
	 : STRING
	 | NUMBER
	 | LIST
	 | STRUCT
	 | BOOL
	 | FUNCTION
	 ;

	STRING   : 'string';
	NUMBER   : 'number';
	LIST     : 'list';
	STRUCT   : 'struct';
	BOOL     : 'bool';
	FUNCTION : 'funct';*/
	public Function(ParseTree block, List<TerminalNode> params, List<TileParser.TypeContext> types){
		ArrayList<Type> paramsID = new ArrayList<Type>(params.size());
		for(int i = 0; i < params.size(); i++){
			if(i < types.size() && types.get(i) != null && !types.get(i).getText().equals("")){
				switch(types.get(i).getText()){
					case "string":
						paramsID.add(Type.STRING);
						break;
					case "number":
						paramsID.add(Type.NUMBER);
						break;
					case "list":
						paramsID.add(Type.LIST);
						break;
					case "struct":
						paramsID.add(Type.STRUCT);
						break;
					case "bool":
						paramsID.add(Type.BOOL);
						break;
					case "funct":
						paramsID.add(Type.FUNCTION);
						break;
					default:
						throw new RuntimeException("The type \'" + types.get(i).getText() + "\" is not a valid variable type for function calls.");
				}

			}else{
				paramsID.add(Type.ANY);
			}
		}

		this.paramsID = paramsID;
    this.block = block;
		this.params = params;
	}

	public void setParamsID(List<Type> paramsID){
		if(paramsID.size() != params.size()){
			throw new RuntimeException("Params ID must be the same length as the params variable."); //TODO: Make this error more comprehensive
		}
		this.paramsID = paramsID;
	}

  public TileValue invoke(List<TileValue> inputParams, List<Type> inputParamsType, Map<String, Struct> structs, Scope scope) {
		if(!checkValid(inputParamsType)){
			throw new RuntimeException("Illegal Function call"); //TODO: Make this error more comprehensive
		}

    for (int i = 0; i < this.params.size(); i++) {
			if(inputParams.get(i).isFunction()){
				scope.assignFunction(this.params.get(i).getText(), inputParams.get(i));
			}else{
				scope.assign(this.params.get(i).getText(), inputParams.get(i));
			}
    }
    TileValue ret = TileValue.VOID;
		EvalVisitor evalVisitor = new EvalVisitor(scope, structs, false);
    try {
    	evalVisitor.visit(this.block);
    } catch (ReturnValue returnValue) {
    	ret = returnValue.value;
    }
    return ret;
  }

	public List<Type> getParamIDs(){
		return paramsID;
	}

	public boolean checkValid(List<Type> inputParams){
		if (inputParams.size() != paramsID.size()){
			return false;
		}
		for(int i = 0; i < paramsID.size(); i++){
			if (paramsID.get(i) != Type.ANY && paramsID.get(i) != inputParams.get(i) && inputParams.get(i) != Type.ANY){
				return false;
			}
		}
		return true;
	}

	public String toString(){
		if(block == null){
			return "Function: Block: " + block + ", Params: " + paramsID;
		}
		return "Function: Block: " + block.toString() + ", Params: " + paramsID;
	}
}
