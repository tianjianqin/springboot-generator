package com.ucmed.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理器
 * 
 * @author czx
 * @email object_czx@163.com
 * @date 2016年10月27日 下午10:16:19
 */
@Component
public class RRExceptionHandler implements HandlerExceptionResolver {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		Result result = new Result();
		try {
			response.setContentType("application/json;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			
			if (ex instanceof RRException) {
				result.put("code", ((RRException) ex).getCode());
				result.put("msg", ((RRException) ex).getMessage());
			}else if(ex instanceof DuplicateKeyException){
				result = Result.error("数据库中已存在该记录");
			}else{
				result = Result.error();
			}
			
			//记录异常日志
			logger.error(ex.getMessage(), ex);
			
			String json = JSON.toJSONString(result);
			response.getWriter().print(json);
		} catch (Exception e) {
			logger.error("RRExceptionHandler 异常处理失败", e);
		}
		return new ModelAndView();
	}
}
