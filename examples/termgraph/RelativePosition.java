package termgraph; 

import tom.library.strategy.mutraveler.Position;

/**
 * Object that represents a relative position in a term
 * the first element in the list represents the number of backtracking
 * steps and the others classicals steps in the term
 */

public class RelativePosition extends Position {

  public RelativePosition(int[] data) {
    super(data);
  }


  public static RelativePosition constructRelativePosition(Position source,Position target) {
    int[] sourceData = source.toArray();
    int[] targetData = target.toArray();
    int min_length =Math.min(sourceData.length,targetData.length); 
    int i=0;
    while(i<min_length && sourceData[i]==targetData[i]){
      i++;
    }
    int[] relativeData = new int[targetData.length-i+1];
    relativeData[0]=sourceData.length-i;
    for(int j=1;j<relativeData.length;j++){
      relativeData[j] = targetData[i+j-1];
    }
    return new RelativePosition(relativeData);
  }



  public Position getAbsolutePosition(Position currentPosition){
    int[] currentData = currentPosition.toArray();
    int prefix = currentPosition.depth()-data[0];
    int absoluteDataLength = prefix+depth()-1;
    int[] absoluteData = new int[absoluteDataLength];
    for(int i =0; i<prefix;i++){
      absoluteData[i]=currentData[i];
    }
    for(int i =prefix; i<absoluteDataLength;i++){
      absoluteData[i]=data[i-prefix+1];
    }
    Position absolutePos = new Position(absoluteData);
    return absolutePos;
  }

}
