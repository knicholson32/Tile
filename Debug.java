public class Debug {
  public static boolean VERBOSE = true;
  private static String INTRO = "";
  private static int INTROAMT = 0;
  public static int level = -1;

  public static void println(String s, int inputLevel){
    if(VERBOSE && (level == -1 || level == inputLevel) || inputLevel == -2)
      System.out.println(INTRO + getColor('r') + s + getColor(' '));
  }

  public static void state(String intro, String message, char color, int inputLevel){
    if(VERBOSE && (level == -1 || level == inputLevel) || inputLevel == -2)
      System.out.println(INTRO + getColor('r') + intro + " " + getColor(color) + message + getColor(' '));
  }

  public static boolean canDebug(int l){
    return level == -1 || l == level || l == -2;
  }

  public static void println(int inputLevel){
    println("", inputLevel);
  }

  public static void println(String s){
    println(s, -1);
  }

  public static void println(){
    println("", -1);
  }

  public static void indent(){
    INTROAMT+=2;
    refreshIndent();
  }
  public static void detent(){
    INTROAMT-=2;
    refreshIndent();
  }

  private static void refreshIndent(){
    INTRO = "";
    for(int i = 0; i < INTROAMT; i++){
      INTRO += " ";
    }
  }

  public static String getColor(char in){
    String color;
    switch(in){
      case 'w':
          color="\u001B[37m";
      break;
      case 'c':
          color="\u001B[0;36m";
      break;
      case 's':
          color="\u001B[37;2m";
      break;
      case 'r':
          color="\u001B[31;1m";
      break;
      case 'g':
          color="\u001B[42m";
      break;
      case 'l':
          color="\u001B[32m";
      break;
      case 'h':
          color="\u001B[32m";
      break;
      case 'q':
          color="\u001B[41;1m";
      break;
      case 'b':
          color="\u001B[34m";
      break;
      case 'y':
          color="\u001B[33m";
      break;
      default:
          color="\033[m";
      break;
    }
    return color;
  }
}
