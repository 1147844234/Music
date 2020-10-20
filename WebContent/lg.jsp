<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>网抑云音乐：病友的力量</title>
<!-- favicon图标 -->
<link rel="icon" href="images/favicon.ico">

<!-- jQuery文件 -->
<script src="js/jquery.min.js"></script>

<!-- 播放器样式表文件 -->
<link rel="stylesheet" type="text/css" href="css/player.css">

<!-- 滚动条美化样式文件 -->
<link rel="stylesheet" type="text/css"
	href="css/jquery.mCustomScrollbar.min.css">

<!-- layer弹窗插件样式文件 -->
<link rel="stylesheet"
	href="plugns/layer/skin/default/layer.css?v=3.0.2302"
	id="layuicss-skinlayercss">

<style type="text/css">
.data-area {
	overflow: scroll;
}

#search-wd {
	width: 996px;
}

.pl {
	border: 1px solid red;
}

video {
	width: 1125px;
	height: 80px;
	margin-left: 10px;
}

.plaing {
	color: white;
	width: 820px;
	margin-left: 355px;
	font-size: 22px;
}
input{
      background-color:transparent; 
      border-radius :3px;
      height:30px;
      width:24.5%;
      color:#bdbdbe;
     }

/* 自定义旋转动画 */
@
keyframes name {
	from {transform: rotate(0deg);
}

to {
	transform: rotate(360deg);
}

}
.cover { /*歌曲的图片封面*/
	border-radius: 50%; /*圆角边框*/
	/* 应用动画 infinite循环 linear匀速 */
	animation: name 5s infinite linear;
}
</style>

<script type="text/javascript">
	// 播放歌曲
	function play(url,url,rid){
  	document.getElementById("bl").src = url;
  
   	$("#music-cover").attr("src",url);
  //异步ajax请求服务器，通过rid获取MP3URL
  	$.get("URLServlet",{rid:rid},function(d){
   //获取播放器
   var v = document.getElementById("bfq");
   //设置播放路径
   v.src=d;
   //播放
   v.play();
  }); 

 }
</script>



<script type="text/javascript">
	//歌曲下载
	function down(rid) {
		// 设置文本“正在下载中”和颜色为“黄色”
		$("#span" + rid).text("下载中...");
		$("#span" + rid).css("color", "yellow");
		$.get("DownServlet", {
			rid : rid
		}, function(d) {
			$("#span" + rid).css("color", "red");
			if (d == "1") {
				$("#span" + rid).text("下载成功");
			} else {
				$("#span" + rid).text("下载失败");
			}
		});
	}
</script>
  
  <script type="text/javascript">
//赞助站长
  function zz(){
	  window.open("zzzz.jsp")
	 }
	function fx(rid){
	  	$.get("URLServlet",{rid:rid},function(d){
	   		alert(d);
	  }); 

	 }
//全选
	function allselect() {
		//  alert("---");
		var cbs = $("[name='cb']");
		cbs.each(function(i,item) {
			cbs[i].checked=true;
		});
	}
	//反选
	function deselect() {
		//获取被选中的checkbox
		var cbs=$("[name='cb']");
		//循环每个checkbox
		cbs.each(function(i,item) {
			cbs[i].checked = !cbs[i].checked;
		});
	}
	
	//一键下载
	function alldown() {
		var cbs = $("[name='cb']:checked");
		var rids = "";
		cbs.each(function(i,item){
			var rid = $(item).attr("key");
			rids += rid + "-";
		});
		if(rids!="") rids = rids.substring(0,rids.lengh-1);
		$("#dowing").css("color","yellow");
		$("#dowing").text("下载中...");
		$.get("ADServlet",{rids:rids},function(d){
			$("#dowing").css("color","red");
			if(d=="1") {
				$("#dowing").text("下载成功");
			}else{
				$("#dowing").text("下载失败");
			}
		});
	}

  </script>
<!-- 头部logo -->
	<div class="header">
		<div class="logo" title="Version 2.32; Based on Meting; Powered by Mengkun">
			♫生而为人，爽的雅痞————大宰总
		</div>
	</div>

</head>
<body>
<div>
	 <img id="bl" style="filter: blur(90px);width:100%;height:100%" alt="" src="images/hs.jpg">
</div>
	<div id="blur-img"></div>

	

	<!-- 中间主体区域 -->
	<div class="center">
	
	<!--class="header"-->
	<div class="data-area">
		<div class="container">
 	<input type="button" value="一键下载" onclick="alldown()"/>
    <input type="button" value="全选" onclick="allselect()"/>
    <input type="button" value="反选" onclick="deselect()"/>
	<input type="button" value="赞助站长" onclick="zz()"/>    

			<!--搜索-->
			<div id="sousuo">
				<form id="search" action="MusicsServlet" method="post">
					<div class="search-group" style="padding: 20px 10px;">
						<input type="text" name="key" id="search-wd" placeholder="搜索歌手、歌名">
						<button type="submit" style=" color:#bdbdbe;background-color:transparent;border-radius :3px;width:100px;margin-left:10px;"
							class="search-submit">搜 索</button>
					</div>
				</form>
			</div>

			
				<!--音乐播放列表-->
				<div class="list-item moban">
					<span class="list-mobile-menu"></span> <span class="music-album">时长</span>
					<span class="auth-name">歌手</span> <span class="music-cz">操作</span>
					<span class="music-name">歌曲</span>
				</div>
				<c:forEach items="${musics }" var="v">

					<div class="list-item moban"
						ondblclick="play('${v.pic }','${v.pic120 }','${v.rid }')">
						<h1 onmouseover="style.color='#FFFF00'"
							onmouseout="style.color='white'">
							<span class="list-mobile-menu"></span> <span class="music-album">
								${v.songTimeMinutes } <span id="span${v.rid }"></span>

							</span> 
							<span class="auth-name">${v.artist }</span> 
							<span
								class="music-cz"> <img src="images/share.png"
								onclick="fx('${v.rid }')" title="点击分享" class="addmusic" />
								&nbsp;&nbsp; <img src="images/download.png"
								onclick="down('${v.rid}')" title="点击下载" class="download" />								
							</span>
													
							 <span class="music-name"><input type="checkbox" name="cb" key="${v.rid }" />
							 ${v.name }</span>
						</h1>
					</div>

				</c:forEach>


			</div>
			</div>

			<!-- 右侧封面及歌词展示 -->
			<div class="player" id="player">
				<!--歌曲封面-->
				<div class="cover">
					<img src="images/player_cover.png" class="music-cover"
						id="music-cover">
				</div>
				<!--歌词-->
				<div class="lyric">
					<ul>
						<li id="lyric"></li>
					</ul>
				</div>
			</div>
		</div>
		<!--class="container"-->
	</div>
	<!--class="center"-->

	<!-- 播放器底部区域 -->
	<div style="clear: both;"></div>
	<div class="footer">
		<video id="bfq" controls="controls"></video>
		<div class="plaing">
			<marquee scrollamount="5" id="mqe" behavior="alternate"></marquee>
		</div>
	</div>

	<!-- layer弹窗插件 -->
	<script src="plugns/layer/layer.js"></script>
	<!-- 背景模糊化插件 -->
	<script src="js/background-blur.min.js"></script>

</body>
</html>