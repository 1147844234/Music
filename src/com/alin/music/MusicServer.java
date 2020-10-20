package com.alin.music;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class MusicServer {
	public static List<Music> searchMusic(String key) throws Exception{
		
		//不能一开始就去请求搜索地址
		//请求酷我官网(execute可以获取请求响应隐藏信息)
		Response res = Jsoup.connect("https://www.kuwo.cn/").execute();
		//获取token值
		String token = res.cookies().get("kw_token");
		  
		//请求搜索歌曲地址
		Document doc = Jsoup.connect("https://www.kuwo.cn/api/www/search/searchMusicBykeyWord?key="+key+"&pn=1&rn=30&httpsStatus=1&reqId=c2b4be40-fe08-11ea-9756-b13061b167b3")
				.header("csrf", token)
				.header("Referer", "https://www.kuwo.cn/")//表示从酷我官网发起的搜索
				.cookie("kw_token", token)//与第一次的token身份值相同
				.ignoreContentType(true)//忽略内容类型
				.get();
		
		//通过json解析获取所有歌曲数据
		String data = JSONObject.parseObject(doc.text()).getString("data");//data数据
		String list = JSONObject.parseObject(data).getString("list");
		
		//将list所有歌曲信息转成java的music集合
		List<Music> musics = JSONObject.parseArray(list,Music.class);
		
		/*
		//获取歌曲的url（MP3路径）
		for(Music m : musics) {//请求播放按钮的地址，改下rid，即可获取响应的mp3路径
			String mp3URL = "https://www.kuwo.cn/url?format=mp3&rid="+m.getRid()+"&response=url&type=convert_url3&br=128kmp3&from=web&t=1600917212287&httpsStatus=1&reqId=ed098801-fe13-11ea-9756-b13061b167b3";
			doc = Jsoup.connect(mp3URL).get();
			//通过Jsoup解析器，获取url地址
			m.setUrl(JSONObject.parseObject(doc.text()).getString("url"));
		}*/
		/*
		for(Music m: musics) {
			String gcURL= "http://m.kuwo.cn/newh5/singles/songinfoandlrc?musicId="+m.getRid()+"&httpsStatus=1&reqId=e8b28050-fecb-11ea-84f7-933531b20713";
			Rsesponse r = Jsoup.connect(gcURL).execute();//连接页面获取歌词
			String body = r.body();//获取响应回的json数据
			data = JSONObject.parseObject(body).getString("data");//从结果里拿到data
			list = JSONObject.parseObject(data).getString("lrclist");//从data里拿到每句歌词
			JSONArray arr = JSONObject.parseArray(list);//从list里拿每句歌词
			//循环拿到每句歌词
			String strGC = "";//累拼所有的歌词
			for (Object o : arr) {
				//获取当前这句歌词的内容
				String gc =  JSONObject.parseObject(o.toString()).getString("lineLyric");
				//累拼（每句歌词在网页上为一行，末尾加<br>）
				strGC += gc + "<br>";
				
			}
			m.setGc(strGC);//将歌词封装
		}
		*/
		return musics;
	}


	//根据rid获取mp3路径
	public static String mp3URL(String rid) throws Exception {
		String mp3URL = "https://www.kuwo.cn/url?format=mp3&rid="+rid+"&response=url&type=convert_url3&br=128kmp3&from=web&t=1600917212287&httpsStatus=1&reqId=ed098801-fe13-11ea-9756-b13061b167b3";
		Document doc = Jsoup.connect(mp3URL).get();
		//通过Jsoup解析器，获取url地址
		return JSONObject.parseObject(doc.text()).getString("url");
	}

	//根据reid来下载歌曲
	public static void mp3Down(String rid) throws Exception {
		String mp3URL = mp3URL(rid);//获取真实MP3路径
		//完成下载功能，边读边写
		URL url = new URL(mp3URL);
		// 获取输入流
		InputStream is = url.openStream();
		// 创建输出流（指定保存至哪个文件内）
		OutputStream os = new FileOutputStream(new File("d:\\Novel\\" + rid + ".mp3"));
		// 创建字节数组用来读取数据（1024个字节）
		byte[] b = new byte[1024];
		// 循环的边读边写（一次读1kb，就等于一次写入1kb）
		while (true) {
			int n = is.read(b); // 将读的数据保存至b数组内
			if (n == -1)
				break; // 说明已读完毕，退出死循环
			// 将读入的字节写入到电脑内
			os.write(b, 0, n);
		}
		is.close(); // 关输入流
		os.close(); // 关输出流

	}
}
