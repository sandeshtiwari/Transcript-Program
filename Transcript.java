public class Transcript
{
   public static void main(String []args)
   {
      StudentTranscript student = new StudentTranscript(60000000);
      System.out.println(student.getName());
      student.showSemGrades();
   }
}