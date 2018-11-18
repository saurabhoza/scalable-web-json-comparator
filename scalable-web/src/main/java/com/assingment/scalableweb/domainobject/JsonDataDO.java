package com.assingment.scalableweb.domainobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class JsonDataDO {

	public JsonDataDO(long id) {
		super();
		this.id = id;
	}

	/**
	 * Using the primitive type, since it does not accept null values.
	 */
	@Id
	private long id;
	
	/**
	 * Left side of the comparison
	 */
	@Lob
	@Column(length = 32000)
	private String left;
	
	/**
	 * Right side of the comparison
	 */
	@Lob
	@Column(length = 32000)
	private String right;

	/**
	 * Empty constructor for creating instances
	 */
	public JsonDataDO() {

	}

	/**
	 * This constructor creates new object instances for validation and persistence.
	 * 
	 * @param id of the object. It must to be informed. It is not auto-generated.
	 * @param left side of data is being sent through the request.
	 * @param right side of data is being sent through the request.
	 */
	public JsonDataDO(long id, String left, String right) {
		this.id = id;
		this.left = left;
		this.right = right;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}
	

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object that) {
		return EqualsBuilder.reflectionEquals(this, that);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}