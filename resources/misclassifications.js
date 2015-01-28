function segmentComparator(a,b){
	var idResult = idComparator(a,b);
	if (idResult != 0){
		return idResult;
	}			
			
	return(timeComparator(a,b));		
}

function selectionIdComparator(a,b){
	if (a.id == b.id){
		return 0;
	}
	return a.id < b.id ? -1 : 1;
}

function segIdComparator(a,b){
	var idResult = idComparator(a,b);
	if (idResult != 0){
		return idResult;
	}			
}			
function idComparator(a,b){
	if (a.deviceId == b.deviceId){
		return 0;
	}
	return a.deviceId < b.deviceId ? -1 : 1;
}

function segLabelComparator(a,b){
	var lResult = labelComparator(a,b);
	if (lResult != 0){
		return lResult;
	}			
}	

function labelComparator(a,b){
	var al = a.labelDetail.labelId;
	var bl = b.labelDetail.labelId;
	
	if (al == bl){
		return 0;
	}
	return al < bl ? -1 : 1;
}

function segPredictionComparator(a,b){
	var pResult = predictionComparator(a,b);
	if (pResult != 0){
		return pResult;
	}			
}			

function predictionComparator(a,b){
	var al = a.predictedLabelDetail.labelId;
	var bl = b.predictedLabelDetail.labelId;
	if (al == bl){
		return 0;
	}
	return al < bl ? -1 : 1;
}	

function timeComparator(a,b){
	var timea = a.timeStamp;
	var timeb = b.timeStamp;
	
	if (timea == timeb){
		return 0;
	}
	return timea < timeb ? -1 : 1;
}

function createCheckboxesFromArray(array, reference, toggleCallback){				
	var checkBoxes = d3.select(reference).selectAll('input').data(array);
	checkBoxes.enter()
        .append("li")
	    .append("label")
	    	.text(function(d) { 
	    		return d.descr; 
    		})
		.append("input")
			.attr("type", "checkbox")
			.on('change', toggleCallback)
			.property('checked', function(d) {
				return d.checked;
			})
    ;
}	

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



function createLabelCheckBoxGroup(clasArray, queryKey, listSelector){
	createLabelCheckboxesFromArray(clasArray, listSelector, schema, onToggle);	
	var clas = getParameter(queryKey);	
	if (clas !== null) {
		checkCheckboxesFromQuerystr(clas, listSelector, schema);
	} 
	else {
		checkAllCheckboxes(listSelector, schema);
	}
}

function createLabelCheckboxesFromArray(array, reference, schema, toggleCallback){				
	var checkBoxes = d3.select(reference).selectAll('input').data(array);
	checkBoxes.enter().append("li")
	checkBoxes.append("span")
			.attr("class", "fa fa-circle")
			.style("color", function(d) {
				var s = schema[d.id];
				var c = getRgbColorString(s);
				return c;
			})
	checkBoxes.append("label")
	    	.text(function(d) { 
	    		return d.descr; 
    		})
		.append("input")
			.attr("type", "checkbox")
			.on('change', toggleCallback)
			.property('checked', function(d) {
				return d.checked;
			})	
	 
    ;
}	

function checkCheckboxesFromQuerystr(classid, reference, schema) {
	var index = + classid + 1;	
	var checkBoxes = d3.select(reference).selectAll('input');
	checkBoxes.property('checked', function(d) {
		if (schema[d.id].labelId == index) {
			d.checked = true;			
		}
		return d.checked;
	});
}

function checkAllCheckboxes(reference, schema) {	
	var checkBoxes = d3.select(reference).selectAll('input');
	checkBoxes.property('checked', function(d){
		d.checked = true;
		return d.checked;
	});
}

function getParameter(paramName) {
	  var searchString = window.location.search.substring(1),
	      i, val, params = searchString.split("&");

	  for (i=0;i<params.length;i++) {
	    val = params[i].split("=");
	    if (val[0] == paramName) {
	      return val[1];
	    }
	  }
	  return null;
	}

function onToggle(d) {
	d.checked = !d.checked;
	drawSelection();
}

// show the selections
function drawSelection() {
	// find the checked tracker ids
	var checkedTrackers = devices.filter(function(d) {return d.checked;	});
	var checkedTrackerIds = d3.set(checkedTrackers.map(function(d) {return d.descr;	}));
	// find the checked labels
	var checkedLabels = labels.filter(function(l) {	return l.checked;})
	var checkedLabelIds = d3.set(checkedLabels.map(function(l) {return l.descr;	}));
	// find the checked predicted classes
	var checkedPredictions = predictions.filter(function(p) {return p.checked;})
	var checkedPredictionIds = d3.set(checkedPredictions.map(function(p) {return p.descr;}));

	var segments = allSegments.filter(function(segment) {
		return (checkedTrackerIds.has(segment.deviceId)) && (checkedLabelIds.has(segment.labelDetail.description)) && (checkedPredictionIds.has(segment.predictedLabelDetail.description));
	});
	
	d3.selectAll('svg').remove();
	
	drawSegments(segments);
}

function getDeviceIds(data) {
  var map ={}; i=-1; dataLength = data.length;
  while (++i < dataLength) map[data[i].deviceId]= 1;
  var deviceIds = d3.keys(map);
  return deviceIds.map(function(d, i) {
	  return {
		  id: d*1,
		  checked: true,
		  descr: d
	  };
  });
}

function getLabelIds(data) {
	  var map ={}; i=0; dataLength = Object.getOwnPropertyNames(data).length;
	  while (++i < dataLength) {
		  item = data[i].description;
		  value = data[i].labelId;
		  map[item]=value;
	  }					  
	  var label_descr = d3.keys(map);
	  var label_ids= d3.values(map);
	  return label_descr.map(function(d,i) {
		  return {
			  id: label_ids[i],
			  checked: false,
			  descr: d 
		  };
	  });
	}

function getPredictionIds(data) {
	  var map ={}; i=0; dataLength = Object.getOwnPropertyNames(data).length;
	  while (++i < dataLength) {
		  item = data[i].description;
		  value = data[i].labelId;
		  map[item]=value;
	  }					  
	  var prediction_descr = d3.keys(map);
	  var prediction_ids= d3.values(map);
	  return prediction_descr.map(function(d,i) {
		  return {
			  id: prediction_ids[i],
			  checked: false,
			  descr: d 
		  };
	  });
	}

function drawSegment(segment) {
  var hasAccel = segment.accData != null;
  var w = hasAccel ? 600 : 300,
  h = 300,
  margin = 20,
  chartw = 0.5*(w - 2*margin),
  charth = (h - 2*margin),
  textw = 0.5 * (w - 2*margin),
  texth = charth;       
  
  var vis = d3.select("#segments")
    .append("svg:svg")
    .attr("width", w)
    .attr("height", h);
  
  if(hasAccel){
    appendChart(vis, segment, segment.accData, chartw, charth, 0, 0);
    appendText(vis, segment, textw, texth, chartw, 0)
  }
  else{
	appendText(vis, segment, textw, texth, 0, 0);
  }  
}

function  drawSegments(segmentsSet) {
	var segmentsLength = segmentsSet.length;			
	for(var iSegment = 0; iSegment < segmentsLength; iSegment++){
		var segment = segmentsSet[iSegment];
		drawSegment(segment);
	}
}
var currentPage = 3;
var pageSize = 6;
var allSegments;
var devices = [];
var labels =[];
var predictions =[];
var schema;

d3.json("data/schema.json", function(error, loadedSchema){	
	schema = loadedSchema;
	loadAndHandleData();       
});

var loadAndHandleData = function(){	
	d3.json("data/misclassifications.json", function(error, segments) {
		if(error != null){
			d3.select("#loaderror").classed("hidden", false);	
		}
		else{
			d3.select("#content").classed("hidden", false);
		}
		d3.select("#loading").classed("hidden", true);
		
		// get the unique ids from the data and create checkboxes
		devices = getDeviceIds(segments);
		devices.sort(selectionIdComparator);
		createCheckboxesFromArray(devices, '#trackers',onToggle);
		
		// get the unique label ids from the schema and create checkboxes
		labels = getLabelIds(schema);
		labels.sort(selectionIdComparator);
		createLabelCheckBoxGroup(labels, "actcls", "#labels");
		
		// get the unique predicted classes ids from the data
		predictions = getPredictionIds(schema);
		predictions.sort(selectionIdComparator);
		createLabelCheckBoxGroup(predictions, "predcls", "#predictions");		
				
		segments.sort(segmentComparator);
		//segments.sort(segLabelComparator);										
		allSegments = segments;		
		
		// Transform data for easier drawing of charts
		addAccelerometerDataArrayToSegment(segments);
		
		drawSelection();
	});	
}

function addAccelerometerDataArrayToSegment(segments){
	var segmentsLength = segments.length;			
	for(var iSegment = 0; iSegment < segmentsLength; iSegment++){
		var segment = segments[iSegment];
							
		measurements = segment.gpsRecord.measurements;
		if (measurements == null){
			continue;
		}
		
		var accx = [];				
		var accy = [];				
		var accz = [];				
		for(var iMeasurement = 0; iMeasurement < measurements.length; iMeasurement++){
			var measurement = measurements[iMeasurement];
			accx.push(measurement.x);
			accy.push(measurement.y);
			accz.push(measurement.z);
		} 				
		
	// 	Accelerometer data
		segment.accData = {x:accx, y:accy, z:accz};		
	}
}


function appendText(vis, segment, w, h, offsetw, offseth){
	var g = vis.append("svg:g")
		    .attr("transform", "translate("+offsetw+", "+h+")");
	
	var messages = getMessages(segment);
	appendMultipleLines(g, messages);
}

function appendMultipleLines(g, messages){
	var startx = 0;
	var starty = -240
	var fontHeight = 14;			
	var columnWidth = 170;
	var linesPerColumn = Math.floor(Math.abs(starty) / fontHeight);		
	for (var iMessage=0; iMessage < messages.length; iMessage++) {
	  var column = Math.floor(iMessage / linesPerColumn); 
	  var cursorx = startx + columnWidth * column;
	  var cursory = starty + fontHeight * (iMessage % linesPerColumn);  
	  var message = messages[iMessage];
	  appendLine(g, message, cursorx, cursory);			  
	};
}

function appendLine(g, message, x, y){
	g.append("svg:text")
			.text(message)
			.attr("x", x)
			.attr("y", y)
			.attr("class", "description")
			.attr("text-anchor", "left");
}

function getMessages(segment){
    var messages = [];                                           
    messages.push("device id: " + segment.deviceId);
    var dateTime = new Date(segment.timeStamp);
    messages.push("time: " + dateTime.toISOString());                                         
    messages.push("label: " + segment.labelDetail.description);
    messages.push("predicted: " + segment.predictedLabelDetail.description);                                        
    messages.push("features: ");
    numbers = formatNumberArray(segment.features);
    for (var i=0; i < numbers.length; i++) {
                    messages.push(segment.featureNames[i] + ": " + numbers[i])
    };
    messages.push();
    return messages;
}


function formatNumberArray(numberArray){
	var format = d3.format("2.3f");			
	var s = "";
	var numbers = [];
	for (var i=0; i < numberArray.length; i++) {
	  	var number = numberArray[i];
	  	numbers.push(format(number));
	};
	return numbers;
}

function appendChart(vis, segment, accData, w, h, offsetw, offseth){			
		var maxy = d3.max([d3.max(accData.x), d3.max(accData.y), d3.max(accData.z), 1]),
			miny = d3.min([d3.min(accData.x), d3.min(accData.y), d3.min(accData.z), -1]),
			length = d3.max([accData.x.length, accData.y.length, accData.z.length]),
			margin = 30;
	
		var g = vis.append("svg:g")
		    .attr("transform", "translate(0, "+h+")");				    				
		
		var	y = d3.scale.linear().domain([miny, maxy]).range([offseth + margin, offseth + h - margin]),
			x = d3.scale.linear().domain([0, length -1]).range([offsetw + margin, offsetw + w - margin]);
		
		var line = d3.svg.line()
		    .x(function(d,i) { return x(i); })
		    .y(function(d) { return -1 * y(d); });
		
		g.append("svg:path").attr("d", line(accData.x)).attr("class", "accx");
		g.append("svg:path").attr("d", line(accData.y)).attr("class", "accy");
		g.append("svg:path").attr("d", line(accData.z)).attr("class", "accz");
		
		g.append("svg:line")
		    .attr("x1", x(0))
		    .attr("y1", -1 * y(0))
		    .attr("x2", x(length-1))
		    .attr("y2", -1 * y(0));

		g.append("svg:line")
		    .attr("x1", x(0))
		    .attr("y1", -1 * y(1))
		    .attr("x2", x(length-1))
		    .attr("y2", -1 * y(1));
		    
		g.append("svg:line")
		    .attr("x1", x(0))
		    .attr("y1", -1 * y(-1))
		    .attr("x2", x(length-1))
		    .attr("y2", -1 * y(-1));

		g.append("svg:line")
		    .attr("x1", x(0))
		    .attr("y1", -1 * y(miny))
		    .attr("x2", x(0))
		    .attr("y2", -1 * y(maxy));
		
		g.append("svg:line")
		    .attr("x1", x(length-1))
		    .attr("y1", -1 * y(miny))
		    .attr("x2", x(length-1))
		    .attr("y2", -1 * y(maxy));
		
		g.selectAll(".xLabel")
		    .data(x.ticks(5))
		    .enter().append("svg:text")
		    .attr("class", "xLabel")
		    .text(String)
		    .attr("x", function(d) { return x(d) })
		    .attr("y", 0)
		    .attr("text-anchor", "middle");

		g.selectAll(".yLabel")
		    .data(y.ticks(4))
		    .enter().append("svg:text")
		    .attr("class", "yLabel")
		    .text(String)
		    .attr("x", 0)
		    .attr("y", function(d) { return -1 * y(d) })
		    .attr("text-anchor", "right")
		    .attr("dy", 4);
		
		g.selectAll(".xTicks")
		    .data(x.ticks(5))
		    .enter().append("svg:line")
		    .attr("class", "xTicks")
		    .attr("x1", function(d) { return x(d); })
		    .attr("y1", -1 * y(0))
		    .attr("x2", function(d) { return x(d); })
		    .attr("y2", -1 * y(-0.3));

		g.selectAll(".yTicks")
		    .data(y.ticks(4))
		    .enter().append("svg:line")
		    .attr("class", "yTicks")
		    .attr("y1", function(d) { return -1 * y(d); })
		    .attr("x1", x(-0.3))
		    .attr("y2", function(d) { return -1 * y(d); })
		    .attr("x2", x(0));
		
}

