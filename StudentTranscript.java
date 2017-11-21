import java.sql.*;
public class StudentTranscript
{
   private String name;
   private int CWID;
   private Connection con = null;
   private Statement stmt;
   private ResultSet result;
   private String query;
   public StudentTranscript(String name, int CWID)
   {
      this.name = name;
      this.CWID = CWID;
      try
      {
         Class.forName("com.mysql.jdbc.Driver").newInstance();
         con = DriverManager.getConnection("jdbc:mysql:///UNIVERSITY", "tiwaris1", "CSCI4055");
      }
      catch(Exception e)
      {
         System.err.println("Exception: " + e.getMessage());
      }
   }
}