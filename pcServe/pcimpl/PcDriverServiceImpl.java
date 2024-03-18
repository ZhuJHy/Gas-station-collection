package com.tulan.system.service.pc.pcimpl;

import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.system.common.resp.pc.PcDriverDetailDto;
import com.tulan.system.common.resp.pc.PcDriverDto;
import com.tulan.system.common.vo.pc.AddDriverVo;
import com.tulan.system.common.vo.pc.UpdateDriverVo;
import com.tulan.system.mapper.pc.PcDriverMapper;
import com.tulan.system.service.pc.PcDriverService;
import com.tulan.system.utils.CommonUtils;
import com.tulan.system.utils.IdUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("PcDriverService")
public class PcDriverServiceImpl implements PcDriverService {

    @Resource
    PcDriverMapper pcDriverMapper;

    @Resource
    IdUtils idUtils;

    @Resource
    CommonUtils commonUtils;

    /**
     * 查询司机列表
     *
     * @param driverName
     * @param phone
     * @return
     */
    @Override
    public List<PcDriverDto> selectDriver(String driverName, String phone,String escortStation) {
        List<PcDriverDto> list = pcDriverMapper.selectDriver(driverName, phone,escortStation);
        return list;
    }

    /**
     * 查询司机细节
     *
     * @param dId
     * @return
     */
    @Override
    public AjaxResult driverDetail(String dId) {
        if (dId == null) {
            return AjaxResult.error("查询失败");
        }
        //查询详情
        PcDriverDetailDto pcDriverDetailDto = pcDriverMapper.driverDetail(dId);
        //查询司机所在气站
        pcDriverDetailDto.setList(pcDriverMapper.driGas(dId));
        return AjaxResult.success(pcDriverDetailDto);
    }

    /**
     * 添加司机
     *
     * @param addDriverVo
     * @return
     */
    @Override
    public AjaxResult addDriver(AddDriverVo addDriverVo) {
        if (!commonUtils.checkPhone(addDriverVo.getDriverPhone())) {
            return AjaxResult.error("手机号格式错误");
        }
//        if (!commonUtils.checkLicense(addDriverVo.getDriverLicense())) {
//            return AjaxResult.error("驾驶证格式错误");
//        }
//        if (!commonUtils.checkCard(addDriverVo.getDriverCard())) {
//            return AjaxResult.error("请输入正确的身份证号");
//        }
//        Integer driverPost = 1;
//        if (addDriverVo.getDriverLicense() == null || addDriverVo.getDriverLicense() == "") {
//            //当驾驶证为空   职务只能为1押送员
//            if (addDriverVo.getDriverPost().equals("司机") || addDriverVo.getDriverPost().equals("司机与押送员")) {
//                return AjaxResult.error("请填写驾驶证");
//            }
//            driverPost = 1;
//        } else if (addDriverVo.getDriverLicense() != null) {
//            if (addDriverVo.getDriverPost().equals("司机")) {
//                driverPost = 0;
//            } else if (addDriverVo.getDriverPost().equals("司机与押送员")) {
//                driverPost = 2;
//            } else {
//                return AjaxResult.error("请输入正确的职务");
//            }
//        }
//        try {
//          Integer.valueOf(addDriverVo.getDriverAge());
//        } catch (Exception e) {
//            return AjaxResult.error("请输入正确的驾龄格式");
//        }
        //司机手机号进行校验
        List<PcDriverDto> list = pcDriverMapper.selectDriver(null, addDriverVo.getDriverPhone(),null);
        if (list.size() > 0){
            return AjaxResult.error("已有手机号,请重新注册");
        }
        String date = idUtils.date();
        try {
            addDriverVo.setDriverPost("2");
            addDriverVo.setEscortStation("0");
            pcDriverMapper.addDriver(addDriverVo, date, null);
            return AjaxResult.success("添加成功");
        } catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 修改司机信息
     *
     * @param updateDriverVo
     * @return
     */
    @Override
    public AjaxResult updateDriver(UpdateDriverVo updateDriverVo) {
        if (!commonUtils.checkPhone(updateDriverVo.getDriverPhone())) {
            return AjaxResult.error("手机号格式错误");
        }
//        if (!commonUtils.checkLicense(updateDriverVo.getDriverLicense())) {
//            return AjaxResult.error("驾驶证格式错误");
//        }
//        if (!commonUtils.checkCard(updateDriverVo.getDriverCard())) {
//            return AjaxResult.error("请输入正确的身份证号");
//        }
//        Integer driverPost = 1;
//        if (updateDriverVo.getDriverLicense() == null || updateDriverVo.getDriverLicense() == "") {
//            //当驾驶证为空   职务只能为1押送员
//            if (updateDriverVo.getDriverPost().equals("司机") || updateDriverVo.getDriverPost().equals("司机与押送员")) {
//                return AjaxResult.error("请填写驾驶证");
//            }
//            driverPost = 1;
//        } else if (updateDriverVo.getDriverLicense() != null) {
//            if (updateDriverVo.getDriverPost() == "司机") {
//                driverPost = 1;
//            } else if (updateDriverVo.getDriverPost() == "司机与押送员") {
//                driverPost = 2;
//            } else {
//                return AjaxResult.error("请输入正确的职务");
//            }
//        }
//        try {
//           Integer.valueOf(updateDriverVo.getDriverAge());
//        } catch (Exception e) {
//            return AjaxResult.error("请输入正确的年龄");
//        }
        String date = idUtils.date();
        try {
            pcDriverMapper.updateDriver(updateDriverVo, date, null);
            return AjaxResult.success("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 批量删除司机
     *
     * @param dId
     * @return
     */
    @Override
    @Transactional
    public AjaxResult deleteDriver(String[] dId) {
        try {
            String date = idUtils.date();
            //逻辑删除
            pcDriverMapper.deleteDriver(dId, date);
            //删除司机锁绑定的所以气站
            pcDriverMapper.delDriverS(dId);
            return AjaxResult.success("操作成功");
        } catch (Exception e) {
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 解绑司机与气站
     *
     * @param dId
     * @param gasId
     * @return
     */
    @Override
    public AjaxResult delGasDriver(String dId, String gasId) {
        try {
            pcDriverMapper.delGasDriver(dId, gasId);
            return AjaxResult.success("解绑成功");
        } catch (Exception e) {
            return AjaxResult.error("操作失败");
        }
    }
}
