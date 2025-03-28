import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        //Populate teachers table
        for (int i = 1; i <= 200; i++) {
            //TODO: FILL THIS WITH REAL TEACHERS AND THEIR DEPARTMENTS
            System.out.println("INSERT INTO Teachers ( teacher_id, name, department_id ) VALUES ( " + i + ", 'Teacher" + i + "', " + i + " );");
        }

        //Populate teacher schedule table

        for (int i = 0; i < 200; i++) {    //200 teachers
            for (int j = 1; j <= 10; j++) {   //each have 10 courses
                System.out.println("INSERT INTO TeacherSchedule ( teacher_id, roster_id ) VALUES ( " + i + " )"); //TODO: ROSTER ID REQUIRED
            }
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

        //Populate courses table




        //Populate course offering table

        //I'm assuming 50 courses but this can be different.

        int course_offering_id = 0;
        String[] room_wings = {"N", "E", "S", "W"};
        String[] floor_numbers = {"B", "1", "2", "3", "4", "5", "6", "7", "8"};
        ArrayList<String> allRoomNumbers = new ArrayList<>();
        for (String floor_number : floor_numbers) {
            for (String room_wing : room_wings) {
                for (int i = 1; i <= 20; i++) {
                    String zeroPad = "";
                    if (i < 10) {
                        zeroPad = "0";
                    }
                    allRoomNumbers.add(floor_number + room_wing + zeroPad + i);
                }
            }
        }
        int room_index = 0;

        for (int i = 0; i < 50; i++) {
            int num_of_course_offerings = (int) (Math.random() * 5) + 1;
            for (int j = 0; j < num_of_course_offerings; j++) {
                System.out.println("INSERT INTO CourseOfferings ( course_offering_id, course_offering_room, course_id, teacher_id, period ) VALUES ( " + course_offering_id + ", " + allRoomNumbers.get(room_index) + " )"); //TODO: MORE VALUES LEFT
                room_index++;
                course_offering_id++;
            }
        }

        //TO ASSIGN PERIODS RANDOMIZE MAKE AN ARRAY OF 1 TO 10
        //ASSIGN A PERIOD TO A COURSE OFFERING
        //REMOVE COURSE OFFERING LATER

        //THEN A STUDENT COULD POPULATE



        //Populate course type table
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 1, 'Elective' )");
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 2, 'Regents' )");
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 3, 'AP' )");

        //Populate roster table

        //I assume 150 course offerings for now.

        for (int i = 0; i < 150; i++) {
            System.out.println("INSERT INTO Rosters ( roster_id, course_offering_id ) VALUES ( " + i + ", " + i + " )");      //each roster id gets its own course offering id
        }

        //Populate student schedule table

        for (int i = 0; i < 5000; i++) {    //5000 students
            for (int j = 1; j <= 10; j++) {   //each have 10 courses
                System.out.println("INSERT INTO StudentSchedule ( student_id, roster_id ) VALUES ( " + i + " )"); //TODO: ROSTER ID REQUIRED
            }
        }
        System.out.println(allRoomNumbers);

    }
}
