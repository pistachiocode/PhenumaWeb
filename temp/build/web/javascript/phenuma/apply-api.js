$(function(){
	
	$("#cy").cy(function(){
		cy.$("node[code]")
			.each(function(){ // make tooltips for each [code] node
				var fn = "" + this.data("code");
				var lines = fn.split("\n");
				var code = "";
				
				for(var i = 1; i < lines.length - 1; i++){
					code += lines[i] + "\n";
				}
				
				this.data("tooltip", '<pre>' + code + '</pre>');
			})
			.not("[?immediate]").bind("click", function(){
				// trigger code on click if not immediate
				this.data("code").apply(this, []);
			});
			
		// apply code right away to immediate == true
		cy.$("node[code][?immediate]").each(function(){
			this.data("code").apply(this, []);
		});

        //apply width from the score (weight) of the edges
        cy.$("edge").each(function(){
            this.bypass("width", this.data("weight"));    
        });

				
	});
	
});