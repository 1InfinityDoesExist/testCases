package com.example.demo.beans;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.example.demo.beans.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity(name = "Product")
@Table(name = "product")
@ApiModel(value = "Prouct", description = "Product Model Class")
public class Product extends BaseEntity implements Serializable {

	@NotBlank(message = "ProductName can't be blank. Please Provide Some Name to The Product")
	@Column(name = "product_name")
	@ApiModelProperty(notes = "This is the name of the product")
	private String productName;

	@ApiModelProperty(notes = "The Number of Product Currently Available In the Inventory")
	@Column(name = "available_product_count")
	private Long availableProductCount;

	@Column(name = "manufactured_date")
	@ApiModelProperty(notes = "The Day Product Got Ready To Be Transported To The Invertory")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate manufacturedDate;

	@ManyToOne // (fetch=FetchType.EAGER, cascade= {CascadeType.MERGE})
	@JoinColumn
	private Customer customer;

	@Column(name = "availability_date")
	@ApiModelProperty(notes = "When this product will be launched in the market... means when it will be delivered to the inventory...!!!")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate availibility_date;

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Product(
			@NotBlank(message = "ProductName can't be blank. Please Provide Some Name to The Product") String productName,
			Long availableProductCount, LocalDate manufacturedDate, Customer customer, LocalDate availibility_date) {
		super();
		this.productName = productName;
		this.availableProductCount = availableProductCount;
		this.manufacturedDate = manufacturedDate;
		this.customer = customer;
		this.availibility_date = availibility_date;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getAvailableProductCount() {
		return availableProductCount;
	}

	public void setAvailableProductCount(Long availableProductCount) {
		this.availableProductCount = availableProductCount;
	}

	public LocalDate getManufacturedDate() {
		return manufacturedDate;
	}

	public void setManufacturedDate(LocalDate manufacturedDate) {
		this.manufacturedDate = manufacturedDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public LocalDate getAvailibility_date() {
		return availibility_date;
	}

	public void setAvailibility_date(LocalDate availibility_date) {
		this.availibility_date = availibility_date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((availableProductCount == null) ? 0 : availableProductCount.hashCode());
		result = prime * result + ((availibility_date == null) ? 0 : availibility_date.hashCode());
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((manufacturedDate == null) ? 0 : manufacturedDate.hashCode());
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
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
		Product other = (Product) obj;
		if (availableProductCount == null) {
			if (other.availableProductCount != null)
				return false;
		} else if (!availableProductCount.equals(other.availableProductCount))
			return false;
		if (availibility_date == null) {
			if (other.availibility_date != null)
				return false;
		} else if (!availibility_date.equals(other.availibility_date))
			return false;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (manufacturedDate == null) {
			if (other.manufacturedDate != null)
				return false;
		} else if (!manufacturedDate.equals(other.manufacturedDate))
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product [productName=" + productName + ", availableProductCount=" + availableProductCount
				+ ", manufacturedDate=" + manufacturedDate + ", customer=" + customer + ", availibility_date="
				+ availibility_date + "]";
	}

}