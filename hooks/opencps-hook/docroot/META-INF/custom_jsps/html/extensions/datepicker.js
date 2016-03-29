AUI.add(
	'render-datepicker',
	function(A) {
	
		var RenderDatePicker = A.Component.create(
			{
				
				NAME: 'renderDatePicker',
				
				ATTRS: {
					trigger : null,
					lang : 'vi',
					mask : '%d/%m/%Y',
					defaultValue : '',
					zIndex : 1
					
				},
				
				prototype: {
					render : function(){
						var instance = this;
						
						var trigger = instance.get('trigger');
						
						var lang = instance.get('lang');
						
						var mask = instance.get('mask');
						
						var zIndex = instance.get('zIndex');
						
						var defaultValue = instance.get('defaultValue');
						
						
						AUI({ lang: lang }).use(
						  'aui-datepicker',
						  function(A) {
						    var datepicker = new A.DatePicker(
						      {
						        trigger: trigger,
						        mask: mask,
						        popover: {
						          toolbars: {
						            header: [[
						              {
						                icon:'icon-trash',
						                label: 'Clear',
						                on: {
						                  click: function() {
						                    datepicker.clearSelection();
						                  }
						                }
						              },
						              {
						                icon:'icon-globe',
						                label: 'Travel date',
						                on: {
						                  click: function() {
						                    datepicker.clearSelection();
						                    datepicker.selectDates(new Date());
						                  }
						                }
						              }
						            ]]
						          },
						          zIndex: 1
						        }
						      }
						    );
						  }
						);
					}
				}
			}
		);
		Liferay.RenderDatePicker = RenderDatePicker;
	},
	'',
	{
		requires: ['aui-datepicker']
	}
);