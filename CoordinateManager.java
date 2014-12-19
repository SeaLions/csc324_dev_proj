        
/*
 * This file is part of the AusStage Utilities Package
 *
 * The AusStage Utilities Package is free software: you can redistribute
 * it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * The AusStage Utilities Package is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the AusStage Utilities Package.  
 * If not, see <http://www.gnu.org/licenses/>.
*/

class CoordinateManager {

  // declare public constants
  
  /**
   * The minimum allowed latitude
   */
  public static float MIN_LATITUDE = Float.valueOf("-90.0000");
  
  /**
   * The maximum allowed latitude
   */
  public static float MAX_LATITUDE = Float.valueOf("90.0000");
  
  /**
   * The minimum allowed longitude
   */
  public static float MIN_LONGITUDE = Float.valueOf("-180.0000");
  
  /**
   * The maximum allowed longitude 
   */
  public static float MAX_LONGITUDE = Float.valueOf("180.0000");
  
  /**
   * The diameter of the Earth used in calculations
   */
  public static float EARTH_DIAMETER = Float.valueOf("12756.274");

  /**
   * A method to validate a latitude value
   *
   * @param latitude the latitude to check is valid
   *
   * @return         true if, and only if, the latitude is within the MIN and MAX latitude
   */
  public static boolean isValidLatitude(float latitude) {
    if(latitude >= MIN_LATITUDE && latitude <= MAX_LATITUDE) {
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * A method to validate a longitude value
   *
   * @param longitude the longitude to check is valid
   *
   * @return          true if, and only if, the longitude is between the MIN and MAX longitude
   */
  public static boolean isValidLongitude(float longitude) {
    if(longitude >= MIN_LONGITUDE && longitude <= MAX_LONGITUDE) {
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * A private method to calculate the latitude constant
   *
   * @return a double representing the latitude constant
   */
  public static double latitudeConstant() {
    return EARTH_DIAMETER * (Math.PI / Float.valueOf("360"));
    //return EARTH_DIAMETER * (Float.valueOf("3.14") / Float.valueOf("360"));
  }
  
  /**
   * A private method to caluclate the longitude constant
   *
   * @param latitude  a latitude coordinate in decimal notation
   *
   * @return a double representing the longitude constant
   */
  public static double longitudeConstant(float latitude) {
  
    //return Math.abs( Math.cos(Math.abs(latitude)));
    return EARTH_DIAMETER * Math.PI * Math.abs(Math.cos(Math.abs(latitude))) / Float.valueOf("360");
  
  }
  
  /**
   * A public method to calculate the distance between two lat/lon points
   * 
   * @param c1  a lat/lon
   * @param c2 a lat/lon
   * 
   * @return the great-circle distance between the two lat/lon points
   */
  public static double distance(Coordinate c1, Coordinate c2) {
      float lat1,lat2,lon1,lon2;
      lat1 = c1.getLatitude();
      lon1 = c1.getLongitude();
      lat2 = c2.getLatitude();
      lon2 = c2.getLongitude();
      double dLat = Math.toRadians(lat2-lat1);  
      double dLon = Math.toRadians(lon2-lon1);  
      double a = Math.sin(dLat/2) * Math.sin(dLat/2) +  
         Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);  
      double c = 2 * Math.asin(Math.sqrt(a));  
      return (double)EARTH_DIAMETER/2.0*1000.0 * c;  
  }
  
  /**
   * A method to add distance in a northerly direction to a coordinate
   *
   * @param latitude  a latitude coordinate in decimal notation
   * @param longitude a longitude coordinate in decimal notation
   * @param distance  the distance to add in metres
   *
   * @return          the new coordinate
   */
  public static Coordinate addDistanceNorth(float latitude, float longitude, int distance) {
  
    // check on the parameters
    if(isValidLatitude(latitude) == false || isValidLongitude(longitude) == false || distance <= 0) {
      throw new IllegalArgumentException("All parameters are required and must be valid");
    }
    
    // convert the distance from metres to kilometers
    float kilometers = distance / new Float(1000);  
    
    // calculate the new latitude
    double newLat = latitude + (kilometers / latitudeConstant());
    
    return new Coordinate(new Float(newLat).floatValue(), longitude);
  
  }
  
  /**
   * A method to add distance in a southerly direction to a coordinate
   *
   * @param latitude  a latitude coordinate in decimal notation
   * @param longitude a longitude coordinate in decimal notation
   * @param distance  the distance to add in metres
   *
   * @return          the new coordinate
   */
  public static Coordinate addDistanceSouth(float latitude, float longitude, int distance) {
  
    // check on the parameters
    if(isValidLatitude(latitude) == false || isValidLongitude(longitude) == false || distance <= 0) {
      throw new IllegalArgumentException("All parameters are required and must be valid");
    }
    
    // convert the distance from metres to kilometers
    float kilometers = distance / new Float(1000);
    
    // calculate the new latitude
    double newLat = latitude - (kilometers / latitudeConstant());
    
    return new Coordinate(new Float(newLat).floatValue(), longitude);
  
  }
  
  /**
   * A method to add distance in an easterly direction to a coordinate
   *
   * @param latitude  a latitude coordinate in decimal notation
   * @param longitude a longitude coordinate in decimal notation
   * @param distance  the distance to add in metres
   *
   * @return          the new coordinate
   */
  public static Coordinate addDistanceEast(float latitude, float longitude, int distance) {
  
    // check on the parameters
    if(isValidLatitude(latitude) == false || isValidLongitude(longitude) == false || distance <= 0) {
      throw new IllegalArgumentException("All parameters are required and must be valid");
    }
    
    // convert the distance from metres to kilometers
    float kilometers = distance / 1000;  
    
    // calculate the new longitude
    double newLng = longitude + (kilometers / longitudeConstant(latitude));
    
    return new Coordinate(latitude, new Float(newLng).floatValue());  
  }
  
  /**
   * A method to add distance in an westerly direction to a coordinate
   *
   * @param latitude  a latitude coordinate in decimal notation
   * @param longitude a longitude coordinate in decimal notation
   * @param distance  the distance to add in metres
   *
   * @return          the new coordinate
   */
  public static Coordinate addDistanceWest(float latitude, float longitude, int distance) {
  
    // check on the parameters
    if(isValidLatitude(latitude) == false || isValidLongitude(longitude) == false || distance <= 0) {
      throw new IllegalArgumentException("All parameters are required and must be valid");
    }
    
    // convert the distance from metres to kilometers
    float kilometers = distance / 1000;  
    
    // calculate the new longitude
    double newLng = longitude - (kilometers / longitudeConstant(latitude));
    
    return new Coordinate(latitude, new Float(newLng).floatValue());  
  }
  
  /**
   * A method to build four coordinates representing a bounding box given a start coordinate and a distance
   *
   * @param latitude  a latitude coordinate in decimal notation
   * @param longitude a longitude coordinate in decimal notation
   * @param distance  the distance to add in metres
   *
   * @return          a hashMap representing the bounding box (NE,SE,SW,NW)
   */
  public static java.util.HashMap<String, Coordinate> getBoundingBox(float latitude, float longitude, int distance) {
  
    // check on the parameters
    if(isValidLatitude(latitude) == false || isValidLongitude(longitude) == false || distance <= 0) {
      throw new IllegalArgumentException("All parameters are required and must be valid");
    }
    
    // convert the distance from metres to kilometers
    float kilometers = distance / 1000;  
    
    // declare helper variables
    java.util.HashMap<String, Coordinate> boundingBox = new java.util.HashMap<String, Coordinate>();
    
    // calculate the coordinates
    Coordinate north = addDistanceNorth(latitude, longitude, distance);
    Coordinate south = addDistanceSouth(latitude, longitude, distance);
    Coordinate east  = addDistanceEast(latitude, longitude, distance);
    Coordinate west  = addDistanceWest(latitude, longitude, distance);
    
    // build the bounding box object
    boundingBox.put("NE", new Coordinate(north.getLatitude(), east.getLongitude()));
    boundingBox.put("SE", new Coordinate(south.getLatitude(), east.getLongitude()));
    boundingBox.put("SW", new Coordinate(south.getLatitude(), west.getLongitude()));
    boundingBox.put("NW", new Coordinate(north.getLatitude(), west.getLongitude()));
    
    // return the bounding box object
    return boundingBox;  
  }
}