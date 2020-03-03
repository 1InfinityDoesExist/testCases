package com.example.demo.beans;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.example.demo.beans.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "customer_product")
@ApiModel(value = "CusomerProduct Class", description = "Contains Information Related To CusomerProduct")
public class CustomerProduct extends BaseEntity implements Serializable {

	@Column(name = "customer_emailid")
	@ApiModelProperty(notes = "Email Id Of the CustomeProduct")
	private String emailId;

	@Column(name = "customer_id")
	@ApiModelProperty(notes = "Cusotmer Id of the CusotmerProduct")
	private Long customerId;

	@Column(name = "product_name")
	@ApiModelProperty(notes = "ProductName of the CusomeProduct")
	private String productName;

	@Column(name = "product_delivery_date")
	@ApiModelProperty(notes = "Delivery Day")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate productDeliveryDate;

	@Column(name = "product_quantity")
	@ApiModelProperty(notes = "Product Quantity of the CustomerProduct")
	private Long productQuantity;

	@Column(name = "billing_date")
	@ApiModelProperty(notes = "Delivery Day")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate billingDate;

	@Column(name = "customer_name")
	@ApiModelProperty(notes = "Customer Name")
	private String customerName;

	@Column(name = "delivery_status")
	@ApiModelProperty(notes = "This will simplyle says whether the deliverydate is up to mark or not...!!!")
	private String deliveryStatus;

	public CustomerProduct() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomerProduct(String emailId, Long customerId, String productName, LocalDate productDeliveryDate,
			Long productQuantity, LocalDate billingDate, String customerName, String deliveryStatus) {
		super();
		this.emailId = emailId;
		this.customerId = customerId;
		this.productName = productName;
		this.productDeliveryDate = productDeliveryDate;
		this.productQuantity = productQuantity;
		this.billingDate = billingDate;
		this.customerName = customerName;
		this.deliveryStatus = deliveryStatus;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public LocalDate getProductDeliveryDate() {
		return productDeliveryDate;
	}

	public void setProductDeliveryDate(LocalDate productDeliveryDate) {
		this.productDeliveryDate = productDeliveryDate;
	}

	public Long getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(Long productQuantity) {
		this.productQuantity = productQuantity;
	}

	public LocalDate getBillingDate() {
		return billingDate;
	}

	public void setBillingDate(LocalDate billingDate) {
		this.billingDate = billingDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((billingDate == null) ? 0 : billingDate.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((customerName == null) ? 0 : customerName.hashCode());
		result = prime * result + ((deliveryStatus == null) ? 0 : deliveryStatus.hashCode());
		result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
		result = prime * result + ((productDeliveryDate == null) ? 0 : productDeliveryDate.hashCode());
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		result = prime * result + ((productQuantity == null) ? 0 : productQuantity.hashCode());
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
		CustomerProduct other = (CustomerProduct) obj;
		if (billingDate == null) {
			if (other.billingDate != null)
				return false;
		} else if (!billingDate.equals(other.billingDate))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		if (deliveryStatus == null) {
			if (other.deliveryStatus != null)
				return false;
		} else if (!deliveryStatus.equals(other.deliveryStatus))
			return false;
		if (emailId == null) {
			if (other.emailId != null)
				return false;
		} else if (!emailId.equals(other.emailId))
			return false;
		if (productDeliveryDate == null) {
			if (other.productDeliveryDate != null)
				return false;
		} else if (!productDeliveryDate.equals(other.productDeliveryDate))
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (productQuantity == null) {
			if (other.productQuantity != null)
				return false;
		} else if (!productQuantity.equals(other.productQuantity))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CustomerProduct [emailId=" + emailId + ", customerId=" + customerId + ", productName=" + productName
				+ ", productDeliveryDate=" + productDeliveryDate + ", productQuantity=" + productQuantity
				+ ", billingDate=" + billingDate + ", customerName=" + customerName + ", deliveryStatus="
				+ deliveryStatus + "]";
	}

}
