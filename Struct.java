import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;

//import tl.antlr4.TileParser.ExpressionContext;

public class Struct {
	private String id;
    private List<String> keys;
		private List<TileValue> values;

    public Struct(String id, List<String> keys) {
    	this.id = id;
      this.keys = keys;
			values = new ArrayList<TileValue>(keys.size());
			for(int i=0; i<keys.size(); i++){
				values.add(TileValue.NULL);
			}
      //this.block = block;
    }

		public void write(String key, TileValue value){
			//System.out.println(key);
			//System.out.println(keys);
			int index = keys.indexOf(key);
			if(index == -1){
				throw new EvalException("There was an attempt to assign a struct variable that is not defined in the struct: Variable: " + key);
			}
			values.set(index,value);
			//System.out.println(values.get(index));
		}

		public TileValue pull(String key){
			int index = keys.indexOf(key);
			if(index == -1){
				throw new EvalException("There was an attempt to access a struct variable that is not defined in the struct: Variable: " + key);
			}
			return values.get(index);
		}
}
