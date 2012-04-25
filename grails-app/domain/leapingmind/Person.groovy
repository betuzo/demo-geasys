package leapingmind

class Person {
	String firstName
	String lastName
	Date birthDate
	
	Date dateCreated
	Date lastUpdated
	
    static constraints = {
		birthDate(nullable:true)
    }
}
