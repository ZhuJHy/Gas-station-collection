package com.tulan.system.service.pc.pcimpl;

import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.system.common.resp.pc.PcCarDetailDto;

import com.tulan.system.common.resp.pc.PcCarDto;
import com.tulan.system.common.vo.pc.AddCarVo;
import com.tulan.system.common.vo.pc.UpdateCarVo;
import com.tulan.system.mapper.pc.PcCarMapper;
import com.tulan.system.service.pc.PcCarService;
import com.tulan.system.utils.CommonUtils;
import com.tulan.system.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service("PcCarService")
public class PcCarServiceImpl implements PcCarService {

    @Resource
    PcCarMapper pcCarMapper;

    @Resource
    IdUtils idUtils;

    @Resource
    CommonUtils commonUtils;

    /**
     * 条件查询车辆
     * @param carName
     * @param carNumber
     * @return
     */
    @Override
    public List<PcCarDto> selectCar(String carName, String carNumber, Integer carStatus) {
        List<PcCarDto> list = pcCarMapper.selectCar(carName,carNumber,carStatus);
        return list;
    }

    /**
     * 查询车辆详情
     * @param cId
     * @return
     */
    @Override
    public AjaxResult carDetail(Integer  cId) {

        try {
            PcCarDetailDto pcCarDetailDto = pcCarMapper.carDetail(cId,null);
            return AjaxResult.success(pcCarDetailDto);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 修改车辆信息
     * @param updateCarVo
     * @return
     */
    @Override
    public AjaxResult updateCar(UpdateCarVo updateCarVo) {
        if (!commonUtils.checkCarNumber(updateCarVo.getCarNumber())){
            return AjaxResult.error("车牌格式错误");
        }
        //修改时间
        String date = idUtils.date();
        try {
            pcCarMapper.updateCar(updateCarVo,date);
            return AjaxResult.success("修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 添加车辆信息
     * @param addCarVo
     * @return
     */
    @Override
    public AjaxResult addCar(AddCarVo addCarVo) {
        if (!commonUtils.checkCarNumber(addCarVo.getCarNumber())){
            return AjaxResult.error("车牌格式错误");
        }
//        try {
//            if (addCarVo.getCarAge() == null){
//                return AjaxResult.error("所传参数为空");
//            }
//            Integer carAge = Integer.valueOf(addCarVo.getCarAge());
//        }catch (Exception e){
//            return AjaxResult.error("车龄输入错误");
//        }
        List<PcCarDto> pcCarDto = pcCarMapper.selectCar(null, addCarVo.getCarNumber(),null);
        if (pcCarDto.size() > 0){
            return AjaxResult.error("已有此车牌");
        }
        String date = idUtils.date();
        try {
            addCarVo.setCarStatus("0");
            pcCarMapper.addCar(addCarVo,date);
            return AjaxResult.success("添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 删除车辆
     * @param cId
     * @return
     */
    @Override
    @Transactional
    public AjaxResult deleteCar(String[] cId) {
        try {
            String date = idUtils.date();
            //逻辑删除信息
            pcCarMapper.deleteCar(cId,date);
            //删除绑定气站
            pcCarMapper.delCarS(cId);
        }catch (Exception e){
            return AjaxResult.error("操作失败");
        }
        return AjaxResult.success("删除成功");
    }

    /**
     * 解绑车辆气站
     * @param cId
     * @param gasId
     * @return
     */
    @Override
    public AjaxResult delGasCar(String cId, String gasId) {
        try {
            //删除绑定气站
            pcCarMapper.delCarGas(cId,gasId);
        }catch (Exception e){
            return AjaxResult.error("操作失败");
        }
        return AjaxResult.success("删除成功");
    }
}
