

var color = d3.scale.category10();

var allData = [];
var schema = {};
var model = null;

var floatFormat = d3.format(".2f");
var percentageFormat = d3.format(".1f");


d3.text("data/classifierdescription.txt", function(error, description){
	if(error != null){
		d3.select("#loaderror").classed("hidden", false);	
	}
	else{
		d3.select("#content")
		.classed("hidden", false)		
	    .append("pre")
	    .html(description);
	}
	d3.select("#loading").classed("hidden", true);
});
