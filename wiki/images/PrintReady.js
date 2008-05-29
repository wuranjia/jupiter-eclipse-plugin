var gAutoPrint = false; // Flag for whether or not to automatically call the print function

function printSpecial() {
	if (document.getElementById != null) {
		var html = '<HTML>\n<HEAD>\n';

		if (document.getElementsByTagName != null) {
			var headTags = document.getElementsByTagName("head");
			if (headTags.length > 0) {
				html += headTags[0].innerHTML;
			}
		}
				
		html += '\n</HEAD>\n<BODY>\n';
		
		var printReadyElement = document.getElementById("printReady");
		
		if (printReadyElement != null) {
				html += printReadyElement.innerHTML;
		}
		else {
			alert("Could not find the printReady section in the HTML");
			return;
		}
			
		html += '\n</BODY>\n</HTML>';
		
		var printWin = window.open("","printSpecial");
		printWin.document.open();
		printWin.document.write(html);
		printWin.document.close();
		if (gAutoPrint) {
			printWin.print();
		}
	}
	else {
		alert("Sorry, the print ready feature is only available in modern browsers.");
	}
}