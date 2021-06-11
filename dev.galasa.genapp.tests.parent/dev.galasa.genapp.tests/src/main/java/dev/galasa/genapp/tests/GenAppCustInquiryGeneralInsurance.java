package dev.galasa.genapp.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import dev.galasa.Test;

import dev.galasa.zos.IZosImage;
import dev.galasa.zos.ZosImage;
import dev.galasa.zos3270.FieldNotFoundException;
import dev.galasa.zos3270.ITerminal;
import dev.galasa.zos3270.KeyboardLockedException;
import dev.galasa.zos3270.TerminalInterruptedException;
import dev.galasa.zos3270.TextNotFoundException;
import dev.galasa.zos3270.TimeoutException;
import dev.galasa.zos3270.Zos3270Terminal;
import dev.galasa.zos3270.spi.NetworkException;

@Test
public class GenAppCustInquiryGeneralInsurance {
	@ZosImage(imageTag="ZDNT")
	public IZosImage        image;

    @Zos3270Terminal(imageTag = "ZDNT")
    public ITerminal        terminal;
    
    @Test
    public void testNotNull() {
        //Check terminal is loaded
        assertThat(terminal).isNotNull();
    }
    
    @Test
    public void testCustInquiry01() throws TimeoutException, KeyboardLockedException, TerminalInterruptedException, NetworkException, FieldNotFoundException, TextNotFoundException, InterruptedException {
        // Login to CICS Terminal in zD&T
        terminal.waitForKeyboard().positionCursorToFieldContaining("L CICSTS55").tab().type("L CICSTS55").enter().waitForKeyboard().tab()
                .clear().waitForKeyboard();
        
        TimeUnit.SECONDS.sleep(2);
        
        // Start the transaction SSC1
        terminal.tab().type("SSC1").enter().waitForKeyboard().tab();
        
        // Assert that the screen is for General Insurance Customer
        assertThat(terminal.retrieveScreen()).containsOnlyOnce("SSC1       General Insurance Customer Menu");
        
        // Do customer inquiry for customer number 1 
        terminal.positionCursorToFieldContaining("Cust Number").tab().type("0000000001")
                .positionCursorToFieldContaining("Select Option").tab().type("1").enter().waitForKeyboard();

        // Retrieve data of customer number 1 to variables 
        String firstName = new String(terminal.retrieveFieldTextAfterFieldWithString("Cust Name :First").trim());
        String lastName = new String(terminal.retrieveFieldTextAfterFieldWithString(":Last").trim());
        String dob = new String(terminal.retrieveFieldTextAfterFieldWithString("DOB").trim());
        String houseNo = new String(terminal.retrieveFieldTextAfterFieldWithString("House Number").trim());
        String postCode = new String(terminal.retrieveFieldTextAfterFieldWithString("Postcode").trim());
        String phoneHome = new String(terminal.retrieveFieldTextAfterFieldWithString("Phone: Home").trim());
        String phoneMob = new String(terminal.retrieveFieldTextAfterFieldWithString("Phone: Mob").trim());
        String emailAddr = new String(terminal.retrieveFieldTextAfterFieldWithString("Email  Addr").trim());

        // Assert the data of customer number 1  
        assertThat(firstName).isEqualTo("Andrew");
        assertThat(lastName).isEqualTo("Pandy");
        assertThat(dob).isEqualTo("1950-07-11");
        assertThat(houseNo).isEqualTo("34");
        assertThat(postCode).isEqualTo("PI101OO");
        assertThat(phoneHome).isEqualTo("01962 811234");
        assertThat(phoneMob).isEqualTo("07799 123456");
        assertThat(emailAddr).isEqualTo("A.Pandy@beebhouse.com");
        
        // Exit SSC1 transaction 
        terminal.pf3();
        TimeUnit.SECONDS.sleep(2);        
        assertThat(terminal.retrieveScreen()).containsOnlyOnce("Transaction ended");
        
    }
    
    @Test
    public void testCustInquiry02() throws TimeoutException, KeyboardLockedException, TerminalInterruptedException, NetworkException, FieldNotFoundException, TextNotFoundException, InterruptedException {
        // Start the transaction SSC1
        terminal.clear().waitForKeyboard().type("SSC1").enter().waitForKeyboard().tab();
        
        // Assert that the screen is for General Insurance Customer
        assertThat(terminal.retrieveScreen()).containsOnlyOnce("SSC1       General Insurance Customer Menu");

        // Do customer inquiry for customer number 2
        terminal.positionCursorToFieldContaining("Cust Number").tab().type("0000000002")
                .positionCursorToFieldContaining("Select Option").tab().type("1").enter().waitForKeyboard();
        
        // Retrieve data of customer number 2 to variables 
        String firstName = new String(terminal.retrieveFieldTextAfterFieldWithString("Cust Name :First").trim());
        String lastName = new String(terminal.retrieveFieldTextAfterFieldWithString(":Last").trim());
        String dob = new String(terminal.retrieveFieldTextAfterFieldWithString("DOB").trim());
        String houseName = new String(terminal.retrieveFieldTextAfterFieldWithString("House Name").trim());
        String houseNo = new String(terminal.retrieveFieldTextAfterFieldWithString("House Number").trim());
        String postCode = new String(terminal.retrieveFieldTextAfterFieldWithString("Postcode").trim());
        String phoneHome = new String(terminal.retrieveFieldTextAfterFieldWithString("Phone: Home").trim());
        String emailAddr = new String(terminal.retrieveFieldTextAfterFieldWithString("Email  Addr").trim());
        
        // Assert the data of customer number 2  
        assertThat(firstName).isEqualTo("Scott");
        assertThat(lastName).isEqualTo("Tracey");
        assertThat(dob).isEqualTo("1965-09-30");
        assertThat(houseName).isEqualTo("Tracey Island");
        assertThat(houseNo).isEqualTo("1");
        assertThat(postCode).isEqualTo("TB14TV");
        assertThat(phoneHome).isEqualTo("001 911911");
        assertThat(emailAddr).isEqualTo("REFROOM@TBHOLDINGS.COM");
        
        // Exit SSC1 transaction 
        terminal.pf3();
        TimeUnit.SECONDS.sleep(2);
        assertThat(terminal.retrieveScreen()).containsOnlyOnce("Transaction ended");        

    }

    @Test
    public void testCustInquiry03() throws TimeoutException, KeyboardLockedException, TerminalInterruptedException, NetworkException, FieldNotFoundException, TextNotFoundException, InterruptedException {
        // Start the transaction SSC1
    	terminal.clear().waitForKeyboard().type("SSC1").enter().waitForKeyboard().tab();
        
        // Assert that the screen is for General Insurance Customer
        assertThat(terminal.retrieveScreen()).containsOnlyOnce("SSC1       General Insurance Customer Menu");
        
        // Do customer inquiry for customer number 100
        terminal.positionCursorToFieldContaining("Cust Number").tab().type("0000000100")
                .positionCursorToFieldContaining("Select Option").tab().type("1").enter().waitForKeyboard();
        
        // Assert that no data returned for custoemr number 100 
        assertThat(terminal.retrieveScreen()).containsOnlyOnce("No data was returned.");        
        
        // Exit SSC1 transaction 
        terminal.pf3();
        TimeUnit.SECONDS.sleep(2);        
        assertThat(terminal.retrieveScreen()).containsOnlyOnce("Transaction ended");
    }
    
}
