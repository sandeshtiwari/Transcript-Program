/**
Name- Sandesh Tiwari
CSCI 4055 Programming assgnment
Dr.Lon Smith
Due - 11/25/2017
This program lets user to create a student transcript and get the details about the student's transcript using the StudentTranscript class
**/
public class Transcript
{
   public static void main(String []args)
   {
      //creating a new student with a given CWID
      StudentTranscript student = new StudentTranscript(60000000);
      //getting the transcript of the student
      student.getTranscript();
   }
}