import java.sql.*;
public class StudentTranscript
{
   private String name;
   private int CWID;
   private Connection con = null;
   private Statement stmt;
   public StudentTranscript(int CWID)
   {
      try
      {
         Class.forName("com.mysql.jdbc.Driver").newInstance();
         con = DriverManager.getConnection("jdbc:mysql:///UNIVERSITY", "tiwaris1", "CSCI4055");
         String query = "select Fname, Lname from PERSON, STUDENT where PERSON.CWID = STUDENT.CWID and PERSON.CWID ="+ CWID;
         stmt = con.createStatement();
         ResultSet result = stmt.executeQuery(query);
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
   public void showSemGrades()
   {
      try 
      {
         String semQuery = "select round(avg(GRADE),2) as 'GPA', Semester, Year from TAKEN where CWID ="+ this.CWID + " group by Year, Semester;";
         String number = "select count(Semester) as num from (select Semester from TAKEN where CWID ="+this.CWID+" group by Year, Semester) as nested;";
         ResultSet result = stmt.executeQuery(number);
         int countSem = result.getInt("num");
         result = stmt.executeQuery(semQuery);
         for(int i = 0; i < countSem; i++)
         {
            if(result.next())
            {
               String currentSem = result.getString("Semester");
               String currentYear = result.getString("Year");
               this.oneSemGrade(currentSem, currentYear);
            }
         }
      }
      catch(Exception e)
      {
         System.err.println("Exception: " + e.getMessage());
      }
   
   }
}