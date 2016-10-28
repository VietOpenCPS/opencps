/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.datamgt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.opencps.datamgt.DuplicateItemCodeException;
import org.opencps.datamgt.model.DictCollection;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.model.impl.DictItemImpl;
import org.opencps.datamgt.service.base.DictItemServiceBaseImpl;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.ac.AccessControlled;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.util.dao.orm.CustomSQLUtil;

/**
 * The implementation of the dict item remote service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.datamgt.service.DictItemService} interface. <p> This is a
 * remote service. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be accessed
 * remotely. </p>
 *
 * @author trungnt
 * @see org.opencps.datamgt.service.base.DictItemServiceBaseImpl
 * @see org.opencps.datamgt.service.DictItemServiceUtil
 */
public class DictItemServiceImpl extends DictItemServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.datamgt.service.DictItemServiceUtil} to access the
	 * dict item remote service.
	 */
	
	@JSONWebService(value = "add-dictitem", method = "POST")
	public DictItem addDictItem(String dictCollectionCode, String
			dictItemCode, String dictItemName, String parentDictItemCode) 
		throws SystemException, PortalException {
		
		return dictItemService.addDictItem(dictCollectionCode, dictItemCode, 
				dictItemName, StringPool.BLANK, parentDictItemCode);
	}
	
	@JSONWebService(value = "add-dictitem", method = "POST")
	public DictItem addDictItem(String dictCollectionCode, String
			dictItemCode, String dictItemName, String dictItemDescription, String parentDictItemCode) 
		throws SystemException, PortalException {
		
		DictCollection dictCollection = dictCollectionPersistence.findByCollectionCode(
				dictCollectionCode);
		
		DictItem dictItem = dictItemPersistence.fetchByC_C_I(
				dictCollection.getDictCollectionId(), dictItemCode);
		
		if(dictItem != null) {
			throw new DuplicateItemCodeException();
		}
		
		long parentItemId = 0;
		
		if(Validator.isNotNull(parentDictItemCode)) {
			DictItem parentDictItem = dictItemPersistence.findByC_C_I(
					dictCollection.getDictCollectionId(), parentDictItemCode);
			
			parentItemId = parentDictItem.getDictItemId();
		}
		
		ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();
		
		Map<Locale, String> itemNameMap = new HashMap<Locale, String>();
		Map<Locale, String> itemDescriptionMap = new HashMap<Locale, String>();
		Locale[] locales = LanguageUtil.getAvailableLocales();
		
		for(Locale locale : locales) {
			itemNameMap.put(locale, dictItemName);
			itemDescriptionMap.put(locale, dictItemDescription);
		}
		
		return dictItemLocalService.addDictItem(getUserId(), dictCollection.getDictCollectionId(),
				dictItemCode, itemNameMap, itemDescriptionMap, parentItemId, serviceContext);
	}
	
	@JSONWebService(value = "update-dictitem", method = "POST")
	public DictItem updateDictItem(String dictCollectionCode, String
			dictItemCode, String dictItemName, String parentDictItemCode) 
		throws SystemException, PortalException {
		
		return dictItemService.updateDictItem(dictCollectionCode, dictItemCode, 
			dictItemName, null, parentDictItemCode);
	}
	
	@JSONWebService(value = "update-dictitem", method = "POST")
	public DictItem updateDictItem(String dictCollectionCode, String
			dictItemCode, String dictItemName, String dictItemDescription, String parentDictItemCode) 
		throws SystemException, PortalException {
		
		DictCollection dictCollection = dictCollectionPersistence.findByCollectionCode(
				dictCollectionCode);
		
		DictItem dictItem = dictItemPersistence.findByC_C_I(
			dictCollection.getDictCollectionId(), dictItemCode);
		
		long parentItemId = 0;
		
		if(Validator.isNotNull(parentDictItemCode)) {
			DictItem parentDictItem = dictItemPersistence.findByC_C_I(
					dictCollection.getDictCollectionId(), parentDictItemCode);
			
			parentItemId = parentDictItem.getDictItemId();
		}
		
		ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();
		
		Map<Locale, String> itemNameMap = new HashMap<Locale, String>();
		Map<Locale, String> itemDescriptionMap = null;
		Locale[] locales = LanguageUtil.getAvailableLocales();

		if(dictItemDescription != null) {
			itemDescriptionMap = new HashMap<Locale, String>();
		}
		
		for(Locale locale : locales) {
			itemNameMap.put(locale, dictItemName);
			
			if(dictItemDescription != null) {
				itemDescriptionMap.put(locale, dictItemDescription);
			}
		}
		
		return dictItemLocalService.updateDictItem(dictItem.getDictItemId(),
				dictCollection.getDictCollectionId(), 0, dictItemCode, itemNameMap, itemDescriptionMap,
				parentItemId, serviceContext);
	}
	
	@JSONWebService(value = "delete-dictitem", method = "POST")
	public void deleteDictItem(String dictCollectionCode, String dictItemCode) 
		throws SystemException, PortalException {
		
		DictCollection dictCollection = dictCollectionPersistence.findByCollectionCode(
				dictCollectionCode);
		
		DictItem dictItem = dictItemPersistence.findByC_C_I(
			dictCollection.getDictCollectionId(), dictItemCode);
		
		dictItemLocalService.deleteDictItemById(dictItem.getDictItemId());
	}

	@JSONWebService(value = "get-dictitem-by-pk")
	@AccessControlled(guestAccessEnabled = true)
	public DictItem getDictItem(long dictItemId)
		throws SystemException, PortalException {

		return dictItemLocalService
			.getDictItem(dictItemId);
	}

	@JSONWebService(value = "get-dictitem-inuse-by-code")
	@AccessControlled(guestAccessEnabled = true)
	public DictItem getDictItemInuseByItemCode(
		long dictCollectionId, String itemCode)
		throws SystemException, PortalException {

		return dictItemLocalService
			.getDictItemInuseByItemCode(dictCollectionId, itemCode);
	}

	@JSONWebService(value = "get-dictitems-by-parentId")
	@AccessControlled(guestAccessEnabled = true)
	public List<DictItem> getDictItemsByParentItemId(long parentItemId)
		throws SystemException {

		return dictItemLocalService
			.getDictItemsByParentItemId(parentItemId);
	}

	@JSONWebService(value = "get-dictitems-by-dictcollectionId")
	@AccessControlled(guestAccessEnabled = true)
	public List<DictItem> getDictItemsByDictCollectionId(long dictCollectionId)
		throws SystemException {

		return dictItemLocalService
			.getDictItemsByDictCollectionId(dictCollectionId);
	}

	@JSONWebService(value = "get-dictitems-inuse-by-dictcollectionId_parentItemId")
	@AccessControlled(guestAccessEnabled = true)
	public List<DictItem> getDictItemsInUseByDictCollectionIdAndParentItemId(
		long dictCollectionId, long parentItemId)
		throws SystemException {

		return dictItemLocalService
			.getDictItemsInUseByDictCollectionIdAndParentItemId(
				dictCollectionId, parentItemId);
	}

	@JSONWebService(value = "get-dictitems-inuse-by-dictcollectionId_parentItemId_datasource")
	@AccessControlled(guestAccessEnabled = true)
	public JSONObject getDictItemsInUseByDictCollectionIdAndParentItemIdDataSource(
		long dictCollectionId, long parentItemId)
		throws SystemException {

		JSONObject jsonObject = JSONFactoryUtil
			.createJSONObject();
		List<DictItem> result = dictItemLocalService
			.getDictItemsInUseByDictCollectionIdAndParentItemId(
				dictCollectionId, parentItemId);
		for (DictItem dictItem : result) {
			jsonObject
				.put(String
					.valueOf(dictItem
						.getDictItemId()),
					dictItem
						.getItemName(Locale
							.getDefault()));
		}
		return jsonObject;
	}
	
	@JSONWebService(value = "get-dictitems_itemCode_datasource")
	@AccessControlled(guestAccessEnabled = true)
	public JSONObject getDictItemsByItemCodeDataSource(
			String collectionCode, String itemCode, long groupId) throws SystemException, PortalException {

		JSONObject jsonObject = JSONFactoryUtil
			.createJSONObject();
		
		DictCollection dictCollection = dictCollectionLocalService.getDictCollection(groupId, collectionCode);
		
		long parentId = 0;
		
		if(!itemCode.equalsIgnoreCase("0")){
			
			DictItem ett = dictItemLocalService
					.getDictItemInuseByItemCode(dictCollection.getDictCollectionId(), itemCode);
			
			parentId = ett.getDictItemId();
			
		}
		
		List<DictItem> result = dictItemLocalService
			.getDictItemsInUseByDictCollectionIdAndParentItemId(
					dictCollection.getDictCollectionId(), parentId);
		
		for (DictItem dictItem : result) {
			jsonObject
				.put(String
					.valueOf(dictItem
						.getItemCode()),
					dictItem
						.getItemName(Locale
							.getDefault()));
		}
		return jsonObject;
	}
	
	@JSONWebService(value = "get-dictitems_itemCode_keywords_datasource")
	@AccessControlled(guestAccessEnabled = true)
	public List<DictItem> getDictItemsByItemCodeDataSourceFitter(
			String collectionCode, String itemCode, String keywords, long groupId) throws SystemException, PortalException {

		List<DictItem> result = new ArrayList<DictItem>();
		
		result = dictItemLocalService
			.searchDictItemByName_like(
					collectionCode, itemCode, keywords, groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
		
		return result;
	}
	
	@JSONWebService(value = "get-dictitems_itemCode_keywords_datasource")
	@AccessControlled(guestAccessEnabled = true)
	public List<DictItem> getDictItemsByItemCodeDataSourceFitter(
			String collectionCode, String itemCode, String keywords, long groupId, int start, int end) throws SystemException, PortalException {

		List<DictItem> result = new ArrayList<DictItem>();
		
		result = dictItemLocalService
			.searchDictItemByName_like(
					collectionCode, itemCode, keywords, groupId, start, end, null);
		
		return result;
	}
	
	@JSONWebService(value = "get-dictitem_itemCode")
	@AccessControlled(guestAccessEnabled = true)
	public DictItem getDictItemByItemCode(
			String collectionCode, String itemCode, long groupId) throws SystemException, PortalException {

		DictItem result = new DictItemImpl();
		
		DictCollection dictCollection = dictCollectionLocalService.getDictCollection(groupId, collectionCode);
		
		if(!itemCode.equalsIgnoreCase("0")){
			
			result = dictItemLocalService
					.getDictItemInuseByItemCode(dictCollection.getDictCollectionId(), itemCode);
			
		}
		
		return result;
	}
}
