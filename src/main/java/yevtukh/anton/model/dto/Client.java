package yevtukh.anton.model.dto;

/**
 * Created by Anton on 18.10.2017.
 */
public class Client {

    private final int id;
    private final String firstName;
    private final String lastName;
    private final Integer age;

    public Client(int id, String firstName, String lastName, Integer age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getAge() {
        return age;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isValid() {
        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty() ||
                ( age != null && age < 0))
            return false;
        return true;
    }
}
