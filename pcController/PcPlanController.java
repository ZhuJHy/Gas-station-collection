package com.tulan.system.controller.pc;


import com.tulan.common.core.utils.poi.ExcelUtil;
import com.tulan.common.core.web.controller.BaseController;
import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.common.core.web.page.TableDataInfo;
import com.tulan.common.log.annotation.Log;
import com.tulan.common.log.enums.BusinessType;
import com.tulan.system.common.resp.pc.PcPlanDto;
import com.tulan.system.common.vo.pc.SelectPlanVo;
import com.tulan.system.common.vo.pc.SignatureVo;
import com.tulan.system.common.vo.pc.UpdatePlanVo;
import com.tulan.system.service.pc.PcPlanService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/plan")
public class PcPlanController extends BaseController {

    @Resource
    PcPlanService pcPlanService;

    /**
     * 查询采购计划列表
     */
    @ApiOperation("采购计划列表")
    @GetMapping("selectList")
    public TableDataInfo selectList(SelectPlanVo selectPlanVo) {
        startPage();  // 此方法配合前端完成自动分页
        List<PcPlanDto> list = pcPlanService.selectList(selectPlanVo);
        return getDataTable(list);
    }

    /**
     * 导出数据
     */
    @ApiOperation("信息员导出数据")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SelectPlanVo selectPlanVo) {
        List<PcPlanDto> list = pcPlanService.selectList(selectPlanVo);
        ExcelUtil<PcPlanDto> util = new ExcelUtil<>(PcPlanDto.class);
        util.exportExcel(response, list, "信息员数据");
    }


    /**
     * 查询计划列表详情
     */
    @ApiOperation("计划详情")
    @GetMapping("/selectDetail")
    public AjaxResult selectDetail(String planId) {
        return pcPlanService.selectDetail(planId);
    }


    /**
     * 订单签字
     */
    @Log(title = "订单签字", businessType = BusinessType.INSERT)
    @ApiOperation("订单签字")
    @PutMapping("/signature")
    public AjaxResult signature(@RequestBody SignatureVo signatureVo) {
        return pcPlanService.signature(signatureVo.getSignature(), signatureVo.getPlanId());
    }

    /**
     * 撤销签字
     */
    @Log(title = "撤销签字", businessType = BusinessType.UPDATE)
    @ApiOperation("撤销签字")
    @PutMapping("/backOut")
    public AjaxResult backOut(String planId) {
        return pcPlanService.backOut(planId);
    }

    /**
     * 修改采购计划状态
     */
    @Log(title = "修改采购计划状态", businessType = BusinessType.UPDATE)
    @ApiOperation("修改采购计划为已完成")
    @PutMapping("/updateStatus")
    public AjaxResult updateStatus(String planId) {
        return pcPlanService.updateStatus(planId);
    }

    /**
     * 修改采购计划
     * @param updatePlan
     * @return
     */
    @Log(title = "修改采购计划", businessType = BusinessType.UPDATE)
    @ApiOperation("修改采购计划")
    @PutMapping("/updatePlan")
    public AjaxResult updatePlan(@RequestBody @Valid UpdatePlanVo updatePlan) {
        return pcPlanService.updatePlan(updatePlan);
    }

    /**
     * 查询气站下的司机
     * @param gasName
     * @return
     */
    @ApiOperation("查询气站下的司机")
    @GetMapping("selectGasDriver")
    public AjaxResult selectGasDriver(String gasName){
        return pcPlanService.selectGasDriver(gasName);
    }

    /**
     * 查询气站下的押运员
     * @param gasName
     * @return
     */
    @ApiOperation("查询气站下的押运员")
    @GetMapping("selectGasEscort")
    public AjaxResult selectGasEscort(String gasName){
        return pcPlanService.selectGasEscort(gasName);
    }

    /**
     * 查询气站下的车辆
     * @param gasName
     * @return
     */
    @ApiOperation("查询气站下的车辆")
    @GetMapping("selectGasCar")
    public AjaxResult selectGasCar(String gasName){
        return pcPlanService.selectGasCar(gasName);
    }

    /**
     * 查询液厂
     * @return
     */
    @ApiOperation("查询液厂")
    @GetMapping("selectFactory")
    public AjaxResult selectFactory(){
        return pcPlanService.selectFactory();
    }
}

