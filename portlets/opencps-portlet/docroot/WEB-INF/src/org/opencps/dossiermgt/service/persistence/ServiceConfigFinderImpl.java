/**
* OpenCPS is the open source Core Public Services software
* Copyright (C) 2016-present OpenCPS community

* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero General Public License for more details.
* You should have received a copy of the GNU Affero General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
*/

package org.opencps.dossiermgt.service.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencps.dossiermgt.bean.ServiceBean;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.model.impl.ServiceConfigImpl;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

/**
 * @author dunglt
 * @author trungnt
 */

public class ServiceConfigFinderImpl extends BasePersistenceImpl<ServiceConfig>
	implements ServiceConfigFinder {

	public static final String COUNT_SERVICE_CONFIG_ADVANCE_SQL =
		ServiceConfigFinder.class
			.getName() + ".countServiceConfigAdvance";

	public static final String COUNT_SERVICE_CONFIG_BY_SERVICE_MODE_SQL =
		ServiceConfigFinder.class
			.getName() + ".countServiceConfigByServiceMode";

	public static final String COUNT_SERVICE_CONFIG_SQL =
		ServiceConfigFinder.class
			.getName() + ".countServiceConfig";

	public static final String SEARCH_SERVICE_CONFIG_ADVANCE_SQL =
		ServiceConfigFinder.class
			.getName() + ".searchServiceConfigAdvance";

	public static final String SEARCH_SERVICE_CONFIG_RECENT_SQL =
		ServiceConfigFinder.class
			.getName() + ".searchServiceConfigRecent";

	public static final String SEARCH_SERVICE_CONFIG_BY_SERVICE_MODE_SQL =
		ServiceConfigFinder.class
			.getName() + ".searchServiceConfigByServiceMode";

	public static final String SEARCH_SERVICE_CONFIG_SQL =
		ServiceConfigFinder.class
			.getName() + ".searchServiceConfig";

	private Log _log = LogFactoryUtil
		.getLog(ServiceConfigFinderImpl.class
			.getName());

	/**
	 * @param groupId
	 * @param keywords
	 * @param govAgencyCode
	 * @param domainCode
	 * @param andOperator
	 * @return
	 */
	private int _countServiceConfig(
		long groupId, String[] keywords, String govAgencyCode,
		String domainCode, boolean andOperator) {

		/*keywords = CustomSQLUtil
			.keywords(keywords, false);*/

		Session session = null;

		try {
			session = openSession();
			// get sql command from sql xml
			String sql = CustomSQLUtil
				.get(COUNT_SERVICE_CONFIG_SQL);
			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossiertemplate.templateName)",
							StringPool.LIKE, true, keywords);
				
				sql = CustomSQLUtil
						.replaceKeywords(sql,
							"lower(opencps_serviceinfo.serviceName)",
								StringPool.LIKE, true, keywords);
			} else {
				sql = StringUtil
								.replace(sql,
									"INNER JOIN opencps_serviceinfo ON opencps_service_config.serviceInfoId = opencps_serviceinfo.serviceinfoId",
										StringPool.BLANK);
				sql = StringUtil
								.replace(sql,
									"INNER JOIN opencps_dossiertemplate ON opencps_service_config.dossierTemplateId = opencps_dossiertemplate.dossierTemplateId",
										StringPool.BLANK);
				sql = StringUtil
								.replace(sql,
									"AND ((lower(opencps_dossiertemplate.templateName) LIKE ? [$AND_OR_NULL_CHECK$])",
										StringPool.BLANK);
				sql = StringUtil
								.replace(sql,
									"OR (lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$]))",
										StringPool.BLANK);
			}
			// remove condition query
			if (Validator.isNull(govAgencyCode)) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_service_config.govAgencyCode = ?)",
						StringPool.BLANK);
			}

			if (domainCode
				.equals("0") || domainCode
					.equals(StringPool.BLANK)) {
				sql = StringUtil
					.replace(sql, "AND (opencps_service_config.domainCode = ?)",
						StringPool.BLANK);
			}
			
			sql = CustomSQLUtil
							.replaceAndOperator(sql, andOperator);
			
			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.setCacheable(false);
			q
				.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);
			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}
			if (!govAgencyCode
				.equals(StringPool.BLANK)) {
				qPos
					.add(govAgencyCode);
			}

			if (!domainCode
				.equals(StringPool.BLANK) && !domainCode
					.equals("0")) {
				qPos
					.add(domainCode);
			}

			Iterator<Integer> itr = q
				.iterate();

			if (itr
				.hasNext()) {
				Integer count = itr
					.next();

				if (count != null) {
					return count
						.intValue();
				}
			}

		}
		catch (Exception e) {
			_log
				.error(e);
		}
		finally {
			session
				.close();
		}
		return 0;
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param servicePortal
	 * @param serviceOnegate
	 * @param serviceBackoffice
	 * @param serviceCitizen
	 * @param serviceBusinees
	 * @param serviceDomainIndex
	 * @param govAgencyIndex
	 * @param andOperator
	 * @return
	 */
	private int _countServiceConfigAdvance(
		long groupId, String[] keywords, int servicePortal, int serviceOnegate,
		int serviceBackoffice, int serviceCitizen, int serviceBusinees,
		String serviceDomainIndex, String govAgencyIndex, boolean andOperator) {

		Session session = null;

		try {
			session = openSession();
			// get sql command from sql xml
			String sql = CustomSQLUtil
				.get(COUNT_SERVICE_CONFIG_ADVANCE_SQL);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_serviceinfo.serviceName)",
						StringPool.LIKE, true, keywords);
				
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_serviceinfo.serviceNo)",
						StringPool.LIKE, true, keywords);
			}
			else {
				sql = StringUtil
					.replace(sql,
						"AND (lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
				
				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_serviceinfo.serviceNo) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			if (servicePortal != 1 && servicePortal != 0) {

				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.servicePortal = ?",
						StringPool.BLANK);

			}

			if (serviceOnegate != 1 && serviceOnegate != 0) {

				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceOnegate = ?",
						StringPool.BLANK);

			}

			if (serviceBackoffice != 1 && serviceBackoffice != 0) {

				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceBackoffice = ?",
						StringPool.BLANK);

			}
			if (serviceCitizen != 1 && serviceCitizen != 0) {

				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceCitizen = ?",
						StringPool.BLANK);

			}

			if (serviceBusinees != 1 && serviceBusinees != 0) {

				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceBusinees = ?",
						StringPool.BLANK);

			}

			if (Validator
				.isNull(serviceDomainIndex)) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_service_config.serviceDomainIndex LIKE ? OR opencps_service_config.serviceDomainIndex = ?)",
						StringPool.BLANK);
			}
			else {
				if (serviceDomainIndex.contains(StringPool.PERIOD)) {
					serviceDomainIndex = serviceDomainIndex
						.substring(0, serviceDomainIndex
							.indexOf(StringPool.PERIOD) + 1);
					sql = StringUtil
						.replace(sql,
							"AND (opencps_service_config.serviceDomainIndex LIKE ? OR opencps_service_config.serviceDomainIndex = ?)",
							"AND (opencps_service_config.serviceDomainIndex LIKE ?");

				}

			}

			if (Validator
				.isNull(govAgencyIndex)) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_service_config.govAgencyIndex LIKE ? OR opencps_service_config.govAgencyIndex = ?)",
						StringPool.BLANK);
			}
			else {
				if (govAgencyIndex.contains(StringPool.PERIOD)) {
					govAgencyIndex = govAgencyIndex
						.substring(0, govAgencyIndex
							.indexOf(StringPool.PERIOD) + 1);

					sql = StringUtil
						.replace(sql,
							"AND (opencps_service_config.govAgencyIndex LIKE ? OR opencps_service_config.govAgencyIndex = ?)",
							"AND (opencps_service_config.govAgencyIndex LIKE ?");

				}

			}
			
			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);

			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.setCacheable(false);
			q
				.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			if (keywords != null && keywords.length > 0) {
				qPos.add(keywords, 2);
				qPos.add(keywords, 2);
			}

			if (servicePortal == 1) {
				qPos
					.add(true);
			}
			else if (servicePortal == 0) {
				qPos
					.add(false);
			}

			if (serviceOnegate == 1) {
				qPos
					.add(true);
			}
			else if (serviceOnegate == 0) {
				qPos
					.add(false);
			}

			if (serviceBackoffice == 1) {
				qPos
					.add(true);
			}
			else if (serviceBackoffice == 0) {
				qPos
					.add(false);
			}

			if (serviceCitizen == 1) {
				qPos
					.add(true);
			}
			else if (serviceCitizen == 0) {
				qPos
					.add(false);
			}

			if (serviceBusinees == 1) {
				qPos
					.add(true);
			}
			else if (serviceBusinees == 0) {
				qPos
					.add(false);
			}

			if (Validator
				.isNotNull(serviceDomainIndex) && serviceDomainIndex.contains(StringPool.PERIOD)) {
				qPos
					.add(serviceDomainIndex + StringPool.PERCENT);

			}
			else if (Validator
				.isNotNull(serviceDomainIndex) && !serviceDomainIndex.contains(StringPool.PERIOD)) {
				qPos
					.add(serviceDomainIndex + StringPool.PERIOD +
						StringPool.PERCENT);
				qPos
					.add(serviceDomainIndex);
			}

			if (Validator
				.isNotNull(govAgencyIndex) && govAgencyIndex.contains(StringPool.PERIOD)) {
				qPos
					.add(govAgencyIndex + StringPool.PERCENT);

			}
			else if (Validator
				.isNotNull(govAgencyIndex) && !govAgencyIndex.contains(StringPool.PERIOD)) {
				qPos
					.add(govAgencyIndex + StringPool.PERIOD +
						StringPool.PERCENT);
				qPos
					.add(govAgencyIndex);
			}

			Iterator<Integer> itr = q
				.iterate();

			if (itr
				.hasNext()) {
				Integer count = itr
					.next();

				if (count != null) {
					return count
						.intValue();
				}
			}

		}
		catch (Exception e) {
			_log
				.error(e);
		}
		finally {
			session
				.close();
		}
		return 0;
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param govAgencyCode
	 * @param domainCode
	 * @param andOperator
	 * @param start
	 * @param end
	 * @return
	 */
	private List<ServiceConfig> _searchServiceConfig(
		long groupId, String[] keywords, String govAgencyCode,
		String domainCode, boolean andOperator, int start, int end) {

		/*keywords = CustomSQLUtil
			.keywords(keywords, false);*/

		Session session = null;

		try {
			session = openSession();
			// get sql command from sql xml
			String sql = CustomSQLUtil
				.get(SEARCH_SERVICE_CONFIG_SQL);
			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossiertemplate.templateName)",
							StringPool.LIKE, true, keywords);
				
				sql = CustomSQLUtil
						.replaceKeywords(sql,
							"lower(opencps_serviceinfo.serviceName)",
								StringPool.LIKE, true, keywords);
			} else {
				sql = StringUtil
								.replace(sql,
									"INNER JOIN opencps_serviceinfo ON opencps_service_config.serviceInfoId = opencps_serviceinfo.serviceinfoId",
										StringPool.BLANK);
				sql = StringUtil
								.replace(sql,
									"INNER JOIN opencps_dossiertemplate ON opencps_service_config.dossierTemplateId = opencps_dossiertemplate.dossierTemplateId",
										StringPool.BLANK);
				sql = StringUtil
								.replace(sql,
									"AND ((lower(opencps_dossiertemplate.templateName) LIKE ? [$AND_OR_NULL_CHECK$])",
										StringPool.BLANK);
				sql = StringUtil
								.replace(sql,
									"OR (lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$]))",
										StringPool.BLANK);
			}
			
			// remove condition query
			if (Validator.isNull(govAgencyCode)) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_service_config.govAgencyCode = ?)",
						StringPool.BLANK);
			}

			if (domainCode
				.equals("0") || domainCode
					.equals(StringPool.BLANK)) {
				sql = StringUtil
					.replace(sql, "AND (opencps_service_config.domainCode = ?)",
						StringPool.BLANK);
			}
			
			sql = CustomSQLUtil
					.replaceAndOperator(sql, andOperator);
			SQLQuery q = session
				.createSQLQuery(sql);
			q
				.setCacheable(false);
			q
				.addEntity("ServiceConfig", ServiceConfigImpl.class);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);
			
			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}
			
			if (!govAgencyCode
				.equals(StringPool.BLANK)) {
				qPos
					.add(govAgencyCode);
			}

			if (!domainCode
				.equals(StringPool.BLANK) && !domainCode
					.equals("0")) {
				qPos
					.add(domainCode);
			}

			return (List<ServiceConfig>) QueryUtil
				.list(q, getDialect(), start, end);
		}
		catch (Exception e) {
			_log
				.error(e);
		}
		finally {
			session
				.close();
		}

		return null;
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param servicePortal
	 * @param serviceOnegate
	 * @param serviceBackoffice
	 * @param serviceCitizen
	 * @param serviceBusinees
	 * @param serviceDomainIndex
	 * @param govAgencyIndex
	 * @param start
	 * @param end
	 * @param orderByComparator
	 * @param andOperator
	 * @return
	 */
	private List<ServiceBean> _searchServiceConfigAdvance(
		long groupId, String[] keywords, int servicePortal, int serviceOnegate,
		int serviceBackoffice, int serviceCitizen, int serviceBusinees,
		String serviceDomainIndex, String govAgencyIndex, int start, int end,
		OrderByComparator orderByComparator, boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(SEARCH_SERVICE_CONFIG_ADVANCE_SQL);
			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_serviceinfo.serviceName)",
						StringPool.LIKE, true, keywords);
				
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_serviceinfo.serviceNo)",
						StringPool.LIKE, true, keywords);
			}
			else {
				sql = StringUtil
					.replace(sql,
						"AND (lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
				
				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_serviceinfo.serviceNo) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			if (servicePortal != 1 && servicePortal != 0) {

				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.servicePortal = ?",
						StringPool.BLANK);

			}

			if (serviceOnegate != 1 && serviceOnegate != 0) {

				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceOnegate = ?",
						StringPool.BLANK);

			}

			if (serviceBackoffice != 1 && serviceBackoffice != 0) {

				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceBackoffice = ?",
						StringPool.BLANK);

			}
			if (serviceCitizen != 1 && serviceCitizen != 0) {

				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceCitizen = ?",
						StringPool.BLANK);

			}

			if (serviceBusinees != 1 && serviceBusinees != 0) {

				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceBusinees = ?",
						StringPool.BLANK);

			}

			if (Validator
				.isNull(serviceDomainIndex)) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_service_config.serviceDomainIndex LIKE ? OR opencps_service_config.serviceDomainIndex = ?)",
						StringPool.BLANK);
			}
			else {
				if (serviceDomainIndex.contains(StringPool.PERIOD)) {
					serviceDomainIndex = serviceDomainIndex
						.substring(0, serviceDomainIndex
							.indexOf(StringPool.PERIOD) + 1);

					sql = StringUtil
						.replace(sql,
							"AND (opencps_service_config.serviceDomainIndex LIKE ? OR opencps_service_config.serviceDomainIndex = ?)",
							"AND (opencps_service_config.serviceDomainIndex LIKE ?");

				}

			}

			if (Validator
				.isNull(govAgencyIndex)) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_service_config.govAgencyIndex LIKE ? OR opencps_service_config.govAgencyIndex = ?)",
						StringPool.BLANK);
			}
			else {
				if (govAgencyIndex.contains(StringPool.PERIOD)) {
					govAgencyIndex = govAgencyIndex
						.substring(0, govAgencyIndex
							.indexOf(StringPool.PERIOD) + 1);

					sql = StringUtil
						.replace(sql,
							"AND (opencps_service_config.govAgencyIndex LIKE ? OR opencps_service_config.govAgencyIndex = ?)",
							"AND (opencps_service_config.govAgencyIndex LIKE ?");

				}

			}

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);
			
			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.setCacheable(false);

			q
				.addEntity("ServiceConfig", ServiceConfigImpl.class);
			q
				.addScalar("serviceName", Type.STRING);
			q
				.addScalar("fullName", Type.STRING);
			q
				.addScalar("serviceNo", Type.STRING);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			if (keywords != null && keywords.length > 0) {
				qPos.add(keywords, 2);
				qPos.add(keywords, 2);
			}

			if (servicePortal == 1) {
				qPos
					.add(true);
			}
			else if (servicePortal == 0) {
				qPos
					.add(false);
			}

			if (serviceOnegate == 1) {
				qPos
					.add(true);
			}
			else if (serviceOnegate == 0) {
				qPos
					.add(false);
			}

			if (serviceBackoffice == 1) {
				qPos
					.add(true);
			}
			else if (serviceBackoffice == 0) {
				qPos
					.add(false);
			}

			if (serviceCitizen == 1) {
				qPos
					.add(true);
			}
			else if (serviceCitizen == 0) {
				qPos
					.add(false);
			}

			if (serviceBusinees == 1) {
				qPos
					.add(true);
			}
			else if (serviceBusinees == 0) {
				qPos
					.add(false);
			}

			if (Validator
				.isNotNull(serviceDomainIndex) && serviceDomainIndex.contains(StringPool.PERIOD)) {
				qPos
					.add(serviceDomainIndex + StringPool.PERCENT);

			}
			else if (Validator
				.isNotNull(serviceDomainIndex) && !serviceDomainIndex.contains(StringPool.PERIOD)) {
				qPos
					.add(serviceDomainIndex + StringPool.PERIOD +
						StringPool.PERCENT);
				qPos
					.add(serviceDomainIndex);
			}

			if (Validator
				.isNotNull(govAgencyIndex) && govAgencyIndex.contains(StringPool.PERIOD)) {
				qPos
					.add(govAgencyIndex + StringPool.PERCENT);

			}
			else if (Validator
				.isNotNull(govAgencyIndex) && !govAgencyIndex.contains(StringPool.PERIOD)) {
				qPos
					.add(govAgencyIndex + StringPool.PERIOD +
						StringPool.PERCENT);
				qPos
					.add(govAgencyIndex);
			}

			Iterator<Object[]> itr = (Iterator<Object[]>) QueryUtil
				.list(q, getDialect(), start, end).iterator();

			List<ServiceBean> serviceBeans = new ArrayList<ServiceBean>();

			if (itr
				.hasNext()) {
				while (itr
					.hasNext()) {
					ServiceBean serviceBean = new ServiceBean();

					Object[] objects = itr
						.next();

					ServiceConfig serviceConfig = (ServiceConfig) objects[0];

					String serviceName = (String) objects[1];
					String fullName = (String) objects[2];
					String serviceNo = (String) objects[3];

					serviceBean
						.setCompanyId(serviceConfig
							.getCompanyId());
					serviceBean
						.setDossierTemplateId(serviceConfig
							.getDossierTemplateId());
					serviceBean
						.setFullName(fullName);

					serviceBean
						.setDomainCode(serviceConfig
							.getDomainCode());

					serviceBean
						.setLevel(serviceConfig
							.getServiceLevel());

					serviceBean
						.setGovAgencyCode(serviceConfig
							.getGovAgencyCode());
					serviceBean
						.setGovAgencyIndex(serviceConfig
							.getGovAgencyIndex());
					serviceBean
						.setGovAgencyName(serviceConfig
							.getGovAgencyName());
					serviceBean
						.setGovAgencyOrganizationId(serviceConfig
							.getGovAgencyOrganizationId());
					serviceBean
						.setGroupId(groupId);
					serviceBean
						.setServiceAdministrationIndex(serviceConfig
							.getServiceAdministrationIndex());
					serviceBean
						.setServiceBackoffice(serviceConfig
							.getServiceBackoffice());
					serviceBean
						.setServiceBusinees(serviceConfig
							.getServiceBusinees());

					serviceBean
						.setServiceCitizen(serviceConfig
							.getServiceCitizen());
					serviceBean
						.setServiceConfigId(serviceConfig
							.getServiceConfigId());
					serviceBean
						.setServiceDomainIndex(serviceDomainIndex);
					serviceBean
						.setServiceInfoId(serviceConfig
							.getServiceInfoId());
					serviceBean
						.setServiceLevel(serviceConfig
							.getServiceLevel());
					serviceBean
						.setServiceName(serviceName);
					serviceBean
						.setServiceNo(serviceNo);
					serviceBean
						.setServiceOnegate(serviceConfig
							.getServiceOnegate());
					serviceBean
						.setServicePortal(serviceConfig
							.getServicePortal());
					serviceBean
						.setServiceProcessId(serviceConfig
							.getServiceProcessId());
					serviceBean
						.setUserId(serviceConfig
							.getUserId());

					serviceBeans
						.add(serviceBean);

				}
			}

			return serviceBeans;
		}
		catch (Exception e) {
			_log
				.error(e);
		}
		finally {
			session
				.close();
		}

		return null;
	}

	public int countServiceConfig(
		long groupId, String keywords, String govAgencyCode,
		String domainCode) {

		String[] names = null;
		boolean andOperator = false;

		if (Validator.isNotNull(keywords)) {
			names = new String[]{
				StringUtil.quote(
					StringUtil.toLowerCase(keywords).trim(), 
					StringPool.PERCENT)};
		}
		else {
			andOperator = true;
		}

		return _countServiceConfig(groupId, names, govAgencyCode, domainCode,
			andOperator);
	}

	public int countServiceConfigAdvance(
		long groupId, String keyword, int servicePortal, int serviceOnegate,
		int serviceBackoffice, int serviceCitizen, int serviceBusinees,
		String serviceDomainIndex, String govAgencyIndex) {

		String[] keywords = null;
		boolean andOperator = false;

		if (Validator.isNotNull(keyword)) {
			keywords = new String[]{
				StringUtil.quote(
					StringUtil.toLowerCase(keyword).trim(), 
					StringPool.PERCENT)};
		}
		else {
			andOperator = true;
		}

		return _countServiceConfigAdvance(groupId, keywords, servicePortal,
			serviceOnegate, serviceBackoffice, serviceCitizen, serviceBusinees,
			serviceDomainIndex, govAgencyIndex, andOperator);
	}

	public int countServiceConfigByServiceMode(
		long groupId, int[] serviceModes) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(COUNT_SERVICE_CONFIG_BY_SERVICE_MODE_SQL);

			if (Validator
				.isNull(serviceModes)) {
				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceMode IN (?)",
						StringPool.BLANK);
			}
			else {
				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceMode IN (?)",
						"AND opencps_service_config.serviceMode IN (" +
							StringUtil
								.merge(serviceModes) +
							")");
			}

			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.setCacheable(false);
			q
				.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			Iterator<Integer> itr = q
				.iterate();

			if (itr
				.hasNext()) {
				Integer count = itr
					.next();

				if (count != null) {
					return count
						.intValue();
				}
			}

		}
		catch (Exception e) {
			_log
				.error(e);
		}
		finally {
			session
				.close();
		}
		return 0;
	}

	public List<ServiceConfig> searchServiceConfig(
		long groupId, String keywords, String govAgencyCode, String domainCode,
		int start, int end) {

		String[] names = null;
		boolean andOperator = false;

		if (Validator.isNotNull(keywords)) {
			names = new String[]{
				StringUtil.quote(
					StringUtil.toLowerCase(keywords).trim(), 
					StringPool.PERCENT)};
		}
		else {
			andOperator = true;
		}

		return _searchServiceConfig(groupId, names, govAgencyCode, domainCode,
			andOperator, start, end);
	}

	/**
	 * @param groupId
	 * @param keyword
	 * @param servicePortal
	 * @param serviceOnegate
	 * @param serviceBackoffice
	 * @param serviceCitizen
	 * @param serviceBusinees
	 * @param serviceDomainIndex
	 * @param govAgencyIndex
	 * @param start
	 * @param end
	 * @param orderByComparator
	 * @return
	 */
	public List searchServiceConfigAdvance(
		long groupId, String keyword, int servicePortal, int serviceOnegate,
		int serviceBackoffice, int serviceCitizen, int serviceBusinees,
		String serviceDomainIndex, String govAgencyIndex, int start, int end,
		OrderByComparator orderByComparator) {

		String[] keywords = null;
		boolean andOperator = false;

		if (Validator.isNotNull(keyword)) {
			keywords = new String[]{
				StringUtil.quote(
					StringUtil.toLowerCase(keyword).trim(), 
					StringPool.PERCENT)};
		}
		else {
			andOperator = true;
		}

		return _searchServiceConfigAdvance(groupId, keywords, servicePortal,
			serviceOnegate, serviceBackoffice, serviceCitizen, serviceBusinees,
			serviceDomainIndex, govAgencyIndex, start, end, orderByComparator,
			andOperator);
	}

	public List<ServiceConfig> searchServiceConfigByServiceMode(
		long groupId, int[] serviceModes, int start, int end,
		OrderByComparator orderByComparator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(SEARCH_SERVICE_CONFIG_BY_SERVICE_MODE_SQL);

			if (Validator
				.isNull(serviceModes)) {
				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceMode IN (?)",
						StringPool.BLANK);
			}
			else {
				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceMode IN (?)",
						"AND opencps_service_config.serviceMode IN (" +
							StringUtil
								.merge(serviceModes) +
							")");
			}

			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.setCacheable(false);
			q
				.addEntity("ServiceConfig", ServiceConfigImpl.class);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			return (List<ServiceConfig>) QueryUtil
				.list(q, getDialect(), start, end);
		}
		catch (Exception e) {
			_log
				.error(e);
		}
		finally {
			session
				.close();
		}

		return null;
	}

	/**
	 * @param groupId
	 * @param userId
	 * @param servicePortal
	 * @param serviceOnegate
	 * @param serviceBackoffice
	 * @param serviceCitizen
	 * @param serviceBusinees
	 * @param start
	 * @param end
	 * @param orderByComparator
	 * @return
	 */
	public List getServiceConfigRecent(
		long groupId, long userId, int servicePortal, int serviceOnegate,
		int serviceBackoffice, int serviceCitizen, int serviceBusinees,
		int start, int end, OrderByComparator orderByComparator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(SEARCH_SERVICE_CONFIG_RECENT_SQL);

			if (servicePortal != 1 && servicePortal != 0) {

				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.servicePortal = ?",
						StringPool.BLANK);

			}

			if (serviceOnegate != 1 && serviceOnegate != 0) {

				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceOnegate = ?",
						StringPool.BLANK);

			}

			if (serviceBackoffice != 1 && serviceBackoffice != 0) {

				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceBackoffice = ?",
						StringPool.BLANK);

			}
			if (serviceCitizen != 1 && serviceCitizen != 0) {

				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceCitizen = ?",
						StringPool.BLANK);

			}

			if (serviceBusinees != 1 && serviceBusinees != 0) {

				sql = StringUtil
					.replace(sql,
						"AND opencps_service_config.serviceBusinees = ?",
						StringPool.BLANK);

			}

			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.setCacheable(false);

			q
				.addEntity("ServiceConfig", ServiceConfigImpl.class);
			q
				.addScalar("serviceName", Type.STRING);
			q
				.addScalar("fullName", Type.STRING);
			q
				.addScalar("serviceNo", Type.STRING);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			if (servicePortal == 1) {
				qPos
					.add(true);
			}
			else if (servicePortal == 0) {
				qPos
					.add(false);
			}

			if (serviceOnegate == 1) {
				qPos
					.add(true);
			}
			else if (serviceOnegate == 0) {
				qPos
					.add(false);
			}

			if (serviceBackoffice == 1) {
				qPos
					.add(true);
			}
			else if (serviceBackoffice == 0) {
				qPos
					.add(false);
			}

			if (serviceCitizen == 1) {
				qPos
					.add(true);
			}
			else if (serviceCitizen == 0) {
				qPos
					.add(false);
			}

			if (serviceBusinees == 1) {
				qPos
					.add(true);
			}
			else if (serviceBusinees == 0) {
				qPos
					.add(false);
			}

			qPos
				.add(userId);

			Iterator<Object[]> itr = (Iterator<Object[]>) QueryUtil
				.list(q, getDialect(), start, end).iterator();

			List<ServiceBean> serviceBeans = new ArrayList<ServiceBean>();

			if (itr
				.hasNext()) {
				while (itr
					.hasNext()) {
					ServiceBean serviceBean = new ServiceBean();

					Object[] objects = itr
						.next();

					ServiceConfig serviceConfig = (ServiceConfig) objects[0];

					String serviceName = (String) objects[1];
					String fullName = (String) objects[2];
					String serviceNo = (String) objects[3];

					serviceBean
						.setCompanyId(serviceConfig
							.getCompanyId());
					serviceBean
						.setDossierTemplateId(serviceConfig
							.getDossierTemplateId());
					serviceBean
						.setFullName(fullName);

					serviceBean
						.setDomainCode(serviceConfig
							.getDomainCode());

					serviceBean
						.setLevel(serviceConfig
							.getServiceLevel());

					serviceBean
						.setGovAgencyCode(serviceConfig
							.getGovAgencyCode());
					serviceBean
						.setGovAgencyIndex(serviceConfig
							.getGovAgencyIndex());
					serviceBean
						.setGovAgencyName(serviceConfig
							.getGovAgencyName());
					serviceBean
						.setGovAgencyOrganizationId(serviceConfig
							.getGovAgencyOrganizationId());
					serviceBean
						.setGroupId(groupId);
					serviceBean
						.setServiceAdministrationIndex(serviceConfig
							.getServiceAdministrationIndex());
					serviceBean
						.setServiceBackoffice(serviceConfig
							.getServiceBackoffice());
					serviceBean
						.setServiceBusinees(serviceConfig
							.getServiceBusinees());

					serviceBean
						.setServiceCitizen(serviceConfig
							.getServiceCitizen());
					serviceBean
						.setServiceConfigId(serviceConfig
							.getServiceConfigId());

					serviceBean
						.setServiceInfoId(serviceConfig
							.getServiceInfoId());
					serviceBean
						.setServiceLevel(serviceConfig
							.getServiceLevel());
					serviceBean
						.setServiceName(serviceName);
					serviceBean
						.setServiceNo(serviceNo);
					serviceBean
						.setServiceOnegate(serviceConfig
							.getServiceOnegate());
					serviceBean
						.setServicePortal(serviceConfig
							.getServicePortal());
					serviceBean
						.setServiceProcessId(serviceConfig
							.getServiceProcessId());
					serviceBean
						.setUserId(serviceConfig
							.getUserId());

					serviceBeans
						.add(serviceBean);

				}
			}

			return serviceBeans;
		}
		catch (Exception e) {
			_log
				.error(e);
		}
		finally {
			session
				.close();
		}

		return null;
	}
}
