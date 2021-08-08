package com.eservice.sinsimiot.dao;

import com.eservice.sinsimiot.common.Mapper;
import com.eservice.sinsimiot.model.departing_staff.DepartingStaff;
import com.eservice.sinsimiot.model.staff.StaffSearchDTO;

import java.util.List;

public interface DepartingStaffMapper extends Mapper<DepartingStaff> {

    List<DepartingStaff> findByKey(StaffSearchDTO staffSearchDTO);
}
