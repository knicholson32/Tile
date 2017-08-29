public class Main {
    public static void main(String[] args) {
        for(String s : args){
          RunFile r = new RunFile(s);
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
    }
}
