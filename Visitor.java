import org.antlr.v4.runtime.tree.ParseTree;
public class Visitor{
  private final EvalVisitor visitor;
  public Visitor(EvalVisitor visitor){
    this.visitor = visitor;
  }

  public void visit(ParseTree mainTree){
    try{
      visitor.visit(mainTree);
    }catch(BreakStatement br){
      throw new EvalException("Illegal break statement: ",br.context());
    }catch(ContinueStatement co){
      throw new EvalException("Illegal continue statement: ",co.context());
    }catch(UserError ue){
      if(ue.context() == null){
        throw new RuntimeException("Error: " + ue.errorString());
      }else{
        throw new EvalException("Error: " + ue.errorString(), ue.context());
      }
    }
  }
}
