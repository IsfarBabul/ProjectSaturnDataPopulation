import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Teacher[] teachers = new Teacher[200];
    private static final Student[] students = new Student[5000];
    private static final String[] departments = {"Biology", "Chemistry", "CTE", "English", "Health & PE", "World Languages & ENL", "Mathematics", "Physics", "Social Studies", "Visual & Performing Arts"};

    private static final ArrayList<Assignments> assignments = new ArrayList<>();

     //NEW STUFF BY MASROOR
    private static final ArrayList<Integer>[] courseOfferingsByPeriod = new ArrayList[10];


    private static int teacherCountForCourseOfferings = 0;
    private static int courseOfferingIDCount = 0;


    
    public static void main(String[] args) {
        for (int i = 0; i < 200; i++) {
            teachers[i] = new Teacher();
        }

        //NEW STUFF BY MASROOR
        for (int i = 0; i < 10; i++) {
            courseOfferingsByPeriod[i] = new ArrayList<>();
        }

    }

    public static void populateAssignments() {
        int assignment_id = 0;
        // 12 minor + 3 major per offering (600 offerings × 15 = 9000 assignments)
        for (int offering_id = 0; offering_id < courseOfferingIDCount; offering_id++) {    //call this after course offering id is called
            for (int j = 0; j < 12; j++) {
                System.out.println(
                    "INSERT INTO Assignments VALUES (" +
                    assignment_id + ", 'Minor" + (j) + "', 1, " + offering_id + ");"
                );
                assignment_id++;
            }
            for (int j = 0; j < 3; j++) {
                System.out.println(
                    "INSERT INTO Assignments VALUES (" +
                    assignment_id + ", 'Major" + (j) + "', 2, " + offering_id + ");"
                );
                assignment_id++;
            }
        }
    } // DONE
    public static void populateAssignmentTypes() {
        System.out.println("INSERT INTO AssignmentType ( assignment_type_id, assignment_type_name ) VALUES ( 1, 'minor' )");
        System.out.println("INSERT INTO AssignmentType ( assignment_type_id, assignment_type_name ) VALUES ( 2, 'major' )");
    } // DONE
    public static void populateAssignmentGrades() {
        for (int i = 0; i < 5000; i++) { //each student gets assignments
            for (int j = 0; j < 15; j++) {     //number of assignments
                System.out.println("INSERT INTO AssignmentGrade ( student_id, grade, assignment_id ) VALUES ( " + i + ", '" + (int) ((Math.random() * 26) + 75) + "', " + j + " );");
            }
        }
    } // NEEDS PROPER VALUES FOR STUDENT_ID - AIVIN       Assignment_Id has some issue where its only being populated with numbers 1 to 15 so only assignments from the very first course offering are being represented - Isfar
    public static void populateStudents() {
        for (int i = 0; i < 5000; i++) {
            System.out.println("INSERT INTO Students ( student_id, name ) VALUES ( " + (i + 1) + ", 'Student" + (i + 1) + "' );");
        }
    } // DONE
    // public static void populateStudentSchedules() {
    //     for (int student_id = 0; student_id < students.length; student_id++) {
    //         for (int period = 1; period < 11; period++) {
    //             System.out.println("INSERT INTO StudentSchedule (student_id, course_offering_id) VALUES (" +
    //                     student_id + ", " + (int)(Math.random() * courseOfferingIDCount) + ");"); // Random roster 0-599( we said 600)    //call this after course offering id is called
    //         }
    //     }
    // } // NEED PROPER IDS FOR STUDENT_ID AND ROSTER_ID


    // NEW STUFF BY MASROOR
        public static void populateStudentSchedules() {
        for (int student_id = 0; student_id < students.length; student_id++) {
            for (int period = 0; period < 10; period++) {
                ArrayList<Integer> offerings = courseOfferingsByPeriod[period];
                if (!offerings.isEmpty()) {
                    int randomIndex = (int)(Math.random() * offerings.size());
                    int course_offering_id = offerings.get(randomIndex);
                    System.out.println("INSERT INTO StudentSchedule (student_id, course_offering_id) VALUES (" +
                            student_id + ", " + course_offering_id + ");");
                }
            }
        }
    }



    

    public static void populateDepartments() {
        for (int i = 0; i < departments.length; i++) {
            System.out.println("INSERT INTO Departments ( department_id, department_name ) VALUES ( " + (i + 1) + ", " + departments[i] + " )");
        }
    } // DONE
    public static void populateTeachers(ArrayList<String> fileData) {
        String[] teacherNames = fileData.get(0).split(",");
        String[] departmentNames = fileData.get(1).split(",");

        for (int i = 0; i < teacherNames.length; i++) {
            String[] teacherNameSplit = teacherNames[i].trim().split(" ");

            teachers[i] = new Teacher();

            String teacherFirstName = teacherNameSplit[0];
            String teacherLastName = teacherNameSplit[teacherNameSplit.length - 1];
            String departmentId = (i < departmentNames.length) ? departmentNames[i].trim() : "NULL";

            System.out.println("INSERT INTO Teachers (teacher_id, name, department_id) VALUES (" +
                              i + ", '" + teacherFirstName + " " + teacherLastName + "', " +
                              departmentId + ");");

            // what is this variable even supposed to represent and how is it not redundant
            teacherCountForCourseOfferings++; // Increment counter
        }

        // Print the total count
        System.out.println("\n-- Total teachers : " + teacherCountForCourseOfferings++);
    } // DONE (PROBABLY)
    public static void populateCourses() {
        ArrayList<String> parsedSubjects = getFileData("Courses.txt");


        ArrayList<String[]> parsedSubjects2DArray = new ArrayList<>();
        // populate parsedSubjects2DArray
        for (String parsedSubject : parsedSubjects) {
            String[] subjectLine = parsedSubject.split("\\|");
            parsedSubjects2DArray.add(subjectLine);
        }

        //BELOW IS FOR PRINTING-----------------------------------------------
        for (String[] strings : parsedSubjects2DArray) {
            for (String string : strings) {
                System.out.println(string);
            }
            System.out.println();
        }
        //ABOVE IS FOR PRINTING---------------------------------------------

        for (int i = 0; i < parsedSubjects2DArray.size(); i++) {
            String course_name = parsedSubjects2DArray.get(i)[1];
            int course_type_id = 1;
            if (course_name.contains("Regents")) {       // contains() is appropriate because we could have regents courses called "Regents Physics" or "10th Grade Regents Global History"
                course_type_id = 2;
            } else if (course_name.startsWith("AP")) {   // startWith() is appropriate to include AP courses like "AP Computer Science A" but to exclude courses such as "Spanish IV - Pre-AP"
                course_type_id = 3;
            }
            System.out.println("INSERT INTO Courses ( course_id, course_name, course_type_id ) VALUES ( " + i + ", " + course_name + ", " + course_type_id + " )");
        }
    } // DONE
    public static void populateCourseTypes() {
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 1, 'Elective' )");
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 2, 'Regents' )");
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 3, 'AP' )");
    } // DONE
    public static void populateCourseOfferings() {
        String[] room_wings = {"N", "E", "S", "W"};
        String[] floor_numbers = {"B", "1", "2", "3", "4", "5", "6", "7", "8"};
        ArrayList<String> allRoomNumbers = new ArrayList<>();
        for (String floor_number : floor_numbers) {
            for (String room_wing : room_wings) {
                for (int i = 1; i < 21; i++) {
                    String zeroPad = "";
                    if (i < 10) {
                        zeroPad = "0";
                    }
                    allRoomNumbers.add(floor_number + room_wing + zeroPad + i);
                }
            }
        }

        int room_index = 0;
        int course_offering_id = 0;

        class blacklistedTeacherPeriodCombo {
            public int teacher_id;
            public int period;

            public blacklistedTeacherPeriodCombo(int teacherId, int period) {
                this.teacher_id = teacherId;
                this.period = period;
            }
        }

        ArrayList<blacklistedTeacherPeriodCombo> blacklistArray = new ArrayList<>();

        for (int i = 0; i < 120; i++) { // SHOULD BE LENGTH OF COURSES ARRAY
            int num_of_course_offerings = (int) (Math.random() * 5) + 1;
            for (int j = 0; j < num_of_course_offerings; j++) {
                int teacher_id = -1;
                int period = -1;
                boolean isBlacklisted = false;
                do {
                    teacher_id = (int) (Math.random() * teacherCountForCourseOfferings);    //WARNING: populateTeachers() method must be called before this method
                    period = (int) (Math.random() * 10) + 1;

                    //NEW STUFF BY MASROOR
                    courseOfferingsByPeriod[period - 1].add(course_offering_id);



                    
                    for (blacklistedTeacherPeriodCombo combo : blacklistArray) {
                        if (teacher_id == combo.teacher_id && period == combo.period) {
                            isBlacklisted = true;
                            break;
                        }
                    }
                } while (isBlacklisted);

                System.out.println("INSERT INTO CourseOfferings ( course_offering_id, course_offering_room, course_id, teacher_id, period ) VALUES ( " + course_offering_id + ", " + allRoomNumbers.get(room_index) + ", " + i + ", " + teacher_id + ", " + period + " )");
                blacklistArray.add(new blacklistedTeacherPeriodCombo(teacher_id, period));
                room_index++;
                course_offering_id++;
                courseOfferingIDCount++;
            }
        }


        // Masroor's Code
//        int[] periodCounts = new int[10];
//        int[] teacherAssignments = new int[teacherCountForCourseOfferings];
//        int room_index = 0;
//
//        for (int course_id = 0; course_id < 120; course_id++) {
//            int num_of_course_offerings = (int)(Math.random() * 5) + 1;
//
//            for (int j = 0; j < num_of_course_offerings; j++) {
//                int period = getValidPeriod(periodCounts);
//                int teacher_id = getValidTeacher(teacherAssignments);
//
//                periodCounts[period-1]++;
//                teacherAssignments[teacher_id]++;
//
//                System.out.println("INSERT INTO CourseOffering VALUES (" +
//                        course_offering_id + ", '" + allRoomNumbers.get(room_index) +
//                        "', " + course_id + ", " + teacher_id + ", " + period + ");");
//
//                room_index++;
//                course_offering_id++;
//            }
//        }

    } // DONE

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

    /*public static void populateRoster() {
        //used to be 150
        for (int i = 0; i < 600; i++) {
            System.out.println("INSERT INTO Rosters ( roster_id, course_offering_id ) VALUES ( " + i + ", " + i + " )");      //each roster id gets its own course offering id
        }
    } // NEEDS PROPER VALUES*/

    //---------------------UTILITY METHODS GO BELOW THIS LINE---------------------------------\\

    private static ArrayList<String> getFileData(String fileName) {
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
