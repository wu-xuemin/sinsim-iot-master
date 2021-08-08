package com.eservice.sinsimiot.service;

import com.eservice.sinsimiot.common.Service;
import com.eservice.sinsimiot.model.staff_reissue_record.ReissueDTO;
import com.eservice.sinsimiot.model.staff_reissue_record.StaffReissueRecord;

import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Mr.xie
 * @date 2021/06/01.
 */
public interface StaffReissueRecordService extends Service<StaffReissueRecord> {

    StaffReissueRecord saveReissueRecord(ReissueDTO reissueDTO);

    StaffReissueRecord confirmReissueRecord(ReissueDTO reissueDTO);

    List<StaffReissueRecord> fetchReissueRecord(ReissueDTO reissueDTO);

    List<StaffReissueRecord> searchRecord(ReissueDTO reissueDTO);

    String exportReissueRecord(ReissueDTO reissueDTO);
}
