package org.jeecg.modules.business.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.BusCustomer;
import org.jeecg.modules.business.entity.BusLivingCustomer;
import org.jeecg.modules.business.service.IBusCustomerService;
import org.jeecg.modules.business.service.IBusLivingCustomerService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.business.vo.BusLivingCustomerVo;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
 * @Description: bus_living_customer
 * @@author 品讯科技
 * @Date:   2023-04-04
 * @Version: V1.0
 */
@Api(tags="bus_living_customer")
@RestController
@RequestMapping("/business/busLivingCustomer")
@Slf4j
public class BusLivingCustomerController extends JeecgController<BusLivingCustomer, IBusLivingCustomerService> {
	@Autowired
	private IBusLivingCustomerService busLivingCustomerService;

	@Resource
	private IBusCustomerService customerService;
	/**
	 * 分页列表查询
	 *
	 * @param busLivingCustomer
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "bus_living_customer-分页列表查询")
	@ApiOperation(value="bus_living_customer-分页列表查询", notes="bus_living_customer-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BusLivingCustomer>> queryPageList(BusLivingCustomer busLivingCustomer,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BusLivingCustomer> queryWrapper = QueryGenerator.initQueryWrapper(busLivingCustomer, req.getParameterMap());
		Page<BusLivingCustomer> page = new Page<BusLivingCustomer>(pageNo, pageSize);
		IPage<BusLivingCustomer> pageList = busLivingCustomerService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param s
	 * @return
	 */
	@AutoLog(value = "bus_living_customer-添加")
	@ApiOperation(value="bus_living_customer-添加", notes="bus_living_customer-添加")
	//@RequiresPermissions("business:bus_living_customer:add")
	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BusLivingCustomerVo s) {
		if(s.getCustomerName() != null && !s.getCustomerName().isEmpty() && s.getPhone() != null && !s.getPhone().isEmpty()){
			BusCustomer customer = customerService.getOne(Wrappers.<BusCustomer>query().eq("name",s.getCustomerName())
					.eq("phone",s.getPhone()));
			LoginUser user = TokenUtils.getAuthUser();
			if(customer == null) {
				// save customer
				BusCustomer newCustomer = new BusCustomer();
				newCustomer.setCreateTime(new Date());
				newCustomer.setCreateUser("");
				newCustomer.setTenantId(user.getRelTenantIds());
				newCustomer.setName(s.getCustomerName());
				newCustomer.setPhone(s.getPhone());
				newCustomer.setCertType(1); // 身份证
				newCustomer.setNation(s.getNation());
				customerService.save(newCustomer);
				s.setCustomerId(newCustomer.getId());
			} else {
				s.setCustomerId(customer.getId());
			}
		} else throw new JeecgBootException("客人信息不能为空");
		busLivingCustomerService.save(s);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param busLivingCustomer
	 * @return
	 */
	@AutoLog(value = "bus_living_customer-编辑")
	@ApiOperation(value="bus_living_customer-编辑", notes="bus_living_customer-编辑")
	//@RequiresPermissions("business:bus_living_customer:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BusLivingCustomer busLivingCustomer) {
		busLivingCustomerService.updateById(busLivingCustomer);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bus_living_customer-通过id删除")
	@ApiOperation(value="bus_living_customer-通过id删除", notes="bus_living_customer-通过id删除")
	//@RequiresPermissions("business:bus_living_customer:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		busLivingCustomerService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bus_living_customer-批量删除")
	@ApiOperation(value="bus_living_customer-批量删除", notes="bus_living_customer-批量删除")
	//@RequiresPermissions("business:bus_living_customer:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.busLivingCustomerService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "bus_living_customer-通过id查询")
	@ApiOperation(value="bus_living_customer-通过id查询", notes="bus_living_customer-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BusLivingCustomer> queryById(@RequestParam(name="id",required=true) String id) {
		BusLivingCustomer busLivingCustomer = busLivingCustomerService.getById(id);
		if(busLivingCustomer==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(busLivingCustomer);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param busLivingCustomer
    */
    //@RequiresPermissions("business:bus_living_customer:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BusLivingCustomer busLivingCustomer) {
        return super.exportXls(request, busLivingCustomer, BusLivingCustomer.class, "bus_living_customer");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("business:bus_living_customer:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BusLivingCustomer.class);
    }

}
