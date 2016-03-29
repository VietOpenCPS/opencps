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

package org.opencps.taglib.datamgt;

import javax.servlet.http.HttpServletRequest;

import com.liferay.taglib.util.IncludeTag;

/**
 * @author trungnt
 */
public class DDRTag extends IncludeTag {

	private static final String _PAGE = "/html/taglib/datamgt/ddr/page.jsp";

	private String _cssClass;

	private int _depthLevel;

	private String _dictCollectionCode;

	private String _displayStyle;

	private long _initDictItemId;

	private String _itemNames;

	private String _itemsEmptyOption;

	private String _name;

	private String _renderMode;

	private String _selectedItems;

	@Override
	protected void cleanUp() {

		_dictCollectionCode = null;
		_initDictItemId = 0;
		_depthLevel = 0;
		_cssClass = null;
		_itemNames = null;
		_selectedItems = null;
		_renderMode = null;
		_name = "dataItem";
		_cssClass = null;
		_displayStyle = "horizontal";
		_itemsEmptyOption = null;
	}

	public String getCssClass() {

		return _cssClass;
	}

	public int getDepthLevel() {

		return _depthLevel;
	}

	public String getDictCollectionCode() {

		return _dictCollectionCode;
	}

	public String getDisplayStyle() {

		return _displayStyle;
	}

	public long getInitDictItemId() {

		return _initDictItemId;
	}

	public String getItemNames() {

		return _itemNames;
	}

	public String getItemsEmptyOption() {

		return _itemsEmptyOption;
	}

	public String getName() {

		return _name;
	}

	@Override
	protected String getPage() {

		return _PAGE;
	}

	public String getRenderMode() {

		return _renderMode;
	}

	public String getSelectedItems() {

		return _selectedItems;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {

		request
			.setAttribute("opencps-datamgt:ddr:dictCollectionCode",
				_dictCollectionCode);

		request
			.setAttribute("opencps-datamgt:ddr:itemsEmptyOption",
				_itemsEmptyOption);
		request
			.setAttribute("opencps-datamgt:ddr:initDictItemId",
				_initDictItemId);
		request
			.setAttribute("opencps-datamgt:ddr:depthLevel", _depthLevel);
		request
			.setAttribute("opencps-datamgt:ddr:itemNames", _itemNames);
		request
			.setAttribute("opencps-datamgt:ddr:selectedItems", _selectedItems);
		request
			.setAttribute("opencps-datamgt:ddr:renderMode", _renderMode);
		request
			.setAttribute("opencps-datamgt:ddr:name", _name);
		request
			.setAttribute("opencps-datamgt:ddr:cssClass", _cssClass);
		request
			.setAttribute("opencps-datamgt:ddr:displayStyle", _displayStyle);
	}

	public void setCssClass(String _cssClass) {

		this._cssClass = _cssClass;
	}

	public void setDepthLevel(int _depthLevel) {

		this._depthLevel = _depthLevel;
	}

	public void setDictCollectionCode(String _dictCollectionCode) {

		this._dictCollectionCode = _dictCollectionCode;
	}

	public void setDisplayStyle(String _displayStyle) {

		this._displayStyle = _displayStyle;
	}

	public void setInitDictItemId(long _initDictItemId) {

		this._initDictItemId = _initDictItemId;
	}

	public void setItemNames(String _itemNames) {

		this._itemNames = _itemNames;
	}

	public void setItemsEmptyOption(String _itemsEmptyOption) {

		this._itemsEmptyOption = _itemsEmptyOption;
	}

	public void setName(String _name) {

		this._name = _name;
	}

	public void setRenderMode(String _renderMode) {

		this._renderMode = _renderMode;
	}

	public void setSelectedItems(String _selectedItems) {

		this._selectedItems = _selectedItems;
	}

}
