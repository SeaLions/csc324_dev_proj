public class CoordinateLine {
   
   private Coordinate[] points;
   
   public CoordinateLine(Coordinate c1, Coordinate c2) {
      
      points = new Coordinate[2];
      
      points[0] = new Coordinate(c1.getLatitude(), c1.getLongitude());
      points[1] = new Coordinate(c2.getLatitude(), c2.getLongitude());
      
   }
   
   public Coordinate getCoordinate(int i) {
      Coordinate temp = (i >= 1 ? points[1] : points[0]);
      return new Coordinate(temp.getLatitude(), temp.getLongitude());
   }
}
