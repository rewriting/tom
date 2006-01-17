public class ToolsEncode{
  public String removeAccents(String s){
    s = s.replaceAll("[éèêë]","e");
    s = s.replaceAll("[ûù]","u");
    s = s.replaceAll("[ïî]","i");
    s = s.replaceAll("[àâ]","a");
    s = s.replaceAll("[Ô]","o");

    s = s.replaceAll("[ü]","ue");
    s = s.replaceAll("[ä]","ae");
    s = s.replaceAll("[ö]","oe");
    
    s = s.replaceAll("[ÈÉÊË]","E");
    s = s.replaceAll("[ÛÙ]","U");
    s = s.replaceAll("[ÏÎ]","I");
    s = s.replaceAll("[ÀÂ]","A");
    s = s.replaceAll("[Ô]","O");

    s = s.replaceAll(" ","");
    return s;
  }
}
