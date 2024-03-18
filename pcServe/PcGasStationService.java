package com.tulan.system.service.pc;

import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.system.common.resp.pc.PcGasDto;
import com.tulan.system.common.vo.pc.AddGasVo;
import com.tulan.system.common.vo.pc.UpdateGasVo;

import java.util.List;

public interface PcGasStationService {

    /**
     * 添加气站
     * @param addGasVo
     * @return
     */
    AjaxResult addGas(AddGasVo addGasVo);

    /**
     * 查询气站列表
     * @return
     */
    List<PcGasDto> selectGas(String state, String gasName);

    /**
     * 查询气站详情
     * @param gasId
     * @return
     */
    AjaxResult selectDetail(String gasId);

    /**
     * 修改气站
     * @param updateGasVo
     * @return
     */
    AjaxResult updateGas(UpdateGasVo updateGasVo);


    /**
     * 批量删除液厂
     * @param arr
     * @return
     */
    AjaxResult deleteArr(String[] arr);
}
