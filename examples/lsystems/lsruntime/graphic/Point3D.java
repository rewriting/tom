package lsruntime.graphic;

import java.awt.geom.Point2D;

public class Point3D extends Point2D.Double {
  
  public double direction;
  
  public Point3D(double x, double y, double direction) {
    super(x,y);
    this.direction = direction;
  }
  
  public Point3D(double x, double y) {
    super(x,y);
    this.direction = 0.0;
  }
  
  public Point3D() {
    super();
    this.direction = 0.0;
  }
  
  public Point3D(Point3D p) {
    super(p.getX(),p.getY());
    this.direction = p.getDirection();
  }
  
  public double getDirection() {
    return direction;
  }
  
  public void setDirection(double dir) {
    this.direction = dir;
  }
  
  public void setLocation(Point3D p) {
    this.setLocation((Point2D) p);
    this.direction = p.getDirection();
  }
  
  public String toString() {
    String buffer = super.toString() + "dir:"+getDirection();
    return buffer;
  }
  
  public void avance(double longueur) {
    double new_x = getX() + longueur * Math.cos( Math.toRadians(getDirection()) );
    double new_y = getY() + longueur * Math.sin( Math.toRadians(getDirection()) );
    setLocation(new_x,new_y);
  }
  
  public void tourne(double angle) {
    direction += angle;
  }
}
