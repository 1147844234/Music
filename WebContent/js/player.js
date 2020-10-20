$(function(){
	// 初始加载数据  默认加载
	$.post('http://localhost:8080/search',{search:'流行歌曲'},function(data){
		var str = $('#music-list').render(data);
		$('#main-list').append(str)
	})
	// 弹框搜索
	$('#btn-area span:last').click(function(){
		layer.open({
		  type: 1,
		  shade: false, // 取消模态框 搜索时可以播放歌曲
		  title: false, //不显示标题
		  area: ['300px', '80px'], //宽高
		  content: $('#sousuo'), //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
		  cancel: function(){
		   	$('#search-wd').val(''); // 关闭时情况搜索框内容
		  }
		});
		$('#search-wd').select();
	})
	// 搜索
	$('#search').submit(function () {
		var search = $('#search-wd').val();
		if(search==''){
			layer.msg('请输入搜索内容');
			return;
		}
		// 根据搜索内容重新加载数据
		$.post('http://localhost:8080/search',{search:search},function(data){
			$('#main-list div:first').nextAll().remove()
			var str = $('#music-list').render(data);
			$('#main-list').append(str)
			// 搜索后内容重新置顶
			$('#main-list')[0].scrollTop=0;
		})
		
		return false;
	})
	
	// 播放暂停
	$('.btn-play').click(function () {
		var bfq = document.getElementById("bfq");
		if(bfq.src == ''){
			layer.msg('请先选择歌曲');
			return;
		}
	    if (bfq.paused){ /*如果已经暂停*/
	    	$('.btn-play').addClass('btn-state-paused'); // 播放按钮样式切换	
	        bfq.play();   //播放
	    }else {
	    	$('.btn-play').removeClass('btn-state-paused');
	        bfq.pause();  //暂停
	    }
	})
	
	// 静音
	$('.btn-quiet').click(function () {
		if(bfq.muted){
			bfq.muted = false; // 取消静音
			$(this).removeClass('btn-state-quiet')
		}else{
			bfq.muted = true;  // 静音
			$(this).addClass('btn-state-quiet')
		}
	})
	// 音量设置  初始70%
	bfq.volume = 0.7;
	$('#volumn').on('input',function(){
		bfq.volume = $(this).val()/100;
	})
	// 播放时进度条改变
	var timer = setInterval(kzPlayer,500);
	
	// 进度条控制播放器
	$('#state').on('input',function(){
		bfq.currentTime = $(this).val();
	})
	
	// 播放列表
	$('#btn-area .btn:eq(2)').click(function(){
		layer.open({
		  type: 1,
		  title: false,
		  closeBtn: 0,
		  area: ['600px', '400px'],
		  shadeClose: true,
		  content: $('#playlist')
		});
	})
	
	// 下载歌曲
	$('body').on('click','.download',function () {
		var musicName = $(this).parent().next().text();
		var rid = $(this).parent().parent().find('input:eq(0)').val();
		location = 'http://localhost:8080/download?musicName='+musicName+'&musicRid='+rid;
	})
	// 播放器歌词移动 当播放位置改变时（比如当用户快进到媒介中一个不同的位置时）运行的脚本。
	bfq.ontimeupdate = function(){
		var currentTime = bfq.currentTime.toFixed(0); //当前播放时间 保留2位小数 对比歌词时间
		
		var b = true,index,dom;
		$('#lyric li[accesskey^=\"'+parseInt(currentTime)+'.\"]').each(function(i,e){
			if($(this).attr('accesskey')>=currentTime){
				$(this).addClass('lrc').siblings().removeClass('lrc');
				b = false;
			}
			dom = e;
			index = i;
		})
		if(b) $(dom).eq(index).addClass('lrc').siblings().removeClass('lrc');
		// 歌词居中
		var lrcTop = $('#lyric li.lrc').position().top;  // 当前播放歌词在父元素的高度
		if(lrcTop > $('#lyric').height()/2){
			$('#lyric li').animate({top:'-=28px'},10)
		}else if(lrcTop+28*3 < $('#lyric').height()/2){
			$('#lyric li').animate({top:'+=28px'},10)
		}
	}
	
})
// 播放列表歌曲时 正在播放列表对应歌曲高亮显示
function showname (li) {
	var rid = $(li).find(':hidden:eq(0)').val();
	$('[name=rid]').each(function(i,e){
		if(e.value == rid){
			$(e).parent().attr('id','color').siblings().removeAttr('id')
			return false;
		}
	})
}

// 滑块控制播放器
function kzPlayer(){
	var mlength = bfq.duration;// 音频长度
	if(isNaN(mlength)) return;
	$('#state').prop('max',mlength)
	$('#state').val(bfq.currentTime.toFixed(1))
	// 
}
// 显示歌词 双击播放新歌曲时调用
function lrc(rid){
	$.post('http://localhost:8080/getlrc',{rid:rid},function(data){
		$('#lyric').empty(); // 清空上一首歌歌词
		for(var i=0;i<data.length;i++){
			$('#lyric').append('<li accesskey="'+data[i].time+'">'+data[i].lineLyric+'</li>')
		}
	})
}

function addList(t){
	// 添加至播放列表
	var music = $(t).prop("outerHTML");
	if($('#playlist ul').find(':hidden[value='+$(t).find(':hidden:eq(0)').val()+']').length==0){
		$('#playlist ul').append('<li ondblclick="showname(this)">'+music+'</li>')
	}
}

// 双击播放
function dbplay(t){
	// 拿到被隐藏的歌曲rid
	var rid = $(t).find(':hidden:eq(0)').val();
	// 请求后端拿到播放地址
	$.post('http://localhost:8080/getmusicurl',{rid:rid},function(url){
		currentMusic = bfq.src.substr(bfq.src.lastIndexOf('/')+1);
		nextMusic = url.substr(url.lastIndexOf('/')+1);
		
	   	// 如果双击时是同一首歌曲
	    if(currentMusic==nextMusic){
	    	if (bfq.paused){ /*如果已经暂停*/
	    		$('.btn-play').addClass('btn-state-paused'); // 播放按钮样式切换	
			    bfq.play();   //播放
			}else {
			    $('.btn-play').removeClass('btn-state-paused');
			    bfq.pause();  //暂停
			}
	    }else{
	    	bfq.src=url;
	    	$('.btn-play').addClass('btn-state-paused'); // 播放按钮样式切换	
			bfq.play();   //播放
			lrc(rid);
	    }
	    	
	})
	// 重置播放器控制滑块回到最左边的点 1
	$('#state').val(1);
	// 切换播放歌曲名
	$('marquee').text($(t).find('span:last').text())
	// 显示专辑图片
	$('#music-cover').attr('src',$(t).find(':hidden:eq(1)').val())
	// 颜色突出显示
	$(t).attr('id','color').siblings().removeAttr('id')
	
	kzPlayer();
}
