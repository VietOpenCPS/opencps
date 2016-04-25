function call_popup(url,strBtn,widthSize) {
	var output = strBtn.replace(/firstReplace/g, '<').replace(/lastReplace/g, '>').replace(/midReplace/g, '/').replace(/charReplace/g, '"');    

	var _title = output;
	AUI().use('aui-base','liferay-util-window','aui-io-plugin-deprecated',function(A){
		  
	    var login_popup= Liferay.Util.Window.getWindow(
	                {
	                    dialog: {
	                        centered: true,
	                        constrain2view: true,
	                        modal: true,
	                        resizable: false,
	                        width: $(window).width() * widthSize
	                    }
	                }).plug(A.Plugin.DialogIframe,
	                     {
	                     autoLoad: true,
	                     uri: url
	                     }).render();
	             login_popup.show();
	                     login_popup.titleNode.html(_title);
	                    
	 
	 });
}