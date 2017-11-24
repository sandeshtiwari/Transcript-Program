public class Transcript
{
   public static void main(String []args)
   {
      StudentTranscript student = new StudentTranscript(30054245);
      System.out.println(student.getName());
      student.showSemGrades();
      System.out.println(student.getTotalGPA());
      System.out.println(student.getMajorGPA());
   }
}