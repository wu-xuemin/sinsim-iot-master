package com.eservice.sinsimiot.dao;


import com.eservice.sinsimiot.common.Mapper;
import com.eservice.sinsimiot.model.log.LogRecord;
import com.eservice.sinsimiot.model.log.LogDTO;

import java.util.List;

/**
* @description :
* @author : silent
* @date: 2020/3/24 16:45
*/
public interface LogRecordMapper extends Mapper<LogRecord> {

    /**
     * 根据条件查询
     * @param logDTO
     * @return
     */
    List<LogRecord> findByCondition(LogDTO logDTO);
}
