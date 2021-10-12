package com.hankun.master.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author ：silent
 * @description ：
 * @date ：2020/11/4 11:47
 **/
@Service
@Slf4j
public class ScheduledService {

    @Value("${date.logger}")
    Integer loggerDate;
    @Value("${date.image}")
    Integer imageDate;
    @Value("${path.excel}")
    String excelPath;
    @Value("${path.upload}")
    String uploadPath;
    @Value("${path.logger}")
    String loggerPath;
    @Value("${path.zip}")
    private String zipPath;


//    @Scheduled(cron = "0 0 0 * * ?")
//    public void delFile() {
//        delFile(loggerPath, loggerDate);
//        delFile(uploadPath, imageDate);
//        delFile(excelPath, imageDate);
//        delFile(zipPath, imageDate);
//    }

//    public void delFile(String path, Integer date) {
//        File dir = new File(path);
//        if (dir.exists()) {
//            dir.setWritable(true, false);
//            if (dir.isDirectory()) {
//                for (File file : dir.listFiles()) {
////                    //跳过底库照的目录
////                    if (file.getPath().contains(Constant.IMAGE_PERSON.replace("/", ""))) {
////                        continue;
////                    }
//                    if (file.isDirectory()) {
//                        delFile(file.getPath(), date);
//                        continue;
//                    }
//                    try {
//                        Long changTime = file.lastModified();
//                        if (System.currentTimeMillis() - changTime > date * 24 * 60 * 60 * 1000L) {
//                            log.info(" File modification time  {} ", file.lastModified());
//                            if (file.delete()) {
//                                log.info(" {}  delete  file  success ", file.getName());
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        log.error(e.getMessage());
//                    }
//                }
//            } else {
//                log.warn(" {} is not directory ! ", path);
//            }
//        } else {
//            log.warn(" {} is not exist ! ", path);
//        }
//    }
//

}
