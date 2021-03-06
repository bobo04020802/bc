package com.zltel.broadcast.um.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zltel.broadcast.common.controller.BaseController;
import com.zltel.broadcast.common.json.R;
import com.zltel.broadcast.um.bean.JoinPartyOrgUser;
import com.zltel.broadcast.um.service.JoinPartyOrgUserService;

import io.swagger.annotations.ApiOperation;

/**
 * 申请入党党员
 * @author 张毅
 * @since jdk1.8.0_172
 * date：2018.8.21
 */
@RequestMapping(value="/join/user")
@RestController
public class JoinPartyOrgUserController extends BaseController {
	@Autowired
	private JoinPartyOrgUserService joinPartyOrgUserService;
	
	/**
	 * 查询申请入党的人员
	 * @param jpop 条件
	 * @return
	 */
	@RequestMapping(value="/queryJoinPartyOrgUsers", method=RequestMethod.POST)
	@ApiOperation(value = "查询申请入党的人员")
	public R queryJoinPartyOrgUsers(@RequestParam Map<String, Object> conditions, int pageNum, int pageSize) {
		try {
			return joinPartyOrgUserService.queryJoinPartyOrgUsers(conditions, pageNum, pageSize);
		} catch (Exception e) {
			return R.error().setMsg("查询申请入党的人员失败");
		}
	}
	
	/**
	 * 申请入党
	 * @param conditions 条件
	 * @return
	 */
	@RequestMapping(value="/insertJoinPartyOrgStep", method=RequestMethod.POST)
	@ApiOperation(value = "申请入党")
	public R insertJoinPartyOrgStep(@RequestParam("submitDate") String submitDate) {
		try {
			return joinPartyOrgUserService.insertJoinPartyOrgStep(submitDate);
		} catch (Exception e) {
			return R.error().setMsg("操作失败");
		}
	}
	
	/**
	 * 申请入党
	 * @param conditions 条件
	 * @return
	 */
	@RequestMapping(value="/insertJoinPartyOrgUsers", method=RequestMethod.POST)
	@ApiOperation(value = "申请入党")
	public R insertJoinPartyOrgUsers(@RequestParam Map<String, Object> conditions) {
		try {
			return joinPartyOrgUserService.insertJoinPartyOrgUsers(conditions);
		} catch (Exception e) {
			return R.error().setMsg("操作失败");
		}
	}
	
	/**
	 * 申请入党-选择组织
	 * @param conditions 条件
	 * @return
	 */
	@RequestMapping(value="/insertJpou", method=RequestMethod.POST)
	@ApiOperation(value = "申请入党-选择组织")
	public R insertJpou(JoinPartyOrgUser jpou) {
		try {
			return joinPartyOrgUserService.insertJpou(jpou);
		} catch (Exception e) {
			return R.error().setMsg("系统出错");
		}
	}
	
	/**
     * 删除此步骤
     * @param jpos
     * @return
     */
	@RequestMapping(value="/deleteJoinPartyOrgSteps", method=RequestMethod.POST)
	@ApiOperation(value = "删除此步骤")
	public R deleteJoinPartyOrgSteps(JoinPartyOrgUser jpou) {
		try {
			return joinPartyOrgUserService.deleteJoinPartyOrgSteps(jpou);
		} catch (Exception e) {
			return R.error().setMsg("删除失败");
		}
	}
}
