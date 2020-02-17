$(function(){
	
    var style = {
		selectors: {
			"node": {
				fillColor: "#555",
				height: 20,
				width: 20,
				labelFontSize: 8,
				labelFillColor: "#000",
				labelValign: "bottom",
				labelHalign: "middle",
				labelText: {
					passthroughMapper: "name"
				},
                borderColor: "white"
			},
			
			"node:selected": {
				borderWidth: 2,
				borderColor: "yellow"
			},
			
			".gene": {
				fillColor: "#1f62b6",
				labelOutlineColor: "#1f62b6"
			},

			
			".phensim": {
				lineColor: "#AAAAAA" //blue
			},
			
            ".funsim": {
                lineColor: "#81F781" //green
            },

            ".ppi": {
                lineColor: "#F79F81" //orange
            },

            ".metabolic": {
                lineColor: "#8181F7"
            },

            ".inferred": {
                lineColor: "#F781BE"
            },

			".semi": {
				opacity: 0.5
			}
		}
	};


var network = { nodes: [ { data: { id: "SEC23A", name: "SEC23A"}, classes:"gene"}, 
                         { data: { id: "GJA8", name: "GJA8"}, classes:"gene"}, 
                         { data: { id: "CRYBA1", name: "CRYBA1"}, classes:"gene"}, 
                         { data: { id: "TGFB3", name: "TGFB3"}, classes:"gene"}, 
                         { data: { id: "HSF4", name: "HSF4"}, classes:"gene"}, 
                         { data: { id: "NHS", name: "NHS"}, classes:"gene"}, 
                         { data: { id: "DSG2", name: "DSG2"}, classes:"gene"}, 
                         { data: { id: "ACTC1", name: "ACTC1"}, classes:"gene"}], 
               edges: [ { data: {source: "CRYBA1", target: "HSF4", weight: 4.948}, classes:"phensim"}, 
                        { data: {source: "CRYBA1", target: "SEC23A", weight: 3.226}, classes:"phensim"}, 
                        { data: {source: "CRYBA1", target: "GJA8", weight: 3.935}, classes:"phensim"}, 
                        { data: {source: "CRYBA1", target: "NHS", weight: 3.46}, classes:"phensim"}, 
                        { data: {source: "ACTC1", target: "DSG2", weight: 3.015}, classes:"phensim"},   
                        { data: {source: "ACTC1", target: "TGFB3", weight: 3.058}, classes:"phensim"}]};
	
$("#cy").cy({

        elements:  network, 

        style: style,
		
        ready: function(){
			window.cy = this; // just for debugging in the console
			
			cy.container().cytoscapewebPanzoom();

		}
	
	});
	
});