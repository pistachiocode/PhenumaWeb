$(function(){
	
	$("#cy").cy(function(){
		var cy = this;
		
		cy.nodes(".style").not(".category").click(function(){
			var node = this;
			var style = node.data("style");
			
			cy.style( style );
		});
	});
	
});