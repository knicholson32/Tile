import org.antlr.v4.runtime.ParserRuleContext;
public class BreakStatement extends RuntimeException {
  private final ParserRuleContext context;
  public BreakStatement(ParserRuleContext context){
    this.context = context;
  }

  public ParserRuleContext context(){
    return this.context;
  }
}
