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

<%@ include file="init.jsp"%>

<%
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
					
					if(!isHorizontal){
						%>
							<aui:row>
								<aui:col id='<%="col_" + i %>' cssClass='<%=cssClass + "_" + i %>' width="<%=colWidth %>">
									<aui:select 
										name='<%=elementName %>' 
										onchange='<%=themeDisplay.getPortletDisplay().getNamespace() + "renderChildItems(this," + i + ",true)" %>'
										cssClass='<%=cssClass %>'
									>
										<%
											if(!itemEmptyOption){
												%>
													<aui:option value="0"></aui:option>
												<%
											}
										%>
									</aui:select>
								</aui:col>
							</aui:row>
						<%
					}else{
						%>
							<aui:col id='<%="col_" + i %>' cssClass='<%=cssClass + "_" + i %>' width="<%=colWidth %>">
							<aui:select 
								name='<%=elementName %>' 
								onchange='<%=themeDisplay.getPortletDisplay().getNamespace() + "renderChildItems(this," + i + ",true)" %>'
								cssClass='<%=cssClass %>'
							>
								<%
									if(!itemEmptyOption){
										%>
											<aui:option value="0"></aui:option>
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

<portlet:renderURL var="selectBoxRenderURL" windowState="<%=LiferayWindowState.EXCLUSIVE.toString() %>">
	<portlet:param name="mvcPath" value="/html/taglib/datamgt/ddr/selectbox_render.jsp"/>
</portlet:renderURL>

<aui:script>

	var selectBoxRenderURL = '<%=selectBoxRenderURL.toString()%>';
	
	var localeCode = '<%=themeDisplay.getLanguageId() %>';
	
	var depthLevel = parseInt('<%=depthLevel %>');
	
	var strSelectItems = '<%=StringUtil.merge(selectedItems) %>';
	
	var strItemEmptyOption = '<%=StringUtil.merge(itemsEmptyOption) %>';
	
	var selectItems = strSelectItems.split(",");
	
	var itemsEmptyOption = strItemEmptyOption.split(",");
	
	var rootDictItemsContainer =  null;
	
	AUI().ready('aui-base','liferay-portlet-url','aui-io', function(A){
	
		rootDictItemsContainer = A.one('#<portlet:namespace/>col_1');
		
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
						var dictCollectionId = obj.dictCollectionId;
						
						<portlet:namespace/>renderRootDataItemsByCollection(dictCollectionId);
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
	
	Liferay.provide(window, '<portlet:namespace/>renderRootDataItemsByCollection', function(dictCollectionId) {
	
		if(dictCollectionId){			
		
			Liferay.Service(
			  '/opencps-portlet.dictitem/get-dictitems-inuse-by-dictcollectionId_parentItemId',
			  {
			    dictCollectionId: dictCollectionId,
			    
			    parentItemId: 0
			  },
			  function(objs) {
			    <portlet:namespace/>renderDataItems(objs, rootDictItemsContainer, 1, false);
			  }
			);
		}
	});
	
	Liferay.provide(window, '<portlet:namespace/>renderDataItems', function(objs, boundingBox, level, clearChild) {
		
		var labelName = boundingBox.one('label').text().trim();
		
		var itemName = '';
		
		var opts = '';
		
		if(itemsEmptyOption.length >= parseInt(level)){
			itemEmptyOption = itemsEmptyOption[parseInt(level) - 1];
		}
			
		if(itemEmptyOption == 'true'){
			opts += '<option value="0"></option>'
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
		  	
		  	for(var n = 0; n < localeItemNames.length; n ++){
		  		var node = localeItemNames[n];
		  		var nodeAttr = node.getAttribute('language-id');
		  		if(nodeAttr === localeCode){
		  			itemName = nodeAttr.nodeValue;
		  			break;
		  		}
		  	}
			
			var selectedItem = 0;
			
			var itemEmptyOption = false;
			
			if(selectItems.length >= parseInt(level)){
				selectedItem = selectItems[parseInt(level) - 1];
			}
			
			
			
			if(parseInt(opt.dictItemId) == selectedItem && clearChild == false){
				opts += '<option value="' + opt.dictItemId + '" selected="selected">' + itemName + '</option>'
			}else{
				opts += '<option value="' + opt.dictItemId + '">' + itemName + '</option>'
				
			}
		}
		
		boundingBox.one('select').empty();
		
		boundingBox.one('select').html(opts);
		
		if(parseInt(selectedItem) > 0 && clearChild == false){
			
			<portlet:namespace/>renderChildItems(boundingBox.one('select'), level, clearChild);
		}
	});
	
	Liferay.provide(window, '<portlet:namespace/>renderChildItems', function(evt, parentLevel, clearChild) {
	
		var A = AUI();
		
		var parent = A.one(evt);
		
		//var parentLevel = parseInt(parent.attr('level'));
		
		var level = parentLevel + 1;
		
		var parentItemId = parent.val();
		
		var boundingBox = null;
		
		if(level <= depthLevel){
			boundingBox = A.one('#<portlet:namespace/>col_' + level);
			
			if(parentItemId != 0){
				Liferay.Service(
				  '/opencps-portlet.dictitem/get-dictitems-by-parentId',
				  {
				    parentItemId: parentItemId
				  },
				  function(objs) {
					  if(objs.length > 0){
					  	 <portlet:namespace/>renderDataItems(objs, boundingBox, level, clearChild);
					  }else{
					  	for(var childLevel = level; childLevel <= depthLevel; childLevel++){
							var childBoundingBox = A.one('#<portlet:namespace/>col_' + childLevel);
							
							if(childBoundingBox){
								childBoundingBox.one('select').empty();
							}
						}
					  }
				  });
			}else{
				
				for(var childLevel = level; childLevel <= depthLevel; childLevel++){
					var childBoundingBox = A.one('#<portlet:namespace/>col_' + childLevel);
					
					if(childBoundingBox){
						childBoundingBox.one('select').empty();
					}
				}
			}
		}
	});
	
</aui:script>