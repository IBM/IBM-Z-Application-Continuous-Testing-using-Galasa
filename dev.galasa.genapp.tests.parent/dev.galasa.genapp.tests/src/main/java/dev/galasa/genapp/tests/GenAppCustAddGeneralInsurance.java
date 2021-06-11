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
public class GenAppCustAddGeneralInsurance {
	@ZosImage(imageTag="ZDNT")
	public IZosImage        image;

    @Zos3270Terminal(imageTag = "ZDNT")
    public ITerminal        terminal;
    
    @Test
    public void testNotNull() {
        //Check terminal data is loaded
        assertThat(terminal).isNotNull();
    }

    @Test
    public void testCustAdd() throws TimeoutException, KeyboardLockedException, TerminalInterruptedException, NetworkException, FieldNotFoundException, TextNotFoundException, InterruptedException {
        // Login to CICS Terminal in zD&T
        terminal.waitForKeyboard().positionCursorToFieldContaining("L CICSTS55").tab().type("L CICSTS55").enter().waitForKeyboard().tab()
                .clear().waitForKeyboard();        
        TimeUnit.SECONDS.sleep(2);
        
        // Start the transaction SSC1
        terminal.tab().type("SSC1").enter().waitForKeyboard().tab();
        
        // Assert that the screen is for General Insurance Customer
        assertThat(terminal.retrieveScreen()).containsOnlyOnce("SSC1       General Insurance Customer Menu");
        
        // Add new customer using option 2 
        terminal.positionCursorToFieldContaining("Cust Name :First").tab().type("ANUPRAKASH")
                .positionCursorToFieldContaining(":Last").tab().type("MOOTHEDATH")
                .positionCursorToFieldContaining("DOB").tab().type("1983-04-13")
                .positionCursorToFieldContaining("House Name").tab().type("METROPOLIS")
                .positionCursorToFieldContaining("House Number").tab().type("217")
                .positionCursorToFieldContaining("Postcode").tab().type("560100")
                .positionCursorToFieldContaining("Phone: Home").tab().type("1234567890")
                .positionCursorToFieldContaining("Phone: Mob").tab().type("0987654321")
                .positionCursorToFieldContaining("Email  Addr").tab().type("anuprakash@gmail.com")
                .positionCursorToFieldContaining("Select Option").tab().type("2").enter().waitForKeyboard();
        
        // Get the customer number for added customer 
        String custNumber = new String(terminal.retrieveFieldTextAfterFieldWithString("Cust Number").trim());        
        
        // Exit SSC1 transaction 
        terminal.pf3();
        TimeUnit.SECONDS.sleep(2);        
        assertThat(terminal.retrieveScreen()).containsOnlyOnce("Transaction ended");
        
        // Start the transaction SSC1
        terminal.clear().waitForKeyboard().type("SSC1").enter().waitForKeyboard().tab();
        // Assert that the screen is for General Insurance Customer
        assertThat(terminal.retrieveScreen()).containsOnlyOnce("SSC1       General Insurance Customer Menu");
        
        // Do customer inquiry for newly added customer
        terminal.positionCursorToFieldContaining("Cust Number").tab().type(custNumber)
                .positionCursorToFieldContaining("Select Option").tab().type("1").enter().waitForKeyboard();
        
        // Retrieve data of customer number 1 to variables
        String firstName = new String(terminal.retrieveFieldTextAfterFieldWithString("Cust Name :First").trim());
        String lastName = new String(terminal.retrieveFieldTextAfterFieldWithString(":Last").trim());
        String dob = new String(terminal.retrieveFieldTextAfterFieldWithString("DOB").trim());
        String houseName = new String(terminal.retrieveFieldTextAfterFieldWithString("House Name").trim());
        String houseNo = new String(terminal.retrieveFieldTextAfterFieldWithString("House Number").trim());
        String postCode = new String(terminal.retrieveFieldTextAfterFieldWithString("Postcode").trim());
        String phoneHome = new String(terminal.retrieveFieldTextAfterFieldWithString("Phone: Home").trim());
        String phoneMob = new String(terminal.retrieveFieldTextAfterFieldWithString("Phone: Mob").trim());
        String emailAddr = new String(terminal.retrieveFieldTextAfterFieldWithString("Email  Addr").trim());
        
        // Assert the data of newly added customer
        assertThat(firstName).isEqualTo("ANUPRAKASH");
        assertThat(lastName).isEqualTo("MOOTHEDATH");
        assertThat(dob).isEqualTo("1983-04-13");
        assertThat(houseName).isEqualTo("METROPOLIS");
        assertThat(houseNo).isEqualTo("217");
        assertThat(postCode).isEqualTo("560100");
        assertThat(phoneHome).isEqualTo("1234567890");
        assertThat(phoneMob).isEqualTo("0987654321");
        assertThat(emailAddr).isEqualTo("anuprakash@gmail.com");
        
        // Exit SSC1 transaction
        terminal.pf3();
        TimeUnit.SECONDS.sleep(2);        
        assertThat(terminal.retrieveScreen()).containsOnlyOnce("Transaction ended");
        
    }
    

}
