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
/**
 * 
 */

package org.opencps.dossiermgt.comparator;

import org.opencps.dossiermgt.model.DossierFile;

import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * @author dunglt
 */
public class DossierFileDossierFileDateComparator extends OrderByComparator {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.liferay.portal.kernel.util.OrderByComparator#compare(java.lang.Object
	 * , java.lang.Object)
	 */

	public DossierFileDossierFileDateComparator() {

		this(false);
	}

	public static final String ORDER_BY_ASC =
		"opencps_dossier_file.dossierFileDate ASC";

	public static final String ORDER_BY_DESC =
		"opencps_dossier_file.dossierFileDate DESC";

	public static final String[] ORDER_BY_FIELDS = {
		"dossierFileDate"
	};

	public DossierFileDossierFileDateComparator(boolean ascending) {

		_ascending = ascending;
	}

	@Override
	public int compare(Object arg0, Object arg1) {

		DossierFile dossierFile = (DossierFile) arg0;
		DossierFile dossierFile2 = (DossierFile) arg1;
		
		int value = 0;
		value = dossierFile.getDossierFileDate().compareTo(dossierFile2.getDossierFileDate());
		
		return _ascending ? value : -value;
	}
	
	@Override
	public String getOrderBy() {

		if (_ascending) {
			return ORDER_BY_ASC;
		}
		else {
			return ORDER_BY_DESC;
		}
	}

	@Override
	public String[] getOrderByFields() {

		return ORDER_BY_FIELDS;
	}

	@Override
	public boolean isAscending() {

		return _ascending;
	}

	private boolean _ascending;

}
