import java.util.List;

public class TileValue implements Comparable<TileValue> {

    public static final TileValue NULL = new TileValue();
    public static final TileValue VOID = new TileValue();
    public static final TileValue EMPTY_RETURN = new TileValue();
    //public static final TileValue BREAK = new TileValue(true);

    private Object value;
    //private boolean breakVal = false;
    private boolean clean = false;

    public static boolean cleanNextNumber = false;

    private TileValue() {
        // private constructor: only used for NULL and VOID
        value = new Object();
    }

    /*private TileValue(boolean breakVal) {
        // private constructor: only used for NULL and VOID
        value = new Object();
        this.breakVal=breakVal;
    }

    public boolean isBreak(){
      return breakVal;
    }*/

    public TileValue(Object v) {
        if(v == null) {
            throw new RuntimeException("v == null");
        }
        value = v;
        // only accept boolean, list, number or string types
        if(!(isBoolean() || isList() || isNumber() || isString() || isFunction())) {
            throw new RuntimeException("invalid data type: " + v + " (" + v.getClass() + ")");
        }
    }

    public void cleanNext() {
      clean = true;
    }

    public Boolean asBoolean() {
      clean = false;
      return (Boolean)value;
    }

    public Double asDouble() {
      if(clean || TileValue.cleanNextNumber == true){
        double val = ((Number)value).doubleValue();
        val = (double)Math.round(val * 1000000d) / 1000000d;
        clean = false;
        TileValue.cleanNextNumber = false;
        return val;
      }
      clean = false;
      return ((Number)value).doubleValue();
    }

    public Long asLong() {
      clean = false;
      return ((Number)value).longValue();
    }

    @SuppressWarnings("unchecked")
    public List<TileValue> asList() {
      clean = false;
      return (List<TileValue>)value;
    }

    public String asString() {
      clean = false;
      return (String)value;
    }

    public FunctionPointer asFunction() {
      clean = false;
      return (FunctionPointer)value;
    }

    //@Override
    public int compareTo(TileValue that) {
        if(this.isNumber() && that.isNumber()) {
            if(this.equals(that)) {
                return 0;
            }
            else {
                return this.asDouble().compareTo(that.asDouble());
            }
        }
        else if(this.isString() && that.isString()) {
            return this.asString().compareTo(that.asString());
        }
        else {
            throw new RuntimeException("illegal expression: can't compare `" + this + "` to `" + that + "`");
        }
    }

    @Override
    public boolean equals(Object o) {
        if(this == VOID || o == VOID) {
            throw new RuntimeException("can't use VOID: " + this + " ==/!= " + o);
        }
        if(this == o) {
            return true;
        }
        if(o == null || this.getClass() != o.getClass()) {
            return false;
        }
        TileValue that = (TileValue)o;
        if(this.isNumber() && that.isNumber()) {
            double diff = Math.abs(this.asDouble() - that.asDouble());
            return diff < 0.00000000001;
        }
        else {
            return this.value.equals(that.value);
        }
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    public boolean isNumber() {
        return value instanceof Number;
    }

    public boolean isList() {
        return value instanceof List<?>;
    }

    public boolean isNull() {
        return this == NULL;
    }

    public boolean isVoid() {
        return this == VOID;
    }

    public boolean isString() {
        return value instanceof String;
    }

    public boolean isFunction() {
        return value instanceof FunctionPointer;
    }

    public Type getType(){
      if (value instanceof Boolean){
        return Type.BOOL;
      }if (value instanceof Number){
        return Type.NUMBER;
      }if (value instanceof List<?>){
        return Type.LIST;
      }if (value instanceof String){
        return Type.STRING;
      }if (value instanceof FunctionPointer){
        return Type.FUNCTION;
      }if (this == NULL){
        return Type.NULL;
      }else{
        return Type.VOID;
      }
    }
    //public enum Type { STRING, NUMBER, LIST, NULL, STRUCT, ANY, STRING_NUMBER, BOOL};


    public String getTypeName(){
      if (value instanceof Boolean){
        return "bool";
      }if (value instanceof Number){
        return "number";
      }if (value instanceof List<?>){
        return "list";
      }if (value instanceof String){
        return "string";
      }if (value instanceof FunctionPointer){
        return "funct";
      }if (this == NULL){
        return "null";
      }else{
        return "void";
      }
    }

    @Override
    public String toString() {
        return isNull() ? "null" : isVoid() ? "void" : String.valueOf(value);
    }
}
