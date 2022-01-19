package com.eservice.sinsimiot.service.impl;

import com.eservice.sinsimiot.dao.MalfunctionMapper;
import com.eservice.sinsimiot.model.malfunction.Malfunction;
import com.eservice.sinsimiot.service.MalfunctionService;
import com.eservice.sinsimiot.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author eservice
* @date 2022/01/19.
*/
@Service
@Transactional
public class MalfunctionServiceImpl extends AbstractService<Malfunction> implements MalfunctionService {
@Resource
private MalfunctionMapper malfunctionMapper;

}
