AUI.add(
	'invoke-dictcollection-by-gc',
	function(A) {
	
		var InvokeDictCollectionBy_GC = A.Component.create(
			{
				//AUGMENTS: [Liferay.PortletBase],

				//EXTENDS: A.Base,

				NAME: 'invokeDictCollectionByGC',
				
				ATTRS: {
					boundingBox : null,
					collectionCode : '',
					groupId	: 0,
					ns : '',
					responseData : null	
				},
			
				prototype: {
				
				_invokeService : function(callback){
					
					var instance = this;
					
					var boundingBox = instance.get('boundingBox');
					
					var collectionCode = instance.get('collectionCode');

					var groupId = instance.get('groupId');
					
					var ns = instance.get('ns');
					
					var responseData = instance.get('responseData');
					
					Liferay.Service(
					'/opencps-portlet.dictcollection/get-dictcollection-by-gc',
						{
							groupId	: groupId,
							collectionCode : collectionCode
						},function(obj) {
							instance._attrs.responseData = JSON.stringify(obj);
							if (callback) {
								//callback.apply(instance, arguments);
							}
						}
					);
				},invoke : function(callback){
					var instance = this;
					instance._invokeService(function(){});
				}
			}
		});
		
		Liferay.InvokeDictCollectionBy_GC = InvokeDictCollectionBy_GC;
	},
	'',
	{
		requires: ['aui-base']
	}
);


AUI.add(
	'render-dictitems-by-dictcollectionId',
	function(A) {
	
		var RenderDictItemsBy_DictCollectionId = A.Component.create(
			{
				//AUGMENTS: [Liferay.PortletBase],

				//EXTENDS: A.Base,

				NAME: 'renderDictItemsByDictCollectionId',
				
				ATTRS: {
					boundingBox : null,
					collectionId : 0,
					depthLevel : 0,
					groupId : 0,
					ns : '',	
					selectedItem : 0,
					url : ''
				},
				
				prototype: {
					render : function(){
						var instance = this;
						
						var boundingBox = instance.get('boundingBox');
						
						var collectionId = instance.get('collectionId');
						
						var groupId = instance.get('groupId');
						
						var depthLevel = instance.get('depthLevel');
						
						var ns = instance.get('ns');
						
						var selectedItem = instance.get('selectedItem');
						
						var url = instance.get('url');
						
						Liferay.Service(
						'/opencps-portlet.dictitem/get-dictitems-by-dictcollectionId',
						{
							dictCollectionId : collectionId
						},function(obj) {
							A.io.request(
								url,
								{
								    dataType : 'json',
								    
								    data :{    	
								    	data : JSON.stringify(obj),
								    	depthLevel : depthLevel,
								    	ns : ns,
								    	selectedItem : selectedItem
								    },
								    
								    on: {
								        success : function(event, id, obj) {
											var instance = this;
											var res = instance.get('responseData');
											
											if(boundingBox){
												boundingBox.empty();
												boundingBox.html(res);
											}
												
										},error : function(){}
									}
								}
							);}
						);
					}
				}
			}
		);
		Liferay.RenderDictItemsBy_DictCollectionId = RenderDictItemsBy_DictCollectionId;
	},
	'',
	{
		requires: ['aui-base','liferay-portlet-url','aui-io']
	}
);


AUI.add(
	'invoke-dictitem-by-pk',
	function(A) {
	
		var InvokeDictItemBy_PK = A.Component.create(
			{
		
				NAME: 'invokeDictItemByPK',
				
				ATTRS: {
					dictItemId : 0,
					ns : ''
				},
				
				prototype: {
					render : function(){
						var instance = this;
						
						var dictItemId = instance.get('dictItemId');
						
						var ns = instance.get('ns');
						
						Liferay.Service(
						'/opencps-portlet.dictitem/get-dictitem-by-pk',
						{
							dictItemId : dictItemId
						},function(obj) {
							  instance._attrs.responseData = JSON.stringify(obj);
						});
					}
				}
			}
		);
		Liferay.InvokeDictItemBy_PK = InvokeDictItemBy_PK;
	},
	'',
	{
		requires: ['aui-base']
	}
);


AUI.add(
	'render-dictitems-by-parentId',
	function(A) {
	
		var RenderDictItemBy_ParentId = A.Component.create(
			{
				//AUGMENTS: [Liferay.PortletBase],

				//EXTENDS: A.Base,

				NAME: 'renderDictItemByParentId',
				
				ATTRS: {
					boundingBox : null,
					depthLevel : 0,
					ns: '',
					parentItemId : 0,
					selectedItem : 0,
					url: ''
				},
				
				prototype: {
					render : function(){
						var instance = this;
						
						var boundingBox = instance.get('boundingBox');
						
						var depthLevel = instance.get('depthLevel');
												
						var ns = instance.get('ns');
						
						var parentItemId = instance.get('parentItemId');
						
						var selectedItem = instance.get('selectedItem');
						
						var url = instance.get('url');
						
						Liferay.Service(
						  '/opencps-portlet.dictitem/get-dictitems-by-parentId',
						  {
						    parentItemId: parentItemId
						  },
						  function(obj) {
							  A.io.request(
								url,
								{
								    dataType : 'json',
								    data:{    	
								    	data : JSON.stringify(obj),
								    	depthLevel: depthLevel,
								    	ns : ns,
								    	parentItemId : parentItemId,
								    	selectedItem : selectedItem
								    },   
								    on: {
								        success: function(event, id, obj) {
											var instance = this;
											var res = instance.get('responseData');
											
											if(boundingBox){
												boundingBox.empty();
												boundingBox.html(res);
											}
												
										},
								    	error: function(){}
									}
								});
						  	}
						);
					}
				}
			}
		);
		Liferay.RenderDictItemBy_ParentId = RenderDictItemBy_ParentId;
	},
	'',
	{
		requires: ['aui-base','liferay-portlet-url','aui-io']
	}
);
