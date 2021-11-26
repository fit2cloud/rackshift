package io.rackshift.dhcpproxy.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MySqlUtil {

    private static SqlSessionFactory sqlSessionFactory;

    static {

        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (Exception e) {
            ConsoleUtil.log("mybatis initialazation failed!: " + e);
        }
    }

    public static SqlSession getConn() {
        return sqlSessionFactory.openSession();
    }
}


