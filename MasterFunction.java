import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class MasterFunction extends Function{

  private String id;
  private List<Type> params;
  private String intName;
  private String typed;

  private static Pattern pat;

  public MasterFunction(String id, List<Type> params, String intName) {
    super(params, null, null);
  	this.id = id;
    this.params = params;
    this.intName = intName;
    this.typed = "";
  }

  // Function information
  public static String[] functionNames   =
  {"time" , "getSystem"  , "sys_print"  , "sys_println" ,"scope",  "shell"     , "type"   , "sys_input", "size"    , "size"       ,"sys_clean", "error"    , "sys_strbreak", "eval","sys_regex_set","sys_regex_match","sys_regex_start"};
  public static String[] functionIntNames=
  {"time" , "getSystem"  ,   "print"    ,   "println"   ,"scope",  "shell"     , "type"   , "input"    , "sizeList" , "sizeStr"   , "clean"   , "error"     , "strbreak", "eval","regex_set","regex_match","regex_start"};
  public static Type[][] functionParams  =
  { { }   ,      { }     , {Type.STRING},  {Type.STRING},  {}   , {Type.STRING},{Type.ANY},    {}      , {Type.LIST},{Type.STRING},     {}    ,{Type.STRING},{Type.STRING},{Type.STRING},{Type.STRING},{Type.STRING},{Type.STRING}};

  // Function implimentation
  private static TileValue runFunction(String intName, String id, List<TileValue> variables, Scope scope){
    switch(intName){
      case "println":
        System.out.println(variables.get(0).asString());
        break;
      case "print":
        System.out.print(variables.get(0).asString());
        break;
      case "time":
        return new TileValue(System.currentTimeMillis());
      case "getSystem":
        return new TileValue("System: " + System.getProperty("os.name") + ", " + System.getProperty("os.arch") + ", java: " + 	System.getProperty("java.version"));
      case "scope":
        scope.scopeTree(-1);
        break;
      case "shell":
        String output = "";
        try{
          Runtime r = Runtime.getRuntime();
          Process p = r.exec(variables.get(0).asString());
          p.waitFor();
          BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
          String line = "";
          output = "";
          while ((line = b.readLine()) != null) {
            output = output + line;
          }
          b.close();
        }catch(Exception e){
          //String text = org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e);
          //throw new RuntimeException("[SHELL] An error occured while running a shell command: " + text);
          // TODO: Fix this
        }
        return new TileValue(output);
      case "type":
        return new TileValue(variables.get(0).getTypeName());
      case "input":
        try {
          BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
          return new TileValue(buffer.readLine());
        } catch (Exception e) {
          throw new RuntimeException("Current platform is unable to resolve system input.");
        }
      case "sizeList":
        return new TileValue(variables.get(0).asList().size());
      case "sizeStr":
        return new TileValue(variables.get(0).asString().length());
      case "clean":
        TileValue.cleanNextNumber = true;
        break;
      case "strbreak":
        String input = variables.get(0).asString();
        char[] arr = input.toCharArray();
        List<TileValue> cList = new ArrayList<TileValue>();
        for(char c : arr) {
            cList.add(new TileValue(Character.toString(c)));
        }
        return new TileValue(cList);
      case "eval":
        String in = variables.get(0).asString().trim();
        if(in.length() > 0 && !in.substring(in.length()-1).equals(";")){
          in = in + ";";
        }
        char[] inputArr = in.toCharArray();
        RunFile r = new RunFile(inputArr, inputArr.length, scope.parent().parent(), false);
        try {
          r.run();
        } catch (Exception e) {
          if ( e.getMessage() != null) {
              System.err.println(e.getMessage());
          } else {
              e.printStackTrace();
          }
        }
        break;
      case "regex_set":
        pat = Pattern.compile(variables.get(0).asString());
        break;
      case "regex_match":
        return new TileValue(pat.matcher(variables.get(0).asString()).matches());
      case "regex_start":
        return new TileValue(pat.matcher(variables.get(0).asString()).start());
      case "error":
        throw new UserError(scope.getContext(), variables.get(0).asString());
      default:
        throw new RuntimeException("[MASTER_FUNCTION] Illegal Function call: "+id+": Function not found:");
    }
    return TileValue.VOID;
  }


  private static TileValue checkFunction(String id, String intName, List<TileValue> inputParams, List<Type> myParams, Map<String, Struct> structs, Scope scope){
    List<TileValue> variables = new ArrayList<TileValue>();
    scope = new Scope(scope);
    EvalVisitor evalVisitor = new EvalVisitor(scope, /*functions,*/ structs, false);
    for (int i = 0; i < inputParams.size(); i++) {
        TileValue value = inputParams.get(i);
        boolean correctType = false;
        switch(myParams.get(i)){
          case STRING:
            correctType = (value.isString());
            break;
          case NUMBER:
            correctType = (value.isNumber());
            break;
          case LIST:
            correctType = (value.isList());
            break;
          case NULL:
            correctType = (value.isNull());
            break;
          case STRUCT:
            break;
          case STRING_NUMBER:
            correctType = (value.isString()) || (value.isNumber()) ;
            break;
          case BOOL:
            correctType = (value.isBoolean());
            break;
          case ANY:
            correctType = true;
            break;
        }
        if(!correctType){
          throw new RuntimeException("Invalid variable type for function '"+intName+"': arg "+(i+1)+": Got '"+value.getTypeName()+"', Expected '"+myParams.get(i)+"'");
        }
        variables.add(value);
    }

    return runFunction(intName, id, variables, scope);

  }

  public TileValue invoke(List<TileValue> inputParams, List<Type> inputParamsType, Map<String, Struct> structs, Scope scope) {
    if(!checkValid(inputParamsType)){
			throw new RuntimeException("Illegal Function call"); //TODO: Make this error more comprehensive
		}
    return this.checkFunction(this.id, this.intName, inputParams, this.params, /*functions,*/ structs, scope);
  }
  /*public TileValue invoke(List<TileParser.ExpressionContext> params, Map<String, Struct> structs, Scope scope) {
    if (params.size() != this.params.size()) {
        throw new RuntimeException("Illegal Function call");
    }
    return this.checkFunction(this.id, params, this.params, structs, scope);
  }*/

}
