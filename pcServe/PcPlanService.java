package com.tulan.system.service.pc;

import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.system.common.resp.pc.PcPlanDto;
import com.tulan.system.common.vo.pc.SelectPlanVo;
import com.tulan.system.common.vo.pc.UpdatePlanVo;

import java.util.List;

public interface PcPlanService {

    /**
     *查询采购计划列表
     * @param selectPlanVo
     * @return
     */
    List<PcPlanDto> selectList(SelectPlanVo selectPlanVo);

    /**
     * 查询计划详情列表
     * @param planId
     * @return
     */
    AjaxResult selectDetail(String planId);

    /**
     * 订单签字
     * @param signature
     * @param planId
     * @return
     */
    AjaxResult signature(String signature, String planId);

    /**
     * 撤销签字
     * @param planId
     * @return
     */
    AjaxResult backOut(String planId);

    /**
     * 修改采购计划状态
     * @param planId
     * @return
     */
    AjaxResult updateStatus( String planId);

    /**
     * 修改采购计划
     * @param updatePlan
     * @return
     */
    AjaxResult updatePlan(UpdatePlanVo updatePlan);

    /**
     *查询气站下的司机
     * @param gasName
     * @return
     */
    AjaxResult selectGasDriver(String gasName);

    /**
     *查询气站下的车辆
     * @param gasName
     * @return
     */
    AjaxResult selectGasCar(String gasName);

    /**
     *查询液厂
     * @return
     */
    AjaxResult selectFactory();

    /**
     * 查看押运员
     * @param gasName
     * @return
     */
    AjaxResult selectGasEscort(String gasName);
}
