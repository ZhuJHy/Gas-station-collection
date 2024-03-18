package com.tulan.system.controller.pc;

import com.tulan.common.core.utils.poi.ExcelUtil;
import com.tulan.common.core.web.controller.BaseController;
import com.tulan.common.core.web.domain.AjaxResult;
import com.tulan.common.core.web.page.TableDataInfo;
import com.tulan.common.log.annotation.Log;
import com.tulan.common.log.enums.BusinessType;
import com.tulan.system.api.domain.Messenger;
import com.tulan.system.common.vo.pc.AddMessenger;
import com.tulan.system.common.vo.pc.UpdateMesStatus;
import com.tulan.system.common.vo.pc.UpdateMessenger;
import com.tulan.system.service.pc.PcMessengerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/messenger")
public class PcMessengerController extends BaseController {

    @Resource
    PcMessengerService pcMessengerService;

    /**
     * 查询信息员列表
     */
    @ApiOperation("查询气站信息员列表")
    @GetMapping("selectList")
    public TableDataInfo selectList(@ApiParam("信息员名称") String mName, @ApiParam("气站名称") String mPhone){
        startPage();  // 此方法配合前端完成自动分页
        List<Messenger> list = pcMessengerService.selectList(mName,mPhone);
        return getDataTable(list);
    }
    /**
     * 导出数据
     */
    @ApiOperation("信息员导出数据")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @ApiParam("信息员名称") String mName, @ApiParam("气站名称") String mPhone) {
        List<Messenger> list = pcMessengerService.selectList(mName,mPhone);
        ExcelUtil<Messenger> util = new ExcelUtil<>(Messenger.class);
        util.exportExcel(response, list, "信息员数据");
    }

    /**
     *信息员详情
     * @param mgId
     * @return
     */
    @ApiOperation("信息员详情")
    @GetMapping("/MessengerDetail")
    public AjaxResult messengerDetail(String mgId){
        return pcMessengerService.messengerDetail(mgId);
    }

    /**
     * 检索气站
     * @param gasName
     * @return
     */
    @ApiOperation("检索气站")
    @GetMapping("/selectGas")
    public AjaxResult selectGas(String gasName){
        return pcMessengerService.selectGas(gasName);
    }

    /**
     * 添加信息员
     * @param addMessenger
     * @return
     */
    @Log(title = "添加信息员", businessType = BusinessType.INSERT)
    @ApiOperation("添加气站信息员")
    @PostMapping("/addMessenger")
    public AjaxResult addMessenger(@RequestBody @Valid AddMessenger addMessenger){
        return pcMessengerService.addMessenger(addMessenger);
    }

    /**
     * 修改信息员
     */
    @Log(title = "修改信息员", businessType = BusinessType.UPDATE)
    @ApiOperation("修改信息员信息")
    @PutMapping("updateMessenger")
    public AjaxResult updateMessenger(@RequestBody @Valid UpdateMessenger updateMessenger){
        return pcMessengerService.updateMessenger(updateMessenger);
    }


    /**
     * 解绑信息员
     */
    @Log(title = "解绑信息员", businessType = BusinessType.UPDATE)
    @ApiOperation("解绑信息员")
    @PutMapping("untieMessenger")
    public AjaxResult untieMessenger(String mgId){
        return pcMessengerService.untieMessenger(mgId);
    }

    /**
     * 删除信息员
     * @return
     */
    @Log(title = "删除信息员", businessType = BusinessType.DELETE)
    @ApiOperation("删除信息员")
    @DeleteMapping("/{deleteMessenger}")
    public AjaxResult deleteMessenger(@PathVariable String[] deleteMessenger){
        return pcMessengerService.delMessenger(deleteMessenger);
    }
    /**
     * 启用禁用信息员
     */
    @Log(title = "修改信息员状态", businessType = BusinessType.UPDATE)
    @ApiOperation("启用禁用信息员")
    @PutMapping("updateStatus")
    public AjaxResult updateStatus(@RequestBody UpdateMesStatus updateMesStatus){
        return pcMessengerService.updateStatus(updateMesStatus.getStatus(),updateMesStatus.getMgId());
    }


}
