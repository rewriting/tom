import polygraphicprogram.*;
import polygraphicprogram.types.*;


public class PolygraphicProgram{

%include { polygraphicprogram/PolygraphicProgram.tom }


public static void main(String[] args) {
	//OnePath z=`OneC0(OneCell("int"),OneCell("String"),OneC0(OneCell("String"),OneCell("int"),OneC0(OneCell("String"),OneCell("int"))),OneId(),OneCell("end"));
	TwoPath z=`TwoC0(TwoId(OneId()),TwoC0(TwoId(OneId())));//-->stackoverflow error
	//TwoPath z=TwoC0(TwoCell("zero",OneId(),OneCell("int"),"cons"));
System.out.println(z);
}


}