package leapingmind

import grails.converters.JSON
import grails.converters.XML
import org.springframework.dao.DataIntegrityViolationException


class PeopleController {

	def static formatDate = 'yyyy-MM-dd'
	def static responseEditor = ['id', 'error', 'fieldErrors', 'data']

	
	/**
	 * View to show data in a jQuery DataTable
	 */
	def index = {
	}

	def postAction = {
		println "Imprimiendo parametros $params"
		switch(params.getRequest().getParameter("action")) {
			case "edit":
				edit(params)
			break
			case "create":
				create(params)
			break
			case "remove":
				remove(params)
			break
		}


	}

	/**
	 * Create row
	 */
	def create = { parameters ->
		def personInstance = new Person()
		personInstance.properties = ["firstName":parameters.get("data[firstName]"), "lastName":parameters.get("data[lastName]"), "birthDate":Date.parse( formatDate, parameters.get("data[birthDate]") ), "lastUpdated":new Date()]
        if (!personInstance.save(flush: true)) {
        	render personInstance as JSON
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'contacto.label', default: 'Contacto'), personInstance.id])
		render personInstance as JSON
    }
	
	/**
	 * Edit row
	 */
	def edit = { parameters ->
		def personInstance = Person.get(parameters.get("id"))
        if (!personInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'contacto.label', default: 'Contacto'), parameters.id])
            render "error" as JSON
            return
        }

        personInstance.properties = ["id": parameters.get("id"), "firstName":parameters.get("data[firstName]"), "lastName":parameters.get("data[lastName]"), "birthDate":Date.parse( formatDate, parameters.get("data[birthDate]") ), "lastUpdated":new Date()]

        if (!personInstance.save(flush: true)) {
            render personInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'contacto.label', default: 'Contacto'), personInstance.id])
		render personInstance as JSON
	}

	/**
	 * Remove row
	 */
	def remove = { parameters ->
		println "Removiendo... $parameters"
		def response = [:]
		for(def it in parameters.get("data[]")) {
			println it
			def personInstance = Person.get(it)
	        if (personInstance) {
		        try {
		            personInstance.delete(flush: true)
					flash.message = message(code: 'default.deleted.message', args: [message(code: 'contacto.label', default: 'Contacto'), params.id])
		        }
		        catch (DataIntegrityViolationException e) {
					flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'contacto.label', default: 'Contacto'), params.id])
		            redirect(action: "show", id: params.id)
		        }
	    	}
		}

		def fieldErrors = []
		def data = []
		response."${responseEditor[0]}" = "-1"
		response."${responseEditor[1]}" = ""
		response."${responseEditor[2]}" = fieldErrors
		response."${responseEditor[3]}" = data
		render response as JSON
	}

   /**
	* Lists all people.
	*
	* GET /people
	* GET /people.json
	* GET /people.xml
	* 
	* @param callback For JSONP.
	*/
	def list = {
		def people = Person.list()
		if ( ! people ) {
			render(text: "No people found", status: 404)
		} else {
			withFormat {
				json {
					if ( params.callback ) {
						render(contentType: 'application/json',
							   text: "${params.callback}(${people as JSON})")
					} else {
						render people as JSON
					}
				}
				xml {
					render people as XML
				}
			}
		}
	}
	
	/**
	 * Shows a specific person.
	 * 
	 * GET /people/1
	 * GET /people/1.json
	 * GET /people/1.xml
	 * 
	 * @param id
	 * @param callback For JSONP
	 */
	def show = {
		def person = Person.get(params.id as int)
		
		if ( ! person ) {
			render(text: "No person with id ${params.id} found", status: 404)
		} else {
			withFormat {
				json {
					if ( params.callback ) {
						render(contentType: 'application/json',
							   text: "${params.callback}(${person as JSON})")	
					} else {
						render person as JSON
					}
				}
				xml {
					render person as XML
				}
			}
		}
	}
	
	
	/**
	 * Generates data for a jQuery DataTables table showing people.
	 * 
	 * @param sEcho
	 * @param sSearch
	 * @param iSortCol_0
	 * @param iSortDir_0
	 * @param iDisplayStart
	 * @param iDisplayLength
	 */
	def dataTablesData = {
		def propertiesToRender = ['id', 'firstName', 'lastName', 'birthDate']
	
		def dataToRender = [:]
		dataToRender.sEcho = params.sEcho
		dataToRender.aaData=[]                // Array of people.
	
		dataToRender.iTotalRecords = Person.count()
		dataToRender.iTotalDisplayRecords = dataToRender.iTotalRecords
	
		// Create the query, possibly with a search filters. We only search
		// String properties in this example.
		def filters = []
		filters << "p.firstName like :filter"
		filters << "p.lastName like :filter"
		
		def filter = filters.join(" OR ")
		def query = new StringBuilder("from Person as p")
		if ( params.sSearch ) {
		   query.append(" where (${filter})")
		}
		
		def sortProperty = params.iSortCol_0 ? propertiesToRender[params.iSortCol_0 as int] : 'firstName'
		def sortDir = params.sSortDir_0?.equalsIgnoreCase('asc') ? 'asc' : 'desc'
		query.append(" order by p.${sortProperty} ${sortDir}")
	
		// Execute the query
		def people = []
		if ( params.sSearch ) {
		   // Revise the number of total display records after applying the filter
		   def countQuery = new StringBuilder("select count(*) from Person as p where (${filter})")
		   def result = Person.executeQuery(countQuery.toString(),
											 [filter: "%${params.sSearch}%"])
		   if ( result ) {
			  dataToRender.iTotalDisplayRecords = result[0]
		   }
		   people = Person.findAll(query.toString(),
			   [filter: "%${params.sSearch}%"],
			   [max: params.iDisplayLength as int, offset: params.iDisplayStart as int])
		} else {
		   def iMax = params.iDisplayLength? params.iDisplayLength as int : 0	
		   def iOffset = params.iDisplayStart? params.iDisplayStart as int : 0
		   people = Person.findAll(query.toString(),
			   [max: iMax, offset: iOffset])
		}
	
		// Process the response
		people?.each { person ->
		   def record = [:]
		   def valFormat = "";
		   def col = "";
		   propertiesToRender.each { 
		   	if (person."${it}" instanceof java.sql.Timestamp)
		   		valFormat = person."${it}".format(formatDate)
		   	else
		   		valFormat = person."${it}"
		   	col = "$it"
		   	if ("$it" == "id")
		   		col = "DT_RowId"
		   	record."${col}" = valFormat
		   }
		   dataToRender.aaData << record
		}
		render dataToRender as JSON	
	}
}
