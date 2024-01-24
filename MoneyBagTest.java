package testingCourse.lab2_currencyEditor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoneyBagTest {
	private MoneyBag moneyBagWithSameCurrency, moneyBagWithDifferentCurrencies;
	private Money m5USD, m10NIS, m15USD, m20EUR, mZero;

	@BeforeEach
	void setUp() throws Exception {
        m5USD = new Money(5, "USD");
        m10NIS = new Money(10, "NIS");
        m15USD = new Money(15, "USD");
        m20EUR = new Money(20, "EUR");
        mZero = new Money(0, "USD");
        moneyBagWithSameCurrency = new MoneyBag(m5USD, m10NIS);
		moneyBagWithDifferentCurrencies = new MoneyBag(m5USD, m20EUR);
	}

	@Test
	void testAddMoneyWithWithSameCurrency() {
		IMoney expected = new MoneyBag(new Money(20, "USD"), m10NIS);
		MoneyBag result = (MoneyBag) moneyBagWithSameCurrency.addMoney(m15USD);
		assertTrue(expected.equals(result));
	}
	
	@Test
    void testAddMoneyWithDifferentCurrencies() {
		IMoney expected = new MoneyBag(m10NIS, moneyBagWithDifferentCurrencies);
		MoneyBag result = (MoneyBag) moneyBagWithDifferentCurrencies.addMoney(m10NIS);
		assertTrue(expected.equals(result));
	}
	
	@Test
	void testAddMoneyWithZeroAmount() {
		IMoney expected = moneyBagWithSameCurrency;
		MoneyBag result = (MoneyBag) moneyBagWithSameCurrency.addMoney(mZero);
		assertTrue(expected.equals(result));		
	}
	
	@Test
    void testAddNullMoney() {
	 assertThrows(NullPointerException.class, 
			 () -> {
				 moneyBagWithSameCurrency.addMoney(null);
			 });
	}

	@Test
	void testContainsWithExistingMoney() {
		assertTrue(moneyBagWithSameCurrency.contains(m5USD));
	}

	@Test
    void testContainsWithNonExistingMoney() {
        assertFalse(moneyBagWithSameCurrency.contains(new Money(10, "USD")));
    }
	
	@Test
    void testContainsWithDifferentCurrency() {
        assertFalse(moneyBagWithSameCurrency.contains(m20EUR));
    }
	
	@Test
    void testContainsWithZeroAmountMoney() {
        assertFalse(moneyBagWithSameCurrency.contains(mZero));
    }
}/*package testingCourse.lab2_currencyEditor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoneyBagTest {
	
	private Money m5USD,m10USD,m5NIS,m10NIS,negm5NIS,negm10USD;
	private MoneyBag mb1;

	@BeforeEach
	void setUp() throws Exception {
		Money m5USD= new Money(5,"USD"); 
		Money m10USD= new Money(10,"USD"); 
		Money m5NIS= new Money(5,"NIS"); 
		Money m10NIS= new Money(10,"NIS");
		Money negm5NIS= new Money(-5,"NIS");
		Money negm10USD= new Money(-10,"USD");
		MoneyBag mb1= new MoneyBag(m5USD, m10NIS);
	}
	// checking functionality: adding one Positive Currency and zero to the other currency
	// input data: 5USD+5USD,10NIS
	// expected result: 10USD,10NIS
	@Test
	void testAddMoney_OnePositiveCurrency() {
		
		MoneyBag expected = new MoneyBag(m10USD,m10NIS);
		MoneyBag result = (MoneyBag) mb1.addMoney(m5USD);
		assertTrue(expected.equals(result));

	}
	// checking functionality: adding two Positive Currencies
	// input data: 5USD+5USD,10NIS+5NIS
	// expected result: 10USD,15NIS
	@Test
	void testAddMoney_TwoPositiveCurrencies() {
			
		MoneyBag expected = new MoneyBag(m10USD,new Money(15,"NIS"));
		MoneyBag result = (MoneyBag) mb1.addMoney(m5USD);
		result = (MoneyBag) mb1.addMoney(m5NIS);
		assertTrue(expected.equals(result));

	}
	// checking functionality: adding one negative currency and zero to the other currency
	// input data: 5USD,10NIS-5NIS
	// expected result: 5USD,5NIS
	@Test
	void testAddMoney_OneNegativeCurrency() {
				
		MoneyBag expected = new MoneyBag(m5USD,m5NIS);
		MoneyBag result = (MoneyBag) mb1.addMoney(negm5NIS);
		assertTrue(expected.equals(result));

	}
	// checking functionality: adding two negative currencies
	// input data: 5USD-10USD,10NIS-5NIS
	// expected result: -5USD,5NIS
	@Test
	void testAddMoney_TwoNegativeCurrencies() {
					
		MoneyBag expected = new MoneyBag(new Money(-5,"USD"),m5NIS);
		MoneyBag result = (MoneyBag) mb1.addMoney(negm5NIS);
		result = (MoneyBag) mb1.addMoney(negm10USD);
		assertTrue(expected.equals(result));

	}
	// checking functionality: contains currency
	@Test
	void testContains_OneCurrency() {
		assertTrue(mb1.contains(m5USD)); 
	}
	// checking functionality: is not null
	@Test
	void testContains_NotNull() {
		assertTrue(!mb1.contains(null)); 
	}
	// checking functionality: does not contain currency with different amount
	@Test
	void testContains_DifferentAmount() {
		assertTrue(!mb1.contains(m10USD)); 
	}
	// checking functionality: does not contain new currency with same amount
	@Test
	void testContains_DifferentCurrency() {
		assertTrue(!mb1.contains(new Money(5, "CHF"))); 
	}

}*/
