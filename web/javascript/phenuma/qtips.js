$(function(){	
	
	$("[tooltip]").qtip({
		content: {
			attr: "tooltip"
		},
		show: {
			event: "mouseenter",
			delay: 0,
			effect: false
		},
		position: {
			my: "top center",
			at: "bottom center",
			viewport: true,
			adjust: {
				method: "shift"
			}
		},
		style: {
			classes: "ui-tooltip-tipsy ui-tooltip-shadow"
		}
	});
	
	$("body").append('<div id="qtip-div"></div>');
	var $qtipDiv = $("#qtip-div");
	
	$("#cy").cy(function(){
		var cy = this;
		
		cy.nodes().live("mouseover", function(){
			var node = this;
			var text = node.data("tooltip");
			var p = node.renderedPosition();
			var d = node.renderedDimensions();
			var offset = $("#cy").offset();
			
			if( text == null || text == "" || node.grabbed() ){
				return;
			}
			
			$qtipDiv.css({
				left: offset.left + p.x - d.width/2,
				top: offset.top + p.y - d.height/2,
				height: d.height,
				width: d.width,
				"border-radius": d.width
			});
			
			$qtipDiv.qtip({
				content: {
					text: text
				},
				show: {
	                delay: 0,
	                event: false,
	                ready: true,
	                effect: false
	            },
	            hide: {
	            	event: "hideqtip"
	            },
				position: {
					my: "top center",
					at: "bottom center",
					viewport: true,
					adjust: {
						method: "shift"
					}
				},
				style: {
					classes: "ui-tooltip-tipsy ui-tooltip-shadow"
				}
			});
		}).live("mouseout mousedown", function(){
			$qtipDiv.trigger("hideqtip");
		});
		
		cy.bind("zoom pan", function(){
			$qtipDiv.trigger("hideqtip");
		});
	});

});