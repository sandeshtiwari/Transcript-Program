/**
Name- Sandesh Tiwari
CSCI 4055 Programming assgnment
Dr.Lon Smith
Due - 11/25/2017
This program lets user to calculate the transcript of a student with a given CWID from the UNIVERSITY database
**/
//importing the standard for SQL 
import java.sql.*;
public class StudentTranscript
{
   //instance variables
   private String name;
   private int CWID;
   //connection variable for the database
   private Connection con = null;
/**
Constructor to initialize the instance variables after establishing a connection to the database
@param CWID the CWID of the given student
**/
   public StudentTranscript(int CWID)
   {
      try
      {
         //loading the driver for the JDBC
         Class.forName("com.mysql.jdbc.Driver").newInstance();
         //establishing connection
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
      //catch if exception
      catch(Exception e)
      {
         System.err.println("Exception: " + e.getMessage());
      }
   }
   /**
   Method to return the name of the student
   @return name of the student
   **/
   public String getName()
   {
      return this.name;
   }
   /**
   Method to give the grades for all the semester studied by the student
   **/
   public void showSemGrades()
   {
      try 
      {
         //query string to get the semesters and the GPA for the respective semester
         String semQuery = "select round(sum(TAKEN.GRADE * SECTION_DETAILS.Hours)/sum(SECTION_DETAILS.Hours),2) as 'GPA', TAKEN.Semester, TAKEN.Year from TAKEN, SECTION_DETAILS where TAKEN.CWID ="+ this.CWID + " group by TAKEN.Year, TAKEN.Semester;";
         //query string to calculate the total number of semesters studied by the student
         String number = "select count(Semester) as num from (select Semester from TAKEN where CWID ="+this.CWID+" group by Year, Semester) as nested;";
         Statement stmt = con.createStatement();
         ResultSet result = stmt.executeQuery(number);
         if(result.next())
         {
            int countSem = result.getInt("num");
            result = stmt.executeQuery(semQuery);
            //looping for all the semsters
            for(int i = 0; i < countSem; i++)
            {
               if(result.next())
               {
                  String currentSem = result.getString("Semester");
                  String currentYear = result.getString("Year");
                  //getting grade for a particular semester
                  this.oneSemGrade(currentSem, currentYear);
                  //printing the GPA for a particular semester
                  System.out.println(currentSem+", "+currentYear+ "'s GPA -- "+result.getString("GPA")+"\n");
               }
            }
         }
      }
      //catch exception 
      catch(Exception e)
      {
         System.err.println("Exception: " + e.getMessage());
      }
   
   }
   /**
   Method to get grade of a given semester term and year
   @param sem the term for the semester
   @param year the given year
   **/
   public void oneSemGrade(String sem, String year)
   {
      try
      {
         System.out.println(sem+ ", "+year);
         //query string to get the classes taken in a particular semester
         String query = "select distinct SECTION_DETAILS.Title, TAKEN.Grade, SECTION_DETAILS.Prefix, SECTION_DETAILS.Number from TAKEN, SECTION_DETAILS"
            +" where TAKEN.CRN = SECTION_DETAILS.CRN and TAKEN.CWID ="+ this.CWID+" and TAKEN.Semester = '"+sem+"' and TAKEN.Year ="+year+" order by TAKEN.Year, TAKEN.Semester;";
         Statement statement = con.createStatement();
         ResultSet result = statement.executeQuery(query);
         //looping for all the classes in a particular semester
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
      //catch exception
      catch(Exception e)
      {
         System.err.println("Exception: "+ e.getMessage());
      }
   }
   /**
   Method to return the cumulative GPA of a student
   @return cumulative GPA 
   */
   public double getTotalGPA()
   {
      try
      {
         //query string to get the cumulative GPA for a particular student
         String query = "select round(sum(TAKEN.GRADE * SECTION_DETAILS.Hours)/sum(SECTION_DETAILS.Hours), 2) as 'GPA' from TAKEN, SECTION_DETAILS where TAKEN.CRN = SECTION_DETAILS.CRN and TAKEN.CWID = "+ this.CWID+";";
         Statement statement = con.createStatement();
         ResultSet result = statement.executeQuery(query);
         if(result.next())
         {
            return result.getDouble("GPA");
         }
         else
            return 0.0;
      }
      //catch exception
      catch(Exception e)
      {
         System.err.println("Exception: "+e.getMessage());
         return 0.0;
      }
   }
   /**
   Method to return the Major's GPA of a student
   @return major's GPA 
   */
   public double getMajorGPA()
   {
      try
      {
         String query = "select round(sum(TAKEN.GRADE * SECTION_DETAILS.Hours)/sum(SECTION_DETAILS.Hours), 2) as 'GPA'"
         +" from TAKEN, SECTION_DETAILS where TAKEN.CRN = SECTION_DETAILS.CRN and TAKEN.CWID = "+this.CWID+" and SECTION_DETAILS.Prefix = '"+this.getMajor()+"';";
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
   /**
   Method to return the Major of a student
   @return Major of a student
   **/
   public String getMajor()
   {
      try
      {
         //query string to get the major of a student
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
   /**
   Method to give the complete transcript of a student
   **/
   public void getTranscript()
   {
      System.out.println("\nName -- "+getName()+"\n");
      showSemGrades();
      System.out.println("Total GPA --"+getTotalGPA());
      System.out.println("Major GPA --"+getMajorGPA()+"\n");
   }

}