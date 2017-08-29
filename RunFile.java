import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;

public class RunFile {
  private List<TileParser> parsers = new ArrayList<TileParser>(3);
  private boolean useCharInput = false;

  private String file;
  private char[] input;
  private int charLength;
  private Scope useScope = null;
  private Scope parentScope = null;
  private boolean allowMaster = true;

  public RunFile(String file){
    this.file = file;
  }

  public RunFile(char[] input, int charLength){
    this.input = input;
    this.charLength = charLength;
    useCharInput = true;
  }

  public RunFile(char[] input, int charLength, Scope useScope){
    this(input,charLength);
    this.useScope = useScope;
  }

  public RunFile(char[] input, int charLength, Scope useScope, boolean allowMaster){
    this(input,charLength);
    this.useScope = useScope;
    this.allowMaster = allowMaster;
  }



  public void setParentScope(Scope s){
    parentScope = s;
  }

  public void run() throws Exception{
    CharStream cs;
    if (useCharInput == false){
      cs = new ANTLRFileStream(file);
    }else{
      cs = new ANTLRInputStream(input, charLength);
    }
    TileLexer lexer = new TileLexer(cs);
    lexer.removeErrorListeners();
    lexer.addErrorListener(DescriptiveErrorListener.INSTANCE);

    Scope scope;
    if(useScope == null){
      if(parentScope == null){
        scope = new Scope();
      }else{
        scope = new Scope(parentScope);
      }
    }else{
      scope = useScope;
    }


    // Visit master functions and store them in the scope
    Map<String, Struct> structs = new HashMap<String, Struct>();
    ArrayList<FunctionPointer> functions = new ArrayList<FunctionPointer>();
    SymbolVisitor symbolVisitor = new SymbolVisitor(functions, structs);
    symbolVisitor.visitMasterFunctions();
    scope.integrateFunctions(functions);

    EvalVisitor visitor = new EvalVisitor(scope, structs);
    //visitor.firstBlock = false;

    TileParser parser = new TileParser(new CommonTokenStream(lexer));
    if(this.allowMaster == true){
      EvalListener ev = new EvalListener(visitor);
      parser.addParseListener(ev);
    }
    parser.setBuildParseTree(true);
    parser.removeErrorListeners();
    //parser.setErrorHandler(new BailErrorStrategy());
    parser.removeErrorListeners();
    parser.addErrorListener(DescriptiveErrorListener.INSTANCE);

    ParseTree mainTree = parser.parse();




    /*// Visit all imported files
    for(TileParser curParser : parsers){
      ParseTree tree = curParser.parse();
      System.out.println(curParser.getSourceName());
      visitor.visit(tree);
    }*/
    new Visitor(visitor).visit(mainTree);

    //System.out.println(structs);
  }
}
