/**
 * OpenCPS is the open source Core Public Services software Copyright (C)
 * 2016-present OpenCPS community This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3
 * of the License, or any later version. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have received a
 * copy of the GNU Affero General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.accountmgt.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opencps.accountmgt.NoSuchCitizenException;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.service.base.CitizenLocalServiceBaseImpl;
import org.opencps.util.DLFolderUtil;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.PortletUtil;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.EmailAddress;
import com.liferay.portal.model.Phone;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.model.Website;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ContactLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserGroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.announcements.model.AnnouncementsDelivery;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;
import com.liferay.util.PwdGenerator;

/**
 * The implementation of the citizen local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.accountmgt.service.CitizenLocalService} interface. <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author khoavd
 * @author trungnt
 * @see org.opencps.accountmgt.service.base.CitizenLocalServiceBaseImpl
 * @see org.opencps.accountmgt.service.CitizenLocalServiceUtil
 */
public class CitizenLocalServiceImpl extends CitizenLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.accountmgt.service.CitizenLocalServiceUtil} to access
	 * the citizen local service.
	 */

	public Citizen addCitizen(
	    String fullName, String personalId, int gender, int birthDateDay,
	    int birthDateMonth, int birthDateYear, String address, String cityCode,
	    String districtCode, String wardCode, String cityName,
	    String districtName, String wardName, String email, String telNo,
	    long repositoryId, String sourceFileName, String mimeType, String title,
	    InputStream inputStream, long size, ServiceContext serviceContext)
	    throws SystemException, PortalException {

		long citizenId = CounterLocalServiceUtil
		    .increment(Citizen.class
		        .getName());

		Citizen citizen = citizenPersistence
		    .create(citizenId);

		Date now = new Date();

		Date birthDate = DateTimeUtil
		    .getDate(birthDateDay, birthDateMonth, birthDateYear);

		PortletUtil.SplitName spn = PortletUtil
		    .splitName(fullName);

		boolean autoPassword = true;
		boolean autoScreenName = true;
		boolean sendEmail = false;

		long[] groupIds = null;
		long[] organizationIds = null;
		long[] roleIds = null;
		long[] userGroupIds = null;

		String password1 = null;
		String password2 = null;
		String screenName = null;

		UserGroup userGroup = null;
		try {
			userGroup = UserGroupLocalServiceUtil
			    .getUserGroup(serviceContext
			        .getCompanyId(),
			        PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN);
		}
		catch (Exception e) {
			_log
			    .warn(e
			        .getMessage());
		}
		if (userGroup == null) {
			userGroup = UserGroupLocalServiceUtil
			    .addUserGroup(serviceContext
			        .getUserId(), serviceContext
			            .getCompanyId(),
			        PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN,
			        StringPool.BLANK, serviceContext);

		}

		if (userGroup != null) {
			userGroupIds = new long[] {
			    userGroup
			        .getUserGroupId()
			};
		}
		password1 = PwdGenerator
		    .getPassword();
		password2 = password1;

		Role adminRole = RoleLocalServiceUtil
		    .getRole(serviceContext
		        .getCompanyId(), "Administrator");
		List<User> adminUsers = UserLocalServiceUtil
		    .getRoleUsers(adminRole
		        .getRoleId());

		PrincipalThreadLocal
		    .setName(adminUsers
		        .get(0).getUserId());
		PermissionChecker permissionChecker;
		try {
			permissionChecker = PermissionCheckerFactoryUtil
			    .create(adminUsers
			        .get(0));
			PermissionThreadLocal
			    .setPermissionChecker(permissionChecker);
		}
		catch (Exception e) {
			_log
			    .error(e);
		}

		User mappingUser = userService
		    .addUserWithWorkflow(serviceContext
		        .getCompanyId(), autoPassword, password1, password2,
		        autoScreenName, screenName, email, 0L, StringPool.BLANK,
		        LocaleUtil
		            .getDefault(),
		        spn
		            .getFirstName(),
		        spn
		            .getMidName(),
		        spn
		            .getLastName(),
		        0, 0, (gender == 1), birthDateMonth, birthDateDay,
		        birthDateYear, "Citizen", groupIds, organizationIds, roleIds,
		        userGroupIds, new ArrayList<Address>(),
		        new ArrayList<EmailAddress>(), new ArrayList<Phone>(),
		        new ArrayList<Website>(),
		        new ArrayList<AnnouncementsDelivery>(), sendEmail,
		        serviceContext);

		int status = WorkflowConstants.STATUS_INACTIVE;

		mappingUser = userService
		    .updateStatus(mappingUser
		        .getUserId(), status);

		String[] folderNames = new String[] {
		    PortletConstants.DestinationRoot.CITIZEN
		        .toString(),
		    cityName, districtName, wardName, String
		        .valueOf(mappingUser
		            .getUserId())
		};

		String destination = PortletUtil
		    .getDestinationFolder(folderNames);

		serviceContext
		    .setAddGroupPermissions(true);
		serviceContext
		    .setAddGuestPermissions(true);

		FileEntry fileEntry = null;

		if (size > 0 && inputStream != null) {
			// Create person folder
			DLFolder dlFolder = DLFolderUtil
			    .getTargetFolder(mappingUser
			        .getUserId(), serviceContext
			            .getScopeGroupId(),
			        repositoryId, false, 0, destination, StringPool.BLANK,
			        false, serviceContext);

			fileEntry = DLAppServiceUtil
			    .addFileEntry(repositoryId, dlFolder
			        .getFolderId(), sourceFileName, mimeType, title,
			        StringPool.BLANK, StringPool.BLANK, inputStream, size,
			        serviceContext);
		}

		citizen
		    .setAccountStatus(PortletConstants.ACCOUNT_STATUS_REGISTERED);
		citizen
		    .setAddress(address);
		citizen
		    .setAttachFile(fileEntry != null ? fileEntry
		        .getFileEntryId() : 0);
		citizen
		    .setBirthdate(birthDate);
		citizen
		    .setCityCode(cityCode);
		citizen
		    .setCompanyId(serviceContext
		        .getCompanyId());
		citizen
		    .setCreateDate(now);
		citizen
		    .setDistrictCode(districtCode);
		citizen
		    .setEmail(email);
		citizen
		    .setFullName(fullName);
		citizen
		    .setGender(gender);
		citizen
		    .setGroupId(serviceContext
		        .getScopeGroupId());
		citizen
		    .setMappingUserId(mappingUser
		        .getUserId());
		citizen
		    .setModifiedDate(now);
		citizen
		    .setPersonalId(personalId);
		citizen
		    .setTelNo(telNo);
		citizen
		    .setUserId(mappingUser
		        .getUserId());
		citizen
		    .setWardCode(wardCode);

		citizen
		    .setUuid(PortalUUIDUtil
		        .generate());

		return citizenPersistence
		    .update(citizen);
	}

	public void deleteCitizenByCitizenId(long citizenId)
	    throws SystemException, PortalException {

		Citizen citizen = citizenPersistence
		    .findByPrimaryKey(citizenId);

		long fileEntryId = citizen
		    .getAttachFile();

		long mappingUserId = citizen
		    .getMappingUserId();

		if (mappingUserId > 0) {
			userLocalService
			    .deleteUser(mappingUserId);
		}

		if (fileEntryId > 0) {
			FileEntry fileEntry = DLAppServiceUtil
			    .getFileEntry(fileEntryId);
			long folderId = fileEntry
			    .getFolderId();

			DLAppServiceUtil
			    .deleteFileEntry(fileEntryId);
			DLFolderLocalServiceUtil
			    .deleteFolder(folderId);
		}

		citizenPersistence
		    .remove(citizenId);

	}

	public Citizen getCitizen(long mappingUserId)
	    throws NoSuchCitizenException, SystemException {

		return citizenPersistence
		    .findByMappingUserId(mappingUserId);
	}

	public Citizen getCitizen(String email)
	    throws NoSuchCitizenException, SystemException {

		return citizenPersistence
		    .findByEmail(email);
	}

	public Citizen getCitizenByUUID(String uuid)
	    throws NoSuchCitizenException, SystemException {

		return citizenPersistence
		    .findByUUID(uuid);
	}

	public Citizen updateCitizen(
	    long citizenId, String address, String cityCode, String districtCode,
	    String wardCode, String cityName, String districtName, String wardName,
	    String telNo, boolean isChangePassWord, String newPassword,
	    String reTypePassword, long repositoryId, ServiceContext serviceContext)
	    throws SystemException, PortalException {

		Citizen citizen = citizenPersistence
		    .findByPrimaryKey(citizenId);

		User mappingUser = userLocalService
		    .getUser(citizen
		        .getMappingUserId());

		Date now = new Date();

		if (mappingUser != null) {
			// Reset password
			if (isChangePassWord) {
				mappingUser = userLocalService
				    .updatePassword(mappingUser
				        .getUserId(), newPassword, reTypePassword, false);
			}

			if ((cityCode != citizen
			    .getCityCode() || districtCode != citizen
			        .getDistrictCode() ||
			    wardCode != citizen
			        .getWardCode()) &&
			    citizen
			        .getAttachFile() > 0) {
				// Move image folder

				String[] newFolderNames = new String[] {
				    PortletConstants.DestinationRoot.CITIZEN
				        .toString(),
				    cityName, districtName, wardName
				};

				String destination = PortletUtil
				    .getDestinationFolder(newFolderNames);

				DLFolder parentFolder = DLFolderUtil
				    .getTargetFolder(mappingUser
				        .getUserId(), serviceContext
				            .getScopeGroupId(),
				        repositoryId, false, 0, destination, StringPool.BLANK,
				        false, serviceContext);

				FileEntry fileEntry = DLAppServiceUtil
				    .getFileEntry(citizen
				        .getAttachFile());

				DLFolderLocalServiceUtil
				    .moveFolder(mappingUser
				        .getUserId(), fileEntry
				            .getFolderId(),
				        parentFolder
				            .getFolderId(),
				        serviceContext);
			}
		}

		citizen
		    .setAddress(address);

		citizen
		    .setCityCode(cityCode);

		citizen
		    .setDistrictCode(districtCode);

		citizen
		    .setModifiedDate(now);

		citizen
		    .setTelNo(telNo);
		citizen
		    .setUserId(mappingUser
		        .getUserId());
		citizen
		    .setWardCode(wardCode);

		return citizenPersistence
		    .update(citizen);

	}

	public Citizen updateCitizen(
	    long citizenId, String fullName, String personalId, int gender,
	    int birthDateDay, int birthDateMonth, int birthDateYear, String address,
	    String cityCode, String districtCode, String wardCode, String cityName,
	    String districtName, String wardName, String telNo, long repositoryId,
	    ServiceContext serviceContext)
	    throws SystemException, PortalException {

		Citizen citizen = citizenPersistence
		    .findByPrimaryKey(citizenId);

		User mappingUser = userLocalService
		    .getUser(citizen
		        .getMappingUserId());

		Date now = new Date();

		Date birthDate = DateTimeUtil
		    .getDate(birthDateDay, birthDateMonth, birthDateYear);

		if (mappingUser != null) {

			if ((cityCode != citizen
			    .getCityCode() || districtCode != citizen
			        .getDistrictCode() ||
			    wardCode != citizen
			        .getWardCode()) &&
			    citizen
			        .getAttachFile() > 0) {
				// Move image folder

				String[] newFolderNames = new String[] {
				    PortletConstants.DestinationRoot.CITIZEN
				        .toString(),
				    cityName, districtName, wardName
				};

				String destination = PortletUtil
				    .getDestinationFolder(newFolderNames);

				DLFolder parentFolder = DLFolderUtil
				    .getTargetFolder(mappingUser
				        .getUserId(), serviceContext
				            .getScopeGroupId(),
				        repositoryId, false, 0, destination, StringPool.BLANK,
				        false, serviceContext);

				FileEntry fileEntry = DLAppServiceUtil
				    .getFileEntry(citizen
				        .getAttachFile());

				DLFolderLocalServiceUtil
				    .moveFolder(mappingUser
				        .getUserId(), fileEntry
				            .getFolderId(),
				        parentFolder
				            .getFolderId(),
				        serviceContext);
			}

			// Change user name
			if (!fullName
			    .equals(citizen
			        .getFullName())) {

				PortletUtil.SplitName spn = PortletUtil
				    .splitName(fullName);

				mappingUser
				    .setFirstName(spn
				        .getFirstName());
				mappingUser
				    .setLastName(spn
				        .getLastName());
				mappingUser
				    .setMiddleName(spn
				        .getMidName());
			}

			mappingUser = userLocalService
			    .updateUser(mappingUser);

			// update birth date
			Contact contact = ContactLocalServiceUtil
			    .getContact(mappingUser
			        .getContactId());

			if (contact != null) {
				contact
				    .setBirthday(birthDate);
				contact = ContactLocalServiceUtil
				    .updateContact(contact);
			}
		}

		citizen
		    .setAddress(address);

		citizen
		    .setBirthdate(birthDate);
		citizen
		    .setCityCode(cityCode);

		citizen
		    .setDistrictCode(districtCode);

		citizen
		    .setFullName(fullName);
		citizen
		    .setGender(gender);
		citizen
		    .setGroupId(serviceContext
		        .getScopeGroupId());

		citizen
		    .setModifiedDate(now);
		citizen
		    .setPersonalId(personalId);
		citizen
		    .setTelNo(telNo);
		citizen
		    .setUserId(serviceContext
		        .getUserId());
		citizen
		    .setWardCode(wardCode);

		return citizenPersistence
		    .update(citizen);

	}

	public Citizen updateStatus(long citizenId, long userId, int accountStatus)
	    throws SystemException, PortalException {

		Citizen citizen = citizenPersistence
		    .findByPrimaryKey(citizenId);

		int userStatus = WorkflowConstants.STATUS_INACTIVE;

		if (accountStatus == PortletConstants.ACCOUNT_STATUS_APPROVED) {
			userStatus = WorkflowConstants.STATUS_APPROVED;
		}

		if (citizen
		    .getMappingUserId() > 0) {
			userLocalService
			    .updateStatus(citizen
			        .getMappingUserId(), userStatus);
		}

		citizen
		    .setUserId(userId);
		citizen
		    .setModifiedDate(new Date());
		citizen
		    .setAccountStatus(accountStatus);

		return citizenPersistence
		    .update(citizen);
	}

	public List<Citizen> getCitizens(int start, int end, OrderByComparator odc)
	    throws SystemException {

		return citizenPersistence
		    .findAll(start, end, odc);
	}

	public List<Citizen> getCitizens(long groupId, int accountStatus)
	    throws SystemException {

		return citizenPersistence
		    .findByG_S(groupId, accountStatus);
	}

	public List<Citizen> getCitizens(
	    long groupId, String fullName, int accountStatus)
	    throws SystemException {

		return citizenPersistence
		    .findByG_N_S(groupId, fullName, accountStatus);
	}

	public List<Citizen> getCitizens(long groupId, String fullName)
	    throws SystemException {

		return citizenPersistence
		    .findByG_N(groupId, fullName);
	}

	public int countAll()
	    throws SystemException {
		
		return citizenPersistence
		    .countAll();
	}
	
	public int countByG_S(long groupId, int accountStatus) 
		throws SystemException {
		
		return citizenPersistence
			.countByG_S(groupId, accountStatus);
	}

	private Log _log = LogFactoryUtil
	    .getLog(CitizenLocalServiceImpl.class
	        .getName());
	
}
