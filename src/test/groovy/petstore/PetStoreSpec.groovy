package petstore

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import pl.leszekp.petstore.client.PetStoreClient
import pl.leszekp.petstore.dto.Pet
import spock.lang.Shared
import spock.lang.Specification

class PetStoreSpec extends Specification{

    PetStoreClient petStoreClient;

    @Shared
    Pet savedPet
/*    @Shared
    PetUpdateResponse bodyPet*/


    def setup() {
        RestTemplate restTemplate = new RestTemplate()
        petStoreClient = new PetStoreClient(restTemplate)
    }


    def "create pet as user should return 200 and pet in response"() {
        given: "name, status, photoUrls"
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
        savedPet.getPhotoUrls().get(0) == "https://pixnio.com/free-images/2018/06/09/2018-06-09-11-57-07-1200x855.png"
    }

    def "get pet should return pet with status available"() {
        given: "status"

        when:
        def response = petStoreClient.getFindByStatus(Pet.Status.AVAILABLE)

        then:
        response.getStatusCodeValue() == 200
        Arrays.stream(response.getBody()).filter(
                { pet -> pet.getStatus() != Pet.Status.AVAILABLE }
        ).count() == 0
    }

    def "update pet as user should return 200 and statute should be pending"() {
        given: "name, status, photoUrls"
        Pet pet = new Pet()
        pet.setName("Rex1")
        pet.setStatus(Pet.Status.PENDING)
        pet.setPhotoUrls(["https://pixnio.com/free-images/2018/06/09/2018-06-09-11-57-07-1200x855.png"])
        when:
        def response = petStoreClient.updatePet(pet)
        savedPet = response.getBody()

        then:
        response.getStatusCodeValue() == 200
        savedPet.getId() != null
        savedPet.getName() == "Rex1"
        savedPet.getStatus() == Pet.Status.PENDING
        savedPet.getPhotoUrls().get(0) == "https://pixnio.com/free-images/2018/06/09/2018-06-09-11-57-07-1200x855.png"
    }

    def "get pet should return pet"() {
        given: "status"

        when:
        def response = petStoreClient.getFindByStatus(Pet.Status.PENDING)

        then:
        response.getStatusCodeValue() == 200
        Arrays.stream(response.getBody()).filter(
                { pet -> pet.getStatus() != Pet.Status.PENDING }
        ).count() == 0
    }

/*    def "update pet should return 200 and pet in response"() {
        given:
        PetUpdateResponse petUpdateResponse = new PetUpdateResponse()

        when:
        def response = petStoreClient.update2Pet(savedPet.getId(), "Rex2", Pet.Status.SOLD)
        bodyPet = response.getBody()

        then:
        response.getStatusCodeValue() == 200
    }*/

    def "update pet as user should return 200 and statute should be sold"() {
        given: "name, status, photoUrls"
        Pet pet = new Pet()
        pet.setName("Rex2")
        pet.setStatus(Pet.Status.SOLD)
        pet.setPhotoUrls(["https://pixnio.com/free-images/2018/06/09/2018-06-09-11-57-07-1200x855.png"])

        when:
        def response = petStoreClient.updatePet(pet)
        savedPet = response.getBody()

        then:
        response.getStatusCodeValue() == 200
        savedPet.getId() != null
        savedPet.getName() == "Rex2"
        savedPet.getStatus() == Pet.Status.SOLD
        savedPet.getPhotoUrls().get(0) == "https://pixnio.com/free-images/2018/06/09/2018-06-09-11-57-07-1200x855.png"
    }

    def "get pet should return pet with status sold"() {
        given: "status"

        when:
        def response = petStoreClient.getFindByStatus(Pet.Status.SOLD)

        then:
        response.getStatusCodeValue() == 200
        Arrays.stream(response.getBody()).filter(
                { pet -> pet.getStatus() != Pet.Status.SOLD }
        ).count() == 0
    }

    def "get pet should return pet with status code 200"() {
        given: "pet_id"

        when:
        def response = petStoreClient.getPet(savedPet.getId())

        then:
        response.getStatusCodeValue() == 200
        savedPet.getId() != null
        savedPet.getName() == "Rex2"
        savedPet.getStatus() == Pet.Status.SOLD
        savedPet.getPhotoUrls().get(0) == "https://pixnio.com/free-images/2018/06/09/2018-06-09-11-57-07-1200x855.png"
    }

    def "update pet as user should return 200"() {
        given: "status"
        Pet pet = new Pet()
        pet.setStatus(Pet.Status.AVAILABLE)

        when:
        def response = petStoreClient.updatePet(pet)
        savedPet = response.getBody()

        then:
        response.getStatusCodeValue() == 200
        savedPet.getId() != null
        savedPet.getStatus() == Pet.Status.AVAILABLE
        savedPet.getName() == null
        savedPet.getPhotoUrls() == null
    }

    def "delete pet should delete pet with status code 200"() {
        given: "pet_id"

        when:
        def response = petStoreClient.deletePet(savedPet.getId())

        then:
        response.getStatusCodeValue() == 200
    }

    def "delete pet1 should delete pet with status code 404"() {
        given: "pet_id"

        when:
        def response = petStoreClient.deletePet(savedPet.getId())

        then:
        def exception = thrown(HttpClientErrorException)
        exception.getStatusCode() == HttpStatus.NOT_FOUND
    }

    def "get pet should return 404 - deleted user does not exist"() {
        given: "pet_id"

        when:
        def response = petStoreClient.getPet(savedPet.getId())

        then:
        def exception = thrown(HttpClientErrorException)
        exception.getStatusCode() == HttpStatus.NOT_FOUND
    }
}
