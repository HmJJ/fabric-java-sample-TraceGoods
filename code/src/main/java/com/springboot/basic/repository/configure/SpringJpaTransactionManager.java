package com.springboot.basic.repository.configure;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import com.springboot.basic.repository.datasource.DynamicDataSourceSwitcher;


/**
 * Spring Jpa TransactionManager
 * 
 * @since repository 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
public class SpringJpaTransactionManager extends JpaTransactionManager {

	private static final long serialVersionUID = 3347643234644930323L;

	/* (non-Javadoc)
	 * @see org.springframework.orm.jpa.JpaTransactionManager#doBegin(java.lang.Object, org.springframework.transaction.TransactionDefinition)
	 */
	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) {
		if (!definition.isReadOnly()) {
			logger.info("write, transaction;");
			DynamicDataSourceSwitcher.setMasterDataSource();
		} else {
			logger.info("read");
			DynamicDataSourceSwitcher.setSlaveDataSource();
		}
		super.doBegin(transaction, definition);
	}

	/* (non-Javadoc)
	 * @see org.springframework.orm.jpa.JpaTransactionManager#doCommit(org.springframework.transaction.support.DefaultTransactionStatus)
	 */
	@Override
	protected void doCommit(DefaultTransactionStatus status) {
		super.doCommit(status);
		logger.info("transaction: commit");
	}

}
