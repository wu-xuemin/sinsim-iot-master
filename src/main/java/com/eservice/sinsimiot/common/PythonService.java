package com.eservice.sinsimiot.common;

import com.eservice.sinsimiot.util.ProcessStream;
//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

@Service
public class PythonService {

//    private Logger logger = Logger.getLogger(PythonService.class);
    private Logger logger = LoggerFactory.getLogger(getClass());

    public String callPythonScript(String pythonFile, String arg1Input) {

        logger.info("call python: " + pythonFile + ", param:" + arg1Input);
        String line = null;

        String command = pythonFile;// C:\Users\wxm\Desktop\xs\IOT\sinsim-iot-master\iot-manage\pythonAccessMongo.py
        String params = arg1Input;//
     //   params = "\"" + params + "\"";
        File pythonFileExist = null;
        pythonFileExist = new File(pythonFile);
        if (!pythonFileExist.exists()) {
            logger.info(pythonFileExist + " 不存在");
            return pythonFileExist +" 不存在";
        } else {
            logger.info(pythonFileExist + "文件存在OK");
        }
        String[] cmd = new String[]{"python3",command,params};
        String charset = "UTF-8";
//        Map<String,Object> rtnMap = new HashMap<>();
        StringBuffer rtnSb = new StringBuffer();
        try {
            Process process = Runtime.getRuntime().exec(cmd);

            // error的要单独开一个线程处理。其实最好分成两个子线程分别处理标准输出流和错误输出流
//            ProcessStream stderr = new ProcessStream(process.getErrorStream(), "ERROR", charset);
//            stderr.start();
// 获取标准输出流的内容
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream(), charset));
            while ((line = stdout.readLine()) != null) {
//                rtnSb.append(line).append("\r\n");
                rtnSb.append(line);
            }
//            rtnMap.put("result", rtnSb.toString());
//            rtnMap.put("error", stderr.getContent());
//关闭流
            stdout.close();
            int status = process.waitFor();
            if (status != 0) {
                System.out.println("return value:" + status);
            }
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        return rtnSb.toString();
    }

}
