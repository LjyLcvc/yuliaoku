package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.file;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/*
 * @author ljy
 * 负责文件操作，应该为单例类，并且应该为线程安全
 */
public class IoFile {
	
	private static int id=0;
	
	/*
	 * 获取文件名的扩展名，并统一转化为小写格式
	 * @author ljy
	 * @param fileName 文件名
	 * @return 扩展名
	 */
	public static String getNameOfExtension(String fileName) {
		String name = new String("");
		String extention = new String();
		if (fileName.length() > 0 && fileName != null) {
			int i = fileName.lastIndexOf(".");
			if (i > -1 && i < fileName.length()) {
				name = fileName.substring(0, i); //文件名
				extention = fileName.substring(i + 1); //扩展名
			}
		}
		//转换为小写，防止一些后缀名为大写例如JPG，导致JAVA因为与jpg不一致认定后缀名不符合要求
		extention=extention.toLowerCase(); 
		return extention;
	}
	

	/*
	 * 更改文件的扩展名
	 * @author ljy
	 * @param fileName 文件名
	 * @param fileExtOfNew 新的文件扩展名
	 * @return 扩展名
	 */
	public static String getNameOfNewExtension(String fileName,String fileExtOfNew) {
		String name = new String("");
		String extention = new String();
		if (fileName.length() > 0 && fileName != null) {
			int i = fileName.lastIndexOf(".");
			if (i > -1 && i < fileName.length()) {
				name = fileName.substring(0, i); //文件名
				extention = fileName.substring(i + 1); //扩展名
			}
		}
		fileName=name+"."+fileExtOfNew;//获取新的文件名
		return fileName;
	}
	
	/**
	 * 获取按规则生成的新文件名，该文件名将作为存储使用，当并发数较大时需进行文件名是否重名判断
	 * @param ext 后缀名，例如doc
	 * @return 根据新规则生成的文件名，注意不包含路径
	 */
	public static String gainFileNameOfNew(String ext) {
		StringBuffer bufferFileName=new StringBuffer();
		bufferFileName.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));// 以yyyyMMdd的方式生成时间
		bufferFileName.append("_");//中间用符号隔开
		bufferFileName.append(new Random().nextInt(10000));// 时间文件名称生成随机数
		bufferFileName.append(".");
		bufferFileName.append(ext);//最后加上原文件名的后缀名
		String fileName = bufferFileName.toString();
		return fileName;
	}
	
	/**
	 * 获取按JAVA的UUID规则生成的新文件名，该文件名将作为存储使用，理论上可以产生不重复的文件名，无需进行重名判断
	 * @param ext 后缀名，例如doc
	 * @return 根据新规则生成的文件名，注意不包含路径
	 */
	public static String gainFileNameOfNewOfUUID(String ext) {
		StringBuffer bufferFileName=new StringBuffer();
		bufferFileName.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));// 以yyyyMMdd的方式生成时间
		bufferFileName.append("_");//中间用符号隔开
		bufferFileName.append(java.util.UUID.randomUUID().toString());//根据JAVA的UUID生成的唯一标识符
		bufferFileName.append(".");
		bufferFileName.append(ext);//最后加上原文件名的后缀名
		String fileName = bufferFileName.toString();
		return fileName;
	}

	
	public static void main(String[] args){
		//System.out.println(new IoFile().getExtStringForSwfUpload());
		/*List<MyFile> list=new IoFile().getMyFileList("d:\\office2010");
		System.out.println(list.size());
		for(int i=0;i<list.size();i++){
			System.out.println("文件：" +i);
			System.out.println(list.get(i).getNameOfParent());
			System.out.println(list.get(i).getPath());
			System.out.println(list.get(i).getName());
			if(list.get(i).getIsDirectoryOrFile()){
				System.out.println("文件夹");
			}else{
				System.out.println("文件");
			}
			System.out.println(list.get(i).getType());
			System.out.println(list.get(i).getLength()+"k");
			System.out.println(list.get(i).getLastUpdateTime());
		}*/
	}
		
}
