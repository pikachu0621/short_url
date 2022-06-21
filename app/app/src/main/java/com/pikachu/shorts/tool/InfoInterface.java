package com.pikachu.shorts.tool;

import com.pikachu.shorts.cls.DataCls;

/**
 * @author Pikachu
 * @Project PK短链
 * @Package com.pikachu.shorts.tool
 * @Date 2021/8/21 ( 下午 10:35 )
 * @description
 */
public interface InfoInterface {



    /**
     * 查询失败
     * @param dataCls
     */
    void lookInfoError(DataCls dataCls);

    /**
     * 查询成功
     * @param dataCls
     */
    void lookInfoOk(DataCls dataCls);



    /**
     * 编辑失败
     * @param dataCls
     */
    void editInfoError(DataCls dataCls);

    /**
     * 编辑成功
     * @param dataCls
     */
    void editInfoOk(DataCls dataCls);

}
