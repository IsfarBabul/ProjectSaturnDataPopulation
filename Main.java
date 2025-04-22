import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    private static final Teacher[] teachers = new Teacher[580];
    private static final Student[] students = new Student[5000];
    private static final String[] departments = {"Biology", "Chemistry", "CTE", "English", "Health & PE", "World Languages & ENL", "Mathematics", "Physics", "Social Studies", "Visual & Performing Arts", "Teachers"};
    private static final ArrayList<Integer>[] courseOfferingsByPeriod = new ArrayList[10];
    private static int teacherCountForCourseOfferings = 580;
    private static int courseOfferingIDCount = 0;
    private static ArrayList<ArrayList<Integer>> allCourseOfferingsPerStudent = new ArrayList<>();
    private static ArrayList<ArrayList<Integer>> allAssignmentsPerCourseOffering = new ArrayList<>();

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
        populateCourseOfferings();

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




    public static void populateDepartments() {
        for (int i = 0; i < departments.length; i++) {
            System.out.println("INSERT INTO Departments ( department_id, department_name ) VALUES ( " + (i + 1) + ", " + departments[i] + " );");
        }
    } // DONE
    public static void populateCourseTypes() {
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 1, 'Elective' );");
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 2, 'Regents' );");
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 3, 'AP' );");
    } // DONE
    public static void populateTeachers(ArrayList<String> fileData) {
        String[] teacherNames = fileData.get(0).split(",");
        String[] departmentNames = fileData.get(1).split(",");

        for (int i = 0; i < teacherNames.length; i++) {
            String[] teacherNameSplit = teacherNames[i].trim().split(" ");
            String teacherFirstName = teacherNameSplit[0];
            String teacherLastName = teacherNameSplit[teacherNameSplit.length - 1];

            String deptName = (i < departmentNames.length) ? departmentNames[i].trim() : "";
            int deptId = getDepartmentId(deptName);

            String departmentIdValue = (deptId == -1) ? "NULL" : String.valueOf(deptId);

            System.out.println("INSERT INTO Teachers (teacher_id, name, department_id) VALUES (" +
                    (i +1) + ", '" + teacherFirstName + " " + teacherLastName + "', " +
                    departmentIdValue + ");");

        }
    }
    private static int getDepartmentId(String name) {
        for (int i = 0; i < departments.length; i++) {
            if (departments[i].equalsIgnoreCase(name)) {
                return i + 1; // 1-based ID
            }
        }
        return 11; // Not found
    }
    public static void populateCourses() {
        ArrayList<String> parsedSubjects = getFileData("Courses.txt");

        ArrayList<String[]> parsedSubjects2DArray = new ArrayList<>();
        for (String parsedSubject : parsedSubjects) {
            String[] subjectLine = parsedSubject.split("\\|");
            parsedSubjects2DArray.add(subjectLine);
        }

        int courseId = 1;

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
    } // DONE

    public static void populateCourseOfferings() {
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
        int course_offering_id = 1;

        class blacklistedTeacherPeriodCombo {
            public int teacher_id;
            public int period;


            public blacklistedTeacherPeriodCombo(int teacherId, int period) {
                this.teacher_id = teacherId;
                this.period = period;
            }
        }

        ArrayList<blacklistedTeacherPeriodCombo> blacklistArray = new ArrayList<>();

        for (int i = 1; i <= 120; i++) { // # of courses
            int num_of_course_offerings = (int) (Math.random() * 5) + 1;
            for (int j = 0; j < num_of_course_offerings; j++) {

                int teacher_id;
                int period;
                boolean isBlacklisted;
                int attempts = 0;

                // try to find a non-blacklisted teacher/period combo
                do {
                    teacher_id = (int) (Math.random() * teacherCountForCourseOfferings)+1;
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
                        //System.out.println("-- Skipping offering " + course_offering_id + " due to blacklist");
                        break;
                    }

                } while (isBlacklisted);


               /*if (attempts > 1000) {
                   continue; // skip if no valid combo found
               }*/


                blacklistArray.add(new blacklistedTeacherPeriodCombo(teacher_id, period));
                courseOfferingsByPeriod[period - 1].add(course_offering_id); // used for student schedule

                System.out.println("INSERT INTO CourseOfferings ( course_offering_id, course_offering_room, course_id, teacher_id, period ) VALUES ( " +
                        course_offering_id + ", " + allRoomNumbers.get(room_index) + ", " + i + ", " + teacher_id + ", " + period + " );");

                room_index++;
                course_offering_id++;
                courseOfferingIDCount++;
            }
        }
    }
    public static void populateStudents() {
        for (int i = 0; i < 5000; i++) {
            System.out.println("INSERT INTO Students ( student_id, name ) VALUES ( " + (i + 1) + ", 'Student" + (i + 1) + "' );");
        }
    } // DONE
    public static void populateStudentSchedules() {
        for (int student_id = 0; student_id <= students.length; student_id++) {
            ArrayList<Integer> course_offering_ids = new ArrayList<>();
            for (int period = 0; period < 10; period++) {
                ArrayList<Integer> offerings = courseOfferingsByPeriod[period];
                if (!offerings.isEmpty()) {
                    int randomIndex = (int)(Math.random() * offerings.size());
                    int course_offering_id = offerings.get(randomIndex);
                    System.out.println("INSERT INTO StudentSchedule (student_id, course_offering_id) VALUES (" +
                            student_id + ", " + course_offering_id + ");");
                    course_offering_ids.add(course_offering_id);
                }
            }
            allCourseOfferingsPerStudent.add(course_offering_ids);
        }
    }
    public static void populateAssignmentTypes() {
        System.out.println("INSERT INTO AssignmentType ( assignment_type_id, assignment_type_name ) VALUES ( 1, 'minor' );");
        System.out.println("INSERT INTO AssignmentType ( assignment_type_id, assignment_type_name ) VALUES ( 2, 'major' );");
    } // DONE
    public static void populateAssignments() {
        int assignment_id = 1;
        // 12 minor + 3 major per offering (600 offerings × 15 = 9000 assignments)
        for (int offering_id = 1; offering_id <= courseOfferingIDCount; offering_id++) {    //call this after course offering id is called
            ArrayList<Integer> assignment_ids = new ArrayList<>();
            for (int j = 1; j <= 12; j++) {
                System.out.println(
                        "INSERT INTO Assignments ( assignment_id, assignment_name, assignment_type_id, course_offering_id ) VALUES (" +
                                assignment_id + ", 'Minor" + (j) + "', 1, " + offering_id + ");"
                );
                assignment_ids.add(assignment_id);
                assignment_id++;
            }
            for (int j = 1; j <= 3; j++) {
                System.out.println(
                        "INSERT INTO Assignments ( assignment_id, assignment_name, assignment_type_id, course_offering_id ) VALUES (" +
                                assignment_id + ", 'Major" + (j) + "', 2, " + offering_id + ");"
                );
                assignment_ids.add(assignment_id);
                assignment_id++;
            }
            allAssignmentsPerCourseOffering.add(assignment_ids);
        }
    } // DONE
    public static void populateAssignmentGrades() {
        for (int student_id = 0; student_id < 5000; student_id++) {  //5000 students
            ArrayList<Integer> student_course_offering_ids = allCourseOfferingsPerStudent.get(student_id);
            for (int course_offering_period = 0; course_offering_period < student_course_offering_ids.size(); course_offering_period++) {   //10 course offerings per student
                int course_offering_id = student_course_offering_ids.get(course_offering_period);
                for (int assignment_id = 0; assignment_id < allAssignmentsPerCourseOffering.get(course_offering_id).size(); assignment_id++) {    //15 assignments per course offering
                    int grade = (int) ((Math.random() * 26) + 75);
                    System.out.println("INSERT INTO AssignmentGrade ( student_id, grade, assignment_id ) VALUES ( "
                            + student_id + ", '" + grade + "', " + allAssignmentsPerCourseOffering.get(course_offering_id).get(assignment_id) + " );");
                }
            }
        }
    }


    //Assignment grades might now be done!


    // NEEDS PROPER VALUES FOR STUDENT_ID - AIVIN       Assignment_Id has some issue where its only being populated with numbers 1 to 15 so only assignments from the very first course offering are being represented - Isfar
    //NEW STUFF BY MASROOR






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



