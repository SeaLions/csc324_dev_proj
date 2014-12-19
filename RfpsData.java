import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.Vector;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class RfpsData extends PlotData
{
   
   private ArrayList<Double> bearingDegrees;
   private ArrayList<ArrayList<CoordinateLine>> coverage;
   private Coordinate center;
   private int computationalRangeInMeters;
   
   public RfpsData()
   {
      super();
      bearingDegrees = new ArrayList<Double>();
      coverage = new ArrayList<ArrayList<CoordinateLine>>();
      center = new Coordinate(0,0);
      computationalRangeInMeters = 0;
   }
   
   public int getComputationalRangeInMeters() { return computationalRangeInMeters; }
   
   public Coordinate getCenter() { return new Coordinate(center.getLatitude(), center.getLongitude()); }
   
   private void readComputationalArea(Node compAreaFolder) {
      
      String centerAndRange = null;
      
      NodeList compAreaFolderChildren = compAreaFolder.getChildNodes();
      for (int i = 0; i < compAreaFolderChildren.getLength(); ++i) {
         Node curNode = compAreaFolderChildren.item(i);
         if (curNode.getNodeName() != null && curNode.getNodeName().equals("description")) {
            centerAndRange = curNode.getTextContent();
            break;
         }
      }
      
      StringTokenizer tokenizer = new StringTokenizer(centerAndRange);
      
      //throw away "Center:"
      tokenizer.nextToken();
      
      //get longitude
      String centerLonString = tokenizer.nextToken();
      char dirChar = centerLonString.charAt(centerLonString.length()-1);
      centerLonString = centerLonString.substring(0, centerLonString.length()-1);
      if (dirChar == 'W')
         centerLonString = "-" + centerLonString;
      center.setLongitude(Float.parseFloat(centerLonString));
      
      //throw away "-"
      tokenizer.nextToken();
      
      String centerLatString = tokenizer.nextToken();
      dirChar = centerLatString.charAt(centerLatString.length()-1);
      centerLatString = centerLatString.substring(0, centerLatString.length()-1);
      if (dirChar == 'S')
         centerLatString = "-" + centerLatString;
      center.setLatitude(Float.parseFloat(centerLatString));
      
      //throw away "Range" and "Extent:"
      tokenizer.nextToken();
      tokenizer.nextToken();
      
      computationalRangeInMeters = (int)Math.floor(Float.parseFloat(tokenizer.nextToken())*1000);
      
   }
   
   private void readCoverage(Node coverageFolder) {
      //get folder containing bearing information
      NodeList coverageFolderChildren = coverageFolder.getChildNodes();
      Node topFolder = null;
      for (int i = 0; i < coverageFolderChildren.getLength(); ++i) {
         Node curNode = coverageFolderChildren.item(i);
         if (curNode.getNodeName() != null && curNode.getNodeName().equals("Folder")) {
            topFolder = curNode;
            break;
         }
      }
      
      //each subfolder is a bearing; for each subfolder, read a bearing
      NodeList bearings = topFolder.getChildNodes();
      for (int i = 0; i < bearings.getLength(); ++i) {
         Node bearing = bearings.item(i);
         if (bearing.getNodeName() != null && bearing.getNodeName().equals("Folder"))
            addBearing(bearing);
      }
      
   }
   
   private void addBearing(Node bearingFolder) {
      //get name tag with bearing information
      Node bearingInfoNode = null;
      NodeList bearingFolderChildren = bearingFolder.getChildNodes();
      for (int i = 0; i < bearingFolderChildren.getLength(); ++i) {
         Node curNode = bearingFolderChildren.item(i);
         if (curNode.getNodeName() != null && curNode.getNodeName().equals("name")) {
            bearingInfoNode = curNode;
            break;
         }
      }
      
      bearingDegrees.add(getBearingDegrees(bearingInfoNode));
      
      coverage.add(new ArrayList<CoordinateLine>());
      Node coordinateLineNode = bearingInfoNode.getNextSibling();
      while (coordinateLineNode != null) {
         if (coordinateLineNode.getNodeName() != null && coordinateLineNode.getNodeName().equals("Placemark")) {
            coverage.get(coverage.size() - 1).add(getCoordinateLine(coordinateLineNode));
         }
         coordinateLineNode = coordinateLineNode.getNextSibling();
         
      }
      
   }
   
   private double getBearingDegrees(Node bearingInfoNode) {
      String bearingInfoString = bearingInfoNode.getTextContent();
      StringTokenizer tokenizer = new StringTokenizer(bearingInfoString);
      
      //throw away "Bearing" and "-"
      tokenizer.nextToken();
      tokenizer.nextToken();
      
      return Double.parseDouble(tokenizer.nextToken());
   }
   
   private CoordinateLine getCoordinateLine(Node coordinateLineNode) {
      Node lineString = null, coordinates = null;
      Node curNode = coordinateLineNode.getFirstChild();
      while (curNode != null) {
         if (curNode.getNodeName() != null && curNode.getNodeName().equals("LineString")) {
            lineString = curNode;
            break;
         }
         curNode = curNode.getNextSibling();
      }
      
      String coordinatesTextContent = null;
      curNode = lineString.getFirstChild();
      while (curNode != null) {
         if (curNode.getNodeName() != null && curNode.getNodeName().equals("coordinates")) {
            coordinatesTextContent = curNode.getTextContent();
         }
         curNode = curNode.getNextSibling();
      }
      
      String[] tokens = coordinatesTextContent.split(",| ");
      Coordinate c1 = new Coordinate(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[0]));//tokens[1]=latitude
      Coordinate c2 = new Coordinate(Float.parseFloat(tokens[4]), Float.parseFloat(tokens[3]));
      
      return new CoordinateLine(c1,c2);
      
   }
   
   public void readData(File rfpsFile)
   {
      
		try
		{
         
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         DocumentBuilder db = dbf.newDocumentBuilder(); 
         Document dom = db.parse(rfpsFile);
         
         //get coverage and computational area nodes
         Node coverageFolder = null, computationalAreaFolder = null;
         NodeList nameNodes = dom.getElementsByTagName("name");
         for (int i = 0; i < nameNodes.getLength(); ++i) {
            Node curNameNode = nameNodes.item(i);
            switch(curNameNode.getTextContent()) {
            case "Coverage":
               coverageFolder = curNameNode.getParentNode();
               break;
            case "Computational Area":
               computationalAreaFolder = curNameNode.getParentNode();
               break;
            }
            if (coverageFolder != null && computationalAreaFolder != null)
               break;
         }
         
         readComputationalArea(computationalAreaFolder);
         
         readCoverage(coverageFolder);
         
//          printData();
         
      }//End try
      catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   //method to compare coverageNearPoint
   public boolean isCoverageNear(Coordinate coord, int distanceMeters)
   {
      double distance = CoordinateManager.distance(center, coord);
      
      if (distance <= distanceMeters) {
         int maxDist = distanceMeters + (int)distance;
         for (int i = 0; i < coverage.size(); ++i) {
            CoordinateLine firstLine = coverage.get(i).get(0);
            if (CoordinateManager.distance(firstLine.getCoordinate(0), coord) <= distanceMeters)
               return true;
         }
      }
      else {
         double bearing = CoordinateManager.bearing(center, coord);
         int closestBearingIndex = -1;
         int max = bearingDegrees.size(), min = 0;
         while (max - min > 0) {
            int mid = (max+min)/2;
            double deg = bearingDegrees.get(mid);
            if (deg < bearing)
               min = mid+1;
            else max = mid-1;
         }
         closestBearingIndex = min;
         
         //search the closest three bearings (change later)
         int searchBearing = closestBearingIndex;
         searchBearing += (searchBearing < 0 ? coverage.size() : 0);
         while (searchBearing <= closestBearingIndex+1) {
            ArrayList<CoordinateLine> curBearing = coverage.get(searchBearing);
            for (int i = 0; i < curBearing.size(); ++i) {
               CoordinateLine curLine = curBearing.get(i);
               double d1 = CoordinateManager.distance(curLine.getCoordinate(0), center),
                      d2 = CoordinateManager.distance(curLine.getCoordinate(1), center);
               if ((d1 <= distance && d2 <= distance) || (d1 >= distance && d2 >= distance)) {
                  if (CoordinateManager.distance(curLine.getCoordinate(0), coord) <= distanceMeters
                      || CoordinateManager.distance(curLine.getCoordinate(1), coord) <= distanceMeters)
                      return true;
               }
               else if (d1 <= distance && d2 >= distance || d1 >= distance && d1 <= distance) {
                  return true;
               }
            }
            ++searchBearing;
         }
      }
      
      return true;
   }
   
   public boolean inRange(Coordinate coord) {
      return CoordinateManager.distance(coord, center) <= (double)computationalRangeInMeters;
   }
   
   public void printData() {
      System.out.println("Range: " + computationalRangeInMeters + " (m)");
      System.out.println("Center: " + center.toString());
      System.out.println("Bearing Count: " + bearingDegrees.size());
      for (int i = 0; i < bearingDegrees.size(); ++i) {
         System.out.print(bearingDegrees.get(i) + ",");
      }
      System.out.println();
      
      for (int bearing = 0; bearing < coverage.size(); ++bearing) {
         System.out.println("Bearing " + bearingDegrees.get(bearing) + ": ");
         for (int line = 0; line < coverage.get(bearing).size(); ++line) {
            CoordinateLine cl = coverage.get(bearing).get(line);
            System.out.print(cl.getCoordinate(0).toString() + "->" + cl.getCoordinate(1).toString() + ", ");
         }
         System.out.println();
      }
   }
   
}//End class
