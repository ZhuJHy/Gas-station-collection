package com.tulan.system.service.pc.pcimpl;

import com.tulan.common.core.web.domain.AjaxResult;

import com.tulan.system.common.resp.pc.PcCarDto;
import com.tulan.system.common.resp.pc.PcDriverDto;
import com.tulan.system.common.resp.pc.PcGasDetailDto;
import com.tulan.system.common.resp.pc.PcGasDto;
import com.tulan.system.common.vo.pc.AddGasVo;
import com.tulan.system.common.vo.pc.UpdateGasVo;
import com.tulan.system.mapper.pc.PcGasStationMapper;
import com.tulan.system.service.pc.PcGasStationService;
import com.tulan.system.utils.CommonUtils;
import com.tulan.system.utils.IdUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("PcGasStationService")
public class PcGasStationServiceImpl implements PcGasStationService {

    @Resource
    PcGasStationMapper pcGasStationMapper;

    @Resource
    IdUtils idUtils;

    @Resource
    CommonUtils commonUtils;

    /**
     * 添加气站
     *
     * @param addGasVo
     * @return
     */
    @Override
    public AjaxResult addGas(AddGasVo addGasVo) {
        if (!commonUtils.checkPhone(addGasVo.getGasPhone())) {
            return AjaxResult.error("请校验手机号格式");
        }
        //查询气站重复名称
        String gasName = pcGasStationMapper.selectName(addGasVo.getGasName());
        if (gasName != null) {
            return AjaxResult.error("已有名称请重新添加");
        }
        String G_id = idUtils.gasId();
        String C_time = idUtils.date();
        addGasVo.setGasProvince(addGasVo.getSelectProv() + addGasVo.getSelectCity());
        try {
            pcGasStationMapper.addGas(addGasVo, G_id, C_time);
            return AjaxResult.success("添加成功");
        } catch (Exception e) {
            return AjaxResult.error("添加失败");
        }
    }

    /**
     * 查询气站列表
     *
     * @param state
     * @param gasName
     * @return
     */
    @Override
    public List<PcGasDto> selectGas(String state, String gasName) {
        List<PcGasDto> list = pcGasStationMapper.selectGas(state, gasName);
        return list;
    }

    /**
     * 查询气站详情
     *
     * @param gasId
     * @return
     */
    @Override
    public AjaxResult selectDetail(String gasId) {
        if (gasId == null) {
            return AjaxResult.error("查询失败");
        }
        try {
            //查询气站详情
            PcGasDetailDto pcGasDetailDto = pcGasStationMapper.selectDetail(gasId);

            //截取字符串
            String gasProvince = pcGasDetailDto.getGasProvince();
            String[] strings = idUtils.provinceJieQu(gasProvince);
            pcGasDetailDto.setSelectProv(strings[0]);
            pcGasDetailDto.setSelectCity(strings[1]);
            //查询气站下的司机
            List<PcDriverDto> newDriverDto = new ArrayList<>();
            List<PcDriverDto> pcDriverDtoList = pcGasStationMapper.selectDriver(gasId,null);
            if (pcDriverDtoList.size() > 0) {
                for (PcDriverDto driverDto : pcDriverDtoList) {
                    PcDriverDto pcDriverDto1 = new PcDriverDto();
                    pcDriverDto1 = driverDto;
                    if (pcDriverDto1.getDriverPost().equals("0")) {
                        pcDriverDto1.setDriverPost("司机");
                    } else if (pcDriverDto1.getDriverPost().equals("1")) {
                        pcDriverDto1.setDriverPost("押送员");
                    } else if (pcDriverDto1.getDriverPost().equals("2")) {
                        pcDriverDto1.setDriverPost("司机与押送员");
                    }
                    newDriverDto.add(pcDriverDto1);
                }
                pcGasDetailDto.setDriver(newDriverDto);
            } else {
                PcDriverDto pcDriverDto1 = new PcDriverDto();
                pcDriverDto1.setDriverName("请联系信息员添加司机");
                pcDriverDtoList.add(pcDriverDto1);
                pcGasDetailDto.setDriver(pcDriverDtoList);
            }
            //查询气站下的车辆
            List<PcCarDto> listCar = pcGasStationMapper.selectCar(gasId);
            if (listCar.size() > 0) {
                pcGasDetailDto.setCar(listCar);
            } else {
                PcCarDto pcCarDto = new PcCarDto();
                pcCarDto.setCarName("请联系信息员添加车辆");
                listCar.add(pcCarDto);
                pcGasDetailDto.setCar(listCar);
            }
            return AjaxResult.success(pcGasDetailDto);
        } catch (Exception e) {
            return AjaxResult.error("查询失败");
        }
    }

    /**
     * 修改气站
     *
     * @param updateGasVo
     * @return
     */
    @Override
    public AjaxResult updateGas(UpdateGasVo updateGasVo) {
        if (!commonUtils.checkPhone(updateGasVo.getGasPhone())) {
            return AjaxResult.error("请校验手机号格式");
        }
        String date = idUtils.date();
        updateGasVo.setGasProvince(updateGasVo.getSelectProv() + updateGasVo.getSelectCity());
        try {
            pcGasStationMapper.updateGas(updateGasVo, date);
            return AjaxResult.success("修改成功");
        } catch (Exception e) {
            return AjaxResult.error("修改失败");
        }
    }


    /**
     * 批量删除气站
     *
     * @param arr
     * @return
     */
    @Override
    @Transactional
    public AjaxResult deleteArr(String[] arr) {
        try {
            String date = idUtils.date();
            pcGasStationMapper.deleteGas(arr, date);
            pcGasStationMapper.deleteGasDriver(arr);
            pcGasStationMapper.deleteGasCar(arr);
            //解绑气站下的信息员
            pcGasStationMapper.deleteMessenger(arr);
            return AjaxResult.success("批量删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("操作失败");
        }
    }
}
