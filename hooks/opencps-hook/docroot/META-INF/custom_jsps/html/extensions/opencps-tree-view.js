Liferay.provide(window, 'buildTreeView', function(boundingBox, nameControl, data, arrayParam, portletURL, mvcPath, windowState, portletMode, url, active, nameSpace, isHidden) {
	var A = AUI();
	if(A.one("#"+boundingBox) != "undefined" &&
			A.one("#"+boundingBox) != null){
		var json = JSON.parse(arrayParam);
		var portletURL = Liferay.PortletURL.createURL(portletURL);
		portletURL.setParameter("mvcPath", mvcPath);
		portletURL.setWindowState(windowState); 
		portletURL.setPortletMode(portletMode);

		var treeView=new A.TreeViewDD({
			boundingBox: '#'+boundingBox,
			children: JSON.parse(data),
			 on: {
				 render: function(){
				 		var lis = A.all('.tree-node');
				 	}
		        }
		}).render();
		
		setTimeout(function(){
			var dataJson = {};
			for(j in json){
				dataJson[nameSpace+j] = json[j];
			}
			
			if(url != null){
				
				A.io.request(
						url,
					{
					    dataType : 'json',
					    data: dataJson,   
					    on: {
					    	start: function() {
					    		console.log("menu js sleep start!");
					    	},
	
					        success: function(event, id, obj) {
								var instance = this;
								var res = instance.get('responseData');
								
								if(res){
									
									var data = res.badge;
			                    	for(j in data){
			                    		var sub_key = data[j].code;
			                            var sub_val = data[j].counter;
			                            
			                            if( A.one('#'+sub_key) != "undefined" &&
			                            		A.one('#'+sub_key) != null){
			                            	
			                            	var elementOBJ = A.one('#'+sub_key);
			                            	
			                            	if( elementOBJ.getAttribute("id") === active){
			                            		elementOBJ.addClass("current");
			                				}
			                            	elementOBJ.appendChild("<span class='badge pull-right'>"+sub_val+"</span>");
			                            	
			                            	if(isHidden == 'true'){
			                            		
			                            		if(sub_val === '0'){
				                            		elementOBJ.setStyle('display', 'none');
				                            	}
			                            	}else{
			                            		
			                            	}
			                            	
			                            }
			                        }
								}
							},
						    end: function() {
						    	console.log("menu js sleep end!");
						    }
						}
					}
				);

			}else{
				var myTreeObj = A.one("#"+boundingBox);
				var allLI = myTreeObj.all( ".tree-node" );
				allLI.each(function (taskNode) {
					
					if(taskNode.getAttribute("id") === active){
						taskNode.addClass("current");
					}else{
						taskNode.removeClass("current");
					}
	             });
			}
		}, 1000);	
		
		treeView.after('lastSelectedChange', function(event) {
			var newCode = event.newVal.get('id');
					
			for(j in json){
				
				var sub_key = j;
	            var sub_val = json[j];
	            if(sub_key === nameControl && newCode.indexOf('yui_patched') < 0 ){
	            	portletURL.setParameter(sub_key, newCode);
	            }else  if(sub_key === nameControl && newCode.indexOf('yui_patched') === 0 ){
	            	portletURL.setParameter(sub_key, '');
	            }else{
	            	portletURL.setParameter(sub_key, sub_val);
	            }
	        }
				window.location = portletURL.toString();
		});
	}
},['aui-tree-view','aui-base','liferay-portlet-url','aui-io']);
