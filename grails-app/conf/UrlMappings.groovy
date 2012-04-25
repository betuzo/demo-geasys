class UrlMappings {

	static mappings = {
		"/people/index/"(controller:"people") {
			action=[GET:"index"]
		}
		"/people/dataTablesData/"(controller:"people") {
			action=[GET:"dataTablesData"]
		}
		"/people"(controller:"people") {
			action=[GET:"list"]
		}
		"/people/$id"(controller:"people") {
			action=[GET:"show"]
		}
		
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
