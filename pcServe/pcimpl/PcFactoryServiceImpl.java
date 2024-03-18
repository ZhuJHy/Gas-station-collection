package com.tulan.system.service.pc.pcimpl;

import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.system.common.resp.pc.PcFactoryDetailDto;
import com.tulan.system.common.resp.pc.PcFactoryDto;
import com.tulan.system.entity.Label;
import com.tulan.system.common.vo.pc.AddFactoryVo;
import com.tulan.system.common.vo.pc.AddLabel;
import com.tulan.system.common.vo.pc.FactoryVo;
import com.tulan.system.common.vo.pc.UpdateFactoryVo;
import com.tulan.system.mapper.pc.PcFactoryMapper;
import com.tulan.system.service.pc.PcFactoryService;
import com.tulan.system.utils.CommonUtils;
import com.tulan.system.utils.IdUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service("PcFactoryService")
public class PcFactoryServiceImpl implements PcFactoryService {

    @Resource
    PcFactoryMapper pcFactoryMapper;

    @Resource
    IdUtils idUtils;

    @Resource
    CommonUtils commonUtils;

    /**
     * 液厂条件查询
     *
     * @param factoryVo
     * @return
     */
    @Override
    public List<PcFactoryDto> lfSelect(FactoryVo factoryVo) {
        return pcFactoryMapper.lfSelect(factoryVo);
    }

    /**
     * 液厂详情查询
     *
     * @param factoryId
     * @return
     */
    @Override
    public AjaxResult selectDetail(String factoryId) {
        try {
            List<Label> list = pcFactoryMapper.selectLabel(factoryId);
            PcFactoryDetailDto fDto = pcFactoryMapper.selectDetail(factoryId);
            //截取省份
            String factoryProvince = fDto.getFactoryProvince();
            String[] strings = idUtils.provinceJieQu(factoryProvince);
            fDto.setSelectProv(strings[0]);
            fDto.setSelectCity(strings[1]);
            fDto.setLabel(list);
            return AjaxResult.success(fDto);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("查询失败");
        }
    }

    /**
     * 添加液厂
     *
     * @param addFactoryVo
     * @return
     */
    @Override
    @Transactional
    public AjaxResult addFactory(AddFactoryVo addFactoryVo) {

        if (!commonUtils.checkPhone(addFactoryVo.getFactoryPhone())) {
            return AjaxResult.error("请校验手机号格式");
        }
        try {
            //保留价格的两位小数
            double price = Double.parseDouble(addFactoryVo.getFactoryPrice());
            BigDecimal bd = new BigDecimal(price);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            addFactoryVo.setFactoryPrice(String.valueOf(bd));
        } catch (Exception e) {
            return AjaxResult.error("请输入正确价格格式");
        }
        //限制液厂重名
        String name = pcFactoryMapper.selectName(addFactoryVo.getFactoryName());
        if (name != null) {
            return AjaxResult.error("已有液厂请重新添加");
        }
        //生成唯一ID
        String id = idUtils.factoryId();
        //获取创建时间
        String date = idUtils.date();
        addFactoryVo.setFactoryProvince(addFactoryVo.getSelectProv() + addFactoryVo.getSelectCity());
        try {
            pcFactoryMapper.addFactory(addFactoryVo, id, date);
            return AjaxResult.success("添加成功");
        } catch (Exception e) {
            return AjaxResult.error("添加失败");
        }
    }

    /**
     * 修改液厂
     *
     * @param updateFactoryVo
     * @return
     */
    @Override
    public AjaxResult updateFactory(UpdateFactoryVo updateFactoryVo) {
        if (!commonUtils.checkPhone(updateFactoryVo.getFactoryPhone())) {
            return AjaxResult.error("请校验手机号格式");
        }
        try {
            double price = Double.parseDouble(updateFactoryVo.getFactoryPrice());
            BigDecimal bd = new BigDecimal(price);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            updateFactoryVo.setFactoryPrice(String.valueOf(bd));
        } catch (Exception e) {
            return AjaxResult.error("请输入正确价格格式");
        }
        //获取修改时间
        String date = idUtils.date();
        updateFactoryVo.setFactoryProvince(updateFactoryVo.getSelectProv() + updateFactoryVo.getSelectCity());
        try {
            pcFactoryMapper.updateFactory(updateFactoryVo, date);
        } catch (Exception e) {
            return AjaxResult.error("更新失败");
        }
        return AjaxResult.success("更新成功");
    }

    /**
     * 关闭和启用每个液厂的状态
     *
     * @param state
     * @param factoryId
     * @return
     */
    @Override
    public AjaxResult updateState(Integer state, String factoryId) {
        if (factoryId == null) {
            return AjaxResult.error("液厂不能为空");
        }
        try {
            if (state == 0) {
                pcFactoryMapper.updateState(state, factoryId);
                return AjaxResult.success("启动成功");
            } else if (state == 1) {
                pcFactoryMapper.updateState(state, factoryId);
                return AjaxResult.error("关闭成功");
            }
        } catch (Exception e) {
            return AjaxResult.error("操作失败");
        }
        return AjaxResult.error("操作失败");
    }

    /**
     * 添加液厂标签
     *
     * @param addLabel
     * @return
     */
    @Override
    public AjaxResult addLabel(AddLabel addLabel) {
        try {
            pcFactoryMapper.addLabel(addLabel.getLabel(), addLabel.getFactoryId());
        } catch (Exception e) {
            return AjaxResult.error("添加失败");
        }
        return AjaxResult.success("添加成功");
    }

    /**
     * 修改液厂标签
     *
     * @return
     */
    @Override
    public AjaxResult updateLabel(String labelName, String labelId) {
        if (labelName == null || labelId == null) {
            return AjaxResult.error("标签内容不能为空");
        }
        try {
            pcFactoryMapper.updateLabel(labelName, labelId);
        } catch (Exception e) {
            return AjaxResult.error("修改失败");
        }
        return AjaxResult.success("修改成功");
    }

    /**
     * 删除液厂标签
     *
     * @param labelId
     * @return
     */
    @Override
    public AjaxResult deleteLabel(String labelId) {
        if (labelId == null) {
            return AjaxResult.error("删除失败");
        }
        try {
            pcFactoryMapper.deleteLabel(labelId);
        } catch (Exception e) {
            return AjaxResult.error("删除失败");
        }
        return AjaxResult.success("删除成功");
    }

    /**
     * 批量删除
     *
     * @param arr
     * @return
     */
    @Override
    @Transactional
    public AjaxResult deleteArr(String[] arr) {
        try {
            String date = idUtils.date();
            pcFactoryMapper.deleteFactory(arr, date);
            pcFactoryMapper.delFactoryLabel(arr);
            return AjaxResult.success("批量删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 查询液厂标签
     *
     * @param factoryId
     * @return
     */
    @Override
    public AjaxResult selLabel(String factoryId) {
        if (factoryId == null) {
            return AjaxResult.error("液厂id不能为空");
        }
        List<Label> list = pcFactoryMapper.selectLabel(factoryId);
        return AjaxResult.success(list);
    }
}
