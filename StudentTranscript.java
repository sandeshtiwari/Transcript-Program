import java.sql.*;
public class StudentTranscript
{
   private String name;
   private int CWID;
   private Connection con = null;
   private Statement stmt;
   private ResultSet result;
   private String query;
   public StudentTranscript(int CWID)
   {
      try
      {
         Class.forName("com.mysql.jdbc.Driver").newInstance();
         con = DriverManager.getConnection("jdbc:mysql:///UNIVERSITY", "tiwaris1", "CSCI4055");
         query = "select Fname, Lname from PERSON, STUDENT where PERSON.CWID = STUDENT.CWID and PERSON.CWID ="+ CWID;
         stmt = con.createStatement();
         result = stmt.executeQuery(query);
         if(!result.next())
         {
            this.name = "NONE";
            this.CWID = 0;        
         }
         else
         {
            this.CWID = CWID;
            this.name = result.getString("Fname") + " " + result.getString("Lname");
 
         }
         /*try
         {
            if(con != null)
               con.close();
         } 
         catch(SQLException e) {}
   */
      }
      catch(Exception e)
      {
         System.err.println("Exception: " + e.getMessage());
      }
   }
   public String getName()
   {
      return this.name;
   }
}