
<%
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@ include file="init.jsp"%>

<%
	String randomInstance = PwdGenerator.getPassword();

	boolean isHorizontal = true;
	
	if(depthLevel > 0){
		
		int colWidth = 100;
		
		if(displayStyle.equals("vertical")){
			colWidth = 100;
			isHorizontal = false;
		}else{
			colWidth = (int) 100/depthLevel;
			isHorizontal = true;
		}
		%>
			<aui:row>
				<%
				for(int i = 1; i <= depthLevel; i++){
					
					String elementName = name + i;
					
					String emptyOptionLabel = StringPool.BLANK;
					
					long selectedItem = 0;
					
					boolean itemEmptyOption = false;
					
					if(itemNames != null && itemNames.length >= i){
						elementName = itemNames[i -1];
					}
					
					if(selectedItems != null && selectedItems.length >= i){
						selectedItem = selectedItems[i - 1];
					}
					
					if(itemsEmptyOption != null && itemsEmptyOption.length >= i){
						itemEmptyOption = itemsEmptyOption[i - 1];
					}
					
					if(emptyOptionLabels != null && emptyOptionLabels.length >= i){
						emptyOptionLabel =  emptyOptionLabels[i - 1];
					}
					
					if(!isHorizontal){
						%>
							<aui:row>
								<aui:col id='<%="col_" + randomInstance + i %>' cssClass='<%=cssClass + "_" + i %>' width="<%=colWidth %>">
									<aui:select 
										name='<%=elementName %>' 
										onchange='<%=themeDisplay.getPortletDisplay().getNamespace() + randomInstance +"renderChildItems(this," + i + ",true)" %>'
										cssClass='<%=cssClass %>'
										inlineField="<%=inlineField %>"
										inlineLabel="<%=inlineLabel %>"
										label="<%=showLabel ? elementName : StringPool.BLANK %>"
									>
										<%
											if(!itemEmptyOption){
												%>
													<aui:option value="0"><%=emptyOptionLabel %></aui:option>
												<%
											}
										%>
									</aui:select>
								</aui:col>
							</aui:row>
						<%
					}else{
						%>
							<aui:col id='<%="col_" + randomInstance + i %>' cssClass='<%=cssClass + "_" + i %>' width="<%=colWidth %>">
							<aui:select 
								name='<%=elementName %>' 
								onchange='<%=themeDisplay.getPortletDisplay().getNamespace() + randomInstance + "renderChildItems(this," + i + ",true)" %>'
								cssClass='<%=cssClass %>'
								inlineField="<%=inlineField %>"
								inlineLabel="<%=inlineLabel %>"
								label="<%=showLabel ? elementName : StringPool.BLANK %>"
							>
								<%
									if(!itemEmptyOption){
										%>
											<aui:option value="0"><%=emptyOptionLabel %></aui:option>
										<%
									}
								%>
							</aui:select>
						</aui:col>
						<%
					}
					
				}
				%>
			</aui:row>
		<%
	}
%>


<aui:script>

	var localeCode = '<%=themeDisplay.getLanguageId() %>';
	
	var depthLevel<%=randomInstance %> = parseInt('<%=depthLevel %>');
	
	var strSelectItems = '<%=StringUtil.merge(selectedItems) %>';
	
	var strItemEmptyOption<%=randomInstance %> = '<%=StringUtil.merge(itemsEmptyOption) %>';
	
	var strEmptyOptionLabel = '<%=StringUtil.merge(emptyOptionLabels) %>';
	
	var selectItems<%=randomInstance %> = strSelectItems.split(",");
	
	var itemsEmptyOption<%=randomInstance %> = strItemEmptyOption<%=randomInstance %>.split(",");
	
	var emptyOptionLabels<%=randomInstance %> = strEmptyOptionLabel.split(",");
	
	var optionValueType<%=randomInstance %> = '<%=optionValueType %>';
	
	var rootDictItemsContainer =  null;
	
	var dictCollectionId;
	
	
	AUI().ready('aui-base','liferay-portlet-url','aui-io', function(A){
	
		rootDictItemsContainer = A.one('#<portlet:namespace/>col_<%=randomInstance %>1');
		
		var dictCollectionCode = '<%=dictCollectionCode %>';
		
		var initDictItemId = parseInt('<%=initDictItemId %>');
		
		var groupId = parseInt('<%=scopeGroupId %>');
		
		if(initDictItemId <= 0){
		
			Liferay.Service(
			'/opencps-portlet.dictcollection/get-dictcollection-by-gc',
				{
					groupId	: groupId,
					
					collectionCode : dictCollectionCode
				},function(obj) {
					
					if(obj){
						dictCollectionId = obj.dictCollectionId;
						
						<portlet:namespace/><%=randomInstance %>renderRootDataItemsByCollection(dictCollectionId);
					}
					
				}
			);
		}else{
			//Todo
		}
		
		/*rootDictItemsContainer.on('change', function(){
			alert(this.val());
		});*/
	});
	
	Liferay.provide(window, '<portlet:namespace/><%=randomInstance %>renderRootDataItemsByCollection', function(dictCollectionId) {
		var A = AUI();
		if(dictCollectionId){			
		
			Liferay.Service(
			  '/opencps-portlet.dictitem/get-dictitems-inuse-by-dictcollectionId_parentItemId',
			  {
			    dictCollectionId: dictCollectionId,
			    
			    parentItemId: 0
			  },
			  function(objs) {
			    <portlet:namespace/><%=randomInstance %>renderDataItems(objs, A.one('#<portlet:namespace/>col_<%=randomInstance %>1'), 1, false);
			  }
			);
		}
	});
	
	Liferay.provide(window, '<portlet:namespace/><%=randomInstance %>renderDataItems', function(objs, boundingBox, level, clearChild) {
		
		var itemName = '';
		
		var opts = '';
		
		var emptyOptionLabel = '';
		
		if(itemsEmptyOption<%=randomInstance %>.length >= parseInt(level)){
			itemEmptyOption = itemsEmptyOption<%=randomInstance %>[parseInt(level) - 1];
		}
		
		if(emptyOptionLabels<%=randomInstance %>.length >= parseInt(level)){
			emptyOptionLabel = emptyOptionLabels<%=randomInstance %>[parseInt(level) - 1];
		}
			
		if(itemEmptyOption == 'true'){
			if(optionValueType<%=randomInstance %> ==='code'){
				opts += '<option value="">' + Liferay.Language.get(emptyOptionLabel) +'</option>'
			}else{
				opts += '<option value="0">' + Liferay.Language.get(emptyOptionLabel) +'</option>'
			}
		}

		for(var i = 0; i < objs.length; i++){
		
			var opt = objs[i];
			
			var itemName = opt.itemName;
			
			var xmlDoc;
			
			if (window.DOMParser){
		    	parser=new DOMParser();
		    	xmlDoc=parser.parseFromString(itemName,"text/xml");
		  	}else{
		  		xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
			    xmlDoc.async=false;
			    xmlDoc.loadXML(itemName);
		  	}
		  	
		  	var localeItemNames = xmlDoc.getElementsByTagName("ItemName");
		  	
		  	var isDefaultLanguage = true;
		  	
		  	for(var n = 0; n < localeItemNames.length; n ++){
		  		var node = localeItemNames[n];
		  		var nodeAttr = node.getAttribute('language-id');
		  		if(nodeAttr === localeCode){
		  			isDefaultLanguage = false;
		  			itemName = node.childNodes[0].nodeValue;
		  			break;
		  		}
		  		
		  		if(isDefaultLanguage){
		  			itemName = localeItemNames[0].childNodes[0].nodeValue;
		  		}
		  	}
			
			var selectedItem = 0;
			
			var itemEmptyOption = false;
			
			if(selectItems<%=randomInstance %>.length >= parseInt(level)){
				selectedItem = selectItems<%=randomInstance %>[parseInt(level) - 1];
			}
		
			if(parseInt(opt.dictItemId) == selectedItem && clearChild == false){
				if(optionValueType<%=randomInstance %> ==='code'){
					opts += '<option value="' + opt.itemCode + '" selected="selected">' + itemName + '</option>'
				}else{
					opts += '<option value="' + opt.dictItemId + '" selected="selected">' + itemName + '</option>'
				}
				
			}else{
				if(optionValueType<%=randomInstance %> ==='code'){
					opts += '<option value="' + opt.itemCode + '">' + itemName + '</option>'
				}else{
					opts += '<option value="' + opt.dictItemId + '">' + itemName + '</option>'
				}
			}
		}
		
		boundingBox.one('select').empty();
		
		boundingBox.one('select').html(opts);
		
		<portlet:namespace/><%=randomInstance %>renderChildItems(boundingBox.one('select'), level, clearChild);
		
		<%-- if(parseInt(selectedItem) > 0 && clearChild == false){
			<portlet:namespace/><%=randomInstance %>renderChildItems(boundingBox.one('select'), level, clearChild);
		}else{
			<portlet:namespace/><%=randomInstance %>renderChildItems(boundingBox.one('select'), level, clearChild);
		} --%>
	});
	
	Liferay.provide(window, '<portlet:namespace/><%=randomInstance %>renderChildItems', function(evt, parentLevel, clearChild) {
	
		var A = AUI();
		
		var parent = A.one(evt);
		
		var level = parentLevel + 1;
		
		var parentItem = parent.val();
		
		var boundingBox = null;
		
				
		if(level <= depthLevel<%=randomInstance %>){
		
			boundingBox = A.one('#<portlet:namespace/>col_<%=randomInstance %>' + level);
			var data = null;
			if(optionValueType<%=randomInstance %> ==='id'){
				
				if(parentItem != 0){
					Liferay.Service(
					  '/opencps-portlet.dictitem/get-dictitems-by-parentId',
					  {
					    parentItemId: parentItem
					  },
					  function(objs) {
						  if(objs.length > 0){
						  	data = objs;
						  }
						  
						  if(data != null){
						  	
							<portlet:namespace/><%=randomInstance %>renderDataItems(objs, boundingBox, level, clearChild);
						  }else{
						  	
							for(var childLevel = level; childLevel <= depthLevel<%=randomInstance %>; childLevel++){
								var childBoundingBox = A.one('#<portlet:namespace/>col_<%=randomInstance %>' + childLevel);
								
								if(childBoundingBox){
									childBoundingBox.one('select').empty();
								}
							}
						  }
					});
				}
			}else{
				
				if(parentItem != ''){
					var itemId = 0;
						
					Liferay.Service(
					  '/opencps-portlet.dictitem/get-dictitem-inuse-by-code',
					  {
					    dictCollectionId: dictCollectionId,
					    itemCode: parentItem
					  },
					  function(obj) {
					  	
					    itemId = obj.dictItemId;
					    
						if(parseInt(itemId) > 0){
							Liferay.Service(
						  '/opencps-portlet.dictitem/get-dictitems-by-parentId',
						  {
						    parentItemId: itemId,
						  },
						  
						  function(objs) {
							  if(objs.length > 0){
							  	 data = objs;
							  }
							  
							  if(data != null){
								<portlet:namespace/><%=randomInstance %>renderDataItems(objs, boundingBox, level, clearChild);
							  }else{
								for(var childLevel = level; childLevel <= depthLevel<%=randomInstance %>; childLevel++){
									var childBoundingBox = A.one('#<portlet:namespace/>col_<%=randomInstance %>' + childLevel);
									
									if(childBoundingBox){
										childBoundingBox.one('select').empty();
									}
								}
							  }
						  });
						}
					  }
					);
				}
				
				
			}
		}
	});
	
</aui:script>