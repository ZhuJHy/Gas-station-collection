package com.tulan.system.controller.pc;

import com.tulan.common.core.utils.poi.ExcelUtil;
import com.tulan.common.core.web.controller.BaseController;
import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.common.core.web.page.TableDataInfo;
import com.tulan.common.log.annotation.Log;
import com.tulan.common.log.enums.BusinessType;
import com.tulan.system.common.resp.pc.PcGasDto;
import com.tulan.system.common.vo.pc.AddGasVo;
import com.tulan.system.common.vo.pc.UpdateGasVo;
import com.tulan.system.service.pc.PcGasStationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/gas")
public class PcGasStationController extends BaseController {

    @Resource
    PcGasStationService pcGasStationService;


    /**
     * 增加气站
     * @return
     */
    @Log(title = "添加气站", businessType = BusinessType.INSERT)
    @ApiOperation("添加气站")
    @PostMapping ("/addGes")
    public AjaxResult addGas(@RequestBody @Valid AddGasVo addGasVo){
        return pcGasStationService.addGas(addGasVo);
    }

    /**
     * 修改气站
     */
    @Log(title = "修改气站", businessType = BusinessType.UPDATE)
    @ApiOperation("修改气站")
    @PutMapping("updateGas")
    public AjaxResult updateGas(@RequestBody @Valid UpdateGasVo updateGasVo){
        return pcGasStationService.updateGas(updateGasVo);
    }


    /**
     * 查询气站
     */
    @ApiOperation("查询列表气站")
    @GetMapping("selectGas")
    public TableDataInfo selectGas(@ApiParam("状态") String gasStatus, @ApiParam("气站名称") String gasName){
        startPage();  // 此方法配合前端完成自动分页
        List<PcGasDto> list = pcGasStationService.selectGas(gasStatus,gasName);
        return getDataTable(list);
    }

    /**
     * 导出数据
     */
    @ApiOperation("气站导出数据")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @ApiParam("状态") String gasStatus, @ApiParam("气站名称") String gasName) {
        List<PcGasDto> list = pcGasStationService.selectGas(gasStatus,gasName);
        ExcelUtil<PcGasDto> util = new ExcelUtil<>(PcGasDto.class);
        util.exportExcel(response, list, "气站数据");
    }

    /**
     * 查询气站详情
     */
    @ApiOperation("查询气站详情")
    @GetMapping("/selectDetail")
    public AjaxResult selectDetail(String gasId){
        return pcGasStationService.selectDetail(gasId);
    }

    /**
     * 批量删除气站
     */
    @Log(title = "批量删除气站", businessType = BusinessType.DELETE)
    @ApiOperation("批量删除成功")
    @DeleteMapping("/{deleteArr}")
    public AjaxResult deleteArr(@PathVariable String[] deleteArr){
        return pcGasStationService.deleteArr(deleteArr);
    }


}
