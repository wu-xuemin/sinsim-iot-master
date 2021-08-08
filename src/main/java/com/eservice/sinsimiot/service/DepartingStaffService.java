package com.eservice.sinsimiot.service;

import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.Service;
import com.eservice.sinsimiot.model.departing_staff.DepartingStaff;
import com.eservice.sinsimiot.model.staff.StaffSearchDTO;

import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Mr.xie
 * @date 2021/06/07.
 */
public interface DepartingStaffService extends Service<DepartingStaff> {

    Result settingStaff(DepartingStaff departingStaff);

    List<DepartingStaff> findByKey(StaffSearchDTO staffSearchDTO);

}
