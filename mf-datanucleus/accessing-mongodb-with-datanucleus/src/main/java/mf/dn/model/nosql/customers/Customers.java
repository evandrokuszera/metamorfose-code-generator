package mf.dn.model.nosql.customers;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Embedded;
import mf.dn.model.nosql.orders.Orders;
import javax.persistence.OneToMany;

@Entity(name="Customers")
public class Customers{
	@Id
	private java.lang.Integer id_customer;
	private java.lang.String firstname;
	private java.lang.String lastname;
	private java.lang.String address1;
	private java.lang.String address2;
	private java.lang.String city;
	private java.lang.String state;
	private java.lang.String zip;
	private java.lang.String country;
	private java.lang.Integer region;
	private java.lang.String email;
	private java.lang.String phone;
	private java.lang.Integer creditcardtype;
	private java.lang.String creditcard;
	private java.lang.String creditcardexpiration;
	private java.lang.String username;
	private java.lang.String password;
	private java.lang.Integer age;
	private java.lang.Integer income;
	private java.lang.String gender;
	@OneToMany(mappedBy="customerid")
	private java.util.List<Orders> orders;
	public java.lang.Integer getId_customer(){
		return id_customer;
	}
	public void setId_customer(java.lang.Integer value) {
		this.id_customer = value;
	}
	public java.lang.String getFirstname(){
		return firstname;
	}
	public void setFirstname(java.lang.String value) {
		this.firstname = value;
	}
	public java.lang.String getLastname(){
		return lastname;
	}
	public void setLastname(java.lang.String value) {
		this.lastname = value;
	}
	public java.lang.String getAddress1(){
		return address1;
	}
	public void setAddress1(java.lang.String value) {
		this.address1 = value;
	}
	public java.lang.String getAddress2(){
		return address2;
	}
	public void setAddress2(java.lang.String value) {
		this.address2 = value;
	}
	public java.lang.String getCity(){
		return city;
	}
	public void setCity(java.lang.String value) {
		this.city = value;
	}
	public java.lang.String getState(){
		return state;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getZip(){
		return zip;
	}
	public void setZip(java.lang.String value) {
		this.zip = value;
	}
	public java.lang.String getCountry(){
		return country;
	}
	public void setCountry(java.lang.String value) {
		this.country = value;
	}
	public java.lang.Integer getRegion(){
		return region;
	}
	public void setRegion(java.lang.Integer value) {
		this.region = value;
	}
	public java.lang.String getEmail(){
		return email;
	}
	public void setEmail(java.lang.String value) {
		this.email = value;
	}
	public java.lang.String getPhone(){
		return phone;
	}
	public void setPhone(java.lang.String value) {
		this.phone = value;
	}
	public java.lang.Integer getCreditcardtype(){
		return creditcardtype;
	}
	public void setCreditcardtype(java.lang.Integer value) {
		this.creditcardtype = value;
	}
	public java.lang.String getCreditcard(){
		return creditcard;
	}
	public void setCreditcard(java.lang.String value) {
		this.creditcard = value;
	}
	public java.lang.String getCreditcardexpiration(){
		return creditcardexpiration;
	}
	public void setCreditcardexpiration(java.lang.String value) {
		this.creditcardexpiration = value;
	}
	public java.lang.String getUsername(){
		return username;
	}
	public void setUsername(java.lang.String value) {
		this.username = value;
	}
	public java.lang.String getPassword(){
		return password;
	}
	public void setPassword(java.lang.String value) {
		this.password = value;
	}
	public java.lang.Integer getAge(){
		return age;
	}
	public void setAge(java.lang.Integer value) {
		this.age = value;
	}
	public java.lang.Integer getIncome(){
		return income;
	}
	public void setIncome(java.lang.Integer value) {
		this.income = value;
	}
	public java.lang.String getGender(){
		return gender;
	}
	public void setGender(java.lang.String value) {
		this.gender = value;
	}
	public java.util.List<Orders> getOrders(){
		return orders;
	}
	public void setOrders(java.util.List<Orders> value) {
		this.orders = value;
	}
}
