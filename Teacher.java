public class Teacher extends Referencable{
    public String firstName;
    public String lastName;
    public int departmentId;
    public boolean[] periods = {false, false, false, false, false, false, false, false, false, false};

    public Teacher(int id, String firstName, String lastName, int departmentId) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.departmentId = departmentId;
    }
}
