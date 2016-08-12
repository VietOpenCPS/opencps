Liferay.provide(window, 'openDialog', function(uri, id, title) {
	var opencpsDialog = Liferay.Util.openWindow(
		{
			dialog: {
				cache: false,
				cssClass: 'opencps-dialog',
				modal: true,
				width: $(window).width() * 0.8
			},
			cache: false,
			id: id,
			title: title,
			uri: uri
			
		},function(evt){
			
		}
	);
});


Liferay.provide(window, 'uploadDossierFile', function(e, uri, ns) {
	
	var A = AUI();

	var instance = A.one(e);
	
	var dossierId = instance.attr('dossier');
	
	var dossierPartId = instance.attr('dossier-part');
	
	var dossierFileId = instance.attr('dossier-file');
	
	var groupDossierPartId = instance.attr('group-dossier-part');
	
	var fileGroupId = instance.attr('file-group');
	
	var groupName = instance.attr('group-name');
	
	//Create render url
	var portletURL = Liferay.PortletURL.createURL(uri);
	portletURL.setParameter("dossierId", dossierId);
	portletURL.setParameter("dossierPartId", dossierPartId);
	portletURL.setParameter("groupName", groupName);
	portletURL.setParameter("dossierFileId", dossierFileId);
	portletURL.setParameter("fileGroupId", fileGroupId);
	portletURL.setParameter("groupDossierPartId", groupDossierPartId);
	portletURL.setParameter("modalDialogId", "upload-dossier-file");
	
	//Open dialog
	openDialog(portletURL.toString(), ns + 'upload-dossier-file', Liferay.Language.get("upload-dossier-file"));
});


Liferay.provide(window, 'viewDossierAttachment', function(e, uri){
	var A = AUI();
	var instance = A.one(e);
	var dossierFileId = instance.attr('dossier-file');
	A.io.request(
		uri.toString(),
		{
			dataType: 'json',
			data:{
				
			},
			on: {
				success: function(event, id, obj) {
					var response = this.get('responseData');
					if(response){
						var url = response.url;
						if(url != ''){
							window.open(url, '_blank');
						}else{
							alert(Liferay.Language.get("not-attachment-file"));
							return;
						}
					}
				}
			}
		}
	);
},['aui-io','liferay-portlet-url']);

Liferay.provide(window, 'addIndividualPartGroup', function(e, uri, ns) {

	var A = AUI();
	
	var instance = A.one(e);
	
	var dossierId = instance.attr('dossier');
	
	var dossierPartId = instance.attr('dossier-part');

	//Create render url
	var portletURL = Liferay.PortletURL.createURL(uri);
	portletURL.setParameter("dossierId", dossierId);
	portletURL.setParameter("dossierPartId", dossierPartId);
	portletURL.setParameter("modalDialogId", "add-individual-part-group");
	
	//Open dialog
	openDialog(portletURL.toString(), ns + 'add-individual-part-group', Liferay.Language.get("add-individual-part-group"));
});

Liferay.provide(window, 'dynamicForm', function(e, uri, ns){
	var A = AUI();
	
	var instance = A.one(e);
	
	var dossierId = instance.attr('dossier');
	
	var dossierPartId = instance.attr('dossier-part');
	
	var dossierFileId = instance.attr('dossier-file');
	
	var fileGroupId = instance.attr('file-group');
	
	var groupDossierPartId = instance.attr('group-dossier-part');
	
	var groupName = instance.attr('group-name');
	
	//Create render url
	var portletURL = Liferay.PortletURL.createURL(uri);
	portletURL.setParameter("dossierId", dossierId);
	portletURL.setParameter("dossierPartId", dossierPartId);
	portletURL.setParameter("groupName", groupName);
	portletURL.setParameter("dossierFileId", dossierFileId);
	portletURL.setParameter("fileGroupId", fileGroupId);
	portletURL.setParameter("groupDossierPartId", groupDossierPartId);
	portletURL.setParameter("modalDialogId", "dossier-dynamic-form");
	
	//Open dialog
	openDialog(portletURL.toString(), ns + 'dossier-dynamic-form', Liferay.Language.get("declaration-online"));
});

Liferay.provide(window, 'viewVersion', function(e, uri, ns) {
	
	var A = AUI();

	var instance = A.one(e);
	
	var dossierId = instance.attr('dossier');
	
	var dossierPartId = instance.attr('dossier-part');
	
	var dossierFileId = instance.attr('dossier-file');
	
	//Create render url
	var portletURL = Liferay.PortletURL.createURL(uri);
	portletURL.setParameter("dossierId", dossierId);
	portletURL.setParameter("dossierPartId", dossierPartId);
	portletURL.setParameter("dossierFileId", dossierFileId);
	portletURL.setParameter("modalDialogId", "view-dossier-file-version");
	
	//Open dialog
	openDialog(portletURL.toString(), ns + 'view-dossier-file-version', Liferay.Language.get("view-dossier-file-version"));
});


Liferay.provide(window, 'closeDialog', function(id, portletName) {
	
	var dialog = Liferay.Util.getWindow(id);
	
	if(portletName){
		var data = {
			'conserveHash': true
		};
		Liferay.Util.getOpener().Liferay.Portlet.refresh('#p_p_id_' + portletName);
	}
	dialog.destroy();
});


