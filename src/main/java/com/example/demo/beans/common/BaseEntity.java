package com.example.demo.beans.common;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.solr.core.mapping.Indexed;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.swagger.annotations.ApiModelProperty;

@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "objectid_generator")
    @SequenceGenerator(name = "objectid_generator", sequenceName = "objectid_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "id")
    @ApiModelProperty(notes = "This Is The PrimaryKey Of Product")
    private Long id;
    @Column
    @CreationTimestamp
    @ApiModelProperty(notes = "Creation Datetime of the product record")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime creationDate;

    @Column
    @UpdateTimestamp
    @ApiModelProperty(notes = "Last Updation Datetime of the product record")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Indexed
    private LocalDateTime lastModificationDate;

    public BaseEntity() {
	super();
	// TODO Auto-generated constructor stub
    }

    public BaseEntity(Long id, LocalDateTime creationDate, LocalDateTime lastModificationDate) {
	super();
	this.id = id;
	this.creationDate = creationDate;
	this.lastModificationDate = lastModificationDate;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public LocalDateTime getCreationDate() {
	return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
	this.creationDate = creationDate;
    }

    public LocalDateTime getLastModificationDate() {
	return lastModificationDate;
    }

    public void setLastModificationDate(LocalDateTime lastModificationDate) {
	this.lastModificationDate = lastModificationDate;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	result = prime * result + ((lastModificationDate == null) ? 0 : lastModificationDate.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	BaseEntity other = (BaseEntity) obj;
	if (creationDate == null) {
	    if (other.creationDate != null)
		return false;
	} else if (!creationDate.equals(other.creationDate))
	    return false;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	if (lastModificationDate == null) {
	    if (other.lastModificationDate != null)
		return false;
	} else if (!lastModificationDate.equals(other.lastModificationDate))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "BaseEntity [id=" + id + ", creationDate=" + creationDate + ", lastModificationDate="
		+ lastModificationDate + "]";
    }

}
