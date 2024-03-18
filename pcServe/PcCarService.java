package com.tulan.system.service.pc;

import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.system.common.resp.pc.PcCarDto;
import com.tulan.system.common.vo.pc.AddCarVo;
import com.tulan.system.common.vo.pc.UpdateCarVo;

import java.util.List;

public interface PcCarService {

    /**
     * 条件查询车辆
     * @return
     */
    List<PcCarDto> selectCar(String carName, String carNumber, Integer carStatus);

    /**
     * 车辆详情查询
     * @param cId
     * @return
     */
    AjaxResult carDetail(Integer  cId);

    /**
     * 修改车辆信息
     * @param updateCarVo
     * @return
     */
    AjaxResult updateCar(UpdateCarVo updateCarVo);

    /**
     * 添加车辆信息
     * @param addCarVo
     * @return
     */
    AjaxResult addCar(AddCarVo addCarVo);

    /**
     * 删除车辆信息
     * @param cId
     * @return
     */
    AjaxResult deleteCar(String[] cId);

    /**
     * 解绑车辆气站
     * @param cId
     * @param gasId
     * @return
     */
    AjaxResult delGasCar(String cId, String gasId);
}
