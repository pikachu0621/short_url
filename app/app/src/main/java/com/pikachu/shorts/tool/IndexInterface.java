package com.pikachu.shorts.tool;

import com.pikachu.shorts.cls.DataCls;

/**
 * @author Pikachu
 * @Project PK短链
 * @Package com.pikachu.shorts.tool
 * @Date 2021/8/16 ( 上午 9:58 )
 * @description
 */
public interface IndexInterface {


    /**
     * 登录失败
     * @param dataCls
     */
    void loginError(DataCls dataCls);

    /**
     * 登录成功
     * @param dataCls
     */
    void loginOK(DataCls dataCls);

    /**
     * 获取短链失败
     * @param dataCls
     */
    void shortError(DataCls dataCls);

    /**
     * 获取短链成功
     * @param dataCls
     */
    void shortOk(DataCls dataCls);

    /**
     * 启用或禁用失败
     * @param dataCls
     */
    void offAndOnError(DataCls dataCls);

    /**
     * 启用或禁用成功
     * @param dataCls
     */
    void offAndOnOk(DataCls dataCls);

    /**
     * 查访问量失败
     * @param dataCls
     */
    void lookNumError(DataCls dataCls);

    /**
     * 查访问量成功
     * @param dataCls
     */
    void lookNumOk(DataCls dataCls);

    /**
     * 读取一条短链失败
     * @param dataCls
     */
    void oneShortError(DataCls dataCls);

    /**
     * 读取一条短链成功
     * @param dataCls
     */
    void oneShortOk(DataCls dataCls);

    /**
     * 编辑失败
     * @param dataCls
     */
    void editError(DataCls dataCls);

    /**
     * 编辑成功
     * @param dataCls
     */
    void editOk(DataCls dataCls);

    /**
     * 加载短链历史失败
     * @param dataCls
     */
    void loadShortError(DataCls dataCls);

    /**
     * 加载短链历史成功
     * @param dataCls
     */
    void loadShortOk(DataCls dataCls);

    /**
     * 重置地址失败
     * @param dataCls
     */
    void reError(DataCls dataCls);

    /**
     * 重置地址成功
     * @param dataCls
     */
    void reOk(DataCls dataCls);


    /**
     * 删除短链失败
     * @param dataCls
     */
    void deleteError(DataCls dataCls);

    /**
     * 删除短链成功
     * @param dataCls
     */
    void deleteOk(DataCls dataCls);







}
