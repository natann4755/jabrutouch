package co.il.jabrutouch.ui.main.donation_screen;

public class Donation {
    private String name;
    private String surname;
    private String country;

    public Donation(String name, String surname, String country) {
        this.name = name;
        this.surname = surname;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    String getSurname() {
        return surname;
    }

    String getCountry() {
        return country;
    }
}
