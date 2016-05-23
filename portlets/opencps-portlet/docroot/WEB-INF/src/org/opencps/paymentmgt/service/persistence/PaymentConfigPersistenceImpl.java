/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package org.opencps.paymentmgt.service.persistence;

import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnmodifiableList;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import org.opencps.paymentmgt.NoSuchPaymentConfigException;
import org.opencps.paymentmgt.model.PaymentConfig;
import org.opencps.paymentmgt.model.impl.PaymentConfigImpl;
import org.opencps.paymentmgt.model.impl.PaymentConfigModelImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The persistence implementation for the Payment configuration service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author trungdk
 * @see PaymentConfigPersistence
 * @see PaymentConfigUtil
 * @generated
 */
public class PaymentConfigPersistenceImpl extends BasePersistenceImpl<PaymentConfig>
	implements PaymentConfigPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link PaymentConfigUtil} to access the Payment configuration persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = PaymentConfigImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(PaymentConfigModelImpl.ENTITY_CACHE_ENABLED,
			PaymentConfigModelImpl.FINDER_CACHE_ENABLED,
			PaymentConfigImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(PaymentConfigModelImpl.ENTITY_CACHE_ENABLED,
			PaymentConfigModelImpl.FINDER_CACHE_ENABLED,
			PaymentConfigImpl.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(PaymentConfigModelImpl.ENTITY_CACHE_ENABLED,
			PaymentConfigModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	public PaymentConfigPersistenceImpl() {
		setModelClass(PaymentConfig.class);
	}

	/**
	 * Caches the Payment configuration in the entity cache if it is enabled.
	 *
	 * @param paymentConfig the Payment configuration
	 */
	@Override
	public void cacheResult(PaymentConfig paymentConfig) {
		EntityCacheUtil.putResult(PaymentConfigModelImpl.ENTITY_CACHE_ENABLED,
			PaymentConfigImpl.class, paymentConfig.getPrimaryKey(),
			paymentConfig);

		paymentConfig.resetOriginalValues();
	}

	/**
	 * Caches the Payment configurations in the entity cache if it is enabled.
	 *
	 * @param paymentConfigs the Payment configurations
	 */
	@Override
	public void cacheResult(List<PaymentConfig> paymentConfigs) {
		for (PaymentConfig paymentConfig : paymentConfigs) {
			if (EntityCacheUtil.getResult(
						PaymentConfigModelImpl.ENTITY_CACHE_ENABLED,
						PaymentConfigImpl.class, paymentConfig.getPrimaryKey()) == null) {
				cacheResult(paymentConfig);
			}
			else {
				paymentConfig.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all Payment configurations.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(PaymentConfigImpl.class.getName());
		}

		EntityCacheUtil.clearCache(PaymentConfigImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the Payment configuration.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(PaymentConfig paymentConfig) {
		EntityCacheUtil.removeResult(PaymentConfigModelImpl.ENTITY_CACHE_ENABLED,
			PaymentConfigImpl.class, paymentConfig.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<PaymentConfig> paymentConfigs) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (PaymentConfig paymentConfig : paymentConfigs) {
			EntityCacheUtil.removeResult(PaymentConfigModelImpl.ENTITY_CACHE_ENABLED,
				PaymentConfigImpl.class, paymentConfig.getPrimaryKey());
		}
	}

	/**
	 * Creates a new Payment configuration with the primary key. Does not add the Payment configuration to the database.
	 *
	 * @param paymentConfigId the primary key for the new Payment configuration
	 * @return the new Payment configuration
	 */
	@Override
	public PaymentConfig create(long paymentConfigId) {
		PaymentConfig paymentConfig = new PaymentConfigImpl();

		paymentConfig.setNew(true);
		paymentConfig.setPrimaryKey(paymentConfigId);

		return paymentConfig;
	}

	/**
	 * Removes the Payment configuration with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param paymentConfigId the primary key of the Payment configuration
	 * @return the Payment configuration that was removed
	 * @throws org.opencps.paymentmgt.NoSuchPaymentConfigException if a Payment configuration with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentConfig remove(long paymentConfigId)
		throws NoSuchPaymentConfigException, SystemException {
		return remove((Serializable)paymentConfigId);
	}

	/**
	 * Removes the Payment configuration with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the Payment configuration
	 * @return the Payment configuration that was removed
	 * @throws org.opencps.paymentmgt.NoSuchPaymentConfigException if a Payment configuration with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentConfig remove(Serializable primaryKey)
		throws NoSuchPaymentConfigException, SystemException {
		Session session = null;

		try {
			session = openSession();

			PaymentConfig paymentConfig = (PaymentConfig)session.get(PaymentConfigImpl.class,
					primaryKey);

			if (paymentConfig == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchPaymentConfigException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(paymentConfig);
		}
		catch (NoSuchPaymentConfigException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected PaymentConfig removeImpl(PaymentConfig paymentConfig)
		throws SystemException {
		paymentConfig = toUnwrappedModel(paymentConfig);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(paymentConfig)) {
				paymentConfig = (PaymentConfig)session.get(PaymentConfigImpl.class,
						paymentConfig.getPrimaryKeyObj());
			}

			if (paymentConfig != null) {
				session.delete(paymentConfig);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (paymentConfig != null) {
			clearCache(paymentConfig);
		}

		return paymentConfig;
	}

	@Override
	public PaymentConfig updateImpl(
		org.opencps.paymentmgt.model.PaymentConfig paymentConfig)
		throws SystemException {
		paymentConfig = toUnwrappedModel(paymentConfig);

		boolean isNew = paymentConfig.isNew();

		Session session = null;

		try {
			session = openSession();

			if (paymentConfig.isNew()) {
				session.save(paymentConfig);

				paymentConfig.setNew(false);
			}
			else {
				session.merge(paymentConfig);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		EntityCacheUtil.putResult(PaymentConfigModelImpl.ENTITY_CACHE_ENABLED,
			PaymentConfigImpl.class, paymentConfig.getPrimaryKey(),
			paymentConfig);

		return paymentConfig;
	}

	protected PaymentConfig toUnwrappedModel(PaymentConfig paymentConfig) {
		if (paymentConfig instanceof PaymentConfigImpl) {
			return paymentConfig;
		}

		PaymentConfigImpl paymentConfigImpl = new PaymentConfigImpl();

		paymentConfigImpl.setNew(paymentConfig.isNew());
		paymentConfigImpl.setPrimaryKey(paymentConfig.getPrimaryKey());

		paymentConfigImpl.setPaymentConfigId(paymentConfig.getPaymentConfigId());
		paymentConfigImpl.setCompanyId(paymentConfig.getCompanyId());
		paymentConfigImpl.setGroupId(paymentConfig.getGroupId());
		paymentConfigImpl.setUserId(paymentConfig.getUserId());
		paymentConfigImpl.setCreateDate(paymentConfig.getCreateDate());
		paymentConfigImpl.setModifiedDate(paymentConfig.getModifiedDate());
		paymentConfigImpl.setGovAgencyOrganizationId(paymentConfig.getGovAgencyOrganizationId());
		paymentConfigImpl.setGovAgencyName(paymentConfig.getGovAgencyName());
		paymentConfigImpl.setGovAgencyTaxNo(paymentConfig.getGovAgencyTaxNo());
		paymentConfigImpl.setInvoiceTemplateNo(paymentConfig.getInvoiceTemplateNo());
		paymentConfigImpl.setInvoiceIssueNo(paymentConfig.getInvoiceIssueNo());
		paymentConfigImpl.setInvoiceLastNo(paymentConfig.getInvoiceLastNo());
		paymentConfigImpl.setBankInfo(paymentConfig.getBankInfo());
		paymentConfigImpl.setPlaceInfo(paymentConfig.getPlaceInfo());
		paymentConfigImpl.setKeypayDomain(paymentConfig.getKeypayDomain());
		paymentConfigImpl.setKeypayVersion(paymentConfig.getKeypayVersion());
		paymentConfigImpl.setKeypayMerchantCode(paymentConfig.getKeypayMerchantCode());
		paymentConfigImpl.setKeypaySecureKey(paymentConfig.getKeypaySecureKey());

		return paymentConfigImpl;
	}

	/**
	 * Returns the Payment configuration with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the Payment configuration
	 * @return the Payment configuration
	 * @throws org.opencps.paymentmgt.NoSuchPaymentConfigException if a Payment configuration with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentConfig findByPrimaryKey(Serializable primaryKey)
		throws NoSuchPaymentConfigException, SystemException {
		PaymentConfig paymentConfig = fetchByPrimaryKey(primaryKey);

		if (paymentConfig == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchPaymentConfigException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return paymentConfig;
	}

	/**
	 * Returns the Payment configuration with the primary key or throws a {@link org.opencps.paymentmgt.NoSuchPaymentConfigException} if it could not be found.
	 *
	 * @param paymentConfigId the primary key of the Payment configuration
	 * @return the Payment configuration
	 * @throws org.opencps.paymentmgt.NoSuchPaymentConfigException if a Payment configuration with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentConfig findByPrimaryKey(long paymentConfigId)
		throws NoSuchPaymentConfigException, SystemException {
		return findByPrimaryKey((Serializable)paymentConfigId);
	}

	/**
	 * Returns the Payment configuration with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the Payment configuration
	 * @return the Payment configuration, or <code>null</code> if a Payment configuration with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentConfig fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		PaymentConfig paymentConfig = (PaymentConfig)EntityCacheUtil.getResult(PaymentConfigModelImpl.ENTITY_CACHE_ENABLED,
				PaymentConfigImpl.class, primaryKey);

		if (paymentConfig == _nullPaymentConfig) {
			return null;
		}

		if (paymentConfig == null) {
			Session session = null;

			try {
				session = openSession();

				paymentConfig = (PaymentConfig)session.get(PaymentConfigImpl.class,
						primaryKey);

				if (paymentConfig != null) {
					cacheResult(paymentConfig);
				}
				else {
					EntityCacheUtil.putResult(PaymentConfigModelImpl.ENTITY_CACHE_ENABLED,
						PaymentConfigImpl.class, primaryKey, _nullPaymentConfig);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(PaymentConfigModelImpl.ENTITY_CACHE_ENABLED,
					PaymentConfigImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return paymentConfig;
	}

	/**
	 * Returns the Payment configuration with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param paymentConfigId the primary key of the Payment configuration
	 * @return the Payment configuration, or <code>null</code> if a Payment configuration with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public PaymentConfig fetchByPrimaryKey(long paymentConfigId)
		throws SystemException {
		return fetchByPrimaryKey((Serializable)paymentConfigId);
	}

	/**
	 * Returns all the Payment configurations.
	 *
	 * @return the Payment configurations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<PaymentConfig> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the Payment configurations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.paymentmgt.model.impl.PaymentConfigModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of Payment configurations
	 * @param end the upper bound of the range of Payment configurations (not inclusive)
	 * @return the range of Payment configurations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<PaymentConfig> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the Payment configurations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.paymentmgt.model.impl.PaymentConfigModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of Payment configurations
	 * @param end the upper bound of the range of Payment configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of Payment configurations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<PaymentConfig> findAll(int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<PaymentConfig> list = (List<PaymentConfig>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_PAYMENTCONFIG);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_PAYMENTCONFIG;

				if (pagination) {
					sql = sql.concat(PaymentConfigModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<PaymentConfig>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<PaymentConfig>(list);
				}
				else {
					list = (List<PaymentConfig>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the Payment configurations from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (PaymentConfig paymentConfig : findAll()) {
			remove(paymentConfig);
		}
	}

	/**
	 * Returns the number of Payment configurations.
	 *
	 * @return the number of Payment configurations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countAll() throws SystemException {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_PAYMENTCONFIG);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Initializes the Payment configuration persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.org.opencps.paymentmgt.model.PaymentConfig")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<PaymentConfig>> listenersList = new ArrayList<ModelListener<PaymentConfig>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<PaymentConfig>)InstanceFactory.newInstance(
							getClassLoader(), listenerClassName));
				}

				listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
			}
			catch (Exception e) {
				_log.error(e);
			}
		}
	}

	public void destroy() {
		EntityCacheUtil.removeCache(PaymentConfigImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_PAYMENTCONFIG = "SELECT paymentConfig FROM PaymentConfig paymentConfig";
	private static final String _SQL_COUNT_PAYMENTCONFIG = "SELECT COUNT(paymentConfig) FROM PaymentConfig paymentConfig";
	private static final String _ORDER_BY_ENTITY_ALIAS = "paymentConfig.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No PaymentConfig exists with the primary key ";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(PaymentConfigPersistenceImpl.class);
	private static PaymentConfig _nullPaymentConfig = new PaymentConfigImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<PaymentConfig> toCacheModel() {
				return _nullPaymentConfigCacheModel;
			}
		};

	private static CacheModel<PaymentConfig> _nullPaymentConfigCacheModel = new CacheModel<PaymentConfig>() {
			@Override
			public PaymentConfig toEntityModel() {
				return _nullPaymentConfig;
			}
		};
}