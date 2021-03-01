package pl.leszekp.petstore.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.leszekp.petstore.dto.Pet;

/**
 * Klasa, której metody wysyłają żądania http
 **/
public class PetStoreClient {

    private final RestTemplate restTemplate;

    public PetStoreClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Pet> createPet(final Pet pet) {

        /**
         * Pierwszy parametr to url do którego wysyłamy rządanie
         * Drugi parametr to obiekt który wysyłamy - rest template pod spodem zmienia go na json
         * Trzeci paramer to typ klasy, który ma zostać zwrócony (tak na prawde zostanie zwrócony json i przekonwertowany na obiek pet)
         * */
        return restTemplate.postForEntity("https://petstore.swagger.io/v2/pet", pet, Pet.class);
    }

    public ResponseEntity<Pet> getPet(final Long id) {

        return restTemplate.getForEntity("https://petstore.swagger.io/v2/pet/" + id, Pet.class);
    }

    public ResponseEntity<String> deletePet(final Long id) {
        return restTemplate.exchange("https://petstore.swagger.io/v2/pet/{id}", HttpMethod.DELETE, new HttpEntity<>(""), String.class, id);
    }

    public ResponseEntity<Pet> getFindByPet(final Pet.Status status) {

        return restTemplate.getForEntity("https://petstore.swagger.io/v2/findByStatus?status=" + status, Pet.class);
    }

    public ResponseEntity<Pet> updatePet(final Pet pet) {
        HttpEntity<Pet> entity = new HttpEntity<>(pet);
        return restTemplate.exchange("https://petstore.swagger.io/v2/pet", HttpMethod.PUT, entity, Pet.class);
    }
}
