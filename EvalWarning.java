import org.antlr.v4.runtime.ParserRuleContext;

public class EvalWarning {
  public EvalWarning(String msg, ParserRuleContext ctx) {
      System.out.println("Warning: " +msg+" : line:"+ctx.start.getLine()+":"+ctx.start.getCharPositionInLine());
  }
  public EvalWarning(String msg, ParserRuleContext ctx, String sourceName) {
      System.out.println("Warning: " +msg+" : " + sourceName + ":"+ctx.start.getLine()+":"+ctx.start.getCharPositionInLine());
  }
}
