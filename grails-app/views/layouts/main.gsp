<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:javascript library="jquery" plugin="jquery" />
        <g:javascript src='jquery.dataTables.js'/>
        <g:javascript src='TableTools.js'/>
        <g:javascript src='dataTables.editor.js'/>
		<jqui:resources />
        <g:javascript library="application" />
        <g:layoutHead />
    </head>
    <body>
        <g:layoutBody />
    </body>
</html>