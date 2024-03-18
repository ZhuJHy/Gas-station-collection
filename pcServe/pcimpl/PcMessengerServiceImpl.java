package com.tulan.system.service.pc.pcimpl;

import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.system.common.resp.pc.PcMessengerDetailDto;
import com.tulan.system.common.resp.pc.PcSelectGasDto;
import com.tulan.system.api.domain.Messenger;
import com.tulan.system.common.vo.pc.AddMessenger;
import com.tulan.system.common.vo.pc.UpdateMessenger;
import com.tulan.system.mapper.pc.PcMessengerMapper;
import com.tulan.system.service.pc.PcMessengerService;
import com.tulan.system.utils.CommonUtils;
import com.tulan.system.utils.IdUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service("PcMessengerService")
public class PcMessengerServiceImpl implements PcMessengerService {

    @Resource
    PcMessengerMapper pcMessengerMapper;

    @Resource
    IdUtils idUtils;

    @Resource
    CommonUtils commonUtils;

    /**
     * 查询信息员列表
     * @param messengerName
     * @param phone
     * @return
     */
    @Override
    public List<Messenger> selectList(String messengerName, String phone) {
        List<Messenger> list = pcMessengerMapper.selectList(messengerName,phone);
        Messenger messenger1 = new Messenger();
        List<Messenger> list1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            messenger1 = list.get(i);
            String mPower = messenger1.getMPower();
            if (mPower.equals("-1")){
                messenger1.setMPower("已解绑");
            }else if (mPower.equals("0")){
                messenger1.setMPower("无权限");
            }else if (mPower.equals("1")){
                messenger1.setMPower("有权限");
            }
            list1.add(messenger1);
        }
        return list1;
    }

    /**
     * 查询信息员详情
     * @param mgId
     * @return
     */
    @Override
    public AjaxResult messengerDetail(String mgId) {
        if (mgId == null){
            return AjaxResult.error("查询失败");
        }
        try {
            PcMessengerDetailDto messengerDetailDto = pcMessengerMapper.messengerDetail(mgId);
            if (messengerDetailDto.getMPower().equals("-1")){
                messengerDetailDto.setMPower("已解绑");
            }else if (messengerDetailDto.getMPower().equals("0")){
                messengerDetailDto.setMPower("无权限");
            }else if (messengerDetailDto.getMPower().equals("1")){
                messengerDetailDto.setMPower("有权限");
            }
            return AjaxResult.success(messengerDetailDto);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("查询失败");
        }
    }

    /**
     * 检索气站
     * @param gasName
     * @return
     */
    @Override
    public AjaxResult selectGas(String gasName) {
        List<PcSelectGasDto> selectGasDto =  pcMessengerMapper.selectGas(null,gasName);
        return AjaxResult.success(selectGasDto);
    }


    /**
     * 添加信息员
     * @param addMessenger
     * @return
     */
    @Override
    public AjaxResult addMessenger(@Validated AddMessenger addMessenger) {
        if (!commonUtils.checkPhone(addMessenger.getMPhone())){
            return AjaxResult.error("请校验手机号格式");
        }
        String phone = pcMessengerMapper.selPhone(addMessenger.getMPhone());
        if (phone != null){
            return AjaxResult.error("已有手机号请重新授权");
        }
        String date = idUtils.date();
        String mgId = idUtils.messengerId();
//        if (addMessenger.getMPower().equals("有权限")){
//            addMessenger.setMPower("1");
//        }else if (addMessenger.getMPower().equals("无权限")){
//            addMessenger.setMPower("0");
//        }
        String gasId = pcMessengerMapper.selectGasId(addMessenger.getGasName());
        if (gasId == null){
            return AjaxResult.error("没有此气站");
        }
        addMessenger.setGasId(gasId);
        try {
            pcMessengerMapper.addMessenger(mgId,addMessenger,date);
            return AjaxResult.success("添加成功");
        }catch (Exception e){
            return AjaxResult.error("操作失败");
        }
    }

    //修改信息员列表
    @Override
    public AjaxResult updateMessenger(UpdateMessenger updateMessenger) {
        if (!commonUtils.checkPhone(updateMessenger.getMPhone())){
            return AjaxResult.error("请校验手机号格式");
        }
        //根据姓名查气站Id
        String gasId = pcMessengerMapper.selectGasId(updateMessenger.getGasName());
        if (gasId == null){
            return AjaxResult.error("没有此气站");
        }
        updateMessenger.setGasId(gasId);
        String date = idUtils.date();
//        Integer mPower = 1;
//        if (updateMessenger.getMPower().equals("有权限")){
//            mPower = 1;
//        }else if (updateMessenger.getMPower().equals("无权限")){
//            mPower = 0;
//        }
        try {
            pcMessengerMapper.updateMessenger(updateMessenger,date);
            return AjaxResult.success("修改成功");
        }catch (Exception e){
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 解绑信息员
     * @param mgId
     * @return
     */
    @Override
    public AjaxResult untieMessenger(String mgId) {
        try {
            pcMessengerMapper.untieMessenger(mgId);
            return AjaxResult.success("解绑成功");
        }catch (Exception e){
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 批量删除信息员
     * @param mgId
     * @return
     */
    @Override
    public AjaxResult delMessenger(String[] mgId) {
        try {
            String date = idUtils.date();
            pcMessengerMapper.deleteMes(mgId,date);
            return AjaxResult.success("删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 启用禁用信息员
     * @param status
     * @param mgId
     * @return
     */
    @Override
    public AjaxResult updateStatus(Integer status, String mgId) {
        try {
            pcMessengerMapper.updateStatus(status,mgId);
            return AjaxResult.success("操作成功");
        }catch (Exception e){
            return AjaxResult.error("操作失败");
        }
    }
}
