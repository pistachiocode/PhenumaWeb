$(function(){
	
	var layoutRunning = false;
	
	$("#cy").cy(function(){
		var cy = this;
		
		cy.bind("layoutstart", function(){
			layoutRunning = true;
		}).bind("layoutstop", function(){
			layoutRunning = false;
		});
		
		cy.elements(".layout").not(".category").bind("click", function(){
			if( layoutRunning ){
				return;
			}
			
			var layout = this.data("id");
			var options = this.data("options");
			
			cy.layout( $.extend(true, {}, options, { name: layout }) );
		});
	});
	
});