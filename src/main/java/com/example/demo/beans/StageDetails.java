package com.example.demo.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StageDetails implements Serializable {

    @ApiModelProperty(notes = "Product 'x' stage1 will be completed in stageFirst days")
    private Long stageFirst;
    @ApiModelProperty(notes = "Product 'x' stage2 will be completed in stageSecond days")
    private Long stageSecond;
    @ApiModelProperty(notes = "Product 'x' stage3 will be completed in stageThird days")
    private Long stageThird;
    @ApiModelProperty(notes = "Product 'x' stage4 will be completed in stageForth days")
    private Long stageFouth;

    public StageDetails() {
	super();
	// TODO Auto-generated constructor stub
    }

    public StageDetails(Long stageFirst, Long stageSecond, Long stageThird, Long stageFouth) {
	super();
	this.stageFirst = stageFirst;
	this.stageSecond = stageSecond;
	this.stageThird = stageThird;
	this.stageFouth = stageFouth;
    }

    public Long getStageFirst() {
	return stageFirst;
    }

    public void setStageFirst(Long stageFirst) {
	this.stageFirst = stageFirst;
    }

    public Long getStageSecond() {
	return stageSecond;
    }

    public void setStageSecond(Long stageSecond) {
	this.stageSecond = stageSecond;
    }

    public Long getStageThird() {
	return stageThird;
    }

    public void setStageThird(Long stageThird) {
	this.stageThird = stageThird;
    }

    public Long getStageFouth() {
	return stageFouth;
    }

    public void setStageFouth(Long stageFouth) {
	this.stageFouth = stageFouth;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((stageFirst == null) ? 0 : stageFirst.hashCode());
	result = prime * result + ((stageFouth == null) ? 0 : stageFouth.hashCode());
	result = prime * result + ((stageSecond == null) ? 0 : stageSecond.hashCode());
	result = prime * result + ((stageThird == null) ? 0 : stageThird.hashCode());
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
	StageDetails other = (StageDetails) obj;
	if (stageFirst == null) {
	    if (other.stageFirst != null)
		return false;
	} else if (!stageFirst.equals(other.stageFirst))
	    return false;
	if (stageFouth == null) {
	    if (other.stageFouth != null)
		return false;
	} else if (!stageFouth.equals(other.stageFouth))
	    return false;
	if (stageSecond == null) {
	    if (other.stageSecond != null)
		return false;
	} else if (!stageSecond.equals(other.stageSecond))
	    return false;
	if (stageThird == null) {
	    if (other.stageThird != null)
		return false;
	} else if (!stageThird.equals(other.stageThird))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "StageDetails [stageFirst=" + stageFirst + ", stageSecond=" + stageSecond + ", stageThird=" + stageThird
		+ ", stageFouth=" + stageFouth + "]";
    }

}
