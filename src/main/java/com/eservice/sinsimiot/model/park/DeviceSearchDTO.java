package com.eservice.sinsimiot.model.park;

import com.eservice.sinsimiot.common.SearchDTO;

/**
 * @author xyh
 * @version v1.0
 * @description TODO
 * @date 2021/5/25 10:45
 */
public class DeviceSearchDTO extends SearchDTO {
    private String parkId;

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }
}
