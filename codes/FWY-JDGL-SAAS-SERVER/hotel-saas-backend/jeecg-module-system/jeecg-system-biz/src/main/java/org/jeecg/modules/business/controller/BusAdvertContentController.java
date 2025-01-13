package org.jeecg.modules.business.controller;

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
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.dynamic.db.DataSourceCachePool;
import org.jeecg.common.util.oConvertUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.business.entity.BusAdvertContent;
import org.jeecg.modules.business.entity.BusHotel;
import org.jeecg.modules.business.service.IBusAdvertContentService;
import org.jeecg.modules.system.entity.SysDataSource;
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
 * @Description: bus_advert_content_info
 * @@author 品讯科技
 * @Date:   2023-03-02
 * @Version: V1.0
 */
@Api(tags="bus_advert_content_info")
@RestController
@RequestMapping("/business/busAdvertContent")
@Slf4j
public class BusAdvertContentController extends JeecgController<BusAdvertContent, IBusAdvertContentService> {
	@Autowired
	private IBusAdvertContentService busAdvertContentService;

	/**
	 * 分页列表查询
	 *
	 * @param busAdvertContent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "bus_advert_content_info-分页列表查询")
	@ApiOperation(value="bus_advert_content_info-分页列表查询", notes="bus_advert_content_info-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BusAdvertContent>> queryPageList(BusAdvertContent busAdvertContent,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BusAdvertContent> queryWrapper = QueryGenerator.initQueryWrapper(busAdvertContent, req.getParameterMap());
		Page<BusAdvertContent> page = new Page<BusAdvertContent>(pageNo, pageSize);
		IPage<BusAdvertContent> pageList = busAdvertContentService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param busAdvertContent
	 * @return
	 */
	@AutoLog(value = "bus_advert_content_info-添加")
	@ApiOperation(value="bus_advert_content_info-添加", notes="bus_advert_content_info-添加")
	//@RequiresPermissions("business:bus_advert_content_info:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BusAdvertContent busAdvertContent) {
		busAdvertContent.setStatus(1);
		busAdvertContent.setDelFlag(CommonConstant.DEL_FLAG_0);
		if(busAdvertContent.getTenantId() == null || busAdvertContent.getTenantId().equals("")){
			LoginUser user = TokenUtils.getAuthUser();
			if(user.getRelTenantIds() != null && !user.getRelTenantIds().equals("")){
				busAdvertContent.setTenantId(user.getRelTenantIds());
			} else {
				throw new JeecgBootException("当前登录人租户信息错误");
			}
		}
		busAdvertContentService.save(busAdvertContent);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param busAdvertContent
	 * @return
	 */
	@AutoLog(value = "bus_advert_content_info-编辑")
	@ApiOperation(value="bus_advert_content_info-编辑", notes="bus_advert_content_info-编辑")
	//@RequiresPermissions("business:bus_advert_content_info:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BusAdvertContent busAdvertContent) {
		if(busAdvertContent.getTenantId() == null || busAdvertContent.getTenantId().equals("")){
			LoginUser user = TokenUtils.getAuthUser();
			if(user.getRelTenantIds() != null && !user.getRelTenantIds().equals("")){
				busAdvertContent.setTenantId(user.getRelTenantIds());
			} else {
				throw new JeecgBootException("当前登录人租户信息错误");
			}
		}
		BusAdvertContent editModel = busAdvertContentService.getById(busAdvertContent.getId());
		editModel.setTitle(busAdvertContent.getTitle());
		editModel.setContent(busAdvertContent.getContent());
		editModel.setOrderBy(busAdvertContent.getOrderBy());
		editModel.setStatus(busAdvertContent.getStatus());
		busAdvertContentService.updateById(editModel);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bus_advert_content_info-通过id删除")
	@ApiOperation(value="bus_advert_content_info-通过id删除", notes="bus_advert_content_info-通过id删除")
	//@RequiresPermissions("business:bus_advert_content_info:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		busAdvertContentService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bus_advert_content_info-批量删除")
	@ApiOperation(value="bus_advert_content_info-批量删除", notes="bus_advert_content_info-批量删除")
	//@RequiresPermissions("business:bus_advert_content_info:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.busAdvertContentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "bus_advert_content_info-通过id查询")
	@ApiOperation(value="bus_advert_content_info-通过id查询", notes="bus_advert_content_info-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BusAdvertContent> queryById(@RequestParam(name="id",required=true) String id) {
		BusAdvertContent busAdvertContent = busAdvertContentService.getById(id);
		if(busAdvertContent==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(busAdvertContent);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param busAdvertContent
    */
    //@RequiresPermissions("business:bus_advert_content_info:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BusAdvertContent busAdvertContent) {
        return super.exportXls(request, busAdvertContent, BusAdvertContent.class, "bus_advert_content_info");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("business:bus_advert_content_info:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BusAdvertContent.class);
    }

}
