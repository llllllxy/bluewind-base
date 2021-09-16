package com.bluewind.base.common.util;

import com.bluewind.base.common.util.http.HttpClientUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author liuxingyu01
 * @date 2021-09-16-13:55
 **/
public class AddressUtils {
    /**
     * 根据IP地址获取地理位置
     */
    public static String getAddressByIP(String ip) {
        // ip = "123.57.11.55";

        if (StringUtils.isBlank(ip)) {
            return "ip为空，无法获取位置";
        }
        if ("127.0.0.1".equals(ip)) {
            return "局域网，无法获取位置";
        }
        String url = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?resource_id=6006&format=json&query=" + ip;
        String result = HttpClientUtils.get(url);
        Map resultMap = JsonTool.getMapFromJsonString(result);
        String status = Optional.ofNullable(resultMap.get("status")).orElse("").toString();
        if (StringUtils.isNotBlank(status) && status.equals("0")) {
            List resultList = (List) resultMap.get("data");
            if (CollectionUtils.isNotEmpty(resultList)) {
                Map dataMap = (Map) resultList.get(0);
                return (String) dataMap.get("location");
            } else {
                return "获取位置失败";
            }
        } else {
            return "获取位置失败";
        }
    }

    public static void main(String[] args) {
        System.out.println(getAddressByIP(null));
    }
}
