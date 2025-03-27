import java.util.Random;

public class Main {
    public static void main(String[] args) {

        //Populate teachers table
        for (int i = 1; i <= 200; i++) {
            //TODO: FILL THIS WITH REAL TEACHERS AND THEIR DEPARTMENTS
            System.out.println("INSERT INTO Teachers ( teacher_id, name, department_id ) VALUES ( " + i + ", 'Teacher" + i + "', " + i + " );");
        }

        //Populate students table
        for (int i = 1; i <= 5000; i++) {
            System.out.println("INSERT INTO Students ( student_id, name ) VALUES ( " + i + ", 'Student" + i + "' );");
        }

        //Populate assignments table
        int assignmentCount = 1;
        for (int i = 1; i <= 5; i++) {      //number of course offerings; 5 is an example
            for (int j = 1; j <= 12; j++) {    //three assignment types
                System.out.println("INSERT INTO Assignments ( assignment_id, assignment_name, assignment_type_id, course_offering_id ) VALUES ( " + (assignmentCount) + ", 'MinorAssignment" + (assignmentCount) + "', " + 1 + ", " + i + " );");
                assignmentCount++;
            }
            for (int j = 1; j <= 3; j++) {    //three assignment types
                System.out.println("INSERT INTO Assignments ( assignment_id, assignment_name, assignment_type_id, course_offering_id ) VALUES ( " + (assignmentCount) + ", 'MajorAssignment" + (assignmentCount) + "', " + 2 + ", " + i + " );");
                assignmentCount++;
            }
            assignmentCount++;
        }

        

        //Populate assignments grade table
        /*for (int i = 1; i <= 5000; i++) { //each student gets assignments
            for (int j = 0; j < 15; j++) {     //number of assignments
                System.out.println("INSERT INTO AssignmentGrade ( student_id, grade, assignment_id ) VALUES ( " + i + ", '" + (int) ((Math.random() * 26) + 75) + "', " + j + "', " + i + " );");
            }
        }*/

        //Populate assignment type table (may not be needed)
        System.out.println("INSERT INTO AssignmentType ( assignment_type_id, assignment_type_name ) VALUES ( 1, 'minor' )");
        System.out.println("INSERT INTO AssignmentType ( assignment_type_id, assignment_type_name ) VALUES ( 2, 'major' )");



    }
}
