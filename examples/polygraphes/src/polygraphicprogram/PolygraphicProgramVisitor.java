package polygraphicprogram;

public interface PolygraphicProgramVisitor {

	public polygraphicprogram.types.ThreePath visit_ThreePath(polygraphicprogram.types.ThreePath arg) throws tom.library.sl.VisitFailure;
	public polygraphicprogram.types.CellType visit_CellType(polygraphicprogram.types.CellType arg) throws tom.library.sl.VisitFailure;
	public polygraphicprogram.types.TwoPath visit_TwoPath(polygraphicprogram.types.TwoPath arg) throws tom.library.sl.VisitFailure;
	public polygraphicprogram.types.OnePath visit_OnePath(polygraphicprogram.types.OnePath arg) throws tom.library.sl.VisitFailure;

}
