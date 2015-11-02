

var color = d3.scale.category10();

var allData = [];
var schema = {};
var model = null;

var floatFormat = d3.format(".2f");
var percentageFormat = d3.format(".1f");


d3.json("data/teststatistics.json", function(error, newModel){
	if(error != null){
		d3.select("#loaderror").classed("hidden", false);	
	}
	else{
		d3.select("#content").classed("hidden", false);
	}
	d3.select("#loading").classed("hidden", true);
	
	model = newModel;
	var loadedSchema = model.labelSchema;
	schema = loadedSchema;
	var allLabelIds = Object.keys(schema);	
	selectedLabels = allLabelIds;
	var labels = d3.select("#labellist").selectAll('li').data(allLabelIds);  	
	var listItems = labels.enter().append("li")	
    listItems    
		.append("label")
		.text(function(d) { 
			return schema[d].description;
		});		
	listItems.append("span")
		.attr("class", "fa fa-circle")
		.style("color", function(d) { 
			var s = schema[d];
			var c = getRgbColorString(s);
			return c;
		});
	
	var confusionMatrix = model.confusionMatrix;	
	
	d3.select("#errorrate").text(model.errorRate);
	d3.select("#instancescorrect").text(model.instancesCorrect);
	d3.select("#instancesincorrect").text(model.instancesIncorrect);
	d3.select("#percentagecorrect").text(percentageFormat(model.percentageCorrect));
	d3.select("#percentageincorrect").text(percentageFormat(model.percentageIncorrect));
	d3.select("#kappa").text(floatFormat(model.kappa));	
	var table = d3.select("#confusionMatrix");
	var tableHead = table
		.append("thead");
	var classTypeRow = tableHead.append("tr");
	classTypeRow.append("th").text("Actual class");
	classTypeRow.append("th").attr("colspan",100).text("Predicted class");
	var headerRow = tableHead		
		.append("tr");

	headerRow.append("th")
			.attr("class", "col-md-1");

	var headers = headerRow
		.selectAll("td")
		.data(allLabelIds);
	headers
		.enter()
		.append("th")
		.text(function(d){ 
			return schema[d].description;
		});
	headers
		.append("span")
		.attr("class", "fa fa-circle")
		.style("color", function(d) { 
			var s = schema[d];
			var c = getRgbColorString(s);
			return c;
		});
	headerRow
		.append("th")
		.attr("class", "col-md-1")
		.append("span")
		.text("Recall");
		
	headers
		.attr("class", "col-md-1");
	
	var body = table	
		.append("tbody");
	var rows = body
		.selectAll("tr")
		.data(confusionMatrix)
		.enter()
		.append("tr");
	rows
		.each(function(row, iRow){
			var tableRow = d3.select(this);
			var label = tableRow
				.append("td")
				.append("label");
			label
				.text(function(d){
					return schema[iRow].description;
				});
			
			label
				.append("span")
				.attr("class", "fa fa-circle")
				.style("color", function(d) { 
					var s = schema[iRow];
					var c = getRgbColorString(s);
					return c;
				});
			
			var cells = tableRow
				.selectAll("th")
				.data(row);
			cells
				.enter()
				.append("td")
				.attr("class",function(d,iCell){
						if (iCell == iRow) {
							return "info";
							}
						if (d > 0) {
							return "danger";
						}
						return "success";
					})
				.append("a")
				.text(function(d){
					return d;
				})
			    .attr("href",function(d, iCell){ if (d > 0 && iCell != iRow) return "misclassifications.html?actcls=" + iRow + "&predcls=" + iCell; });	
			
			var toFloatNoNan = function(d) {
				if(isNaN(d)){
					return "-";
				}
				return floatFormat(d);
			};
			tableRow
				.append("td")
				.text(toFloatNoNan(model.recallVector[iRow]));
		});	
	
	var precisionRow = body.append("tr");
	precisionRow
		.append("td")
		.append("label")
		.text("Precision");
	precisionRow
		.selectAll("th")
		.data(model.precisionVector)
		.enter()
		.append("td")
		.text(function(d) {
				if(isNaN(d)){
					return "-";
				}
				return floatFormat(d);
			});
		
		

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
