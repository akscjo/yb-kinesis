package com.yb.ybkinesis.controller;

import com.yb.ybkinesis.dao.YBServerInfoDAO;
import com.yb.ybkinesis.model.YBServerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class YBServerInfoController {
    @Autowired
    private YBServerInfoDAO ybServerInfoDAO;

    @GetMapping("/api/ybserverinfo")
    public List<YBServerModel> getYBServerInfo(){
        List<YBServerModel> serverInfoList = new ArrayList<>();
        serverInfoList = ybServerInfoDAO.getAll();
        // Add dummy data for testing
//        YBServerModel test = new YBServerModel();
//        test.setHost("127.0.0.2");
//        test.setPort("5433");
//        test.setCloud("cloud1");
//        test.setRegion("datacenter1");
//        test.setZone("rack2");
//
//        YBServerModel test2 = new YBServerModel();
//        test2.setHost("127.0.0.3");
//        test2.setPort("5433");
//        test2.setCloud("cloud1");
//        test2.setRegion("datacenter1");
//        test2.setZone("rack3");
//
//        serverInfoList.add(test);
//        serverInfoList.add(test2);

        return serverInfoList;
    }
}

