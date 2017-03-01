function ServerSuggestions() {
	this.suggestions = [];
}
ServerSuggestions.prototype.sendAjaxRequest = function(oAutoSuggestControl, bTypeAhead, input) {
	var request = "/eBay/suggest?q="+encodeURI(input);
	var xmlHttp = new XMLHttpRequest();
	var oThis = this;
	
	console.log("entered AJAX");

	xmlHttp.open("GET", request);
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4 && xmlHttp.responseXML != null) {
			// get the CompleteSuggestion elements from the response
			var s = xmlHttp.responseXML.getElementsByTagName('CompleteSuggestion');

			for (i = 0; i < s.length; i++) {
			    oThis.suggestions.push(s[i].childNodes[0].getAttribute("data"));
			}
			
		}
		
		console.log("updated list: " + oThis.suggestions);
		
		oThis.requestSuggestions(oAutoSuggestControl, bTypeAhead);
	};
	console.log("not ready");
	xmlHttp.send(null);
};

ServerSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,
                                                          bTypeAhead /*:boolean*/) {
	console.log("requested suggestion list" + this.suggestions);
    oAutoSuggestControl.autosuggest(this.suggestions, bTypeAhead);
	//flush old requests out of "buffer" 
	this.suggestions = [];
};

