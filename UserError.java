import org.antlr.v4.runtime.ParserRuleContext;
public class UserError extends RuntimeException {
  private final ParserRuleContext context;
  private final String errorString;
  public UserError(ParserRuleContext context, String errorString){
    this.context = context;
    this.errorString = errorString;
  }

  public ParserRuleContext context(){
    return this.context;
  }

  public String errorString(){
    return this.errorString;
  }
}
