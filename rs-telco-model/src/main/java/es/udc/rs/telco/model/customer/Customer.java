package es.udc.rs.telco.model.customer;

import java.time.LocalDateTime;

public class Customer {

    private Long customerId;
    private String name;
    private String dni;
    private String address;
    private LocalDateTime creationDate;
    private String phoneNumber;
    

	public Customer(Long customerId, String name, String dni, String address, String phoneNumber, LocalDateTime creationDate) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.dni = dni;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.creationDate = creationDate;
	}

	// Constructor sin customerId (para creación inicial)
	public Customer(String name, String dni, String address, String phoneNumber) {
		this(null, name, dni, address, phoneNumber, LocalDateTime.now());
	}

	//Para a capa de servicios
	public Customer(Long customerId,String name, String dni, String address, String phoneNumber) {
		this(name, dni, address, phoneNumber);
		this.customerId = customerId;
	}



	public Customer(Customer c){
		this(c.getCustomerId(),c.getName(), c.getDni(), c.getAddress(), c.getPhoneNumber());
	}


	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "Customer{" +
				"customerId=" + customerId +
				", name='" + name + '\'' +
				", dni='" + dni + '\'' +
				", address='" + address + '\'' +
				", creationDate=" + creationDate +
				", phoneNumber='" + phoneNumber + '\'' +
				'}';
	}

}