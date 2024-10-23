package PetStore;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;

import java.io.File;
import java.util.List;

import static io.restassured.RestAssured.given;

public class PetStorePage {
    private final String BASE_URL;

    public PetStorePage() {
        BASE_URL = ConfProperties.PROPERTIES.getProperty("BASE_URL");
        System.out.println("Loaded BASE_URL: " + BASE_URL);
        if (BASE_URL == null) {
            throw new RuntimeException("BASE_URL not found in configuration file.");
        }
    }

    public Response addNewPet(int petId, int categoryId, String nameCategory, String name, int tagId, String nameTags,String status) throws JsonProcessingException {
        Pet pet=new Pet(petId,categoryId,nameCategory,name, List.of("string"),List.of(new Pet.Tag(tagId,nameTags)),status);
        String newPet= pet.toJson();

        return given()
                .relaxedHTTPSValidation()
                .baseUri(BASE_URL)
                .header("Content-Type","application/json")
                .body(newPet)
                .when()
                .post("/pet/");


        }

    public Response getPetById(int petId){
        return given()
                .relaxedHTTPSValidation()
                .baseUri(BASE_URL)
                .when()
                .get("/pet/"+petId);
    }

    public Response getPetByStatus(String status){
        return given()
                .relaxedHTTPSValidation()
                .baseUri(BASE_URL)
                .queryParam("status",status)
                .when()
                .get("/pet/findByStatus/");
    }

    public Response postPetUpdate(int petId, String name, String status){
        return given()
                .relaxedHTTPSValidation()
                .baseUri(BASE_URL)
                .contentType("application/x-www-form-urlencoded")
                .formParam("name",name)
                .formParam("status",status)
                .when()
                .post("pet/"+petId);
    }

    public Response putPetUpdate(int petId, int categoryId, String nameCategory, String name, int tagId, String nameTags,String status) throws JsonProcessingException {
        Pet pet=new Pet(petId,categoryId,nameCategory,name, List.of("string"),List.of(new Pet.Tag(tagId,nameTags)),status);
        String updatePet= pet.toJson();

        return given()
                .relaxedHTTPSValidation()
                .baseUri(BASE_URL)
                .header("Content-Type","application/json")
                .body(updatePet)
                .when()
                .put("/pet/");
    }

    public Response postPetUploadImage(int petId, String additionalMetadata, String filePath){
        File file=new File(filePath);
        if(!file.exists()){
            throw new RuntimeException("файл не найден "+filePath);
        }
        return  given()
                .relaxedHTTPSValidation()
                .baseUri(BASE_URL)
                .header("Content-Type","multipart/form-data")
                .multiPart("file",file)
                .formParam("additionalMetadata", additionalMetadata)
                .pathParam("petId",petId)
                .when()
                .post("/pet/{petId}/uploadImage");
    }

    public Response deletePet(int petId){
        return given()
                .relaxedHTTPSValidation()
                .baseUri(BASE_URL)
                .when()
                .delete("/pet/"+petId);
    }

    public Response getPetByIdAfterDelete(int petId){
        return given()
                .relaxedHTTPSValidation()
                .baseUri(BASE_URL)
                .when()
                .get("/pet/"+petId);
    }
}
