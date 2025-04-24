import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Main {
    private static final Teacher[] teachers = new Teacher[580];
    private static final Student[] students = new Student[5000];
    private static final Course[] courses = new Course[120];
    private static final String[] departments = {"Biology", "Chemistry", "CTE", "English", "Health & PE", "World Languages & ENL", "Mathematics", "Physics", "Social Studies", "Visual & Performing Arts", "Teachers"};
    private static final ArrayList<CourseOffering> courseOfferings = new ArrayList<>();

    public static void main(String[] args) {
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
    } // DONE FLAWLESS
    public static void populateCourseTypes() {
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 1, 'Elective' );");
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 2, 'Regents' );");
        System.out.println("INSERT INTO CourseType ( course_type_id, course_type_name ) VALUES ( 3, 'AP' );");
    } // DONE FLAWLESS
    public static void populateTeachers(ArrayList<String> fileData) {
        String[] teacherNames = fileData.get(0).split(",");
        String[] departmentNames = fileData.get(1).split(",");

        for (int i = 0; i < teacherNames.length; i++) {
            String[] teacherNameSplit = teacherNames[i].trim().split(" ");

            int id = i + 1;
            String teacherFirstName = teacherNameSplit[0];
            String teacherLastName = teacherNameSplit[teacherNameSplit.length - 1];
            String deptName = (i < departmentNames.length) ? departmentNames[i].trim() : "";
            int deptId = getDepartmentId(deptName);

            teachers[i] = new Teacher(id, teacherFirstName, teacherLastName, deptId);
            System.out.println("INSERT INTO Teachers (teacher_id, name, department_id) VALUES (" + id + ", '" + teacherFirstName + " " + teacherLastName + "', " + deptId + ");");
        }
    } // DONE FLAWLESS
    private static int getDepartmentId(String name) {
        for (int i = 0; i < departments.length; i++) {
            if (departments[i].equalsIgnoreCase(name)) {
                return i + 1; // 1-based ID
            }
        }
        return 11; // Not found
    } // DONE FLAWLESS
    public static void populateCourses() {
        ArrayList<String> parsedCourses = getFileData("Courses.txt");


        ArrayList<String> parsedCoursesArray = new ArrayList<>();

        for (String parsedCourse : parsedCourses) {
            String[] CourseLine = parsedCourse.split("\\|");
            parsedCoursesArray.addAll(Arrays.asList(CourseLine).subList(1, CourseLine.length));
        }

        for (int i = 0; i < parsedCoursesArray.size(); i++) {
            String course = parsedCoursesArray.get(i);
            int courseTypeId = 1; // Default to Elective
            if (course.contains("Regents")) {
                courseTypeId = 2; // Regents
            } else if (course.startsWith("AP")) {
                courseTypeId = 3; // AP
            }

            courses[i] = new Course(i + 1, course, courseTypeId);
            System.out.println("INSERT INTO Courses ( course_id, course_name, course_type_id ) VALUES ( "
                    + (i + 1) + ", '" + course + "', " + courseTypeId + " );");
        }
    } // DONE FLAWLESS

    public static void populateCourseOfferings() {
        ArrayList<String> allRoomNumbers = getAllRoomNumbers();

        int room_index = 0;
        int course_offering_index = 0;

        for (int i = 0; i < courses.length; i++) { // # of courses
            int num_of_course_offerings = (int) (Math.random() * 3) + 3;
            for (int j = 0; j < num_of_course_offerings; j++) {
                int teacher_index = (int) (Math.random() * teachers.length);
                int period_index = (int) (Math.random() * 10);

                while (teachers[teacher_index].periods[period_index]) { // trigger if teacher is already teaching period
                    teacher_index = (int) (Math.random() * teachers.length);
                    period_index = (int) (Math.random() * 10);
                }

                teachers[teacher_index].periods[period_index] = true;

                courseOfferings.add(new CourseOffering(course_offering_index + 1, period_index + 1, teachers[teacher_index]));
                System.out.println("INSERT INTO CourseOfferings ( course_offering_id, course_offering_room, course_id, teacher_id, period ) VALUES ( " +
                        (course_offering_index + 1) + ", " + allRoomNumbers.get(room_index) + ", " + i + ", " + (teacher_index + 1) + ", " + (period_index + 1) + " );");

                room_index++;
                course_offering_index++;
            }
        }
    }

    private static ArrayList<String> getAllRoomNumbers() {
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
        return allRoomNumbers;
    } // DONE

    public static void populateStudents() {
        for (int i = 0; i < 5000; i++) {
            students[i] = new Student(i + 1);
            System.out.println("INSERT INTO Students ( student_id, name ) VALUES ( " + (i + 1) + ", 'Student" + (i + 1) + "' );");
        }
    } // DONE FLAWLESS
    public static void populateStudentSchedules() {
        for (int student_index = 0; student_index < students.length; student_index++) {
            ArrayList<Integer> course_offering_ids = new ArrayList<>();
            for (int period = 1; period <= 10; period++) {
                int random_offering_index = (int) (Math.random() * courseOfferings.size());
                CourseOffering offering =  courseOfferings.get(random_offering_index);
                while (offering.period != period || offering.roster.size() > 200) {
                    random_offering_index = (int) (Math.random() * courseOfferings.size());
                    offering =  courseOfferings.get(random_offering_index);
                }

                courseOfferings.get(random_offering_index).roster.add(students[student_index]);
                System.out.println("INSERT INTO StudentSchedule (student_id, course_offering_id) VALUES (" +
                        (student_index + 1) + ", " + (random_offering_index + 1) + ");");
                course_offering_ids.add(random_offering_index + 1);
            }
        }
    } // DONE
    public static void populateAssignmentTypes() {
        System.out.println("INSERT INTO AssignmentType ( assignment_type_id, assignment_type_name ) VALUES ( 1, 'minor' );");
        System.out.println("INSERT INTO AssignmentType ( assignment_type_id, assignment_type_name ) VALUES ( 2, 'major' );");
    } // DONE FLAWLESS
    public static void populateAssignments() {
        int assignment_id = 1;
        // 12 minor + 3 major per offering (600 offerings × 15 = 9000 assignments)
        for (int i = 0; i < courseOfferings.size(); i++) {    //call this after course offering id is called
            ArrayList<Integer> assignment_ids = new ArrayList<>();
            for (int j = 1; j <= 12; j++) {
                System.out.println(
                        "INSERT INTO Assignments ( assignment_id, assignment_name, assignment_type_id, course_offering_id ) VALUES (" +
                                assignment_id + ", 'Minor" + (j) + "', 1, " + courseOfferings.get(i).id + ");"
                );
                courseOfferings.get(i).assignments.add(new Assignment(assignment_id));
                assignment_ids.add(assignment_id);
                assignment_id++;
            }
            for (int j = 1; j <= 3; j++) {
                System.out.println(
                        "INSERT INTO Assignments ( assignment_id, assignment_name, assignment_type_id, course_offering_id ) VALUES (" +
                                assignment_id + ", 'Major" + (j) + "', 2, " + courseOfferings.get(i).id + ");"
                );
                courseOfferings.get(i).assignments.add(new Assignment(assignment_id));
                assignment_ids.add(assignment_id);
                assignment_id++;
            }
        }
    } // DONE
    public static void populateAssignmentGrades() {
//        for (int student_id = 0; student_id < students.length; student_id++) {  //5000 students
//            ArrayList<Integer> student_course_offerings = allCourseOfferingsPerStudent.get(student_id);
//            for (int course_offering_period = 0; course_offering_period < student_course_offerings.size() - 2; course_offering_period++) {   //10 course offerings per student
//                int course_offering_id = student_course_offerings.get(course_offering_period);
//                ArrayList<Integer> assignmentsInCourseOffering = allAssignmentsPerCourseOffering.get(course_offering_id);
//                for (int assignment_id_index = 0; assignment_id_index < assignmentsInCourseOffering.size(); assignment_id_index++) {    //15 assignments per course offering
//                    int grade = (int) ((Math.random() * 26) + 75);
//                    System.out.println("INSERT INTO AssignmentGrade ( student_id, grade, assignment_id ) VALUES ( "
//                            + (1) + ", '" + grade + "', " + 1 + " );");
//                    System.out.println("assignment id index: " + assignment_id_index);
//                }
//                System.out.println("course offering id:" + course_offering_id);
//            }
//            System.out.println("student id:" + student_id);
//        }
        for (int i = 0; i < courseOfferings.size(); i++) {
            CourseOffering offering = courseOfferings.get(i);
            for (int j = 0; j < offering.assignments.size(); j++) {
                for (int k = 0; k < offering.roster.size(); k++) {
                    int grade = (int) ((Math.random() * 26) + 75);
                    System.out.println("INSERT INTO AssignmentGrade ( student_id, grade, assignment_id ) VALUES ( "
                            + offering.roster.get(k).id + ", '" + grade + "', " + offering.assignments.get(j).id + " );");
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



