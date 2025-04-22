public class Teacher extends Referencable{
    public String firstName;
    public String lastName;
    public int departmentId;

    public Teacher(int id, String firstName, String lastName, int departmentId) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.departmentId = departmentId;
    }
}
