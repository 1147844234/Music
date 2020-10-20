<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script src="jquery.min.js"></script>
<script type="text/javascript">
	//播放歌曲
	function play(rid){//rid表示播放歌曲的唯一标识符
		//异步ajax请求服务器，通过rid获取MP3URL
		alert(rid);
		$.get("URLServlet",{rid:rid},function(d){
			//获取播放器
			var v = document.getElementById("vdo");
			//设置播放路径
			v.src=d;
			//播放
			v.play();
		});
	}
</script>
</head>
<body>
	
	<table border="1">
		<tr>
			<td>歌曲</td>
			<td>歌手</td>
			<td>时长</td>
			<td>操作</td>
		</tr>
		<c:forEach items="${musics }" var="v">
			<tr>
				<td>${v.name }</td>
				<td>${v.artist }</td>
				<td>${v.songTimeMinutes }</td>
				<td>
					<input type="button" value="播放" onclick="play('${v.rid }')">
				</td>
			</tr>
		</c:forEach>
		
	</table>
	<video id="vdo" controls="controls" ></video>
</body>
</html>
