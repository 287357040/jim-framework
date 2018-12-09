package com.jim.framework.core.cloud.api;

import com.jim.framework.common.mvc.JimResponse;
import com.jim.framework.core.cloud.registry.LinkedInfo;

/**
 * 注册中ixnde
 * Created by celiang.hu on 2018-11-22.
 */
public interface LinkedService {
    /**
     * 与注册服务器尝试连接
     * @return
     */
    JimResponse<Boolean> touch();

    /**
     * 校验ticket,此处参数不传入应用实体，认证只对传入的票据做校验即可
     * @param ticket
     * @return
     */
    JimResponse<LinkedInfo> checkTicket(String ticket);
}
