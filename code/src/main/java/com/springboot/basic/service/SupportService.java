package com.springboot.basic.service;

import java.io.Serializable;

import com.springboot.basic.entity.SupportModel;

public interface SupportService<T extends SupportModel, PK extends Serializable> {

}
