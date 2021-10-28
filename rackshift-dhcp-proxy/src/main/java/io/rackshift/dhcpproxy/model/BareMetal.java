package io.rackshift.dhcpproxy.model;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.dhcpproxy.util.MySqlUtil;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BareMetal {
    private String id;
    private String pxeMac;
    private String status;

    public String getPxeMac() {
        return pxeMac;
    }

    public void setPxeMac(String pxeMac) {
        this.pxeMac = pxeMac;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static BareMetal findByMac(String pxeMac) {
        String sql = "select id,pxe_mac,status from bare_metal where pxe_mac = ?";
        ResultSet r = MySqlUtil.select(sql, pxeMac);
        if (r == null) {
            return null;
        }
        try {
            if (r.next()) {
                BareMetal bareMetal = new BareMetal();
                bareMetal.setId(r.getString(1));
                bareMetal.setPxeMac(r.getString(2));
                bareMetal.setStatus(r.getString(3));
                return bareMetal;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public boolean discovered() {
        if (!this.status.equalsIgnoreCase("onrack") && !this.status.equalsIgnoreCase("discovering")) {
            return true;
        }

        return false;
    }

    public boolean isRunningTask() {
        String sql = "select * from task where bare_metal_id = ?";
        ResultSet r = MySqlUtil.select(sql, id);
        try {
            if (r != null && r.next())
                return true;
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean isRequestProfile() {
        String graphObjectStr = null;
        ResultSet r = MySqlUtil.select("select graph_objects from task where bare_metal_id = ? and status = 'running'", id);
        try {
            if (r != null && r.next()) {
                graphObjectStr = r.getString(1);
            } else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (StringUtils.isNotBlank(graphObjectStr)) {
            JSONObject tasks = JSONObject.parseObject(graphObjectStr);
            boolean shouldProxy = false;
            for (String s : tasks.keySet()) {
                JSONObject task = tasks.getJSONObject(s);
                if (task.containsKey("options")) {
                    if (task.getJSONObject("options").containsKey("profile") && !StringUtils.equals(task.getString("state"), "succeeded") ) {
                        shouldProxy = true;
                        break;
                    }
                }
            }
            return shouldProxy;
        }

        return false;
    }
}
