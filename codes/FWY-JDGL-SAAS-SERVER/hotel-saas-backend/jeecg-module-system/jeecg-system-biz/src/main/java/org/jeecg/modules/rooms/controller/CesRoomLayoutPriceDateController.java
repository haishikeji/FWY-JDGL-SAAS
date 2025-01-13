package org.jeecg.modules.rooms.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.ObjectUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.business.entity.BusMemberCard;
import org.jeecg.modules.rooms.entity.CesRoomLayoutPriceDate;
import org.jeecg.modules.rooms.service.ICesRoomLayoutPriceDateService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: ces_room_layout_price_date
 * @@author 品讯科技
 * @Date:   2023-03-23
 * @Version: V1.0
 */
@Api(tags="ces_room_layout_price_date")
@RestController
@RequestMapping("/rooms/cesRoomLayoutPriceDate")
@Slf4j
public class CesRoomLayoutPriceDateController extends JeecgController<CesRoomLayoutPriceDate, ICesRoomLayoutPriceDateService> {
	@Autowired
	private ICesRoomLayoutPriceDateService cesRoomLayoutPriceDateService;
	
	/**
	 * 分页列表查询
	 *
	 * @param cesRoomLayoutPriceDate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "ces_room_layout_price_date-分页列表查询")
	@ApiOperation(value="ces_room_layout_price_date-分页列表查询", notes="ces_room_layout_price_date-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<CesRoomLayoutPriceDate>> queryPageList(CesRoomLayoutPriceDate cesRoomLayoutPriceDate,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CesRoomLayoutPriceDate> queryWrapper = QueryGenerator.initQueryWrapper(cesRoomLayoutPriceDate, req.getParameterMap());
		Page<CesRoomLayoutPriceDate> page = new Page<CesRoomLayoutPriceDate>(pageNo, pageSize);
		IPage<CesRoomLayoutPriceDate> pageList = cesRoomLayoutPriceDateService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 房价列表
	  * @param cesRoomLayoutPriceDate
	  * @param pageNo
	  * @param pageSize
	  * @param startTime
	  * @param endTime
	  * @param hotelIds
	  * @param req
	  * @return
	  */
	 @ApiOperation(value="ces_room_layout_price_date-分页列表查询", notes="ces_room_layout_price_date-分页列表查询")
	 @GetMapping(value = "/pageList")
	 public Result<IPage<HashMap<String,Object>>> pageList(CesRoomLayoutPriceDate cesRoomLayoutPriceDate,
														   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														   @RequestParam(name="startTime",required = true) String startTime,
														   @RequestParam(name="endTime",required = true) String endTime,
														   @RequestParam(name="hotelIds[]",required = true) ArrayList<String> hotelIds,
														   HttpServletRequest req) {
		 Page<HashMap<String,Object>> page = new Page<HashMap<String,Object>>(pageNo, pageSize);
		 IPage<HashMap<String,Object>> pageList = cesRoomLayoutPriceDateService.pageList(page,hotelIds, DateUtil.parseDate(startTime),DateUtil.parseDate(endTime));
		 return Result.OK(pageList);
	 }
	
	/**
	 *   添加
	 *
	 * @param cesRoomLayoutPriceDate
	 * @return
	 */
	@AutoLog(value = "ces_room_layout_price_date-添加")
	@ApiOperation(value="ces_room_layout_price_date-添加", notes="ces_room_layout_price_date-添加")
	//@RequiresPermissions("rooms:ces_room_layout_price_date:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CesRoomLayoutPriceDate cesRoomLayoutPriceDate) {
		cesRoomLayoutPriceDateService.save(cesRoomLayoutPriceDate);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param cesRoomLayoutPriceDate
	 * @return
	 */
	@AutoLog(value = "ces_room_layout_price_date-编辑")
	@ApiOperation(value="ces_room_layout_price_date-编辑", notes="ces_room_layout_price_date-编辑")
	//@RequiresPermissions("rooms:ces_room_layout_price_date:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CesRoomLayoutPriceDate cesRoomLayoutPriceDate) {
		cesRoomLayoutPriceDateService.updateById(cesRoomLayoutPriceDate);
		return Result.OK("编辑成功!");
	}

	 /**
	  * 编辑价格
	  * @param cesRoomLayoutPriceDate
	  * @return
	  */
	 @AutoLog(value = "ces_room_layout_price_date-编辑")
	 @ApiOperation(value="ces_room_layout_price_date-编辑", notes="ces_room_layout_price_date-编辑")
	 //@RequiresPermissions("rooms:ces_room_layout_price_date:edit")
	 @RequestMapping(value = "/editPrice", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> editPrice(@RequestBody CesRoomLayoutPriceDate cesRoomLayoutPriceDate) {
		 LambdaQueryWrapper<CesRoomLayoutPriceDate> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		 lambdaQueryWrapper.eq(CesRoomLayoutPriceDate::getRoomLayoutPriceId, cesRoomLayoutPriceDate.getRoomLayoutPriceId());
		 lambdaQueryWrapper.eq(CesRoomLayoutPriceDate::getDate, cesRoomLayoutPriceDate.getDate());
		 CesRoomLayoutPriceDate roomLayoutPriceDate = cesRoomLayoutPriceDateService.getOne(lambdaQueryWrapper);
		 if (ObjectUtils.isNotEmpty(roomLayoutPriceDate)) {
			 roomLayoutPriceDate.setPrice(cesRoomLayoutPriceDate.getPrice());
			 cesRoomLayoutPriceDateService.updateById(roomLayoutPriceDate);
		 } else {
			 roomLayoutPriceDate=new CesRoomLayoutPriceDate();
			 roomLayoutPriceDate.setRoomLayoutPriceId(cesRoomLayoutPriceDate.getRoomLayoutPriceId());
			 roomLayoutPriceDate.setDate(cesRoomLayoutPriceDate.getDate());
			 roomLayoutPriceDate.setPrice(cesRoomLayoutPriceDate.getPrice());
			 roomLayoutPriceDate.setCreateDate(new Date());
			 cesRoomLayoutPriceDateService.save(roomLayoutPriceDate);
		 }
		 return Result.OK("编辑成功!");
	 }
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "ces_room_layout_price_date-通过id删除")
	@ApiOperation(value="ces_room_layout_price_date-通过id删除", notes="ces_room_layout_price_date-通过id删除")
	//@RequiresPermissions("rooms:ces_room_layout_price_date:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		cesRoomLayoutPriceDateService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "ces_room_layout_price_date-批量删除")
	@ApiOperation(value="ces_room_layout_price_date-批量删除", notes="ces_room_layout_price_date-批量删除")
	//@RequiresPermissions("rooms:ces_room_layout_price_date:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cesRoomLayoutPriceDateService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "ces_room_layout_price_date-通过id查询")
	@ApiOperation(value="ces_room_layout_price_date-通过id查询", notes="ces_room_layout_price_date-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<CesRoomLayoutPriceDate> queryById(@RequestParam(name="id",required=true) String id) {
		CesRoomLayoutPriceDate cesRoomLayoutPriceDate = cesRoomLayoutPriceDateService.getById(id);
		if(cesRoomLayoutPriceDate==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(cesRoomLayoutPriceDate);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cesRoomLayoutPriceDate
    */
    //@RequiresPermissions("rooms:ces_room_layout_price_date:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CesRoomLayoutPriceDate cesRoomLayoutPriceDate) {
        return super.exportXls(request, cesRoomLayoutPriceDate, CesRoomLayoutPriceDate.class, "ces_room_layout_price_date");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("rooms:ces_room_layout_price_date:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CesRoomLayoutPriceDate.class);
    }

}
