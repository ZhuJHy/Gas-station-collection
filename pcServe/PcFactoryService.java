package com.tulan.system.service.pc;

import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.system.common.resp.pc.PcFactoryDto;
import com.tulan.system.common.vo.pc.AddFactoryVo;
import com.tulan.system.common.vo.pc.AddLabel;
import com.tulan.system.common.vo.pc.FactoryVo;
import com.tulan.system.common.vo.pc.UpdateFactoryVo;

import java.util.List;

public interface PcFactoryService {
    /**
     * 液厂条件查询
     *
     * @param factoryVo
     * @return
     */
    List<PcFactoryDto> lfSelect(FactoryVo factoryVo);

    /**
     * 液厂启动与关闭状态
     *
     * @param state
     * @param factoryId
     * @return
     */
    AjaxResult updateState(Integer state, String factoryId);

    /**
     * 液厂详情查询
     *
     * @param factoryId
     * @return
     */
    AjaxResult selectDetail(String factoryId);

    /**
     * 添加液厂
     *
     * @param addFactoryVo
     * @return
     */
    AjaxResult addFactory(AddFactoryVo addFactoryVo);

    /**
     * 修改液厂
     * @param updateFactoryVo
     * @return
     */
    AjaxResult updateFactory(UpdateFactoryVo updateFactoryVo);

    /**
     *添加标签
     * @param addLabel
     * @return
     */
    AjaxResult addLabel(AddLabel addLabel);

    /**
     * 修改标签
     * @param
     * @return
     */
    AjaxResult updateLabel(String label, String labelId);

    /**
     * 删除标签
     * @param labelId
     * @return
     */
    AjaxResult deleteLabel(String labelId);


    /**
     * 批量液厂删除
     * @param arr
     * @return
     */
    AjaxResult deleteArr(String[] arr);

    /**
     * 查询液厂标签
     * @param factoryId
     * @return
     */
    AjaxResult selLabel(String factoryId);
}
