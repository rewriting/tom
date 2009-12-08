public class ToolsEncode{
  public static String removeAccents(String s){
    s = s.replaceAll("[\u00E8\u00E9\u00EB]","e");

    s = s.replaceAll("[\u00FC]","ue");
    
    s = s.replaceAll(" ","");
    return s;
  }
}
