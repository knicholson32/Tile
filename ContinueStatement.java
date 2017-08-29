import org.antlr.v4.runtime.ParserRuleContext;
public class ContinueStatement extends RuntimeException {
  private final ParserRuleContext context;
  public ContinueStatement(ParserRuleContext context){
    this.context = context;
  }

  public ParserRuleContext context(){
    return this.context;
  }
}
