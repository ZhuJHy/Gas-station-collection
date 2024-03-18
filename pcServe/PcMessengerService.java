package com.tulan.system.service.pc;


import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.system.api.domain.Messenger;
import com.tulan.system.common.vo.pc.AddMessenger;
import com.tulan.system.common.vo.pc.UpdateMessenger;

import java.util.List;

public interface PcMessengerService {


    /**
     * 查询信息员列表
     * @param messengerName
     * @param phone
     * @return
     */
    List<Messenger> selectList(String messengerName, String phone);

    /**
     * 添加信息员
     * @param addMessenger
     * @return
     */
    AjaxResult addMessenger(AddMessenger addMessenger);

    /**
     * 修改信息员
     * @param updateMessenger
     * @return
     */
    AjaxResult updateMessenger(UpdateMessenger updateMessenger);

    /**
     * 解绑信息员
     * @param mgId
     * @return
     */
    AjaxResult untieMessenger(String mgId);

    /**
     * 删除信息员
     * @param mgId
     * @return
     */
    AjaxResult delMessenger(String[] mgId);

    /**
     * 启用和禁用信息员
     * @param status
     * @param mgId
     * @return
     */
    AjaxResult updateStatus(Integer status, String mgId);

    /**
     * 查询信息员详情
     * @param mgId
     * @return
     */
    AjaxResult messengerDetail(String mgId);

    /**
     * 检索气站
     *
     * @param gasName
     * @param
     * @return
     */
    AjaxResult selectGas(String gasName);
}
