public class Course extends Referencable{
    private String course_name;
    private int course_type_id;

    public Course(int id, String course_name, int course_type_id) {
        super(id);
        this.course_name = course_name;
        this.course_type_id = course_type_id;
    }
}
