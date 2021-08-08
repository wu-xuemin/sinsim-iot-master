package com.eservice.sinsimiot.dao;

import com.eservice.sinsimiot.common.Mapper;
import com.eservice.sinsimiot.model.staff_reissue_record.ReissueDTO;
import com.eservice.sinsimiot.model.staff_reissue_record.StaffReissueRecord;

import java.util.List;

public interface StaffReissueRecordMapper extends Mapper<StaffReissueRecord> {

    List<StaffReissueRecord> fetchReissueRecord(ReissueDTO reissueDTO);

    List<StaffReissueRecord> searchRecord(ReissueDTO reissueDTO);
}
