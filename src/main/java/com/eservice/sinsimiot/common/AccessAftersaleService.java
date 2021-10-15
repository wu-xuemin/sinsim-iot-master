package com.eservice.sinsimiot.common;

import com.eservice.sinsimiot.model.iot_machine.AftersaleMachine;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
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
public class AccessAftersaleService {

    private Connection aftersaleMysqlConn;

    /**
     * 售后数据库
     */
    @Value("${spring.datasource-aftersale-mysql-1.jdbc-url}")
    private String aftersale_db_url;

    @Value("${spring.datasource.username}")
    private String user_aftersale_db;

    @Value("${spring.datasource.password}")
    private String password_aftersale_db = "hello123!";

    /**
     * 生产部数据库
     */
//    @Value("${spring.datasource_sinsim_db.url}")
//    private String sinsim_db_url;
//
//    @Value("${spring.datasource_sinsim_db.username}")
//    private String user_sinsim_db;
//
//    @Value("${spring.datasource_sinsim_db.password}")
//    private String password_sinsim_db;

    private Logger logger = Logger.getLogger(AccessAftersaleService.class);

//    private List<MachineInfosInProcessDb> installedNotBoundedMachineList = new ArrayList<>();

    public List<AftersaleMachine> accessAs() {
        List<AftersaleMachine> aftersaleMachineList = null;
        try {
            aftersaleMysqlConn = DriverManager.getConnection(aftersale_db_url,user_aftersale_db,password_aftersale_db);

            ResultSet rs1 = aftersaleMysqlConn.prepareStatement("SELECT * FROM machine").executeQuery();
//            ResultSet rs2 = aftersaleMysqlConn.prepareStatement(" select m.*,mo.contract_ship_date,mt.`name` as machine_type_name," +                    "mo.needle_num,mo.head_num,mo.head_distance,mo.x_distance,mo.y_distance,mo.order_num, mt.name as machine_type_name,c.contract_num,c.customer_name" +
//                    " from  machine m " +
//                    "left join machine_order mo on mo.id=m.order_id " +
//                    "left join machine_type mt on mt.id=m.machine_type " +
//                    "left join contract c on c.id = mo.contract_id " +
//                    "where m.status='" + com.eservice.api.service.common.Constant.MACHINE_INSTALLED + "' ").executeQuery();
//            List<MachineInfosInProcessDb> installedMachineList = new ArrayList<>();
            aftersaleMachineList = new ArrayList<>();
            while(rs1.next()) {
                rs1.getString("status");
                AftersaleMachine aftersaleMachine = new AftersaleMachine();
                aftersaleMachine.setStatus(rs1.getString("status"));
                aftersaleMachine.setCustomer(rs1.getInt("customer"));
                aftersaleMachine.setNameplate(rs1.getString("nameplate"));
                aftersaleMachine.setOrderNum(rs1.getString("order_num"));
                aftersaleMachine.setHeadNum(rs1.getString("head_num"));
                aftersaleMachine.setNeedleNum(rs1.getString("needle_num"));
                aftersaleMachineList.add(aftersaleMachine);
            }
            logger.info("aftersaleMachineList.size:" + aftersaleMachineList.size());
            aftersaleMysqlConn.close();
//            sinsimMysqlConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("Query data exception: " + e.toString());
        }
        return aftersaleMachineList;
    }
}
