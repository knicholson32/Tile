import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.antlr.v4.runtime.tree.ParseTree;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;


public class EvalVisitor extends TileBaseVisitor<TileValue> {
	private static ReturnValue returnValue = new ReturnValue();
    private Scope scope;
		private Map<String, Struct> structs;

		public boolean firstBlock = true;
		private boolean createBlockScope = true;

    public EvalVisitor(Scope scope, Map<String, Struct> structs) {
        this.scope = scope;
				this.structs = structs;

    }

		public EvalVisitor(Scope scope, Map<String, Struct> structs, boolean firstBlock){
			this(scope, structs);
			this.firstBlock = firstBlock;
		}

    // functionDecl
    @Override
    public TileValue visitFunctionDecl(TileParser.FunctionDeclContext ctx) {
        return TileValue.VOID;
    }

		// Def '(' idList? ')' Start block End      #functionDeclExpression
		@Override
		public TileValue visitFunctionDeclExpression(TileParser.FunctionDeclExpressionContext ctx) {
			List<TerminalNode> params = ctx.idList() != null ? ctx.idList().Identifier() : new ArrayList<TerminalNode>();
      List<TileParser.TypeContext> types  = ctx.idList() != null ? ctx.idList().type() : new ArrayList<TileParser.TypeContext>();
      ParseTree block = ctx.block();
			FunctionPointer newPointer = new FunctionPointer();
      Function f = new Function(block, params, types);
			newPointer.addFunctionReferece(f);
			return new TileValue(newPointer);
		}

		@Override
		public TileValue visitStructDecl(TileParser.StructDeclContext ctx) {
			return TileValue.VOID;
		}

		// expression ISA type                      #isaTypeExpression
		@Override
		public TileValue visitIsaTypeExpression(TileParser.IsaTypeExpressionContext ctx) {
			TileValue result = this.visit(ctx.expression());
			String type = result.getTypeName();
			String compareToType = ctx.type().getText();

			return new TileValue(type.equals(compareToType));
		}

    // list: '[' exprList? ']'
    @Override
    public TileValue visitList(TileParser.ListContext ctx) {
        List<TileValue> list = new ArrayList<TileValue>();
        if (ctx.exprList() != null) {
	        for(TileParser.ExpressionContext ex: ctx.exprList().expression()) {
	            list.add(this.visit(ex));
	        }
        }
        return new TileValue(list);
    }


    // '-' expression                           #unaryMinusExpression
    @Override
    public TileValue visitUnaryMinusExpression(TileParser.UnaryMinusExpressionContext ctx) {
    	TileValue v = this.visit(ctx.expression());
    	if (v == null || !v.isNumber()) {
    	    throw new EvalException(ctx);
        }
    	return new TileValue(-1 * v.asDouble());
    }


    // '!' expression                           #notExpression
    @Override
    public TileValue visitNotExpression(TileParser.NotExpressionContext ctx) {
    	TileValue v = this.visit(ctx.expression());
    	if(v == null || !v.isBoolean()) {
    	    throw new EvalException(ctx);
        }
    	return new TileValue(!v.asBoolean());
    }

    // expression '^' expression                #powerExpression
    @Override
    public TileValue visitPowerExpression(TileParser.PowerExpressionContext ctx) {
    	TileValue lhs = this.visit(ctx.expression(0));
    	TileValue rhs = this.visit(ctx.expression(1));
    	if (lhs != null && rhs != null && lhs.isNumber() && rhs.isNumber()) {
    		return new TileValue(Math.pow(lhs.asDouble(), rhs.asDouble()));
    	}
    	throw new EvalException(ctx);
    }

		// expression MulDivMod expression          #multiplyDivideModExpression
		@Override
		public TileValue visitMultiplyDivideModExpression(TileParser.MultiplyDivideModExpressionContext ctx) {
			TileValue lhs = this.visit(ctx.expression(0));
			TileValue rhs = this.visit(ctx.expression(1));

			String operator = ctx.mulDivMod().getText();

			if(lhs == null || rhs == null) {
					throw new EvalException(ctx);
			}

			switch(operator){
				case "*":
					if(lhs.isNumber() && rhs.isNumber()) {
							return new TileValue(lhs.asDouble() * rhs.asDouble());
					}
					if(lhs.isBoolean() && rhs.isBoolean()) {
							return new TileValue(lhs.asBoolean() && rhs.asBoolean());
					}
					throw new EvalException("The \'" + operator + "\' operator requires [number, number] or [bool, bool] only: received: [" + lhs.getTypeName() + ", " + rhs.getTypeName() + "]",ctx);
				case "/":
					if(lhs.isNumber() && rhs.isNumber()) {
						return new TileValue(lhs.asDouble() / rhs.asDouble());
					}
					throw new EvalException("The \'" + operator + "\' operator requires [number, number] only: received: [" + lhs.getTypeName() + ", " + rhs.getTypeName() + "]",ctx);
				case "%":
					if(lhs.isNumber() && rhs.isNumber()) {
						return new TileValue(lhs.asDouble() % rhs.asDouble());
					}
					throw new EvalException("The \'" + operator + "\' operator requires [number, number] only: received: [" + lhs.getTypeName() + ", " + rhs.getTypeName() + "]",ctx);
				default:
					throw new EvalException("Unknown operator: " + operator,ctx);
			}
		}

		// expression ('+' | '-') expression        #addSubExpression
		@Override
		public TileValue visitAddSubExpression(TileParser.AddSubExpressionContext ctx) {
			TileValue lhs = this.visit(ctx.expression(0));
			TileValue rhs = this.visit(ctx.expression(1));

			String operator = ctx.addSub().getText();

			/*if(operator.equals('-') && lhs == null && rhs.isNumber()){
				return new TileValue(rhs.asDouble());
			}*/

			if(lhs == null || rhs == null) {
					throw new EvalException(ctx);
			}

			switch(operator){
				case "+":
					// number + number
					if(lhs.isNumber() && rhs.isNumber()) {
							return new TileValue(lhs.asDouble() + rhs.asDouble());
					}

					if(lhs.isBoolean() && rhs.isBoolean()) {
							return new TileValue(lhs.asBoolean() || rhs.asBoolean());
					}

					// list + any
					if(lhs.isList()) {
							List<TileValue> list = lhs.asList();
							list.add(rhs);
							return new TileValue(list);
					}

					// string + any
					if(lhs.isString()) {
							//System.out.println(rhs.toString());
							return new TileValue(lhs.asString() + "" + rhs.toString());
					}

					// any + string
					if(rhs.isString()) {
							return new TileValue(lhs.toString() + "" + rhs.asString());
					}

					return new TileValue(lhs.toString() + rhs.toString());
				case "-":
					if (lhs.isNumber() && rhs.isNumber()) {
						return new TileValue(lhs.asDouble() - rhs.asDouble());
					}
					throw new EvalException("Unknown type for \'-\': " + lhs.getTypeName() + ":",ctx);
				default:
					throw new EvalException("Unknown operator: " + operator,ctx);
			}

		}

		// Assignment '++' | '--'
		@Override
		public TileValue visitSingularAssignment(@NotNull TileParser.SingularAssignmentContext ctx) {
			String type = ctx.SingularOp().getSymbol().getText();
			int op = 0;
			switch(type){
				case "++":
					op = 1;
					break;
				case "--":
					op = -1;
					break;
				default:
					throw new EvalException("Invalid Operator: " + type + ":", ctx);
			}

			TileValue val = scope.resolve(ctx.Identifier().getText());
			if(val == null) {throw new EvalException("Invalid identifier: Variable may not be assigned:", ctx);}
			if(val.isList() && ctx.indexes() == null) {throw new EvalException("Operator: Cannot add list using \'"+type+"\':", ctx);}
			if(val.isString()) {throw new EvalException("Operator: Cannot add String using \'"+type+"\':", ctx);}
			if(val.isNumber()) {
				TileValue newVal = new TileValue(val.asDouble() + op);
		    String id = ctx.Identifier().getText();
		    scope.assign(id, newVal);
				return newVal;
			}
			if(val.isList()) {
				List<TileParser.ExpressionContext> exps = ctx.indexes().expression();
				TileValue value = resolveIndexes(ctx, val, exps);
				if(!value.isNumber()){
					throw new EvalException("Operator: Cannot use \'"+type+"\' on " + value.getTypeName(), ctx);
				}
				TileValue newVal = new TileValue(value.asDouble() + op);
		    setAtIndex(ctx, exps, val, newVal);
				return newVal;
			}
			return TileValue.NULL;
		}

		private boolean checkSame(TileValue a, TileValue b){
			return (a.getTypeName() == b.getTypeName());
		}

		private TileValue calculateAssignent(String type, TileValue val, TileValue amt, @NotNull TileParser.FullAssignmentContext ctx, boolean assign){
			TileValue output = TileValue.NULL;
			if(val.isList()){
				if(ctx.indexes() != null){
					// Variable is in a list
					List<TileParser.ExpressionContext> exps = ctx.indexes().expression();
					TileValue value = resolveIndexes(ctx, val, exps);
					output = calculateAssignent(type, value, amt, new TileParser.FullAssignmentContext(new TileParser.AssignmentContext(new ParserRuleContext(),0)), false);
					setAtIndex(ctx, exps, val, output);
					return output;
				}
			}
			switch(type){
				case "+=":
					if(val.isNumber()){
						if(!amt.isNumber()){throw new EvalException("Invalid RHS variable type: " + amt.getTypeName() + " for op \'"+type+"\':", ctx);}
						output = new TileValue(val.asDouble() + amt.asDouble());
						if(assign){
				    	String id = ctx.Identifier().getText();
				    	scope.assign(id, output);
						}
					}else if(val.isList()){
						// Variable is a list
						List<TileValue> list = val.asList();
            list.add(amt);
            return new TileValue(list);
					}else if(val.isString()){
						output = new TileValue(val.asString() + "" + amt.toString());
						if(assign){
				    	String id = ctx.Identifier().getText();
				    	scope.assign(id, output);
						}
					}
					return output;
				case "-=":
					if(val.isNumber()){
						if(!amt.isNumber()){throw new EvalException("Invalid RHS variable type: " + amt.getTypeName() + " for op \'"+type+"\':", ctx);}
						output = new TileValue(val.asDouble() - amt.asDouble());
						if(assign){
							String id = ctx.Identifier().getText();
							scope.assign(id, output);
						}
						return output;
					}
					throw new EvalException("Invalid -= variable type: " + val.getTypeName() + " for op \'"+type+"\':", ctx);
				case "*=":
					if(val.isNumber()){
						if(!amt.isNumber()){throw new EvalException("Invalid RHS variable type: " + amt.getTypeName() + " for op \'"+type+"\':", ctx);}
						output = new TileValue(val.asDouble() * amt.asDouble());
						if(assign){
							String id = ctx.Identifier().getText();
							scope.assign(id, output);
						}
						return output;
					}
					throw new EvalException("Invalid variable type: " + val.getTypeName() + " for op \'"+type+"\':", ctx);
				case "/=":
					if(val.isNumber()){
						if(!amt.isNumber()){throw new EvalException("Invalid RHS variable type: " + amt.getTypeName() + " for op \'"+type+"\':", ctx);}
						output = new TileValue(val.asDouble() / amt.asDouble());
						if(assign){
							String id = ctx.Identifier().getText();
							scope.assign(id, output);
						}
						return output;
					}
					throw new EvalException("Invalid variable type: " + val.getTypeName() + " for op \'"+type+"\':", ctx);
				default:
					throw new EvalException("Invalid Operator: " + type + ":", ctx);
			}
		}

		// Assignment '+=' '-=' '*-' '/='
		@Override
		public TileValue visitFullAssignment(@NotNull TileParser.FullAssignmentContext ctx) {
			TileValue val = scope.resolve(ctx.Identifier().getText());
			TileValue amt = this.visit(ctx.expression());
			if (val == null) {val = TileValue.NULL;}
			if (amt == null) {amt = TileValue.NULL;}
			String type = ctx.MultiOp().getSymbol().getText();

			if(val.isBoolean() || val.isVoid() || val.isNull()){
				throw new EvalException("Invalid variable type: " + val.getTypeName() + ":", ctx);
			}

			return calculateAssignent(type,val,amt,ctx,true);

		}

    // expression '>=' expression               #gtEqExpression
    @Override
    public TileValue visitGtEqExpression(TileParser.GtEqExpressionContext ctx) {
    	TileValue lhs = this.visit(ctx.expression(0));
    	TileValue rhs = this.visit(ctx.expression(1));
			if (lhs == null) {lhs = TileValue.NULL;}
			if (rhs == null) {rhs = TileValue.NULL;}
    	if (lhs.isNumber() && rhs.isNumber()) {
    		return new TileValue(lhs.asDouble() >= rhs.asDouble());
    	}
    	if(lhs.isString() && rhs.isString()) {
            return new TileValue(lhs.asString().compareTo(rhs.asString()) >= 0);
        }
    	throw new EvalException(ctx);
    }

    // expression '<=' expression               #ltEqExpression
    @Override
    public TileValue visitLtEqExpression(TileParser.LtEqExpressionContext ctx) {
    	TileValue lhs = this.visit(ctx.expression(0));
    	TileValue rhs = this.visit(ctx.expression(1));
			if (lhs == null) {lhs = TileValue.NULL;}
			if (rhs == null) {rhs = TileValue.NULL;}
    	if (lhs.isNumber() && rhs.isNumber()) {
    		return new TileValue(lhs.asDouble() <= rhs.asDouble());
    	}
    	if(lhs.isString() && rhs.isString()) {
            return new TileValue(lhs.asString().compareTo(rhs.asString()) <= 0);
        }
    	throw new EvalException(ctx);
    }

    // expression '>' expression                #gtExpression
    @Override
    public TileValue visitGtExpression(TileParser.GtExpressionContext ctx) {
    	TileValue lhs = this.visit(ctx.expression(0));
    	TileValue rhs = this.visit(ctx.expression(1));
			if (lhs == null) {lhs = TileValue.NULL;}
			if (rhs == null) {rhs = TileValue.NULL;}
    	if (lhs.isNumber() && rhs.isNumber()) {
    		return new TileValue(lhs.asDouble() > rhs.asDouble());
    	}
    	if(lhs.isString() && rhs.isString()) {
            return new TileValue(lhs.asString().compareTo(rhs.asString()) > 0);
        }
    	throw new EvalException(ctx);
    }

    // expression '<' expression                #ltExpression
    @Override
    public TileValue visitLtExpression(TileParser.LtExpressionContext ctx) {
    	TileValue lhs = this.visit(ctx.expression(0));
    	TileValue rhs = this.visit(ctx.expression(1));
			if (lhs == null) {lhs = TileValue.NULL;}
			if (rhs == null) {rhs = TileValue.NULL;}
    	if (lhs.isNumber() && rhs.isNumber()) {
    		return new TileValue(lhs.asDouble() < rhs.asDouble());
    	}
    	if(lhs.isString() && rhs.isString()) {
            return new TileValue(lhs.asString().compareTo(rhs.asString()) < 0);
        }
    	throw new EvalException(ctx);
    }

    // expression '==' expression               #eqExpression
    @Override
    public TileValue visitEqExpression(@NotNull TileParser.EqExpressionContext ctx) {
      TileValue lhs = this.visit(ctx.expression(0));
      TileValue rhs = this.visit(ctx.expression(1));
      if (lhs == null) {lhs = TileValue.NULL;}
			if (rhs == null) {rhs = TileValue.NULL;}
      return new TileValue(lhs.equals(rhs));
    }

    // expression '!=' expression               #notEqExpression
    @Override
    public TileValue visitNotEqExpression(@NotNull TileParser.NotEqExpressionContext ctx) {
      TileValue lhs = this.visit(ctx.expression(0));
      TileValue rhs = this.visit(ctx.expression(1));
			if (lhs == null) {lhs = TileValue.NULL;}
			if (rhs == null) {rhs = TileValue.NULL;}
      return new TileValue(!lhs.equals(rhs));
    }

    // expression '&&' expression               #andExpression
    @Override
    public TileValue visitAndExpression(TileParser.AndExpressionContext ctx) {
    	TileValue lhs = this.visit(ctx.expression(0));
    	TileValue rhs = this.visit(ctx.expression(1));
			if (lhs == null) {lhs = TileValue.NULL;}
			if (rhs == null) {rhs = TileValue.NULL;}

    	if(!lhs.isBoolean() || !rhs.isBoolean()) {
    	    throw new EvalException(ctx);
        }
		return new TileValue(lhs.asBoolean() && rhs.asBoolean());
    }

    // expression '||' expression               #orExpression
    @Override
    public TileValue visitOrExpression(TileParser.OrExpressionContext ctx) {
    	TileValue lhs = this.visit(ctx.expression(0));
    	TileValue rhs = this.visit(ctx.expression(1));
			if (lhs == null) {lhs = TileValue.NULL;}
			if (rhs == null) {rhs = TileValue.NULL;}

    	if(!lhs.isBoolean() || !rhs.isBoolean()) {
    	    throw new EvalException(ctx);
        }
		return new TileValue(lhs.asBoolean() || rhs.asBoolean());
    }

    // expression In expression                 #inExpression
	@Override
	public TileValue visitInExpression(TileParser.InExpressionContext ctx) {
		TileValue lhs = this.visit(ctx.expression(0));
  	TileValue rhs = this.visit(ctx.expression(1));
		if (lhs == null) {lhs = TileValue.NULL;}
		if (rhs == null) {rhs = TileValue.NULL;}

  	if (rhs.isList()) {
  		for(TileValue val: rhs.asList()) {
  			if (val.equals(lhs)) {
  				return new TileValue(true);
  			}
  		}
  		return new TileValue(false);
  	}
  	throw new EvalException(ctx);
	}

    // Number                                   #numberExpression
    @Override
    public TileValue visitNumberExpression(@NotNull TileParser.NumberExpressionContext ctx) {
        return new TileValue(Double.valueOf(ctx.getText()));
    }

    // Bool                                     #boolExpression
    @Override
    public TileValue visitBoolExpression(@NotNull TileParser.BoolExpressionContext ctx) {
        return new TileValue(Boolean.valueOf(ctx.getText()));
    }

    // Null                                     #nullExpression
    @Override
    public TileValue visitNullExpression(@NotNull TileParser.NullExpressionContext ctx) {
        return TileValue.NULL;
    }

    private TileValue resolveIndexes(ParserRuleContext ctx, TileValue val, List<TileParser.ExpressionContext> indexes) {
    	for (TileParser.ExpressionContext ec: indexes) {
    		TileValue idx = this.visit(ec);
    		if (!idx.isNumber() || (!val.isList() && !val.isString()) ) {
        		throw new EvalException("Problem resolving indexes on "+val+" at "+idx, ec);
    		}
    		int i = idx.asDouble().intValue();
    		if (val.isString()) {
    			val = new TileValue(val.asString().substring(i, i+1));
    		} else {
					List<TileValue> l = val.asList();
					if(l.size()<= i || i < 0){
						throw new EvalException("Index out of bounds: index="+i+", size="+l.size()+", Array=" + val + ":", ec);
					}
    			val = l.get(i);
    		}
    	}
    	return val;
    }

    private void setAtIndex(ParserRuleContext ctx, List<TileParser.ExpressionContext> indexes, TileValue val, TileValue newVal) {
			if (val == null) {
				throw new EvalException("Illegal expression: Variable does not exist. Initilize an empty array first.", ctx);
			}
			if (!val.isList()) {
    		throw new EvalException("Illegal expression: Variable is not an array.",ctx);
    	}
    	// TODO some more list size checking in here
    	for (int i = 0; i < indexes.size() - 1; i++) {
    		TileValue idx = this.visit(indexes.get(i));
    		if (!idx.isNumber()) {
        		throw new EvalException(ctx);
    		}
				int curSize = val.asList().size();
				int reqIndex = idx.asDouble().intValue();
				if(reqIndex >= curSize){
					throw new EvalException("Array out of bounds: Size: " + curSize + " : Index: " + reqIndex + ":",ctx);
				}
    		val = val.asList().get(idx.asDouble().intValue());
    	}
    	TileValue idx = this.visit(indexes.get(indexes.size() - 1));
			if (!idx.isNumber()) {
	    		throw new EvalException(ctx);
			}
			int curSize = val.asList().size();
			int reqIndex = idx.asDouble().intValue();
			if(reqIndex >= curSize){
				// Need to add the value to the end. Must make sure we are one before the end location.
				for(int j = curSize; j < reqIndex; j++){
					val.asList().add(TileValue.NULL);
				}
				val.asList().add(newVal);
			}else{
    		val.asList().set(idx.asDouble().intValue(), newVal);
			}
    }

    // functionCall indexes?                    #functionCallExpression
    @Override
    public TileValue visitFunctionCallExpression(TileParser.FunctionCallExpressionContext ctx) {
    	TileValue val = this.visit(ctx.functionCall());
    	if (ctx.indexes() != null) {
        	List<TileParser.ExpressionContext> exps = ctx.indexes().expression();
        	val = resolveIndexes(ctx, val, exps);
        }
    	return val;
    }

    // list indexes?                            #listExpression
    @Override
    public TileValue visitListExpression(TileParser.ListExpressionContext ctx) {
    	TileValue val = this.visit(ctx.list());
    	if (ctx.indexes() != null) {
        	List<TileParser.ExpressionContext> exps = ctx.indexes().expression();
        	val = resolveIndexes(ctx, val, exps);
        }
    	return val;
    }

    // Identifier indexes?                      #identifierExpression
    @Override
    public TileValue visitIdentifierExpression(@NotNull TileParser.IdentifierExpressionContext ctx) {
        String id = ctx.Identifier().getText();
        TileValue val = scope.resolve(id);

				if(val == null){
					throw new EvalException("Cannot resolve the variable \'" + id + "\':", ctx);
				}

        if (ctx.indexes() != null) {
        	List<TileParser.ExpressionContext> exps = ctx.indexes().expression();
        	val = resolveIndexes(ctx, val, exps);
        }
        return val;
    }

    // String indexes?                          #stringExpression
    @Override
    public TileValue visitStringExpression(@NotNull TileParser.StringExpressionContext ctx) {
        String text = ctx.getText();
        text = text.substring(1, text.length() - 1).replaceAll("\\\\(.)", "$1");
        TileValue val = new TileValue(text);
        if (ctx.indexes() != null) {
        	List<TileParser.ExpressionContext> exps = ctx.indexes().expression();
        	val = resolveIndexes(ctx, val, exps);
        }
        return val;
    }

		@Override
		public TileValue visitBreakStatement(TileParser.BreakStatementContext ctx) {
			throw new BreakStatement(ctx);
		}

		@Override
		public TileValue visitContinueStatement(TileParser.ContinueStatementContext ctx) {
			throw new ContinueStatement(ctx);
		}

		/*@Override
		public TileValue visitStringFunctionExpression(TileParser.StringFunctionExpressionContext ctx) {
			TileValue string = this.visit(ctx.expression());
			if(!string.isString()){throw new EvalException("String Functions: Operand must be a String:", ctx);}
			String type = ctx.stringFunctions().getStart().getText();
			TileValue output = TileValue.NULL;
			switch(type){
				case "substring":
					if(ctx.stringFunctions().expression(0) != null && ctx.stringFunctions().expression(1) != null){
						TileValue val0 = this.visit(ctx.stringFunctions().expression(0));
						TileValue val1 = this.visit(ctx.stringFunctions().expression(1));
						if(!val0.isNumber()||val0.asDouble().intValue() != val0.asDouble()){throw new EvalException("Substring: Argument 1 must be an integer:", ctx);}
						if(!val1.isNumber()||val1.asDouble().intValue() != val1.asDouble()){throw new EvalException("Substring: Argument 2 must be an integer:", ctx);}
						int first = val0.asDouble().intValue();
						int second = val1.asDouble().intValue();
						output = new TileValue(string.asString().substring(first,second));
					}else if(ctx.stringFunctions().expression(0) != null){
						TileValue val0 = this.visit(ctx.stringFunctions().expression(0));
						if(!val0.isNumber()||val0.asDouble().intValue() != val0.asDouble()){throw new EvalException("Substring: Argument 1 must be an integer:", ctx);}
						int first = val0.asDouble().intValue();
						output = new TileValue(string.asString().substring(first));
					}else{
						throw new EvalException("Substring: Not enough arguments:", ctx);
					}
					break;
				case "length":
					if(ctx.stringFunctions().expression(0) != null){
						throw new EvalException("length: No arguments required:", ctx);
					}
					output = new TileValue(string.asString().length());
					break;
				case "indexOf":
					if(ctx.stringFunctions().expression(0) != null && ctx.stringFunctions().expression(1) != null){
						TileValue val0 = this.visit(ctx.stringFunctions().expression(0));
						TileValue val1 = this.visit(ctx.stringFunctions().expression(1));
						if(!val1.isNumber()||val1.asDouble().intValue() != val1.asDouble()){throw new EvalException("IndexOf: Argument 2 must be an integer:", ctx);}
						if(!val0.isString()){throw new EvalException("IndexOf: Argument 1 must be a String:", ctx);}
						String first = val0.asString();
						int second = val1.asDouble().intValue();
						output = new TileValue(string.asString().indexOf(first,second));
					}else if(ctx.stringFunctions().expression(0) != null){
						TileValue val0 = this.visit(ctx.stringFunctions().expression(0));
						if(!val0.isString()){throw new EvalException("IndexOf: Argument 1 must be a String:", ctx);}
						String first = val0.asString();
						output = new TileValue(string.asString().indexOf(first));
					}else{
						throw new EvalException("IndexOf: Not enough arguments:", ctx);
					}
					break;
				case "toNumber":
					if(ctx.stringFunctions().expression(0) != null){
						throw new EvalException("toNumber: No arguments are required:", ctx);
					}
					try{
						output = new TileValue(Double.parseDouble(string.asString()));
					}catch(java.lang.NumberFormatException e){
						output = new TileValue(0);
					}
					break;
				default:
					throw new EvalException("No viable operator for String found:", ctx);
			}
			return output;
		}*/

		@Override
		public TileValue visitListFunctionStatment(TileParser.ListFunctionStatmentContext ctx) {
			TileValue val = this.visit(ctx.expression());
			if(!val.isList()){throw new EvalException("List Functions: Operand must be a List:", ctx);}
			String type = ctx.listFunctions().getStart().getText();
			TileValue output = TileValue.NULL;
			switch(type){
				case "remove":
					if(ctx.listFunctions().expression() != null){
						TileValue removeVal = this.visit(ctx.listFunctions().expression());
						if(removeVal.isNumber()){
							if(removeVal.asDouble().intValue() != removeVal.asDouble()){
								throw new EvalException("Remove: Argument must be an integer:", ctx);
							}
							List<TileValue> list = val.asList();
							list.remove(removeVal.asDouble().intValue());
							output = new TileValue(list);
						}else if(!removeVal.isVoid() && !removeVal.isNull()){
							List<TileValue> list = val.asList();
							int i = 0;
							boolean done=false;
							while(!done && i < list.size()){
								if(removeVal.equals(list.get(i))){
									list.remove(i);
									done = true;
								}
								i++;
							}
							output = new TileValue(list);
						}else{
							throw new EvalException("Remove: Unknown remove type:", ctx);
						}
					}else{
						throw new EvalException("Remove: Not enough arguments: Nothing to remove:", ctx);
					}
					break;
				case "clear":
					List<TileValue> list = val.asList();
					list.clear();

					output = new TileValue(list);
					//System.out.println(output.getTypeName());
					break;
				default:
					throw new EvalException("No viable operator for List found:", ctx);
			}
			return output;
		}

    // '(' expression ')' indexes?              #expressionExpression
    @Override
    public TileValue visitExpressionExpression(TileParser.ExpressionExpressionContext ctx) {
        TileValue val = this.visit(ctx.expression());
        if (ctx.indexes() != null) {
        	List<TileParser.ExpressionContext> exps = ctx.indexes().expression();
        	val = resolveIndexes(ctx, val, exps);
        }
        return val;
    }

		@Override
		public TileValue visitAssignmentExpression(TileParser.AssignmentExpressionContext ctx) {
			return this.visit(ctx.assignment());
		}


		// assignment
    // : Identifier indexes? '=' expression
    // ;
    @Override
    public TileValue visitRegularAssignment(@NotNull TileParser.RegularAssignmentContext ctx) {
				//if(ctx.expression(2) == null && ctx.expression(1) == null){
	        TileValue newVal = this.visit(ctx.expression());

					if(structs.get(ctx.Identifier().getText()) != null){
						throw new EvalException("The variable \'"+ctx.Identifier().getText()+"\' is already defined as a struct:",ctx);
					}

	        if (ctx.indexes() != null) {
	        	TileValue val = scope.resolve(ctx.Identifier().getText());
	        	List<TileParser.ExpressionContext> exps = ctx.indexes().expression();
	        	setAtIndex(ctx, exps, val, newVal);
	        } else {
	        	String id = ctx.Identifier().getText();
						scope.assign(id, newVal);
					}
				return newVal;
    }

    // Identifier '(' exprList? ')' #identifierFunctionCall
    @Override
    public TileValue visitIdentifierFunctionCall(TileParser.IdentifierFunctionCallContext ctx) {
			createBlockScope = true;
        List<TileParser.ExpressionContext> params = ctx.exprList() != null ? ctx.exprList().expression() : new ArrayList<TileParser.ExpressionContext>();
        String id = ctx.Identifier().getText();// + params.size();
        //Function function;

				//System.out.println(">> ID: " + id);

        //if ((function = functions.get(id)) != null) {
        //    return function.invoke(params, functions, structs, scope);
        //}

				//System.out.println(id + " : ");

				//TODO: Parse through each parameter and Eval. Get types. Resolve functions based on this.
				scope.setContext(ctx);
				TileValue functionHolder = scope.resolveFunction(id);
				if(functionHolder != null){
					if(functionHolder.isFunction()){
						FunctionPointer function = functionHolder.asFunction();
						TileValue t = function.invoke(params, structs, scope);
						createBlockScope = false;
						return t;
					}else{
						createBlockScope = false;
						throw new EvalException("The variable \'"+ctx.Identifier().getText()+"\' could not be found as a function: Type found: " + functionHolder.getTypeName(),ctx);
					}
				}else{
					createBlockScope = false;
					throw new EvalException("The variable \'"+ctx.Identifier().getText()+"\' could not be found as a function:",ctx);
				}
    }

		@Override
		public TileValue visitAccessStructExpression(TileParser.AccessStructExpressionContext ctx) {
				String id = ctx.Identifier(0).getText();
				String name = ctx.Identifier(1).getText();
				Struct s = structs.get(id);
				return s.pull(name);
    }

		@Override
		public TileValue visitStructAssignment(@NotNull TileParser.StructAssignmentContext ctx) {
			//System.out.println("Assign Struct");
			TileValue value = this.visit(ctx.expression());

			//List<TileParser.ExpressionContext> params = ctx.exprList() != null ? ctx.exprList().expression() : new
			String id = ctx.Identifier(0).getText();// + params.size();
			String name = ctx.Identifier(1).getText();
			//System.out.println("ID: " + id);
			//System.out.println("Name: " + name);

			Struct s = structs.get(id);

			s.write(name, value);

			//System.out.println(s);


			return TileValue.NULL;
		}

    /*// Println '(' expression? ')'  #printlnFunctionCall
    @Override
    public TileValue visitPrintlnFunctionCall(@NotNull TileParser.PrintlnFunctionCallContext ctx) {
				TileParser.ExpressionContext ex;
				if ((ex = ctx.expression()) != null) {
					System.out.println(this.visit(ex));
				}else{
					System.out.println();
				}
        return TileValue.VOID;
    }*/

    // Assert '(' expression ')'    #assertFunctionCall
    @Override
    public TileValue visitAssertFunctionCall(TileParser.AssertFunctionCallContext ctx) {
    	TileValue value = this.visit(ctx.expression());

				if (value == null){throw new EvalException("Variable not found:", ctx);}

        if(!value.isBoolean()) {
            throw new EvalException(ctx);
        }

        if(!value.asBoolean()) {
            throw new AssertionError("Failed Assertion "+ctx.expression().getText()+" line:"+ctx.start.getLine());
        }

        return TileValue.VOID;
    }

    // ifStatement
    //  : ifStat elseIfStat* elseStat? End
    //  ;
    //
    // ifStat
    //  : If expression Do block
    //  ;
    //
    // elseIfStat
    //  : Else If expression Do block
    //  ;
    //
    // elseStat
    //  : Else Do block
    //  ;
    @Override
    public TileValue visitIfStatement(@NotNull TileParser.IfStatementContext ctx) {
			createBlockScope = false;
        // if ...
        if(this.visit(ctx.ifStat().expression()).asBoolean()) {
            return this.visit(ctx.ifStat().block());
        }

        // else if ...
        for(int i = 0; i < ctx.elseIfStat().size(); i++) {
            if(this.visit(ctx.elseIfStat(i).expression()).asBoolean()) {
                return this.visit(ctx.elseIfStat(i).block());
            }
        }

        // else ...
        if(ctx.elseStat() != null) {
            return this.visit(ctx.elseStat().block());
        }

        return TileValue.VOID;
    }

    // block
    // : (statement | functionDecl | structDecl)* (Return expression? ';')? functionDecl*
    // ;
    @Override
    public TileValue visitBlock(TileParser.BlockContext ctx) {
				if(!firstBlock && createBlockScope == true){
    			scope = new Scope(scope); // create new local scope
				}

				ArrayList<FunctionPointer> functions = new ArrayList<FunctionPointer>();
		    SymbolVisitor symbolVisitor = new SymbolVisitor(functions, structs);
		    symbolVisitor.visit(ctx);
		    scope.integrateFunctions(functions);

        for (TileParser.StatementContext sx: ctx.statement()) {
        	TileValue val = this.visit(sx);
        }
        TileParser.ExpressionContext ex;
        if ((ex = ctx.expression()) != null) {
					returnValue.value = this.visit(ex);

					if(createBlockScope == true){
						scope = scope.parent();
					}

					/*if(returnValue.value.isBreak()){
						return returnValue.value;
					}*/

        	throw returnValue;
        }
				if(ctx.Return() != null){
					returnValue.value = TileValue.EMPTY_RETURN;
					if(createBlockScope == true){
						scope = scope.parent();
					}
					throw returnValue;
				}
				if(!firstBlock && createBlockScope == true){
        	scope = scope.parent();
					firstBlock = false;
				}
        return TileValue.VOID;
    }

		// '@' Identifier                           #functionRefExpression
		@Override
		public TileValue visitFunctionRefExpression(TileParser.FunctionRefExpressionContext ctx) {
			String functionName = ctx.Identifier().getText();
			TileValue result = scope.resolveFunction(functionName);

			if(result == null || result.isNull()){
				throw new EvalException("No function named \'" + functionName + "\' could be resolved: ",ctx);
			}
			return result;
		}

    // forStatement
    // For '(' Identifier '=' ( (expression To expression) | (expression To expression To expression) ) ')' Start block End   #forStepStatment
    // ;
    @Override
    public TileValue visitForStepStatment(TileParser.ForStepStatmentContext ctx) {
				createBlockScope = false;
        double start = this.visit(ctx.expression(0)).asDouble();
        double stop = 0;
				double step = 1;
				if(ctx.expression(2) != null){
					step = this.visit(ctx.expression(1)).asDouble();
					stop = this.visit(ctx.expression(2)).asDouble();
				}else{
					stop = this.visit(ctx.expression(1)).asDouble();
				}


        for(double i = start; i <= stop; i += step) {
            scope.assign(ctx.Identifier().getText(), new TileValue(i));
						TileValue returnValue = TileValue.VOID;
						try{
							returnValue = this.visit(ctx.block());
						}catch(BreakStatement br){
							break;
						}catch(ContinueStatement co){}
            if(returnValue != TileValue.VOID) {
                return returnValue;
            }
        }
        return TileValue.VOID;
    }


		@Override
		public TileValue visitForSTDStatment(TileParser.ForSTDStatmentContext ctx) {
			//System.out.println("FOR STD");
			createBlockScope = false;
			double start = this.visit(ctx.expression(0)).asDouble();
			scope.assign(ctx.Identifier().getText(), new TileValue(start));
			boolean done = !this.visit(ctx.expression(1)).asBoolean();;
			while(!done){
				TileValue returnValue = TileValue.VOID;
				try{
					returnValue = this.visit(ctx.block());
				}catch(BreakStatement br){
					break;
				}catch(ContinueStatement co){}
				if(returnValue != TileValue.VOID) {
						return returnValue;
				}
				start = this.visit(ctx.expression(2)).asDouble();
				//start = (double)Math.round(start * 1000000d) / 1000000d;
				scope.assign(ctx.expression(0).getText(), new TileValue(start));
				done = !this.visit(ctx.expression(1)).asBoolean();
			}
			return TileValue.VOID;
		}

		@Override
		public TileValue visitForEachStatment(TileParser.ForEachStatmentContext ctx) {
			createBlockScope = false;
			TileValue list = this.visit(ctx.expression());
			if(!list.isList()){throw new EvalException("Iterator must be a List: Type:" + list.getTypeName() + ":",ctx);}
			for(TileValue t : list.asList()){
				scope.assign(ctx.Identifier().getText(), t);
				TileValue returnValue = TileValue.VOID;
				try{
					returnValue = this.visit(ctx.block());
				}catch(BreakStatement br){
					break;
				}catch(ContinueStatement co){}
				if(returnValue != TileValue.VOID) {
						return returnValue;
				}
			}
			return TileValue.VOID;
		}

    // whileStatement
    // : While expression OBrace block CBrace
    // ;
    @Override
    public TileValue visitWhileStatement(TileParser.WhileStatementContext ctx) {
				createBlockScope = false;
        while( this.visit(ctx.expression()).asBoolean() ) {
					TileValue returnValue = TileValue.VOID;
					try{
						returnValue = this.visit(ctx.block());
					}catch(BreakStatement br){
						break;
					}catch(ContinueStatement co){}
          if (returnValue != TileValue.VOID) {
              return returnValue;
          }

						/*if (returnValue.isBreak()){
							break;
						}*/
        }
        return TileValue.VOID;
    }

		@Override
		public TileValue visitRunStatment(TileParser.RunStatmentContext ctx) {
			TileValue path = this.visit(ctx.expression());
			if(!path.isString() && !path.isList()){
				throw new EvalException("Run must receive a String or array of Strings as a path variable: Type:" + path.getTypeName() + ":",ctx);
			}

			List<TileValue> paths;

			if(path.isString()){
				paths = new ArrayList<TileValue>(1);
				paths.add(path);
			}else{
				paths = path.asList();
			}

			for(TileValue t : paths){
				RunFile r = new RunFile(t.asString());
				try {
						r.run();
				} catch (Exception e) {
						if ( e.getMessage() != null) {
								System.err.println(e.getMessage());
						} else {
								e.printStackTrace();
						}
				}
			}
			return TileValue.VOID;
		}

}
