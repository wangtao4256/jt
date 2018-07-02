package com.jt.manage.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.service.PropertieService;
import com.jt.common.spring.exetend.PropertyConfig;
import com.jt.common.vo.PicUploadResult;

@Controller
public class PicUploadController {
	@PropertyConfig
	private String REPOSITORY_PATH;
	
	@Autowired
	private PropertieService PropertieService;
	
	//怎么把对象转成json串，jackson fastxml
	//private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//请求路径action="/pic/upload"
	//返回json格式要求，url，width，height，error是否出错（非法，木马），对象PicUploadResult
	//页面上的文件就被封装到这个MultipartFile
	@RequestMapping("/pic/upload")
	@ResponseBody
	public PicUploadResult upload(MultipartFile uploadFile) throws IllegalStateException, IOException{
		PicUploadResult result = new PicUploadResult();
		
		//错误标识
		boolean isLeagl = false;
		
		String[] allowType = new String[]{".jpg",".png"};
		for(String type : allowType){
			if(uploadFile.getOriginalFilename().endsWith(type)){
				isLeagl = true;
				break;		//如果有一个匹配就跳出
			}
		}
		if(!isLeagl){
			result.setError(1);		//0代表没有错误，1代表有错误
		}else{
			result.setError(0);
		}
		
		//扩展名称
		String extName = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf("."));
		//yyyyMMddHHmmssSSSS+3位随机值
		String fileName = System.currentTimeMillis() + RandomUtils.nextInt(100, 999) + extName;
		
		Date curDate = new Date();	//获取当前时间
		
		//绝对路径
		String  path = new DateTime(curDate).toString("yyyy")+ File.separator + new DateTime(curDate).toString("MM")+ File.separator + new DateTime(curDate).toString("dd")+ File.separator;

		//图片路径url
		String url = PropertieService.IMAGE_BASE_URL +"/images/"+path;
		path = PropertieService.REPOSITORY_PATH +"/images/"+path;
		
		File dir = new File(path);
		//如果目录不存在，创建目录
		if(!dir.exists()){
			dir.mkdirs();		//防止文件夹为空
		}
		
		//把文件保存下来
		uploadFile.transferTo(new File(path+fileName));
		
		//判断木马，前提，图片文件是有特殊属性的，width，height
		//文件已经在服务器
		File newFile = new File(path+fileName);
		BufferedImage image = ImageIO.read(newFile);
		try{
			result.setUrl(url+"/"+fileName);
			result.setWidth(""+image.getWidth());
			result.setHeight(""+image.getHeight());
		}catch(Exception e){
			result.setError(1);	//有错误
			newFile.delete();	//如果是木马文件要删除
		}
		
		//response.setContentType(MediaType.TEXT_HTML_VALUE);
		//把java对象转成json串
		//return MAPPER.writeValueAsString(result);
		return result;
	}
}
