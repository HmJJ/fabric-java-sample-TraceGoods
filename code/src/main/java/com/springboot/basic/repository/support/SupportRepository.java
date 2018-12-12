package com.springboot.basic.repository.support;

import java.io.Serializable;

import com.springboot.basic.entity.SupportModel;

public interface SupportRepository<T extends SupportModel, PK extends Serializable> {

}