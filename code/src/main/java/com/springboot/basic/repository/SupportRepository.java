package com.springboot.basic.repository;

import java.io.Serializable;

import com.springboot.basic.entity.SupportModel;

public interface SupportRepository<T extends SupportModel, PK extends Serializable> {

}
