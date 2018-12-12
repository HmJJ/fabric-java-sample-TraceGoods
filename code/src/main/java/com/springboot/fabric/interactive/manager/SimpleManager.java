package com.springboot.fabric.interactive.manager;

import com.springboot.fabric.base.BaseManager;
import com.springboot.fabric.code.FabricManager;
import com.springboot.fabric.code.OrgManager;

/**
 * 描述：
 *
 */
public class SimpleManager extends BaseManager {

    private static SimpleManager instance;

    private FabricManager fabricManager;

    public static SimpleManager obtain() throws Exception {
        if (null == instance) {
            synchronized (SimpleManager.class) {
                if (null == instance) {
                    instance = new SimpleManager();
                }
            }
        }
        return instance;
    }

    private SimpleManager() throws Exception {
        fabricManager = obtainFabricManager();
    }

    public FabricManager getFabricManager() {
        return fabricManager;
    }

    private FabricManager obtainFabricManager() throws Exception {
        OrgManager orgManager = new OrgManager();
        OrgManager orgManager2 = new OrgManager();
        orgManager
                .init("Org1", false, false)
                .setUser("Admin", getCryptoConfigPath("abric"), getChannleArtifactsPath("abric"))
//                .setUser("haha", "mAtBqOymDtBI", "org1.department1", new HashSet<>(Arrays.asList("hf.Revoker", "hf.GenCRL", "admin")), getCryptoConfigPath("aberic"))
                .setCA("ca", "http://39.108.64.144:7054")
                .setPeers("Org1MSP", "org1.example.com")
                .addPeer("peer0.org1.example.com", "peer0.org1.example.com", "grpc://39.108.64.144:7051", "grpc://39.108.64.144:7053", true)
                .setOrderers("example.com")
                .addOrderer("orderer.example.com", "grpc://39.108.64.144:7050")
                .setChannel("mychannel")
                .setChainCode("example02", "/opt/gopath", "github.com/hyperledger/fabric/aberic/chaincode/go/example02", "1.0", 90000, 240)
                .setBlockListener(map -> {
                    logger.debug(map.get("code"));
                    logger.debug(map.get("data"));
                })
                .add();
        orgManager2
        .init("Org1", false, false)
        .setUser("Admin", getCryptoConfigPath("sheep"), getChannleArtifactsPath("sheep"))
//        .setUser("haha", "mAtBqOymDtBI", "org1.department1", new HashSet<>(Arrays.asList("hf.Revoker", "hf.GenCRL", "admin")), getCryptoConfigPath("aberic"))
        .setCA("ca", "http://47.244.142.213:7054")
        .setPeers("Org1MSP", "org1.example.com")
        .addPeer("peer0.org1.example.com", "peer0.org1.example.com", "grpc://47.244.142.213:7051", "grpc://47.244.142.213:7053", true)
        .setOrderers("example.com")
        .addOrderer("orderer.example.com", "grpc://47.244.142.213:7050")
        .setChannel("mychannel")
        .setChainCode("tracegoods", "/opt/gopath", "github.com/hyperledger/fabric/sheep/chaincode/go/trace_goods", "1.0", 90000, 240)
        .setBlockListener(map -> {
            logger.debug(map.get("code"));
            logger.debug(map.get("data"));
        })
        .add();
        return orgManager.use("Org1");
    }

}
