import java.sql.*;
public class StudentTranscript
{
   private String name;
   private int CWID;
   private Connection con = null;
   public StudentTranscript(int CWID)
   {
      try
      {
         Class.forName("com.mysql.jdbc.Driver").newInstance();
         con = DriverManager.getConnection("jdbc:mysql:///UNIVERSITY", "tiwaris1", "CSCI4055");
         String query = "select Fname, Lname from PERSON, STUDENT where PERSON.CWID = STUDENT.CWID and PERSON.CWID ="+ CWID;
         Statement stmt = con.createStatement();
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
         stmt.close();
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
         String semQuery = "select round(avg(GRADE),2) as GPA, Semester, Year from TAKEN where CWID ="+ this.CWID + " group by Year, Semester;";
         String number = "select count(Semester) as num from (select Semester from TAKEN where CWID ="+this.CWID+" group by Year, Semester) as nested;";
         Statement stmt = con.createStatement();
         ResultSet result = stmt.executeQuery(number);
         if(result.next())
         {
            int countSem = result.getInt("num");
            result = stmt.executeQuery(semQuery);
            for(int i = 0; i < countSem; i++)
            {
               if(result.next())
               {
                  String currentSem = result.getString("Semester");
                  String currentYear = result.getString("Year");
                  this.oneSemGrade(currentSem, currentYear);
                  System.out.println(currentSem+", "+currentYear+ "'s GPA -- "+result.getString("GPA")+"\n");
               }
            }
         
         }
      }
      catch(Exception e)
      {
         System.err.println("Exception: " + e.getMessage());
      }
   
   }
   public void oneSemGrade(String sem, String year)
   {
      try
      {
         System.out.println(sem+ ", "+year);
         String query = "select distinct SECTION_DETAILS.Title, TAKEN.Grade, SECTION_DETAILS.Prefix, SECTION_DETAILS.Number from TAKEN, SECTION_DETAILS"
            +" where TAKEN.CRN = SECTION_DETAILS.CRN and TAKEN.CWID ="+ this.CWID+" and TAKEN.Semester = '"+sem+"' and TAKEN.Year ="+year+" order by TAKEN.Year, TAKEN.Semester;";
         Statement statement = con.createStatement();
         ResultSet result = statement.executeQuery(query);
         while(result.next())
         {
            String title = result.getString("Title");
            String prefix = result.getString("Prefix");
            int number = result.getInt("Number");
            int grade = result.getInt("Grade");
            String letterGrade= "";
            if(grade == 4)
               letterGrade = "A";
            else if(grade == 3)
               letterGrade = "B";
            else if(grade == 2)
               letterGrade = "C";
            else if(grade < 2)
               letterGrade = "F";
            System.out.println(prefix+" "+ number +" "+title+ " -- "+ letterGrade);   
         }
         result.close();
      }
      catch(Exception e)
      {
         System.err.println("Exception: "+ e.getMessage());
      }
   }
   public double getTotalGPA()
   {
      try
      {
         String query = "select round(avg(GRADE),2) as 'GPA' from TAKEN where CWID = "+ this.CWID+";";
         Statement statement = con.createStatement();
         ResultSet result = statement.executeQuery(query);
         if(result.next())
         {
            return result.getDouble("GPA");
         }
         else
            return 0.0;
      }
      catch(Exception e)
      {
         System.err.println("Exception: "+e.getMessage());
         return 0.0;
      }
   }
   public double getMajorGPA()
   {
      try
      {
         String query = "select round(avg(TAKEN.Grade),2) as 'GPA' from TAKEN, SECTION_DETAILS where TAKEN.CRN = SECTION_DETAILS.CRN and TAKEN.CWID = "+this.CWID+" and SECTION_DETAILS.Prefix = '"+this.getMajor()+"';";
         Statement statement = con.createStatement();
         ResultSet result = statement.executeQuery(query);
         if(result.next())
         {
            return result.getDouble("GPA");
         }
         else
            return 0.0;
      }
      catch(Exception e)
      {
         System.err.println("Exception: here"+e.getMessage());
         return 0.0;
      }
   
   }
   public String getMajor()
   {
      try
      {
         String query = "select MjrCode from MAJOR where CWID = "+this.CWID+";";
         Statement statement = con.createStatement();
         ResultSet result = statement.executeQuery(query);
         if(result.next())
         {
            return result.getString("MjrCode");
         }
         else
            return "Not a Student or not valid CWID";
      }
      catch(Exception e)
      {
         System.err.println("Exception: "+e.getMessage());
         return "Not a Student or not valid CWID";
      }
   }
   public void getTranscript()
   {
      System.out.println("\nName -- "+getName()+"\n");
      showSemGrades();
      System.out.println("Total GPA --"+getTotalGPA());
      System.out.println("Major GPA --"+getMajorGPA()+"\n");
   }

}