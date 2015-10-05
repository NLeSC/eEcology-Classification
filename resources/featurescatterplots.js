var width = 960,
    size = 200,
    padding = 19.5,
    outerPadding = 10;

var x = d3.scale.linear()
    .range([padding / 2, size - padding / 2]);

var y = d3.scale.linear()
    .range([size - padding / 2, padding / 2]);

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom")
    .ticks(4);

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left")
    .ticks(4);

var color = d3.scale.category10();

var defaultFeatureCount = 6;
var defaultMaxPoints = 20000;
var subsamplePercentage = 100;
var features = [];
var selectedFeatures = [];
var selectedLabels = [];
var allData = [];
var selectedData = [];
var data = [];
var domainByFeature = {};
var schema = {};

var onPercentageChange = function(){
    var percentageInput = document.getElementById("subsamplepercentage");
    var percentage = percentageInput.value;    
    var newSubsamplePercentage = Math.max(0, Math.min(100, percentage));
    if (newSubsamplePercentage == subsamplePercentage){
        return;
    }
    subsamplePercentage = newSubsamplePercentage;
    percentageInput.value = subsamplePercentage;
    updatePlots();
};

var onFeatureSelectionChange = function(event){
    if (this.checked){
        selectedFeatures = features.filter(function(f){return selectedFeatures.indexOf(f) != -1 || f == event;});
    } else {        
        selectedFeatures = selectedFeatures.filter(function(f){return f != event;});
    }
    setDefaultSubsamplePercentage();
    updatePlots();
}

var onLabelSelectionChange = function(event){        
    if (this.checked){
        selectedLabels = Object.keys(schema).filter(function(f){return selectedLabels.indexOf(f) != -1 || f == event;});
    } else {        
        selectedLabels = selectedLabels.filter(function(f){return f != event;});
    }
    setSelectedData();
    setDefaultSubsamplePercentage();
    updatePlots();
}

var loadAndHandleData = function(){
	d3.csv("data/featurescomplete.csv", function(error, loadedData) {
		if(error != null){
			d3.select("#loaderror").classed("hidden", false);	
		}
		else{
			d3.select("#content").classed("hidden", false);
		}
		d3.select("#loading").classed("hidden", true);
		
		allData = loadedData;
		updateFeatures();
		setSelectedData();
		setDefaultSubsamplePercentage();
		addFeatureSelectionControls();	
		updatePlots();
	});
}

var updatePlots = function(){
	updateData();    
	addPlots();
}

d3.json("data/schema.json", function(error, loadedSchema){	
	schema = loadedSchema;
	var allLabelIds = Object.keys(schema);	
	selectedLabels = allLabelIds;
	var labels = d3.select("#labellist").selectAll('input').data(allLabelIds);  	
	var listItems = labels.enter().append("li")
	listItems.append("span")
		.attr("class", "fa fa-circle")
		.style("color", function(d) { 
			var s = schema[d];
			var c = getRgbColorString(s);
			return c;
		});
    listItems    
		.append("label")
		.text(function(d) { 
			return schema[d].description;
		})		
		.append("input") 
		.attr("type", "checkbox")  
		.on("change", onLabelSelectionChange)    
		.attr("checked", "true");	
	loadAndHandleData();       
});

var getRgbColorString = function(labelDetail){
	var result = "rgb(";
	result += get256Color(labelDetail.colorR) + ", ";
	result += get256Color(labelDetail.colorG) + ", ";
	result += get256Color(labelDetail.colorB);
	result += ")";	
	return result;
}

var get256Color = function(floatColor){
	return Math.floor(255 * floatColor);
}

var setSelectedData = function(){
	selectedData = allData.filter(function(d){return selectedLabels.indexOf(d.class_id) != -1; } );
}

var setDefaultSubsamplePercentage = function(){
    subsamplePercentage = Math.min(100, Math.floor(100 * defaultMaxPoints / (selectedData.length * selectedFeatures.length * selectedFeatures.length)));
    var percentageInput = document.getElementById("subsamplepercentage").value = subsamplePercentage;
}

var addFeatureSelectionControls = function(){	
    var trackers = d3.select("#featurelist").selectAll('input').data(features);  
    trackers.enter()
    .append("li")
    .append("label")
    .text(function(d) { 
        return d;
    })
    .append("input")
    .attr("type", "checkbox")  
    .on("change", onFeatureSelectionChange)
    .filter(function(checkBox) { 
        return selectedFeatures.indexOf(checkBox) != -1; })
    .attr("checked", "true");
}

var updateFeatures = function(){
    features = d3.keys(allData[0]).filter(function(d) { return d != "device_info_serial" && 
		  d != "date_time" && 
		  d != "lon" && 
		  d != "lat" && 
		  d != "alt" && 
		  d != "class_id" && 
		  d != "set" &&
		  d != "label_description" &&
		  d != "color_r" &&
		  d != "color_g" &&
		  d != "color_b" ; });    
    selectedFeatures = features.slice(0, defaultFeatureCount);
}

var updateData = function(){  
    data = selectedData.filter(function(d){return 100 * Math.random() < subsamplePercentage;});
};

var addPlots = function(){
	selectedFeatures.forEach(function(feature) {
		domainByFeature[feature] = d3.extent(data, function(d) { 
			return parseFloat(d[feature]); });
	});

	xAxis.tickSize(size * selectedFeatures.length);
	yAxis.tickSize(-size * selectedFeatures.length);

	var brush = d3.svg.brush()
		.x(x)
		.y(y)
		.on("brushstart", brushstart)
		.on("brush", brushmove)
		.on("brushend", brushend);

	d3.select("#plots").selectAll("svg").remove();
  
	var svg = d3.select("#plots").append("svg")
		.attr("width", size * (selectedFeatures.length + padding) + outerPadding)
		.attr("height", size * (selectedFeatures.length + padding) + outerPadding)
		.append("g")
		.attr("transform", "translate(" + (padding + outerPadding) + "," + padding / 2 + ")");

	svg.selectAll(".x.axis")
		.data(selectedFeatures)
		.enter().append("g")
		.attr("class", "x axis")
		.attr("transform", function(d, i) { return "translate(" + (i) * size + ",0)"; })
		.each(function(d) { x.domain(domainByFeature[d]); d3.select(this).call(xAxis); });

	svg.selectAll(".y.axis")
		.data(selectedFeatures)
		.enter().append("g")
		.attr("class", "y axis")
		.attr("transform", function(d, i) { return "translate(0," + i * size + ")"; })      
		.each(function(d) { y.domain(domainByFeature[d]); d3.select(this).call(yAxis); });

	var cell = svg.selectAll(".cell")
		.data(cross(selectedFeatures, selectedFeatures))
		.enter()
		.append("g")
		.attr("class", "cell")
		.attr("transform", function(d) { return "translate(" + d.i * size + "," + d.j * size + ")"; })
		.on("mouseover", function(d) {      
			var boundingBox = this.getBoundingClientRect();
			var left = (boundingBox.left + window.pageXOffset + 40);
			var top = (boundingBox.top + window.pageYOffset - 20)
			tooltip.transition()        
				.duration(200)      
				.style("opacity", .9);      
			tooltip .html("x: " + d.x + "<br/> y: " + d.y)  
				.style("left", left + "px")     
				.style("top", top + "px");    
		})                  
		.on("mouseout", function(d) {       
			tooltip.transition()        
				.duration(500)      
				.style("opacity", 0);   
		})
        .each(plot);

	// Titles for the diagonal.
	cell.filter(function(d) { return d.i === d.j; }).append("text")
		.attr("x", padding)
		.attr("y", padding)
		.attr("dy", ".71em")
		.text(function(d) { return d.x; });

	cell.call(brush);

	function plot(p) {
		var cell = d3.select(this);

		x.domain(domainByFeature[p.x]);
		y.domain(domainByFeature[p.y]);

		cell.append("rect")
			.attr("class", "frame")
			.attr("x", padding / 2)
			.attr("y", padding / 2)
			.attr("width", size - padding)
			.attr("height", size - padding);

		cell.selectAll("circle")
			.data(data)
			.enter()
			.append("circle")
			.attr("cx", function(d) { return x(d[p.x]); })
			.attr("cy", function(d) { return y(d[p.y]); })
			.attr("r", 1.5)			
			.style("fill", function(d) { return getRgbColorString(schema[d.class_id]) });
	}

	var brushCell;

	// Clear the previously-active brush, if any.
	function brushstart(p) {
		if (brushCell !== this) {
			d3.select(brushCell).call(brush.clear());
			x.domain(domainByFeature[p.x]);
			y.domain(domainByFeature[p.y]);
			brushCell = this;
		}
	}

	// Highlight the selected circles.
	function brushmove(p) {
		var e = brush.extent();
		svg.selectAll("circle").classed("hidden-circle", function(d) {
			return e[0][0] > d[p.x] || d[p.x] > e[1][0]
				|| e[0][1] > d[p.y] || d[p.y] > e[1][1];
		});
	}

	// If the brush is empty, select all circles.
	function brushend() {
		if (brush.empty()) svg.selectAll(".hidden-circle").classed("hidden-circle", false);
	}

	function cross(a, b) {
		var c = [], n = a.length, m = b.length, i, j;
		for (i = -1; ++i < n;) for (j = -1; ++j < m;) c.push({x: a[i], i: i, y: b[j], j: j});
			return c;
		}

		d3.select(self.frameElement).style("height", size * selectedFeatures.length + padding + 20 + "px");
}

var tooltip = d3.select("body").append("div")   
    .attr("class", "tooltip")               
    .style("opacity", 0);
