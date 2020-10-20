package com.alin.music;
//音乐实体类
public class Music {
	String name;//歌曲名
	String pic;//大图片
	String pic120;
	int rid;//音乐编号
	String songTimeMinutes;//歌曲时长
	String artist;//歌手名
	String url;//歌曲路径
	String gc;//歌词
	
	public String getGc() {
		return gc;
	}
	public void setGc(String gc) {
		this.gc = gc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getPic120() {
		return pic120;
	}
	public void setPic120(String pic120) {
		this.pic120 = pic120;
	}
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public String getSongTimeMinutes() {
		return songTimeMinutes;
	}
	public void setSongTimeMinutes(String songTimeMinutes) {
		this.songTimeMinutes = songTimeMinutes;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
