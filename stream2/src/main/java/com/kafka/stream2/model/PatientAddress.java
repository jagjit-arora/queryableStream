package com.kafka.stream2.model;

public class PatientAddress {
	private int patientId;
	private String name;
	private int contact;
	private Address address;
	private String gender;
	
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getContact() {
		return contact;
	}
	public void setContact(int contact) {
		this.contact = contact;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public PatientAddress(int patientId,String name,Address address,int contact,String gender) {
		this.patientId=patientId;
		this.address=address;
		this.name=name;
		this.gender=gender;
		this.contact=contact;
	}
	

}
