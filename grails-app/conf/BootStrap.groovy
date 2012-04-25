import leapingmind.Person

class BootStrap {

    def init = { servletContext ->
		createPerson([firstName: 'John', lastName: 'Doe', birthDate: Date.parse('yyyy-MM-dd', '1980-01-06')])
		createPerson([firstName: 'Jane', lastName: 'Doe', birthDate: Date.parse('yyyy-MM-dd', '1981-02-05')])
		createPerson([firstName: 'Ben', lastName: 'Dover', birthDate: Date.parse('yyyy-MM-dd', '1982-03-04')])
		createPerson([firstName: 'Seymour', lastName: 'Butts', birthDate: Date.parse('yyyy-MM-dd', '1983-04-03')])
		createPerson([firstName: 'Hugh', lastName: 'Jass', birthDate: Date.parse('yyyy-MM-dd', '1984-05-02')])
		createPerson([firstName: 'Dixie', lastName: 'Normus', birthDate: Date.parse('yyyy-MM-dd', '1985-06-01')])
    }
	
    def destroy = {
    }
	
	private def createPerson(properties) {
		def person = Person.findByFirstNameAndLastName(properties.firstName, properties.lastName)
		if ( ! person ) {
			person = new Person(properties)
			if ( ! person.save() ) {
				println "Unable to save person: ${person.errors}"
			}
		}
	}
}
