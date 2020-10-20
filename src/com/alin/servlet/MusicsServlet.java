package com.alin.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alin.music.Music;
import com.alin.music.MusicServer;

/**
 * Servlet implementation class MusicsServlet
 */
@WebServlet("/MusicsServlet")
public class MusicsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MusicsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//设置编码格式
		request.setCharacterEncoding("utf-8");//请求编码
		response.setContentType("text/html;charset=utf-8");//响应编码
		
	
		try {
			//获取关键字
			String key = request.getParameter("key");
			if(key==null)//没有搜索关键字
				key="李荣浩";//默认歌曲
			//获取音乐数据
			List<Music> musics = MusicServer.searchMusic(key);
			//封装数据
			request.setAttribute("musics", musics);
			//跳转至jsp页面
			request.getRequestDispatcher("lg.jsp").forward(request, response);;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
