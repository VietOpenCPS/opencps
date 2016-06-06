Liferay.provide(window, 'openDialog', function(uri, id, title) {
	var opencpsDialog = Liferay.Util.openWindow(
		{
			dialog: {
				cache: false,
				cssClass: 'opencps-dialog',
				modal: true,
				
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
	
	var fileGroupId = instance.attr('file-group');
	
	var groupName = instance.attr('group-name');
	
	//Create render url
	var portletURL = Liferay.PortletURL.createURL(uri);
	portletURL.setParameter("dossierId", dossierId);
	portletURL.setParameter("dossierPartId", dossierPartId);
	portletURL.setParameter("groupName", groupName);
	portletURL.setParameter("dossierFileId", dossierFileId);
	portletURL.setParameter("fileGroupId", fileGroupId);
	portletURL.setParameter("modalDialogId", "upload-dossier-file");
	
	//Open dialog
	openDialog(uri, ns + 'upload-dossier-file', Liferay.Language.get("upload-dossier-file"));
});

Liferay.provide((window, 'dynamicForm'), function(e, uri, ns){
	var A = AUI();
});