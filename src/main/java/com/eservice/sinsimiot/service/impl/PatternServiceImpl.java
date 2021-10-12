package com.eservice.sinsimiot.service.impl;

import com.eservice.sinsimiot.dao.PatternMapper;
import com.eservice.sinsimiot.model.pattern.Pattern;
import com.eservice.sinsimiot.service.PatternService;
import com.eservice.sinsimiot.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author eservice
* @date 2021/10/12.
*/
@Service
@Transactional
public class PatternServiceImpl extends AbstractService<Pattern> implements PatternService {
@Resource
private PatternMapper patternMapper;

}
