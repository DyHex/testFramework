import core.utilities.dataGenerators.GenerateSSN
import someCompanyName_API_implementation_examples.CreatePerson
import someCompanyName_API_implementation_examples_models.PersonRequest


/**
 *  Create token request... TODO: To implement the request, use 'CreatePerson.groovy' as an example
 * @author Magnus
 *	
 * CreateToken token = new CreateToken(username: "admin", password: "123")
 * token.sendRequestVerifyAndEvaluateResponse()
 * GlobalVariable.authToken = token.responseBody.token
 * 
 * Idealy, create a utility method that does the above..
 */

// Create person
PersonRequest person = new PersonRequest(givenName: "Kurt", lastName: "Dofus", phoneNumber: "+4791991199", ssn: GenerateSSN.norwegian())

CreatePerson createPerson = new CreatePerson(person)
createPerson.sendRequestVerifyAndEvaluateResponse()