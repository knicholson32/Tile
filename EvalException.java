import org.antlr.v4.runtime.ParserRuleContext;

public class EvalException extends RuntimeException {
    public EvalException(ParserRuleContext ctx) {
        this("Illegal expression: "+ctx.getText()+":"+ctx.start.getCharPositionInLine(), ctx);
    }

    public EvalException(String msg, ParserRuleContext ctx) {
        super(msg+" line:"+ctx.start.getLine()+":"+ctx.start.getCharPositionInLine());
    }

    public EvalException(String msg) {
        super(msg);
    }
}
