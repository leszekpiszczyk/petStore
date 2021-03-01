package petstore

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import pl.leszekp.petstore.client.PetStoreClient
import pl.leszekp.petstore.dto.Pet
import spock.lang.Shared
import spock.lang.Specification

import java.net.http.HttpClient

class PetStoreSpec extends Specification{

    PetStoreClient petStoreClient;

    @Shared
    Pet savedPet


    def setup() {
        RestTemplate restTemplate = new RestTemplate()
        petStoreClient = new PetStoreClient(restTemplate)
    }


    def "create pet as user should return 200 and pet in response"() {
        given:
//        Pet pet = new Pet(
//           name: "Rex",
//           status: Pet.Status.SOLD
//        )
        Pet pet = new Pet()
        pet.setName("Rex")
        pet.setPhotoUrls(["https://pixnio.com/free-images/2018/06/09/2018-06-09-11-57-07-1200x855.png"])
        pet.setStatus(Pet.Status.AVAILABLE)

        when:
        def response = petStoreClient.createPet(pet)
        savedPet = response.getBody()

        then:
        response.getStatusCodeValue() == 200
        savedPet.getId() != null
        savedPet.getName() == "Rex"
        savedPet.getStatus() == Pet.Status.AVAILABLE
    }

    def "get pet should return pet with status code 200"() {
        when:
        def response = petStoreClient.getPet(savedPet.getId())

        then:
        response.getStatusCodeValue() == 200
        savedPet.getId() != null
        savedPet.getName() == "Rex"
        savedPet.getStatus() == Pet.Status.AVAILABLE
    }

    def "update pet as user should return 200 and pet in response"() {
        given:
        Pet pet = new Pet()
        pet.setName("Rex1")
        pet.setStatus(Pet.Status.PENDING)
        when:
        def response = petStoreClient.updatePet(pet)
        savedPet = response.getBody()

        then:
        response.getStatusCodeValue() == 200
        savedPet.getId() != null
        savedPet.getName() == "Rex1"
        savedPet.getStatus() == Pet.Status.PENDING
    }

    def "update pet as user should return 200 and statute should be sold"() {
        given:
        Pet pet = new Pet()
        pet.setStatus(Pet.Status.SOLD)
        when:
        def response = petStoreClient.updatePet(pet)
        savedPet = response.getBody()

        then:
        response.getStatusCodeValue() == 200
        savedPet.getId() != null
        savedPet.getStatus() == Pet.Status.SOLD
    }

    def "delete pet should delete pet with status code 200"() {
        when:
        def response = petStoreClient.deletePet(savedPet.getId())

        then:
        response.getStatusCodeValue() == 200
    }

    def "delete pet1 should delete pet with status code 404"() {
        when:
        def response = petStoreClient.deletePet(savedPet.getId())

        then:
        def exception = thrown(HttpClientErrorException)
        exception.getStatusCode() == HttpStatus.NOT_FOUND
    }
}
