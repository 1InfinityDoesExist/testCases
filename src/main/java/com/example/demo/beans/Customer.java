package com.example.demo.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.beans.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity(name = "Customer")
@Table(name = "customer")
@EntityListeners(AuditingEntityListener.class)
@TypeDefs({ @TypeDef(name = "ProductType", typeClass = ProductType.class) })
@ApiModel(value = "Cusotmer Class", description = "It Contains All The Customer Related Things")
public class Customer extends BaseEntity implements Serializable {

	@Column(name = "customer_name")
	@ApiModelProperty(notes = "Name Of The Customer")
	private String customerName;

	@NotBlank(message = "EmailID cannot be null or blank")
	@Column(name = "email", unique = true)
	@ApiModelProperty(notes = "Email of the customer")
	private String email;

	@Column(name = "customer_product", columnDefinition = "jsonb")
	@ApiModelProperty(notes = "The Products That Customer 'X' Buys")
	/*
	 * @Type(type = "com.example.demo.beans.ProductType", parameters = {
	 * 
	 * @org.hibernate.annotations.Parameter(name = "type", value = "list"),
	 * 
	 * @org.hibernate.annotations.Parameter(name = "element", value =
	 * "com.example.domo.beans.Product") })
	 */
	// @ElementCollection(targetClass = Product.class)
	// private Set<Product> product;

	@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	private Set<Product> product;

	@Column(name = "billing_date")
	@ApiModelProperty(notes = "Delivery Day")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate billingDate;

	@Column(name = "notes")
	@ApiModelProperty(notes = "Notes Form The Inventory To The Customer")
	private String notes;

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Customer(String customerName, @NotBlank(message = "EmailID cannot be null or blank") String email,
			Set<Product> product, LocalDate billingDate, String notes) {
		super();
		this.customerName = customerName;
		this.email = email;
		this.product = product;
		this.billingDate = billingDate;
		this.notes = notes;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		customerName = customerName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Product> getProduct() {
		return product;
	}

	public void setProduct(Set<Product> product) {
		this.product = product;
	}

	public LocalDate getBillingDate() {
		return billingDate;
	}

	public void setBillingDate(LocalDate billingDate) {
		this.billingDate = billingDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((customerName == null) ? 0 : customerName.hashCode());
		result = prime * result + ((billingDate == null) ? 0 : billingDate.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		if (billingDate == null) {
			if (other.billingDate != null)
				return false;
		} else if (!billingDate.equals(other.billingDate))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Customer [CustomerName=" + customerName + ", email=" + email + ", product=" + product + ", billingDate="
				+ billingDate + ", notes=" + notes + "]";
	}

}
