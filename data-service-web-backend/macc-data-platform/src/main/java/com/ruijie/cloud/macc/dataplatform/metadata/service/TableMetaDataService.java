package com.ruijie.cloud.macc.dataplatform.metadata.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.TableMetadataFetcher;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.TableInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TableMetaDataService {
	private static final int CACHE_TIME_SECONDS = 30 * 60; // 30分钟
	private static final String TABLE_NAME_CACHE_KEY = "mdp-tn:";
	private static final String TABLE_DETAIL_CACHE_KEY = "mdp-td:";
	@Autowired
	private List<TableMetadataFetcher> fetchers;
	@Autowired
	private DataSourceService dataSourceService;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	public List<String> tableNameList(String dataSourceKey, boolean disableCache) {
		String redisKey = TABLE_NAME_CACHE_KEY + dataSourceKey;
		if (!disableCache) {
			// 优先查询缓存
			String cacheValue = stringRedisTemplate.opsForValue().get(redisKey);
			if (StringUtils.isNotBlank(cacheValue)) {
				List<String> tableNameList = JSON.parseArray(cacheValue, String.class);
				return tableNameList;
			}
		}

		DataSourceModel dataSource = dataSourceService.selectByDataSourceKey(dataSourceKey);
		try {
			TableMetadataFetcher tableMetadataFetcher = getFetcher(dataSource.getDbType());

			List<String> tableList = tableMetadataFetcher.getTableNameList(dataSource);
			if (tableList != null) {
				stringRedisTemplate.opsForValue().set(redisKey, JSON.toJSONString(tableList), CACHE_TIME_SECONDS,
						TimeUnit.SECONDS);
			}
			return tableList;
		} catch (Exception e) {
			log.error("datasource : {} list tablename failed", dataSourceKey, e);
			return null;
		}
	}

	public TableInfo tableInfo(String dataSourceKey, String tableName) {
		return tableInfo(dataSourceKey, tableName, false);
	}

	public TableInfo tableInfo(String dataSourceKey, String tableName, boolean disableCache) {
		// 优先查询缓存
		String redisKey = TABLE_DETAIL_CACHE_KEY + dataSourceKey + ":" + tableName;
		if (!disableCache) {
			String cacheValue = stringRedisTemplate.opsForValue().get(redisKey);
			if (StringUtils.isNotBlank(cacheValue)) {
				return JSON.parseObject(cacheValue, TableInfo.class);
			}
		}

		DataSourceModel dataSource = dataSourceService.selectByDataSourceKey(dataSourceKey);
		try {
			TableMetadataFetcher tableMetadataFetcher = getFetcher(dataSource.getDbType());

			TableInfo info = tableMetadataFetcher.getTableDetail(dataSource, tableName);
			info.setDataSourceType(dataSource.getDbType());

			stringRedisTemplate.opsForValue().set(redisKey, JSON.toJSONString(info), CACHE_TIME_SECONDS,
					TimeUnit.SECONDS);
			return info;
		} catch (Exception e) {
			log.error("datasource : {} list tablename failed", dataSourceKey, e);
			return null;
		}
	}

	private TableMetadataFetcher getFetcher(DataSourceType dbType) {
		for (TableMetadataFetcher fetcher : fetchers) {
			if (fetcher.getClass().equals(dbType.getDbMetaFetcher())) {
				return fetcher;
			}
		}

		return null;
	}
}
