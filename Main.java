import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Teacher[] teachers = new Teacher[200];
    private static final Student[] students = new Student[5000];
    private static final String[] departments = {"Biology", "Chemistry", "CTE", "English", "Health & PE", "World Languages & ENL", "Mathematics", "Physics", "Social Studies", "Visual & Performing Arts"};

    private static int teacherCountForCourseOfferings = 0


    
    public static void main(String[] args) {
        for (int i = 0; i < 200; i++) {
            teachers[i] = new Teacher();
        }
    }

    public static void populateAssignments() {
        int assignmentCount = 1;
        for (int i = 0; i < 5; i++) {      //number of course offerings; 5 is an example
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
    }
    public static void populateAssignmentTypes() {
        System.out.println("INSERT INTO AssignmentType ( assignment_type_id, assignment_type_name ) VALUES ( 1, 'minor' )");
        System.out.println("INSERT INTO AssignmentType ( assignment_type_id, assignment_type_name ) VALUES ( 2, 'major' )");
    }
    public static void populateAssignmentGrades() {
        for (int i = 1; i <= 5000; i++) { //each student gets assignments
            for (int j = 0; j < 15; j++) {     //number of assignments
                System.out.println("INSERT INTO AssignmentGrade ( student_id, grade, assignment_id ) VALUES ( " + i + ", '" + (int) ((Math.random() * 26) + 75) + "', " + j + "', " + i + " );");
            }
        }
    }
    public static void populateStudents() {
        for (int i = 0; i < 5000; i++) {
            System.out.println("INSERT INTO Students ( student_id, name ) VALUES ( " + (i + 1) + ", 'Student" + (i + 1) + "' );");
        }
    }
    public static void populateStudentSchedules() {
        for (int i = 0; i < students.length; i++) {    //5000 students
            for (int j = 0; j < 10; j++) {   //each have 10 courses
                System.out.println("INSERT INTO StudentSchedule ( student_id, roster_id ) VALUES ( " + (i + 1) + " )"); //TODO: ROSTER ID REQUIRED
            }
        }
    }
    public static void populateDepartments() {
        for (int i = 0; i < departments.length; i++) {
            System.out.println("INSERT INTO Departments ( department_id, department_name ) VALUES ( " + (i + 1) + ", " + departments[i] + " )");
        }
    }


    public static void populateTeachers(ArrayList<String> fileData) {
    String[] teacherNames = fileData.get(0).split(",");
    String[] departmentNames = fileData.get(1).split(",");
    
    int teacherCount = 0; // Counter for number of teachers 

    for (int i = 0; i < teacherNames.length; i++) {
        String[] teacherNameSplit = teacherNames[i].trim().split(" ");

        teachers[i] = new Teacher();

        String teacherFirstName = teacherNameSplit[0];
        String teacherLastName = teacherNameSplit[teacherNameSplit.length - 1];
        String departmentId = (i < departmentNames.length) ? departmentNames[i].trim() : "NULL";

        System.out.println("INSERT INTO Teachers (teacher_id, name, department_id) VALUES (" + 
                          i + ", '" + teacherFirstName + " " + teacherLastName + "', " + 
                          departmentId + ");");
        
        teacherCountForCourseOfferings++ // Increment counter
    }
    
    // Print the total count 
    System.out.println("\n-- Total teachers : " + teacherCountForCourseOfferings++);
}

    
    // public static void populateTeacherSchedules() {
    //     for (int i = 0; i < 200; i++) {    //200 teachers
    //         for (int j = 1; j <= 10; j++) {   //each have 10 courses
    //             System.out.println("INSERT INTO TeacherSchedule ( teacher_id, roster_id ) VALUES ( " + i + " )"); //TODO: ROSTER ID REQUIRED
    //         }
    //     }
    // }


    
    public static void populateCourses() {
        ArrayList<String> parsedSubjects = getFileData("Courses.txt");
        ArrayList<String[]> parsedSubjects2DArray = new ArrayList<>();
        //System.out.println("PARSED: " + parsedSubjects.size());
        for (int i = 0; i < parsedSubjects.size(); i++) {
            String[] subjectLine = parsedSubjects.get(i).split("\\|");
            parsedSubjects2DArray.add(subjectLine);
        }
        System.out.println(parsedSubjects);
        System.out.println(parsedSubjects2DArray);
        //BELOW IS FOR PRINTING
        for (int i = 0; i < parsedSubjects2DArray.size(); i++) {
            for (int j = 0; j < parsedSubjects2DArray.get(i).length; j++) {
                System.out.println(parsedSubjects2DArray.get(i)[j]);
            }
            System.out.println();
        }
        //ABOVE IS FOR PRINTING
        int course_id = 0;
        for (int i = 0; i < parsedSubjects2DArray.size(); i++) {
            for (int j = 1; j < parsedSubjects2DArray.get(i).length; j++) {   //Index 0 would be the department the course is in which we do not want to include!
                String course_name = parsedSubjects2DArray.get(i)[j];
                int course_type_id = 1;
                if (course_name.contains("Regents")) {       //contains() is appropriate because we could have regents courses called "Regents Physics" or "10th Grade Regents Global History"
                    course_type_id = 2;
                } else if (course_name.startsWith("AP")) {   //startWith() is appropriate to include AP courses like "AP Computer Science A" but to exclude courses such as "Spanish IV - Pre-AP"
                    course_type_id = 3;
                }
                System.out.println("INSERT INTO Courses ( course_id, course_name, course_type_id ) VALUES ( " + course_id + ", " + course_name + ", " + course_type_id + " )");
                course_id++;
            }
        }
    }
    public static void populateCourseTypes() {
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 1, 'Elective' )");
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 2, 'Regents' )");
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 3, 'AP' )");
    }
    // public static void populateCourseOfferings() {
    //     int course_offering_id = 0;
    //     String[] room_wings = {"N", "E", "S", "W"};
    //     String[] floor_numbers = {"B", "1", "2", "3", "4", "5", "6", "7", "8"};
    //     ArrayList<String> allRoomNumbers = new ArrayList<>();
    //     for (String floor_number : floor_numbers) {
    //         for (String room_wing : room_wings) {
    //             for (int i = 1; i <= 20; i++) {
    //                 String zeroPad = "";
    //                 if (i < 10) {
    //                     zeroPad = "0";
    //                 }
    //                 allRoomNumbers.add(floor_number + room_wing + zeroPad + i);
    //             }
    //         }
    //     }
    //     int room_index = 0;

    //     for (int i = 0; i < 50; i++) {
    //         int num_of_course_offerings = (int) (Math.random() * 5) + 1;
    //         for (int j = 0; j < num_of_course_offerings; j++) {

    //             int teacher_id = (int)(Math.random() * teacherCountForCourseOfferings);
    //             System.out.println("INSERT INTO CourseOfferings ( course_offering_id, course_offering_room, course_id, teacher_id, period ) VALUES ( " + course_offering_id + ", " + allRoomNumbers.get(room_index) + " )"); //TODO: MORE VALUES LEFT
    //             room_index++;
    //             course_offering_id++;
    //         }
    //     }
    // }


    public static void populateCourseOfferings() {
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
    
    int[] periodCounts = new int[10];
    int[] teacherAssignments = new int[teacherCountForCourseOfferings];
    int room_index = 0;

    for (int course_id = 0; course_id < 120; course_id++) {
        int num_of_course_offerings = (int)(Math.random() * 5) + 1;
        
        for (int j = 0; j < num_of_course_offerings; j++) {
            int period = getValidPeriod(periodCounts);
            int teacher_id = getValidTeacher(teacherAssignments);
            
            periodCounts[period-1]++;
            teacherAssignments[teacher_id]++;
            
            System.out.println("INSERT INTO CourseOffering VALUES (" + 
                course_offering_id + ", '" + allRoomNumbers.get(room_index) + 
                "', " + course_id + ", " + teacher_id + ", " + period + ");");
            
            room_index++;
            course_offering_id++;
        }
    }
}

private static int getValidPeriod(int[] periodCounts) {
    int period;
    while (true) {
        period = (int)(Math.random() * 10) + 1;
        if (periodCounts[period-1] < 60) {
            return period;
        }
    }
}

private static int getValidTeacher(int[] teacherAssignments) {
    int teacher_id;
    while (true) {
        teacher_id = (int)(Math.random() * teacherCountForCourseOfferings);
        if (teacherAssignments[teacher_id] < 5) {
            return teacher_id;
        }
    }
}








    
    
    public static void populateRoster() {
        for (int i = 0; i < 150; i++) {
            System.out.println("INSERT INTO Rosters ( roster_id, course_offering_id ) VALUES ( " + i + ", " + i + " )");      //each roster id gets its own course offering id
        }
    }

    //---------------------UTILITY METHODS GO BELOW THIS LINE---------------------------------\\

    public static ArrayList<String> getFileData(String fileName) {
        ArrayList<String> fileData = new ArrayList<>();
        try {
            File f = new File(fileName);
            Scanner s = new Scanner(f);
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (!line.isEmpty())
                    fileData.add(line);
            }
            return fileData;
        } catch (FileNotFoundException e) {
            return fileData;
        }
    }
}
