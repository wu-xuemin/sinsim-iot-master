package com.eservice.sinsimiot.common;

import com.eservice.sinsimiot.model.iot_machine.AftersaleMachine;
import com.eservice.sinsimiot.model.iot_machine.SinsimProcessMachineType;
//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Eservice
 */
@Service
public class AccessSinsimProcessService {

    private Connection sinsimProcessMysqlConn;

    /**
     * 生产部数据库
     */
    @Value("${spring.datasource-sinsim_db-mysql-1.jdbc-url}")
    private String sinsim_db_url;

    @Value("${spring.datasource-sinsim_db-mysql-1.username}")
    private String user_sinsim_db;

    @Value("${spring.datasource-sinsim_db-mysql-1.password}")
    private String password_sinsim_db;

//    private Logger logger = Logger.getLogger(AccessSinsimProcessService.class);
    private Logger logger = LoggerFactory.getLogger(getClass());

    public List<SinsimProcessMachineType> getMachineType() {
        List<SinsimProcessMachineType> sinsimProcessMachineTypeList = null;
        try {
            sinsimProcessMysqlConn = DriverManager.getConnection(sinsim_db_url,user_sinsim_db,password_sinsim_db);

            ResultSet rs1 = sinsimProcessMysqlConn.prepareStatement("SELECT * FROM machine_type").executeQuery();
//            ResultSet rs2 = aftersaleMysqlConn.prepareStatement(" select m.*,mo.contract_ship_date,mt.`name` as machine_type_name," +                    "mo.needle_num,mo.head_num,mo.head_distance,mo.x_distance,mo.y_distance,mo.order_num, mt.name as machine_type_name,c.contract_num,c.customer_name" +
//                    " from  machine m " +
//                    "left join machine_order mo on mo.id=m.order_id " +
//                    "left join machine_type mt on mt.id=m.machine_type " +
//                    "left join contract c on c.id = mo.contract_id " +
//                    "where m.status='" + com.eservice.api.service.common.Constant.MACHINE_INSTALLED + "' ").executeQuery();
//            List<MachineInfosInProcessDb> installedMachineList = new ArrayList<>();
            sinsimProcessMachineTypeList = new ArrayList<>();
            while(rs1.next()) {
                SinsimProcessMachineType sinsimProcessMachineType = new SinsimProcessMachineType();
                sinsimProcessMachineType.setName(rs1.getString("name"));
                sinsimProcessMachineType.setFinished(rs1.getInt("finished"));
                sinsimProcessMachineTypeList.add(sinsimProcessMachineType);
            }
            logger.info("sinsimProcessMachineTypeList.size:" + sinsimProcessMachineTypeList.size());
            sinsimProcessMysqlConn.close();
//            sinsimMysqlConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("Query data exception: " + e.toString());
        }
        return sinsimProcessMachineTypeList;
    }
}
