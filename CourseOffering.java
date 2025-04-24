import java.util.ArrayList;

public class CourseOffering extends Referencable {
    public int period;
    public Teacher teacher;
    public ArrayList<Student> roster;

    public CourseOffering(int id, int period, Teacher teacher) {
        super(id);
        this.period = period;
        this.teacher = teacher;
        roster = new ArrayList<>();
    }
}
