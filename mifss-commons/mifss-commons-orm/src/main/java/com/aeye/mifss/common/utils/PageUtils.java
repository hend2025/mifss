package com.aeye.mifss.common.utils;

import cn.hsa.ims.common.utils.AeyePageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public class PageUtils{
	/**
	 * 适配mybatis-plus的分页对象转换为hsaf分页对象；AeyePageInfo为hsaf适配分页对象
	 */
	public static <T> AeyePageResult pageConvert(IPage<T> page) {
		AeyePageResult aeyePageResult = new AeyePageResult();
		//List数据
		aeyePageResult.setData(page.getRecords());
		//记录条数
		aeyePageResult.setRecordCounts((int)page.getTotal());
		//每页大小
		aeyePageResult.setPageSize((int)page.getSize());
		//当前多少页
		aeyePageResult.setPageNum((int)page.getCurrent());
		//总分页大小
		aeyePageResult.setPages((int)page.getPages());
		aeyePageResult.setFirstPage(page.getCurrent() == 1);
		aeyePageResult.setLastPage(page.getCurrent() >= page.getPages());
		return aeyePageResult;
	}
	/**
	 *AeyePageResult<T> 转 AeyePageResult<R>
	 */
	public static  <T, R> AeyePageResult<R> convertPageResult(AeyePageResult<T> oldPageResult, List<R> collect) {
		AeyePageResult<R> newPageResult = new AeyePageResult<>();
		newPageResult.setData(collect);
		newPageResult.setPageNum(oldPageResult.getPageNum());
		newPageResult.setPageSize(oldPageResult.getPageSize());
		newPageResult.setPages(oldPageResult.getPages());
		newPageResult.setRecordCounts(oldPageResult.getRecordCounts());
		newPageResult.setFirstPage(oldPageResult.isFirstPage());
		newPageResult.setLastPage(oldPageResult.isLastPage());
		return newPageResult;
	}

	/**
	 *IPage<T> 转 IPage<R>
	 */
	public static  <T, R> IPage<R> convertIpage(IPage<T> oldPageResult, List<R> collect) {
		IPage<R> newPageResult = new Page<>();
		newPageResult.setRecords(collect);
		newPageResult.setPages(oldPageResult.getPages());
		newPageResult.setPages(oldPageResult.getPages());
		newPageResult.setTotal(oldPageResult.getTotal());
		newPageResult.setCurrent(oldPageResult.getCurrent());
		newPageResult.setSize(oldPageResult.getSize());
		return newPageResult;
	}
}
