package com.springboot.basic.repository.datasource;

import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSourceConfigure extends AbstractRoutingDataSource {

	private Object masterDataSource;
	private Map<Object, Object> slaveDataSources;

	public DynamicDataSourceConfigure() {
		// 
	}

	/**
	 * @param masterDataSource
	 * @param slaveDataSources
	 */
	public DynamicDataSourceConfigure(Object masterDataSource, Map<Object, Object> slaveDataSources) {
		setMasterDataSource(masterDataSource);
		setSlaveDataSources(slaveDataSources);
	}

	/**
	 * @return the masterDataSource
	 */
	public Object getMasterDataSource() {
		return masterDataSource;
	}

	/**
	 * @param masterDataSource the masterDataSource to set
	 */
	public void setMasterDataSource(Object masterDataSource) {
		this.masterDataSource = masterDataSource;
		super.setDefaultTargetDataSource(masterDataSource);
	}

	/**
	 * @return the slaveDataSources
	 */
	public Map<Object, Object> getSlaveDataSources() {
		if (this.slaveDataSources == null) {
			throw new IllegalArgumentException("Property 'slaveDataSources' is required");
		}
		return slaveDataSources;
	}

	/**
	 * @param slaveDataSources the slaveDataSources to set
	 */
	public void setSlaveDataSources(Map<Object, Object> slaveDataSources) {
		this.slaveDataSources = slaveDataSources;
		super.setTargetDataSources(slaveDataSources);
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return DynamicDataSourceSwitcher.get(getSlaveDataSources().keySet());
	}

}
