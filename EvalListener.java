import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class EvalListener extends TileBaseListener {

  private EvalVisitor visitor;
  public EvalListener (EvalVisitor visitor){
    super();
    this.visitor = visitor;
  }

  public void addImport(String importStr){
    //System.out.println("Add: " + importStr);
  }

  @Override
  public void exitImportStatmemt(TileParser.ImportStatmemtContext ctx){
    //System.out.println(">>>> Adding Parser");
    //TileValue val = this.visit(ctx.expression());
    String importName = ctx.expression().getText();
    importName = importName.substring(1,importName.length()-1);
    try{
      ANTLRFileStream aFile = new ANTLRFileStream(importName.trim());
      TileLexer lexer = new TileLexer(new ANTLRFileStream(importName));
      lexer.removeErrorListeners();
      lexer.addErrorListener(DescriptiveErrorListener.INSTANCE);
      TileParser parser = new TileParser(new CommonTokenStream(lexer));

      EvalListener ev = new EvalListener(visitor);
      parser.setBuildParseTree(true);
      parser.addParseListener(ev);
      parser.removeErrorListeners();
      //parser.setErrorHandler(new BailErrorStrategy());
      parser.removeErrorListeners();
      parser.addErrorListener(DescriptiveErrorListener.INSTANCE);

      new Visitor(visitor).visit(parser.parse());

    }catch(IOException e){
      if ( e.getMessage() != null) {
          System.err.println(e.getMessage());
      } else {
          e.printStackTrace();
      }

      throw new EvalException("Import file does not exist: " + importName + ":",ctx);
    }

  }
}
