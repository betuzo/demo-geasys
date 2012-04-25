<html>
 <head>
 <meta name="layout" content="main" />
 <title>People</title>
 <jqDT:resources />
 <g:javascript>
 	var editor; // use a global for the submit and return data rendering in the examples

    $(document).ready(function() {
		editor = new $.fn.dataTable.Editor( {
		"ajaxUrl": "php/browsers.php",
		"domTable": "#people",
		"fields": [ {
				"label": "Browser:",
				"name": "browser"
			}, {
				"label": "Rendering engine:",
				"name": "engine"
			}, {
				"label": "Platform:",
				"name": "platform"
			}, {
				"label": "Version:",
				"name": "version"
			}
		]
	} );

       $('#people').dataTable({
       	  sDom: 'Tfrtip',
          sScrollY: '70%',
          bProcessing: true,
          bServerSide: true,
          sAjaxSource: '${request.contextPath + '/people/dataTablesData'}' ,
          sPaginationType: "full_numbers",
          aLengthMenu: [[3, 5, 10, 30, -1], [3, 5, 10, 30, "All"]],
          iDisplayLength: 3,
          aoColumns: [
          	/* Id */         {bVisible: false},
          	/* First Name */ null,
          	/* Last Name */  null,
          	/* Birth Date */ null
          ],
          oTableTools: {
			sRowSelect: 'multi',
			"aButtons": [
				{ "sExtends": "editor_create", "editor": editor },
				{ "sExtends": "editor_edit",   "editor": editor },
				{ "sExtends": "editor_remove", "editor": editor }
			]
		  },
          fnRowCallback: function(nRow, aData, iDisplayIndex) {
          	$(nRow).dblclick(function(){
          		var id = aData[0];
				$.ajax({
					dataType: 'json',
					url: '${request.contextPath + '/people/'}' + id + '.json',
					success: function(data, status, xhr) {
						var dl = $('<dl class="personDetails"></dl>')
									.append('<dt>Id:</dt><dd>' + data.id +'</dd>')
									.append('<dt>First name:</dt><dd>' + data.firstName +'</dd>')
									.append('<dt>Last name:</dt><dd>' + data.lastName +'</dd>')
									.append('<dt>Birth date:</dt><dd>' + data.birthDate + '</dd>')
									.append('<dt>Date created:</dt><dd>' + data.dateCreated + '</dd>')
									.append('<dt>Last updated:</dt><dd>' + data.lastUpdated + '</dd>');
						
						$('<div></div>')
							.append(dl)
							.dialog({
								modal: true
							});
					},
					error: function(xhr, status, err) { },
					complete: function(xhr, status) { }
				});
          	});
          	return nRow;
          }
       });
    });
 </g:javascript>
 <link type="text/css" href="${resource(dir:'css',file:'personDetail.css')}" />
 <link type="text/css" href="${resource(dir:'css',file:'jquery.dataTables.css')}" />
 <link type="text/css" href="${resource(dir:'css',file:'TableTools.css')}" />
 <link type="text/css" href="${resource(dir:'css',file:'dataTables.editor.css')}" />
 </head>

 <body id="dt_example">
		<div id="container">
			<div class="full_width big">
				DataTables Editor basic example
			</div>
			
			
			<div id="demo">

		<table id="people">
		<thead>
		   <tr>
		      <th>Id</th>
		      <th>First Name</th>
		      <th>Last Name</th>
		      <th>Birth Date</th>
		   </tr>
		</thead>
		<tbody></tbody>
		<tfoot>
		   <tr>
		      <th>Id</th>
		      <th>First Name</th>
		      <th>Last Name</th>
		      <th>Birth Date</th>
		   </tr>
		</tfoot>
		</table>
	</div>
 </body>
</html>
