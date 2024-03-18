package com.tulan.system.controller.pc;

import com.tulan.common.core.utils.poi.ExcelUtil;
import com.tulan.common.core.web.controller.BaseController;
import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.common.core.web.page.TableDataInfo;
import com.tulan.common.log.annotation.Log;
import com.tulan.common.log.enums.BusinessType;
import com.tulan.system.common.resp.pc.PcCarDto;
import com.tulan.system.common.vo.pc.AddCarVo;
import com.tulan.system.common.vo.pc.UpdateCarVo;
import com.tulan.system.service.pc.PcCarService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/car")
public class PcCarController extends BaseController {

    @Resource
    PcCarService pcCarService;

    /**
     * 条件查询车辆
     *
     * @param carName
     * @param carNumber
     * @return
     */
    @ApiOperation("查询车辆列表")
    @GetMapping("/selectCar")
    public TableDataInfo selectCar(String carName, String carNumber, Integer carStatus) {
        startPage();  // 此方法配合前端完成自动分页
        List<PcCarDto> list = pcCarService.selectCar(carName,carNumber,carStatus);
        return getDataTable(list);
    }

    /**
     * 导出数据
     */
    @ApiOperation("车辆导出数据")
    @PostMapping("/export")
    public void export(HttpServletResponse response, String carName, String carNumber, Integer carStatus) {
        List<PcCarDto> list = pcCarService.selectCar(carName, carNumber,carStatus);
        ExcelUtil<PcCarDto> util = new ExcelUtil<>(PcCarDto.class);
        util.exportExcel(response, list, "车辆数据");
    }

    /**
     * 车辆详情查询
     */
    @ApiOperation("车辆详情查询")
    @GetMapping("/carDetail")
    public AjaxResult carDetail(Integer cId) {
        return pcCarService.carDetail(cId);
    }

    /**
     * 修改车辆详情
     */
    @Log(title = "修改车辆详情", businessType = BusinessType.UPDATE)
    @ApiOperation("修改车辆详情")
    @PutMapping("/updateCar")
    public AjaxResult updateCar(@RequestBody @Valid UpdateCarVo updateCarVo) {
        return pcCarService.updateCar(updateCarVo);
    }

    /**
     * 增加车辆
     */
    @Log(title = "添加车辆", businessType = BusinessType.INSERT)
    @ApiOperation("增加车辆")
    @PostMapping("/addCar")
    public AjaxResult addCar(@RequestBody @Valid AddCarVo addCarVo) {
        return pcCarService.addCar(addCarVo);
    }

    /**
     * 删除车辆信息
     */
    @Log(title = "删除车辆", businessType = BusinessType.DELETE)
    @ApiOperation("删除车辆")
    @DeleteMapping("/{deleteCar}")
    public AjaxResult deleteCar(@PathVariable String[] deleteCar) {
        return pcCarService.deleteCar(deleteCar);
    }

    /**
     * 车辆解绑气站
     */
    @Log(title = "车辆与气站解绑", businessType = BusinessType.DELETE)
    @ApiOperation("解绑车辆气站")
    @DeleteMapping("/delGasCar")
    public AjaxResult delGasCar(String cId, String gasId) {
        return pcCarService.delGasCar(cId, gasId);
    }


}
