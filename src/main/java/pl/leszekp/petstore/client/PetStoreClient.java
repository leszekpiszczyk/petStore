package pl.leszekp.petstore.client;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.leszekp.petstore.dto.Pet;
import pl.leszekp.petstore.dto.PetUpdateResponse;

/**
 * Klasa, której metody wysyłają żądania http
 **/
public class PetStoreClient {

    private final RestTemplate restTemplate;

    public PetStoreClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Pet> createPet(final Pet pet) {

        return restTemplate.postForEntity("https://petstore.swagger.io/v2/pet", pet, Pet.class);
    }

    public ResponseEntity<Pet> getPet(final Long id) {

        return restTemplate.getForEntity("https://petstore.swagger.io/v2/pet/" + id, Pet.class);
    }

    public ResponseEntity<String> deletePet(final Long id) {

        return restTemplate.exchange("https://petstore.swagger.io/v2/pet/{id}", HttpMethod.DELETE, new HttpEntity<>(""), String.class, id);
    }

    public ResponseEntity<Pet[]> getFindByStatus(final Pet.Status status) {

        return restTemplate.getForEntity("https://petstore.swagger.io/v2/pet/findByStatus?status=" + status, Pet[].class);
    }

    public ResponseEntity<Pet> updatePet(final Pet pet) {
        HttpEntity<Pet> entity = new HttpEntity<>(pet);
        return restTemplate.exchange("https://petstore.swagger.io/v2/pet", HttpMethod.PUT, entity, Pet.class);
    }

/*    public ResponseEntity<PetUpdateResponse> update2Pet(final Long id, final String name, final Pet.Status status) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("name", name);
        map.add("status", status.toString());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        return restTemplate.exchange("https://petstore.swagger.io/v2/pet/{id}", HttpMethod.POST, request, PetUpdateResponse.class, id);

    }*/
}
