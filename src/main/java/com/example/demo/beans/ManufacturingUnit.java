package com.example.demo.beans;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Type;
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

@Entity(name = "ManufacturingUnit")
@Table(name = "manufacturing_unit")
@EntityListeners(AuditingEntityListener.class)
@TypeDefs({ @TypeDef(name = "StageDetailsTypes", typeClass = StageDetailsTypes.class) })
@ApiModel(value = "ManufacturingUnit Class", description = "Manufacturing Unit Class")
public class ManufacturingUnit extends BaseEntity implements Serializable {

	@NotBlank(message = "ProductName can't be blank. Please Provide Some Name to The Product")
	@Column(name = "product_name", nullable = false)
	@ApiModelProperty(notes = "This is the name of the product")
	private String productName;

	@Column(name = "batch_number")
	@ApiModelProperty(notes = "BatchNumber - Which Batch It Belongs Too.")
	private Long batchNumber;

	@Column(name = "delete_flag", columnDefinition = "BOOLEAN DEFAULT false")
	@ApiModelProperty(notes = "To keep record of product manufacture previously")
	private Boolean deleteFlag;

	@Column(name = "four_stage", columnDefinition = "jsonb")
	@ApiModelProperty(notes = "Stages Details Like Stage1 Takes 'x'days to be Completed and so on")
	@Type(type = "StageDetailsTypes")
	private StageDetails stage;

	@Column(name = "manufactured_date")
	@ApiModelProperty(notes = "The Day Product Got Ready To Be Transported To The Invertory")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate manufacturedEndDate;

	@Column(name = "manufacture_start_date")
	@ApiModelProperty(notes = "The Day Prduct Started To Be Manufacture")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate manufacturingStartingDate;

	@Column(name = "total_prduct")
	@ApiModelProperty(notes = "How many products has been manufactured in this particular batch")
	private Long productCount;

	@Column(name = "availability_date")
	@ApiModelProperty(notes = "When this product will be launched in the market... means when it will be delivered to the inventory...!!!")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate availibility_date;

	public ManufacturingUnit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ManufacturingUnit(
			@NotBlank(message = "ProductName can't be blank. Please Provide Some Name to The Product") String productName,
			Long batchNumber, Boolean deleteFlag, StageDetails stage, LocalDate manufacturedEndDate,
			LocalDate manufacturingStartingDate, Long productCount, LocalDate availibility_date) {
		super();
		this.productName = productName;
		this.batchNumber = batchNumber;
		this.deleteFlag = deleteFlag;
		this.stage = stage;
		this.manufacturedEndDate = manufacturedEndDate;
		this.manufacturingStartingDate = manufacturingStartingDate;
		this.productCount = productCount;
		this.availibility_date = availibility_date;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Long batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Boolean getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public StageDetails getStage() {
		return stage;
	}

	public void setStage(StageDetails stage) {
		this.stage = stage;
	}

	public LocalDate getManufacturedEndDate() {
		return manufacturedEndDate;
	}

	public void setManufacturedEndDate(LocalDate manufacturedEndDate) {
		this.manufacturedEndDate = manufacturedEndDate;
	}

	public LocalDate getManufacturingStartingDate() {
		return manufacturingStartingDate;
	}

	public void setManufacturingStartingDate(LocalDate manufacturingStartingDate) {
		this.manufacturingStartingDate = manufacturingStartingDate;
	}

	public Long getProductCount() {
		return productCount;
	}

	public void setProductCount(Long productCount) {
		this.productCount = productCount;
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
		result = prime * result + ((availibility_date == null) ? 0 : availibility_date.hashCode());
		result = prime * result + ((batchNumber == null) ? 0 : batchNumber.hashCode());
		result = prime * result + ((deleteFlag == null) ? 0 : deleteFlag.hashCode());
		result = prime * result + ((manufacturedEndDate == null) ? 0 : manufacturedEndDate.hashCode());
		result = prime * result + ((manufacturingStartingDate == null) ? 0 : manufacturingStartingDate.hashCode());
		result = prime * result + ((productCount == null) ? 0 : productCount.hashCode());
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		result = prime * result + ((stage == null) ? 0 : stage.hashCode());
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
		ManufacturingUnit other = (ManufacturingUnit) obj;
		if (availibility_date == null) {
			if (other.availibility_date != null)
				return false;
		} else if (!availibility_date.equals(other.availibility_date))
			return false;
		if (batchNumber == null) {
			if (other.batchNumber != null)
				return false;
		} else if (!batchNumber.equals(other.batchNumber))
			return false;
		if (deleteFlag == null) {
			if (other.deleteFlag != null)
				return false;
		} else if (!deleteFlag.equals(other.deleteFlag))
			return false;
		if (manufacturedEndDate == null) {
			if (other.manufacturedEndDate != null)
				return false;
		} else if (!manufacturedEndDate.equals(other.manufacturedEndDate))
			return false;
		if (manufacturingStartingDate == null) {
			if (other.manufacturingStartingDate != null)
				return false;
		} else if (!manufacturingStartingDate.equals(other.manufacturingStartingDate))
			return false;
		if (productCount == null) {
			if (other.productCount != null)
				return false;
		} else if (!productCount.equals(other.productCount))
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (stage == null) {
			if (other.stage != null)
				return false;
		} else if (!stage.equals(other.stage))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ManufacturingUnit [productName=" + productName + ", batchNumber=" + batchNumber + ", deleteFlag="
				+ deleteFlag + ", stage=" + stage + ", manufacturedEndDate=" + manufacturedEndDate
				+ ", manufacturingStartingDate=" + manufacturingStartingDate + ", productCount=" + productCount
				+ ", availibility_date=" + availibility_date + "]";
	}

}
