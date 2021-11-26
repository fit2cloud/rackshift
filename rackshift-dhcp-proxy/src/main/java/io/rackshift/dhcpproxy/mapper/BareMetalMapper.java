package io.rackshift.dhcpproxy.mapper;

import io.rackshift.dhcpproxy.model.BareMetal;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface BareMetalMapper {

    @Results(id = "BareMetalResult", value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "pxe_mac", property = "pxeMac"),
            @Result(column = "status", property = "status"),
    })
    @Select("select * from bare_metal where pxe_mac = #{pxeMac}")
    BareMetal findByMac(String pxeMac);
}
