package com.yxgy.service;

import java.util.Date;
import java.util.List;

import com.yxgy.mapper.VideosMapper;
import com.yxgy.pojo.Videos;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class VideoService {

	@Autowired
	private VideosMapper videosMapper;

	
	@Autowired
	private Sid sid;

	//保存视频
	@Transactional(propagation = Propagation.REQUIRED)
	public String saveVideo(Videos video) {
		
		String id = sid.nextShort();
		video.setId(id);
		videosMapper.insertSelective(video);
		
		return id;
	}

	//修改视频封面相对路径
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateVideo(String videoId, String coverPath) {

		Videos video = new Videos();
		video.setId(videoId);
		video.setCoverPath(coverPath);
		videosMapper.updateByPrimaryKeySelective(video);
	}

}
