package com.tulan.system.controller.pc;


import com.tulan.common.core.utils.poi.ExcelUtil;
import com.tulan.common.core.web.controller.BaseController;
import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.common.core.web.page.TableDataInfo;
import com.tulan.common.log.annotation.Log;
import com.tulan.common.log.enums.BusinessType;
import com.tulan.system.common.resp.pc.PcFactoryDto;
import com.tulan.system.common.vo.pc.*;
import com.tulan.system.service.pc.PcFactoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Api("液厂信息维护")
@RestController
@RequestMapping("/factory")
public class PcFactoryController extends BaseController {

    @Resource
    PcFactoryService pcFactoryService;


    /**
     * 添加液厂
     *
     * @param addFactoryVo
     * @return
     */
    @Log(title = "液厂添加", businessType = BusinessType.INSERT)
    @ApiOperation("液厂添加")
    @PostMapping("/addFactory")
    public AjaxResult addFactory(@RequestBody @Valid AddFactoryVo addFactoryVo) {
        return pcFactoryService.addFactory(addFactoryVo);
    }


    /**
     * 更新液厂信息
     *
     * @return
     */
    @Log(title = "液厂修改", businessType = BusinessType.UPDATE)
    @ApiOperation("液厂更新")
    @PutMapping("/updateFactory")
    public AjaxResult updateFactory(@RequestBody @Valid UpdateFactoryVo updateFactoryVo) {
        return pcFactoryService.updateFactory(updateFactoryVo);
    }

    /**
     * 液厂的条件分页查询
     *
     * @param factoryVo
     * @return
     */
    @ApiOperation("液厂查询")
    @GetMapping("/list")
    public TableDataInfo factorySelect(FactoryVo factoryVo) {
        startPage();  // 此方法配合前端完成自动分页
        List<PcFactoryDto> list = pcFactoryService.lfSelect(factoryVo);
        return getDataTable(list);
    }

    /**
     * 导出数据
     */
    @ApiOperation("液厂导出数据")
    @PostMapping("/export")
    public void export(HttpServletResponse response, FactoryVo factoryVo) {
        List<PcFactoryDto> list = pcFactoryService.lfSelect(factoryVo);
        ExcelUtil<PcFactoryDto> util = new ExcelUtil<>(PcFactoryDto.class);
        util.exportExcel(response, list, "液厂数据");
    }

    /**
     * 液厂详情查询
     *
     * @param factoryId
     * @return
     */
    @ApiOperation("液厂详情")
    @GetMapping("/selectDetail")
    public AjaxResult factoryDetail(String factoryId) {
        return pcFactoryService.selectDetail(factoryId);
    }

    /**
     * 关闭或启用液厂的状态
     *
     * @param state
     * @param factoryId
     * @return
     */
    @Log(title = "修改液厂状态", businessType = BusinessType.UPDATE)
    @ApiOperation("液厂更新状态")
    @PutMapping("/updateState")
    public AjaxResult updateState(Integer state, String factoryId) {
        return pcFactoryService.updateState(state, factoryId);
    }

    /**
     * 添加标签
     *
     * @param addLabel
     * @return
     */
    @Log(title = "液厂标签添加", businessType = BusinessType.INSERT)
    @ApiOperation("液厂添加标签")
    @PostMapping("/addLabel")
    public AjaxResult addLabel(@RequestBody @Valid AddLabel addLabel) {
        return pcFactoryService.addLabel(addLabel);
    }

    /**
     * 修改标签
     *
     * @param updateLabel
     * @return
     */
    @Log(title = "修改液厂标签", businessType = BusinessType.UPDATE)
    @ApiOperation("液厂标签修改")
    @PutMapping("/updateLabel")
    public AjaxResult updateLabel(@RequestBody UpdateLabel updateLabel) {
        return pcFactoryService.updateLabel(updateLabel.getLabelName(), updateLabel.getLabelId());
    }

    /**
     * 删除标签
     *
     * @param labelId
     * @return
     */
    @Log(title = "液厂标签删除", businessType = BusinessType.DELETE)
    @ApiOperation("液厂标签删除")
    @DeleteMapping("/deleteLabel")
    public AjaxResult deleteLabel(String labelId) {
        return pcFactoryService.deleteLabel(labelId);
    }

    /**
     * 批量删除
     */
    @Log(title = "液厂批量删除", businessType = BusinessType.DELETE)
    @ApiOperation("批量逻辑删除")
    @DeleteMapping("/{deleteArr}")
    public AjaxResult deleteArr(@PathVariable String[] deleteArr) {
        return pcFactoryService.deleteArr(deleteArr);
    }

    /**
     * 查询液厂list标签
     *
     * @param factoryId
     * @return
     */
    @ApiOperation("查询液厂list标签")
    @GetMapping("selLabel")
    public AjaxResult selLabel(String factoryId) {
        return pcFactoryService.selLabel(factoryId);
    }
}
