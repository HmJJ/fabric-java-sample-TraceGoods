package com.springboot.basic.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
public class DefaultModel extends SupportModel {
	
	private static final long serialVersionUID = -5915184808202092984L;
	
	@Id
	@Column(name="ID",length=64)
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "uuid2")
	@Getter
	@Setter
	private String id;
	
	@Version
	@Column(name="VERSION")
	protected Integer version;

	@Column(name="IS_DELETE",nullable=false)
	@Getter
	@Setter
	private Boolean delete = Boolean.FALSE;

	@Column(name="CREATE_DATE",nullable=false)
	@Getter
	@Setter
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate= new Date();

	@Column(name="MODIFY_DATE",nullable=false)
	@Getter
	@Setter
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifydate = new Date();
	
}
