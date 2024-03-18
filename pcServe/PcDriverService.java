package com.tulan.system.service.pc;

import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.system.common.resp.pc.PcDriverDto;
import com.tulan.system.common.vo.pc.AddDriverVo;
import com.tulan.system.common.vo.pc.UpdateDriverVo;

import java.util.List;

public interface PcDriverService {

    /**
     * 司机列表查询
     * @param driverName
     * @param phone
     * @return
     */
    List<PcDriverDto> selectDriver(String driverName, String phone,String escortStation);

    /**
     * 司机详情查询
     *
     * @param dId
     * @return
     */
    AjaxResult driverDetail(String dId);

    /**
     * 添加司机
     * @param addDriverVo
     * @return
     */
    AjaxResult addDriver(AddDriverVo addDriverVo);

    /**
     * 修改司机信息
     * @param updateDriverVo
     * @return
     */
    AjaxResult updateDriver(UpdateDriverVo updateDriverVo);

    /**
     * 删除司机
     * @param dId
     * @return
     */
    AjaxResult deleteDriver(String[] dId);

    /**
     * 解绑司机与气站
     * @param dId
     * @param gasId
     * @return
     */
    AjaxResult delGasDriver(String dId, String gasId);

}
