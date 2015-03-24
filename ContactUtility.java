/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class ContactUtility {
    private String identifier;
    private String firstName;
    private String lastName;
    private String middleInitial;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipcode;
    private String phoneNumber;
    private String gender;
    private String email;
    private String country;
    
    public ContactUtility()
    {
        
    }
    
    public ContactUtility(String firstName, String lastName, String middleInitial, 
            String addressLine1, String addressLine2, String city,
            String state, String zipcode, String phoneNumber, String gender, String email, String country)
    {
        this.identifier = firstName.toLowerCase() + middleInitial.toLowerCase()  + lastName.toLowerCase() ;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleInitial = middleInitial;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.email = email;
        this.country = country;
    }

    public String getFullName(){ 
        return firstName+" "+middleInitial+ " "+lastName;
    }
    
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {  
        this.firstName = firstName;
    }

    public String getLastName() {  
        return lastName;
    }

    public void setLastName(String lastName) {  
        this.lastName = lastName;
    }

    public String getMiddleInitial() {         return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) { 
        this.middleInitial = middleInitial;
    }

    public String getAddressLine1() {  
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) { 
        this.city = city;
    }

    public String getState() { 
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() { 
        return zipcode;
    }

    public void setZipcode(String zipcode) { 
        this.zipcode = zipcode;
    }

    public String getPhoneNumber() { 
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {  
        this.gender = gender;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
    
}
