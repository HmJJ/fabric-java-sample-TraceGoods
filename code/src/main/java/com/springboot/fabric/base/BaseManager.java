package com.springboot.fabric.base;


import java.io.File;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class BaseManager {

    protected static Logger logger = LogManager.getLogger(BaseManager.class);

    /**
     * 获取channel-artifacts配置路径
     *
     * @return /WEB-INF/classes/fabric/channel-artifacts/
     */
    protected String getChannleArtifactsPath(String module) {
        String directorys = BaseManager.class.getClassLoader().getResource("fabric").getFile();
//    	String directorys = "/opt/gopath/src/github.com/hyperledger/fabric";
    	logger.debug("directorys = " + directorys);
        File directory = new File(directorys);
        logger.debug("directory = " + directory.getPath());

        return directory.getPath() + "/" + module + "/channel-artifacts/";
    }

    /**
     * 获取crypto-config配置路径
     *
     * @return /WEB-INF/classes/fabric/crypto-config/
     */
    protected String getCryptoConfigPath(String module) {
        String directorys = BaseManager.class.getClassLoader().getResource("fabric").getFile();
//    	String directorys = "/opt/gopath/src/github.com/hyperledger/fabric";
    	logger.debug("directorys = " + directorys);
        System.out.println("directorysdirectorys:" + BaseManager.class.getClassLoader());
        File directory = new File(directorys);
        logger.debug("directory = " + directory.getPath());

        return directory.getPath() + "/" + module + "/crypto-config/";
    }

}
