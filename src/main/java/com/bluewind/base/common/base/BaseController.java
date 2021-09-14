package com.bluewind.base.common.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuxingyu01
 * @date 2021-09-13-17:08
 **/
public abstract class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
    public static final String RESULT_ROWS = "rows";
    public static final String RESULT_TOTLAL = "total";

    public BaseController() {
    }

}
