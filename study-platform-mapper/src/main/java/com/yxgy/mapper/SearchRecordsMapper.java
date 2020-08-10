package com.yxgy.mapper;

import java.util.List;

import com.yxgy.pojo.SearchRecords;
import com.yxgy.utils.MyMapper;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
	
	public List<String> getHotwords();
}