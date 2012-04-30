package leapingmind

import grails.converters.JSON
import grails.converters.XML


class PeopleController {
	
	/**
	 * View to show data in a jQuery DataTable
	 */
	def index = {
	}

	def postAction = {
		println params.get("data[0]")

		def personInstance = Person.get(params.get("data[0]"))
        if (!personInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'contacto.label', default: 'Contacto'), params.id])
            render "error" as JSON
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (personInstance.version > version) {
                personInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'contacto.label', default: 'Contacto')] as Object[],
                          "Another user has updated this Contacto while you were editing")
                render "error" as JSON
                return
            }
        }

        personInstance.properties = ["id": params.get("data[0]"), "firstName":params.get("data[1]"), "lastName":params.get("data[2]"), "birthDate":new Date(), "lastUpdated":new Date(), "dateCreated":new Date()]

        if (!personInstance.save(flush: true)) {
            render personInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'contacto.label', default: 'Contacto'), personInstance.id])
		render personInstance as JSON
	}
	
	/**
	 * Edit row
	 */
	def edit = {
		println "Editando..."
	}

	/**
	 * Remove row
	 */
	def remove = {
		println "Removiendo..."
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
		println "dataTablesData... $params"
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
		   def record = []
		   propertiesToRender.each { record << person."${it}" }
		   dataToRender.aaData << record
		}
		render dataToRender as JSON	
	}
}
