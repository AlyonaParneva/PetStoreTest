package main;
import org.testng.annotations.BeforeClass;

public class BaseTest {

    protected static PetStorePage petStoragePage;

    @BeforeClass
    public void setup(){
        petStoragePage=new PetStorePage();
    }
}
