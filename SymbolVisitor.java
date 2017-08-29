import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class SymbolVisitor extends TileBaseVisitor<TileValue> {
    //Map<String, Function> functions;
    Map<String, Struct> structs;
    ArrayList<FunctionPointer> functionPointers = new ArrayList<FunctionPointer>();

    public SymbolVisitor(ArrayList<FunctionPointer> functionPointers, Map<String, Struct> structs) {
        this.functionPointers = functionPointers;
        this.structs = structs;
    }

    // Def Identifier '(' idList? ')' Start block End
    @Override
    public TileValue visitFunctionDecl(TileParser.FunctionDeclContext ctx) {
      List<TerminalNode> params = ctx.idList() != null ? ctx.idList().Identifier() : new ArrayList<TerminalNode>();
      List<TileParser.TypeContext> types  = ctx.idList() != null ? ctx.idList().type() : new ArrayList<TileParser.TypeContext>();
      ParseTree block = ctx.block();
      String id = ctx.Identifier().getText();// + params.size();
      //functions.put(id, new Function(id, params, block));
      integrateFunction(id, new Function(block, params, types));
      return TileValue.VOID;
    }

    public void visitMasterFunctions(){
      for(int i=0; i<MasterFunction.functionNames.length; i++){
        String externalID = MasterFunction.functionNames[i];
        List<Type> types = new ArrayList<Type>(Arrays.asList(MasterFunction.functionParams[i]));
        MasterFunction function = new MasterFunction(externalID, types, MasterFunction.functionIntNames[i]);
        //functions.put(s, f);
        integrateFunction(externalID, function);
      }
      //return TileValue.VOID;
    }

    @Override
    public TileValue visitStructDecl(TileParser.StructDeclContext ctx) {

      List<TerminalNode> params = ctx.idList() != null ? ctx.idList().Identifier() : new ArrayList<TerminalNode>();

      List<String> keys = new ArrayList<String>(params.size());
      for(TerminalNode p : params){
        keys.add(p.getText());
      }

      String id = ctx.Identifier().getText();
      structs.put(id, new Struct(id, keys));

      return TileValue.VOID;
    }

    public void integrateFunction(String id, Function funct){
      for(FunctionPointer f : functionPointers){
        if(id.equals(f.getID())){
          f.addFunctionReferece(funct);
          return;
        }
      }
      FunctionPointer newPointer = new FunctionPointer(id);
      newPointer.addFunctionReferece(funct);
      functionPointers.add(newPointer);
    }
}
