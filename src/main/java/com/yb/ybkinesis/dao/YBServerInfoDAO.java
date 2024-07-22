package com.yb.ybkinesis.dao;


import com.yb.ybkinesis.model.YBServerModel;

import java.util.List;

public interface YBServerInfoDAO {
    List<YBServerModel> getAll();
}
