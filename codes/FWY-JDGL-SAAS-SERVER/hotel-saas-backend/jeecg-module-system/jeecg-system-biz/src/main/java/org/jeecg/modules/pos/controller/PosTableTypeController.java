package org.jeecg.modules.pos.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pos.entity.PosTableType;
import org.jeecg.modules.pos.entity.PosType;
import org.jeecg.modules.pos.service.IPosTableTypeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.pos.service.IPosTypeService;
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
 * @Description: pos_table_type
 * @@author 品讯科技
 * @Date:   2023-04-11
 * @Version: V1.0
 */
@Api(tags="pos_table_type")
@RestController
@RequestMapping("/pos/posTableType")
@Slf4j
public class PosTableTypeController extends JeecgController<PosTableType, IPosTableTypeService> {
	@Autowired
	private IPosTableTypeService posTableTypeService;
	 @Autowired
	 private IPosTypeService posTypeService;
	/**
	 * 分页列表查询
	 *
	 * @param posTableType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "pos_table_type-分页列表查询")
	@ApiOperation(value="pos_table_type-分页列表查询", notes="pos_table_type-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PosTableType>> queryPageList(PosTableType posTableType,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PosTableType> queryWrapper = QueryGenerator.initQueryWrapper(posTableType, req.getParameterMap());
		Page<PosTableType> page = new Page<PosTableType>(pageNo, pageSize);
		IPage<PosTableType> pageList = posTableTypeService.page(page, queryWrapper);
		pageList.getRecords().forEach(item -> {
			PosType type = posTypeService.getById(item.getPosTypeId());
			if (type != null) {
				item.setTypeName(type.getName());
			}
		});
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param posTableType
	 * @return
	 */
	@AutoLog(value = "pos_table_type-添加")
	@ApiOperation(value="pos_table_type-添加", notes="pos_table_type-添加")
	//@RequiresPermissions("pos:pos_table_type:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PosTableType posTableType) {
		if (posTableType.getTenantId() == null || posTableType.getTenantId().equals("")) {
			LoginUser user = TokenUtils.getAuthUser();
			if (user.getRelTenantIds() != null && !user.getRelTenantIds().equals("")) {
				posTableType.setTenantId(user.getRelTenantIds());
			} else {
				throw new JeecgBootException("当前登录人租户信息错误");
			}
		}
		posTableTypeService.save(posTableType);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param posTableType
	 * @return
	 */
	@AutoLog(value = "pos_table_type-编辑")
	@ApiOperation(value="pos_table_type-编辑", notes="pos_table_type-编辑")
	//@RequiresPermissions("pos:pos_table_type:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PosTableType posTableType) {
		posTableTypeService.updateById(posTableType);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "pos_table_type-通过id删除")
	@ApiOperation(value="pos_table_type-通过id删除", notes="pos_table_type-通过id删除")
	//@RequiresPermissions("pos:pos_table_type:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		posTableTypeService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "pos_table_type-批量删除")
	@ApiOperation(value="pos_table_type-批量删除", notes="pos_table_type-批量删除")
	//@RequiresPermissions("pos:pos_table_type:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.posTableTypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "pos_table_type-通过id查询")
	@ApiOperation(value="pos_table_type-通过id查询", notes="pos_table_type-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PosTableType> queryById(@RequestParam(name="id",required=true) String id) {
		PosTableType posTableType = posTableTypeService.getById(id);
		if(posTableType==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(posTableType);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param posTableType
    */
    //@RequiresPermissions("pos:pos_table_type:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PosTableType posTableType) {
        return super.exportXls(request, posTableType, PosTableType.class, "pos_table_type");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("pos:pos_table_type:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, PosTableType.class);
    }

}
