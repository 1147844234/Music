package com.alin.servlet;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Servlet implementation class URLServlet
 * 根据rid获取歌词信息
 *
 */
@WebServlet("/getLrc")
public class getLrcServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public getLrcServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");	 // 请求编码
		response.setContentType("text/html;charset=utf-8"); // 响应编码
		// 获取rid
		String rid = request.getParameter("rid");
		// 获取歌词url
		String lrcURL = "http://106.52.140.38:8081/lrc";
		String data="musicid="+rid;
		String result="";
		try {
			result= getLrcServlet.SendHttpPOST(lrcURL,data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		PrintWriter out =null ;
		out =response.getWriter() ;
		out.write(result);
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	//http封装
	public static String SendHttpPOST(String url, String data) throws Exception {
		String result = null;
		//打开连接
		//要发送的POST请求url?Key=Value&amp;Key2=Value2&amp;Key3=Value3的形式
		//URL requestUrl = new URL(url + "?" + requestParam);
		URL requestUrl = new URL(url);
		HttpURLConnection httpConn = (HttpURLConnection)requestUrl.openConnection();

		//加入数据
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream(),"UTF-8");

		//body参数在这里put到JSONObject中

		writer.write(data);
		writer.flush();
		//获取输入流
		BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"utf-8"));
		int code = httpConn.getResponseCode();
		if (HttpURLConnection.HTTP_OK == code || HttpURLConnection.HTTP_CREATED == code){
			String temp = in.readLine();
			/*连接成一个字符串*/
			while (temp != null) {
				if (result != null) {
					result += temp;
				}else {
					result = temp;
				}
				temp = in.readLine();
			}
		}
		return result;
	}

}
