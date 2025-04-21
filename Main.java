import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Teacher[] teachers = new Teacher[548];
    private static final Student[] students = new Student[5000];
    private static final String[] departments = {"Biology", "Chemistry", "CTE", "English", "Health & PE", "World Languages & ENL", "Mathematics", "Physics", "Social Studies", "Visual & Performing Arts"};

   

     //NEW STUFF BY MASROOR
    private static final ArrayList<Integer>[] courseOfferingsByPeriod = new ArrayList[10];


    private static int teacherCountForCourseOfferings = 0;
    private static int courseOfferingIDCount = 0;


        
    public static void main(String[] args) {
        // Step 0: Setup teacher array and courseOfferingsByPeriod
        for (int i = 0; i < 200; i++) {
            teachers[i] = new Teacher();
        }
        for (int i = 0; i < 10; i++) {
            courseOfferingsByPeriod[i] = new ArrayList<>();
        }
    
        // Step 1: Setup database-independent constants
        populateDepartments();
        populateCourseTypes();
    
        // Step 2: Teachers (this sets teacherCountForCourseOfferings)
        ArrayList<String> teacherFileData = getFileData("Teachers.txt");
        populateTeachers(teacherFileData);
    
        // Step 3: Courses made
       populateCourses();
    
        // Step 4: Course Offerings (sets up  courseOfferingIDCount, fills  up the courseOfferingsByPeriod[])
        populateCourseOfferingsFixed();
    
        // Step 5: Students made
        populateStudents();
    
        // Step 6: Assign people to course offerings (uses courseOfferingsByPeriod[])
        populateStudentSchedules();
    
        // Step 7: Assignment Types ( define stuff )
        populateAssignmentTypes();
    
        // Step 8: Assignments ( generate 15 per offering)
        populateAssignments();
    
        // Step 9: Grades 
    
        populateAssignmentGrades();
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
    //NEW STUFF BY MASROOR
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


            //COMMENTED THIS CAUSE THIS WAS CAUSING ISSUES
            //teachers[i] = new Teacher();

            String teacherFirstName = teacherNameSplit[0];
            String teacherLastName = teacherNameSplit[teacherNameSplit.length - 1];
            String departmentId = (i < departmentNames.length) ? departmentNames[i].trim() : "NULL";

            System.out.println("INSERT INTO Teachers (teacher_id, name, department_id) VALUES (" +
                              i + ", '" + teacherFirstName + " " + teacherLastName + "', " +
                              departmentId + ");");

            // what is this variable even supposed to represent and how is it not redundant
            teacherCountForCourseOfferings++; // Increment counter
        }

    } // DONE (PROBABLY)


    public static void populateCourses() {
        ArrayList<String> parsedSubjects = getFileData("Courses.txt");

        ArrayList<String[]> parsedSubjects2DArray = new ArrayList<>();
        for (String parsedSubject : parsedSubjects) {
            String[] subjectLine = parsedSubject.split("\\|");
            parsedSubjects2DArray.add(subjectLine);
        }

        int courseId = 0;

        for (String[] subjectArray : parsedSubjects2DArray) {
            // subjectArray[0] is the dept
            for (int i = 1; i < subjectArray.length; i++) {
                String courseName = subjectArray[i];
                int courseTypeId = 1; // Default to Elective

                if (courseName.contains("Regents")) {
                    courseTypeId = 2; // Regents
                } else if (courseName.startsWith("AP")) {
                    courseTypeId = 3; // AP
                }


                System.out.println("INSERT INTO Courses ( course_id, course_name, course_type_id ) VALUES ( "
                        + courseId + ", '" + courseName + "', " + courseTypeId + " );");

                courseId++;
            }
        }
    }

    //  method to return course type ID based on the dept name
    public static int getCourseTypeId(String category) {
        switch (category) {
            case "Biology":
                return 1;
            case "Chemistry":
                return 2;
            case "CTE":
                return 3;
            case "English":
                return 4;
            case "Health & PE":
                return 5;
            case "World Languages & ENL":
                return 6;
            case "Mathematics":
                return 7;
            case "Physics":
                return 8;
            case "Social Studies":
                return 9;
            case "Visual & Performing Arts":
                return 10;
            default:
                return 0;  // Default case for unknown categories
        }
    }


    public static void populateCourseTypes() {
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 1, 'Elective' )");
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 2, 'Regents' )");
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 3, 'AP' )");
    } // DONE


    public static void populateCourseOfferingsFixed() {
        String[] room_wings = {"N", "E", "S", "W"};
        String[] floor_numbers = {"B", "1", "2", "3", "4", "5", "6", "7", "8"};
        ArrayList<String> allRoomNumbers = new ArrayList<>();

        for (String floor_number : floor_numbers) {
            for (String room_wing : room_wings) {
                for (int i = 1; i < 21; i++) {
                    String zeroPad = (i < 10) ? "0" : "";
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

        for (int i = 0; i < 120; i++) { // # of courses
            int num_of_course_offerings = (int) (Math.random() * 5) + 1;
            for (int j = 0; j < num_of_course_offerings; j++) {

                int teacher_id = -1;
                int period = -1;
                boolean isBlacklisted;
                int attempts = 0;

                // try to find a non-blacklisted teacher/period combo
                do {
                    teacher_id = (int) (Math.random() * teacherCountForCourseOfferings);
                    period = (int) (Math.random() * 10) + 1;

                    isBlacklisted = false;
                    for (blacklistedTeacherPeriodCombo combo : blacklistArray) {
                        if (teacher_id == combo.teacher_id && period == combo.period) {
                            isBlacklisted = true;
                            break;
                        }
                    }

                    attempts++;
                    if (attempts > 1000) {
                        // too many tries, skip this offering
                        System.out.println("-- Skipping offering " + course_offering_id + " due to blacklist");
                        break;
                    }

                } while (isBlacklisted);

                if (attempts > 1000) {
                    continue; // skip if no valid combo found
                }

                blacklistArray.add(new blacklistedTeacherPeriodCombo(teacher_id, period));
                courseOfferingsByPeriod[period - 1].add(course_offering_id); // used for student schedule

                System.out.println("INSERT INTO CourseOfferings ( course_offering_id, course_offering_room, course_id, teacher_id, period ) VALUES ( " +
                        course_offering_id + ", " + allRoomNumbers.get(room_index) + ", " + i + ", " + teacher_id + ", " + period + " )");

                room_index++;
                course_offering_id++;
                courseOfferingIDCount++;
            }
        }
    }



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
