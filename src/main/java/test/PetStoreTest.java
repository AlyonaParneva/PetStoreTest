package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import main.BaseTest;
import main.Pet;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.assertEquals;

@Epic("PetStore API Tests")
@Feature("Pet Management")
public class PetStoreTest extends BaseTest {

    @Test
    @Story("Добавление(post) нового pet")
    @Description("Тест для добавления нового pet и проверки коррекности добавленного")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Добавление нового pet")
    public  void testAddNewPet() throws JsonProcessingException{
        int petId=1;
        int categoryId=2;
        String nameCategory="dog";
        String name="TestPet";
        int tagId=3;
        String nameTags="test";
        String status="available";

        Response response=petStoragePage.addNewPet(petId,categoryId,nameCategory,name,tagId,nameTags,status);
        response.then().statusCode(200);

        ObjectMapper objectMapper = new ObjectMapper();
        Pet petFromResponse = objectMapper.readValue(response.asString(), Pet.class);

        verifyPetResponse(petFromResponse,petId,categoryId,nameCategory,name,tagId,nameTags,status);
    }

    @Step("Проверка добавленного нового pet")
    public void verifyPetResponse(Pet pet, int petId,int categoryId,String nameCategory,String name,int tagId,String nameTags,String status) {
        assertEquals(pet.getId(), petId);
        assertEquals(pet.getCategory().getId(), categoryId);
        assertEquals(pet.getCategory().getName(), nameCategory);
        assertEquals(pet.getName(), name);
        assertEquals(pet.getTags().get(0).getId(), tagId);
        assertEquals(pet.getTags().get(0).getName(), nameTags);
        assertEquals(pet.getStatus(), status);
    }

    @Test(dependsOnMethods = "testAddNewPet")
    @Story("Поиск(get) pet по ID")
    @Description("Тест для поиска pet по заданному ID и проверка найденного")
    @Severity(SeverityLevel.NORMAL)
    @Step("Поиск pet по ID")
    public void testGetPetById() throws JsonProcessingException{
        int petId=1;
        Response response= petStoragePage.getPetById(petId);

        response.then().statusCode(200);

        ObjectMapper objectMapper = new ObjectMapper();
        Pet petFromResponse = objectMapper.readValue(response.asString(), Pet.class);
        verifyPetResponsePetById(petFromResponse,petId);

    }

    @Step("Проверка найденного pet по ID")
    public void verifyPetResponsePetById(Pet pet,int petId){
        assertEquals(pet.getId(), petId);
    }

    @Test(dependsOnMethods = "testGetPetById")
    @Story("Поиск(get) pet по status")
    @Description("Тест для поиска pet по status и проверки найденнного")
    @Severity(SeverityLevel.NORMAL)
    @Step("Поиск pet по status")
    public void testGetPetByStatus() throws JsonProcessingException{
        String status="available";
        Response response= petStoragePage.getPetByStatus(status);

        response.then().statusCode(200);

        ObjectMapper objectMapper = new ObjectMapper();
        Pet[] petFromResponse = objectMapper.readValue(response.asString(), Pet[].class);

        for(Pet pet : petFromResponse){
        verifyPetStatus(pet, status);}
    }

    @Step("Проверка status найденного pet")
    public void verifyPetStatus(Pet pet, String status){
        assertEquals(pet.getStatus(),status);
    }

    @Test(dependsOnMethods = "testGetPetByStatus")
    @Story("Обновление(post) pet по 3 параметрам")
    @Description("Тест для обновления информации о pet по параметрам petId,name,status и проверка обновленнного")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Обновление pet и проверка статуса ответа")
    public void testPostPetUpdate() throws JsonProcessingException{
        int petId=1;
        String name="NewTestPet";
        String status="sold";

        Response response= petStoragePage.postPetUpdate(petId,name,status);

        response.then().statusCode(200);
    }

    @Test(dependsOnMethods = "testPostPetUpdate")
    @Story("Обновление(put) pet по всем параметрам")
    @Description("Тест для обновления информации о pet по всем параметрам и проверка обновленного")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Обновление pet по всем параметрам")
    public void testPutPetUpdate() throws JsonProcessingException {
        int petId = 1;
        int categoryId = 0;
        String nameCategory = "dog";
        String name = "TestPetUpdate";
        int tagId = 3;
        String nameTags = "test";
        String status = "available";
        Response response = petStoragePage.putPetUpdate(petId, categoryId, nameCategory, name, tagId, nameTags, status);
        response.then().statusCode(200);

        ObjectMapper objectMapper = new ObjectMapper();
        Pet petFromResponse = objectMapper.readValue(response.asString(), Pet.class);
        verifyPetResponsePutPetUpdate(petFromResponse, petId, categoryId, nameCategory, name, tagId, nameTags, status);
    }

    @Step("Проверка обновленного pet по всем параметрам")
    public void verifyPetResponsePutPetUpdate(Pet pet,int petId,int categoryId,String nameCategory,String name,int tagId,String nameTags,String status) {
        assertEquals(pet.getId(), petId);
        assertEquals(pet.getCategory().getId(), categoryId);
        assertEquals(pet.getCategory().getName(), nameCategory);
        assertEquals(pet.getName(), name);
        assertEquals(pet.getTags().get(0).getId(), tagId);
        assertEquals(pet.getTags().get(0).getName(), nameTags);
        assertEquals(pet.getStatus(), status);
    }


    @Test(dependsOnMethods = "testPutPetUpdate")
    @Story("Загрузка(post) изображения для pet")
    @Description("Тест для загрузки изображения pet и проверка загруженного")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Загрузка изображения pet и проверка статуса ответа")
    public void testPostPetUploadImage() throws JsonProcessingException{
        int petId=1;
        String additionalMetadata="it`s a dog";
        String filePath="src//main//java//main//img//dog1.jpg";
        Response response= petStoragePage.postPetUploadImage(petId,additionalMetadata,filePath);

        response.then().statusCode(200);
    }

    @Test(dependsOnMethods = "testPostPetUploadImage")
    @Story("Удаление(delete) pet по ID")
    @Description("Тест для удаления pet по ID и проверка статуса удаления")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Удаление pet и проверка статуса ответа")
    public void testDeletePet(){
        int petId=1;
        Response response= petStoragePage.deletePet(petId);
        response.then().statusCode(200);
    }

    @Test(dependsOnMethods = "testDeletePet")
    @Story("Поиск(get) удаленного pet по ID ")
    @Description("Тест для проверки удаленного pet по его ID")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Поиск pet по ID и проверка статуса ответа")
    public void testGetPetByIdAfterDelete(){
        int petId=1;
        Response response= petStoragePage.getPetByIdAfterDelete(petId);

        response.then().statusCode(404);
    }


}
