<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<link rel="shortcut icon" type="image/ico" href="http://www.datatables.net/favicon.ico" />
		<g:javascript src='jquery.js'/>
        <g:javascript src='jquery.dataTables.js'/>
        <g:javascript src='TableTools.js'/>
        <g:javascript src='dataTables.editor.js'/>
		<script type="text/javascript" charset="utf-8" src="http://ajax.aspnetcdn.com/ajax/jquery.ui/1.8.	17/jquery-ui.min.js"></script>		

		<style type="text/css">
			@import "http://ajax.aspnetcdn.com/ajax/jquery.ui/1.8.17/themes/smoothness/jquery-ui.css";
			@import "../css/jquery.dataTables.css";
			@import "../css/TableTools.css";
			@import "../css/dataTables.editor.css";
		</style>

		<title>DataTables Editor example</title>
		<script type="text/javascript" charset="utf-8" id="init-code">
			var editor; // use a global for the submit and return data rendering in the examples

			$(document).ready(function() {
				editor = new $.fn.dataTable.Editor( {
					"ajaxUrl": '${request.contextPath + '/people/dataTablesData'}',
					"domTable": "#example",
					"fields": [ {
							"label": "Id:",
							"name": "0",
						}, {
							"label": "Fisrt Name:",	
							"name": "1"
						}, {
							"label": "Second Name:",
							"name": "2"
						}, {
							"label": "BirthDate:",
							"name": "3",
							"type": "date"
						}
					]
				} );

				$('#example').dataTable( {
					"sDom": "Tfrtip",
					"sAjaxSource": '${request.contextPath + '/people/dataTablesData'}',
					"aoColumns": [
						{ "mDataProp": 0 },
						{ "mDataProp": 1 },
						{ "mDataProp": 2, "sClass": "center" },
						{ "mDataProp": 3, "sClass": "center" }
					],
					"oTableTools": {
						"sRowSelect": "multi",
						"aButtons": [
							{ "sExtends": "editor_create", "editor": editor },
							{ "sExtends": "editor_edit",   "editor": editor },
							{ "sExtends": "editor_remove", "editor": editor }
						]
					}
				} );
			} );
		</script>
				 
	</head>
	<body id="dt_example">
		<div id="container">
			<div id="demo">
<table cellpadding="0" cellspacing="0" border="0" class="display" id="example" width="100%">
	<thead>
		<tr>
			<th width="30%">Id</th>
			<th width="20%">Fisrt Name</th>
			<th width="18%">Second Name</th>
			<th width="20%">BirthDate</th>
		</tr>
	</thead>
	<tfoot>
		<tr>
			<th>Id</th>
			<th>Fisrt Name</th>
			<th>Second Name</th>
			<th>BirthDate</th>
		</tr>
	</tfoot>
</table>
<h1>Submitted</h1>
<pre id="submitted_data" class="brush: js;">// No data yet submitted</pre>
<h1>Response</h1>
<pre id="response" class="brush: js;">// No data yet received</pre>
<h1>Initialisation code</h1>
			<pre id="display-init-code" class="brush: js;"></pre>
			
			<script type="text/javascript">
				$(document).ready( function () {
					/* Put the code used from the example into the display */
					var initCode = document.getElementById('init-code');
					var displayCode = document.getElementById('display-init-code');
					displayCode.innerHTML = initCode.text.replace(/&/g, '&amp;').replace(/>/g, '&gt;').replace(/</g, '&lt;');

					/* Show and syntax highlight submit and return data */
					$(editor).bind('onPreSubmit', function (e, data) {
						var n = document.getElementById('submitted_data');
						n.innerHTML = JSON.stringify( data, null, 2 );
						n.className = "brush: js;"
						//SyntaxHighlighter.highlight({}, n);
					} );

					$(editor).bind('onSubmitLoaded', function (e, json, data) {
						var n = document.getElementById('response');
						n.innerHTML = JSON.stringify( json, null, 2 );
						n.className = "brush: js;"
						//SyntaxHighlighter.highlight({}, n);
					} );
				} );		
			</script>
		</div>
	</body>
</html>