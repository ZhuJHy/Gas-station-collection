package com.tulan.system.service.pc.pcimpl;

import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.system.common.resp.pc.*;
import com.tulan.system.common.vo.pc.SelectPlanVo;
import com.tulan.system.common.vo.pc.UpdatePlanVo;
import com.tulan.system.mapper.pc.*;
import com.tulan.system.service.pc.PcPlanService;
import com.tulan.system.utils.CommonUtils;
import com.tulan.system.utils.DateUtils;
import com.tulan.system.utils.IdUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service("PcPlanService")
public class PcPlanServiceImpl implements PcPlanService {


    @Resource
    PcPlanMapper pcPlanMapper;

    @Resource
    PcDriverMapper pcDriverMapper;

    @Resource
    PcCarMapper pcCarMapper;

    @Resource
    PcGasStationMapper pcGasStationMapper;

    @Resource
    PcFactoryMapper pcFactoryMapper;

    @Resource
    IdUtils idUtils;

    @Resource
    DateUtils dateUtils;

    @Resource
    CommonUtils commonUtils;

    /**
     * 查询列表
     *
     * @param selectPlanVo
     * @return
     */
    @Override
    public List<PcPlanDto> selectList(SelectPlanVo selectPlanVo) {
        String star = null, end = null;
        if (selectPlanVo.getCreateTimeStart() != null) {
            star = dateUtils.morning(selectPlanVo.getCreateTimeStart());
        }
        if (selectPlanVo.getCreateTimeEnd() != null) {
            end = dateUtils.evening(selectPlanVo.getCreateTimeEnd());
        }
        List<PcPlanDto> list = pcPlanMapper.selectList(selectPlanVo, star, end);
        return list;
    }

    /**
     * 查询详细列表
     *
     * @param planId
     * @return
     */
    @Override
    public AjaxResult selectDetail(String planId) {
        try {
            PlanDetailDto planDetailDto = pcPlanMapper.selectDetail(planId);
            //拿出司机手机号去找身份证
            String driverPhone = planDetailDto.getDriverPhone();
            String escortPhone = planDetailDto.getEscortPhone();
            PcDriverDetailDto pcDriverDetailDto = pcDriverMapper.selectDriverCard(driverPhone);
            PcDriverDetailDto pcEscortDetailDto = pcDriverMapper.selectDriverCard(escortPhone);
            if (pcDriverDetailDto != null) {
                planDetailDto.setDriverCard(pcDriverDetailDto.getDriverCard());
            } else {
                planDetailDto.setDriverCard("无");
            }
            if (pcEscortDetailDto != null) {
                planDetailDto.setEscortCard(pcEscortDetailDto.getDriverCard());
            } else {
                planDetailDto.setEscortCard("无");
            }

            //拿出车辆去找车辆信息
            String carNumber = planDetailDto.getCarNumber();
            PcCarDetailDto pcCarDetailDto = pcCarMapper.carDetail(null, carNumber);
            if (pcCarDetailDto != null){
                planDetailDto.setTrailerId(pcCarDetailDto.getTrailerId());
                planDetailDto.setRiskCarId(pcCarDetailDto.getRiskCarId());
                planDetailDto.setTankUseId(pcCarDetailDto.getTankUseId());
            }
            return AjaxResult.success(planDetailDto);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 订单签字
     *
     * @param signature
     * @param planId
     * @return
     */
    @Override
    @Transactional
    public AjaxResult signature(String signature, String planId) {
        if (signature == null) {
            return AjaxResult.error("请输入签字名称");
        }
        PlanDetailDto planDetailDto = pcPlanMapper.selectDetail(planId);
        if (planDetailDto.getStatus() != 1) {
            return AjaxResult.error("未提交,请先提交");
        }
        if (planDetailDto.getPlanSignature() != null) {
            return AjaxResult.error("请先撤销签字");
        }
        try {
            //订单签字
            pcPlanMapper.signature(signature, planId);
            //订单签字后修改状态
            Integer status = 2;
            pcPlanMapper.updateStatus(planId, status);
            return AjaxResult.success("已成功签字");
        } catch (Exception e) {
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 撤销签字
     *
     * @param planId
     * @return
     */
    @Override
    public AjaxResult backOut(String planId) {

        PlanDetailDto planDetailDto1 = pcPlanMapper.selectDetail(planId);
        Integer statusTest = planDetailDto1.getStatus();
        if (statusTest.equals(3)) {
            return AjaxResult.error("订单已完成,不能修改");
        }
        if (planDetailDto1.getPlanSignature() == null) {
            return AjaxResult.error("为签字,请先签字");
        }
        try {
            //撤销签字
            pcPlanMapper.signature(null, planId);
            //修改状态
            Integer status = 1;
            pcPlanMapper.updateStatus(planId, status);
            return AjaxResult.success("撤销成功");
        } catch (Exception e) {
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 修改采购计划为3已完成   司机状态改成0  司机所驾驶的车辆删除  车辆状态改为0
     *
     * @param planId
     * @return
     */
    @Override
    @Transactional
    public AjaxResult updateStatus(String planId) {
        PlanDetailDto planDetailDto = pcPlanMapper.selectDetail(planId);
        if (planDetailDto.getStatus() != 2) {
            return AjaxResult.error("请检查订单是否已经完成");
        }
        try {
            String driverPhone = planDetailDto.getDriverPhone();
            String escortPhone = planDetailDto.getEscortPhone();
            String carNumber = planDetailDto.getCarNumber();
            //修改采购计划状态为已完成
            Integer status = 3;
            pcPlanMapper.updateStatus(planId, status);
            //修改司机状态已经所驾驶的车辆
            Integer useStatus = 0;
            pcDriverMapper.updateDriverStatus(driverPhone, useStatus);
            pcDriverMapper.updateDriverStatus(escortPhone, useStatus);
            //修改司机所驾驶的车辆
            pcDriverMapper.updateCar(driverPhone);
            //修改车辆状态
            pcCarMapper.updateCarStatus(carNumber, useStatus);
            return AjaxResult.success("操作失败");
        } catch (Exception e) {
            return AjaxResult.error("操作失败");
        }
    }


    /**
     * 修改采购计划
     *
     * @param updatePlan
     * @return
     */
    @Override
    @Transactional
    public AjaxResult updatePlan(UpdatePlanVo updatePlan) {

        //查询未修改前的信息
        PlanDetailDto planDetailDto1 = pcPlanMapper.selectDetail(updatePlan.getPlanId());
        Integer status = planDetailDto1.getStatus();

        if (status.equals(2)) {
            return AjaxResult.error("订单已签字,请先撤销");
        } else if (status.equals(3)) {
            return AjaxResult.error("订单已完成,不能修改");
        }
        System.out.println(updatePlan.getDriverPhone()+"----------------------");
        //查看所选司机是否在运输中
        if (!updatePlan.getDriver().equals("") && !updatePlan.getEscort().equals("")){
            if (updatePlan.getDriverPhone().equals(updatePlan.getEscortPhone())) {
                return AjaxResult.error("司机和押送员不能为同一个人");
            }
        }
        try {
            pcCarMapper.updateCarOther(updatePlan.getRiskCarId(),updatePlan.getTankUseId(),updatePlan.getTrailerId(),updatePlan.getCarNumber());
            pcPlanMapper.updatePlanOther(updatePlan.getPlanId(),updatePlan.getRiskCarId(),updatePlan.getTankUseId(),updatePlan.getTrailerId());
            if (status.equals(0)){
                //修改采购计划
                pcPlanMapper.updatePlan(updatePlan, idUtils.date());
                return AjaxResult.success("修改成功");
            }
            //修改以前司机押送员的状态 1 为运输状态 0 为未运输
            pcDriverMapper.updateDriverStatus(planDetailDto1.getDriverPhone(), 0);
            pcDriverMapper.updateDriverStatus(planDetailDto1.getEscortPhone(), 0);
            //修改之前车辆的状态为不使用 1 为启用  0 为未启用
            pcCarMapper.updateCarStatus(planDetailDto1.getCarNumber(), 0);
            //司机修改状态
            pcDriverMapper.updateDriverStatus(updatePlan.getDriverPhone(), 1);
            //押送员修改
            pcDriverMapper.updateDriverStatus(updatePlan.getEscortPhone(), 1);
            //修改车辆
            pcDriverMapper.updateDriverStatus(updatePlan.getCarNumber(), 1);
            //液厂修改
            //修改采购计划
            pcPlanMapper.updatePlan(updatePlan, idUtils.date());
            return AjaxResult.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 查询气站下的司机
     *
     * @param gasName
     * @return
     */
    @Override
    public AjaxResult selectGasDriver(String gasName) {
        String gasId = pcGasStationMapper.selectName(gasName);
        List<PcDriverDto> pcDriverDtoList = pcGasStationMapper.selectDriver(gasId,0);
        return AjaxResult.success(pcDriverDtoList);
    }

    /**
     * 查询气站下的车辆
     *
     * @param gasName
     * @return
     */
    @Override
    public AjaxResult selectGasCar(String gasName) {
        String gasId = pcGasStationMapper.selectName(gasName);
        List<PcCarDto> pcCarDtoList = pcGasStationMapper.selectCar(gasId);
        return AjaxResult.success(pcCarDtoList);
    }

    /**
     * 查询液厂
     *
     * @return
     */
    @Override
    public AjaxResult selectFactory() {
        List<PcFactoryDto> pcFactoryDto = pcFactoryMapper.lfSelect(null);
        List<PcFactoryPlanDto> pcFactoryPlanDto = new ArrayList<>();
        if (pcFactoryDto.size() > 0) {
            for (PcFactoryDto factoryDto : pcFactoryDto) {
                PcFactoryPlanDto pcFactoryPlanDto1 = new PcFactoryPlanDto();
                pcFactoryPlanDto1.setAddress(factoryDto.getFactoryAddress());
                pcFactoryPlanDto1.setFactoryName(factoryDto.getFactoryName());
                pcFactoryPlanDto.add(pcFactoryPlanDto1);
            }
        }
        return AjaxResult.success(pcFactoryPlanDto);
    }

    /**
     * 查看气站下的押运员
     *
     * @param gasName
     * @return
     */
    @Override
    public AjaxResult selectGasEscort(String gasName) {
        String gasId = pcGasStationMapper.selectName(gasName);
        List<PcDriverDto> pcDriverDtoList = pcGasStationMapper.selectDriver(gasId,0);
        List<PcEscortDto> pcEscortDtoList = new ArrayList<>();
        for (PcDriverDto pcDriverDto : pcDriverDtoList) {
            PcEscortDto pcEscortDto = new PcEscortDto();
            pcEscortDto.setEscort(pcDriverDto.getDriverName());
            pcEscortDto.setEscortPhone(pcDriverDto.getDriverPhone());
            pcEscortDtoList.add(pcEscortDto);
        }
        return AjaxResult.success(pcEscortDtoList);
    }
}
