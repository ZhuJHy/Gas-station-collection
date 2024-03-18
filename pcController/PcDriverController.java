package com.tulan.system.controller.pc;

import com.tulan.common.core.utils.poi.ExcelUtil;
import com.tulan.common.core.web.controller.BaseController;
import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.common.core.web.page.TableDataInfo;
import com.tulan.common.log.annotation.Log;
import com.tulan.common.log.enums.BusinessType;
import com.tulan.system.common.resp.pc.PcDriverDto;
import com.tulan.system.common.vo.pc.AddDriverVo;
import com.tulan.system.common.vo.pc.UpdateDriverVo;
import com.tulan.system.service.pc.PcDriverService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/driver")
public class PcDriverController extends BaseController {


    @Resource
    PcDriverService pcDriverService;

    /**
     * 司机查看
     */
    @ApiOperation("司机查询")
    @GetMapping("/selectDriver")
    public TableDataInfo selectDriver(String driverName, String phone,String escortStation) {
        startPage();  // 此方法配合前端完成自动分页
        List<PcDriverDto> list = pcDriverService.selectDriver(driverName, phone,escortStation);
        return getDataTable(list);
    }

    /**
     * 导出数据
     */
    @ApiOperation("司机导出数据")
    @PostMapping("/export")
    public void export(HttpServletResponse response, String driverName, String phone,String escortStation) {
        List<PcDriverDto> list = pcDriverService.selectDriver(driverName, phone,escortStation);
        ExcelUtil<PcDriverDto> util = new ExcelUtil<>(PcDriverDto.class);
        util.exportExcel(response, list, "司机数据");
    }


    /**
     * 司机详情
     */
    @ApiOperation("司机详情")
    @GetMapping("/driverDetailId")
    public AjaxResult driverDetailId(String driverDetailId) {
        return pcDriverService.driverDetail(driverDetailId);
    }

    /**
     * 添加司机
     */
    @Log(title = "添加司机", businessType = BusinessType.INSERT)
    @ApiOperation("添加司机")
    @PostMapping("/addDriver")
    public AjaxResult addDriver(@RequestBody @Valid AddDriverVo addDriverVo) {
        return pcDriverService.addDriver(addDriverVo);
    }

    /**
     * 修改司机
     */
    @Log(title = "修改司机", businessType = BusinessType.UPDATE)
    @ApiOperation("修改司机")
    @PutMapping("/updateDriver")
    public AjaxResult updateDriver(@RequestBody @Valid UpdateDriverVo updateDriverVo) {
        return pcDriverService.updateDriver(updateDriverVo);
    }

    /**
     * 删除司机
     */
    @Log(title = "删除司机", businessType = BusinessType.DELETE)
    @ApiOperation("删除司机")
    @DeleteMapping("/{deleteDriver}")
    public AjaxResult deleteDriver(@PathVariable String[] deleteDriver){
        return pcDriverService.deleteDriver(deleteDriver);
    }

    /**
     * 解绑气站司机
     */
    @Log(title = "司机与气站解绑", businessType = BusinessType.DELETE)
    @ApiOperation("解绑司机与气站")
    @DeleteMapping("/delGasDriver")
    public AjaxResult delGasDriver(String dId,String gasId){
        return pcDriverService.delGasDriver(dId,gasId);
    }





}
