package cn.hsa.ims.common.contoller;

import cn.hsa.hsaf.core.framework.HsafController;
import cn.hsa.hsaf.core.framework.util.CurrentUser;
import cn.hsa.hsaf.core.framework.util.PageInfo;
import cn.hsa.ims.common.utils.AeyeLoginUtil;
import cn.hsa.ims.common.utils.AeyePageInfo;
import cn.hsa.ims.common.utils.AeyeReflectionUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public abstract class AeyeAbstractController extends HsafController {
	protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected HttpSession session;
	@Autowired
	protected HttpServletResponse response;
	
	protected CurrentUser getUser() {
		return AeyeLoginUtil.getCurrentUser();
	}

	protected String getUserId() {
		return getUser().getUserAcctID();
	}

	protected String getDeptId() {
		return getUser().getDeptID();
	}

	protected HttpSession getSession() {
		return this.session;
	}

	protected HttpServletRequest getRequest() {
		return this.request;
	}

	protected HttpServletResponse getResponse() {
		return this.response;
	}

	protected AeyePageInfo buildPageInfo(){
		String pageNum = request.getHeader(AeyeReflectionUtil.getFieldName(PageInfo::getPageNum));
		String pageSize = request.getHeader(AeyeReflectionUtil.getFieldName(PageInfo::getPageSize));
		String orderField = request.getHeader(AeyeReflectionUtil.getFieldName(PageInfo::getOrderField));
		String orderType = request.getHeader(AeyeReflectionUtil.getFieldName(PageInfo::getOrderType));
		AeyePageInfo pageInfo = new AeyePageInfo();
		pageInfo.setPageNum(StrUtil.isBlank(pageNum) ? 1 : Integer.parseInt(pageNum));
		pageInfo.setPageSize(StrUtil.isBlank(pageSize) ? 10 : Integer.parseInt(pageSize));
		pageInfo.setOrderField(orderField);
		pageInfo.setOrderType(orderType);
		return pageInfo;
	}
}
