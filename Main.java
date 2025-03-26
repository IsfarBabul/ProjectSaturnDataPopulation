public class Main {
    public static void main(String[] args) {

        //Populate teachers table
        for (int i = 1; i <= 200; i++) {
            //TODO: FILL THIS WITH REAL TEACHERS AND THEIR DEPARTMENTS
            System.out.println("INSERT INTO Teachers ( teacher_id, first_name, last_name, department_id ) VALUES ( " + i + ", 'FirstTeacher" + i + ", 'LastTeacher" + i + "', " + i + " );");
        }


        for (int i = 1; i <= 5000; i++) {
            System.out.println("INSERT INTO Students ( student_id, first_name, last_name ) VALUES ( " + i + ", 'FirstStudent" + i + ", 'LastStudent" + i + "' );");
        }

        int assignmentCount = 1;
        for (int i = 1; i <= 5; i++) {      //number of course offerings; 5 is an example
            for (int j = 1; j <= 3; j++) {    //three assignment types
                System.out.println("INSERT INTO Assignments ( assignment_id, assignment_name, assignment_type_id, course_offering_id ) VALUES ( " + (assignmentCount) + ", 'Assignment" + (assignmentCount) + "', " + j + "', " + i + " );");
                assignmentCount++;
            }
        }

        for (int i = 1; i <= 5000; i++) { //each student gets assignments
            for (int j = 0; j < 15; j++) {     //number of assignments
                System.out.println("INSERT INTO AssignmentGrade ( student_id integer, grade integer, assignment_id integer ) VALUES ( " + i + ", 'Assignment" + (assignmentCount) + "', " + j + "', " + i + " );");


            }
        }

    }
}
