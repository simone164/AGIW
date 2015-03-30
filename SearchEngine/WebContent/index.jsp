<html>
<head>
<link rel="stylesheet" type="text/css" href="css/index.css">
<link rel="icon" href="favicon.ico" type="image/ico"></link>
<link rel="shortcut icon" href="favicon.ico" type="image/ico"></link>
<link href='http://fonts.googleapis.com/css?family=Sniglet'rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Gochi+Hand' rel='stylesheet' type='text/css'>
<title>Cache</title>
</head>
<body>
 
	<table width="100%" border="0" align="center">
		<tr>
			<td width="50%" align="center" colspan="2">
				<div class="logo"><img alt="" src="img/CacheDownloaderl.png" align="center" style="width: 316px; height: 208px"></div>
			</td>
		</tr>
		<tr>
			<td width="50%" align="center" colspan="2">
				<form id="form_ricerca" name="form_ricerca" action="Result.jsp"
					method="get" style="margin-top: 45px">
					<input name="query" class="input_ricerca" value="Cerca..."
						onclick="if(this.value == 'Cerca...'){ this.value = '' }"></input>
					<img alt="" src="css/img/search.png" width="28" height="28"
						style="margin-left: -35px; margin-bottom: -8px; cursor: pointer"
						onclick='document.form_ricerca.submit()'>
				</form>
			</td>
		</tr>
		<tr>
			<td height="80px" colspan="2"></td>
		</tr>
		<tr>
			<td width="50%" align="center"><script
					src="http://www.gmodules.com/ig/ifr?url=http://www.ilmeteo.it/widgets/igoogle-meteocitta.xml&amp;up_loc=Roma&amp;synd=open&amp;w=300&amp;h=250&amp;title=&amp;border=%23ffffff%7C0px%2C1px+solid+%2382CAFA%7C0px%2C2px+solid+%23BDEDFF%7C0px%2C3px+solid+%23E0FFFF&amp;output=js"></script>
			</td>
			<td width="50%" align="center"><iframe
					src="http://www.google.com/uds/modules/elements/newsshow/iframe.html?format=300x250"
					frameborder="0" width="300" height="250" marginwidth="0"
					marginheight="0"> </iframe></td>
		</tr>
	</table>
</body>
</html>
