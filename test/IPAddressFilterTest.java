package test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import finished.IPAddressFilter;


public class IPAddressFilterTest {

    public static void main(String[] args) {
        JUnitCore.main(IPAddressFilterTest.class.getName());
    }

	@Test
	public void passesWithLiteralFilter() {
		IPAddressFilter ipFilter = new IPAddressFilter("192.168.13.37");
		assertTrue(ipFilter.passes("192.168.13.37"));
	}

	@Test
	public void notPassesWithLiteralFilter() {
		IPAddressFilter ipFilter = new IPAddressFilter("192.168.13.37");
		assertFalse(ipFilter.passes("192.168.13.36"));
		assertFalse(ipFilter.passes("192.168.13.38"));
		assertFalse(ipFilter.passes("191.168.13.37"));
		assertFalse(ipFilter.passes("192.169.13.37"));
		assertFalse(ipFilter.passes("192.168.12.37"));
	}

	@Test
	public void passesWithWildCardFilter() {
		IPAddressFilter ipFilter = new IPAddressFilter("192.168.13.*");
		assertTrue(ipFilter.passes("192.168.13.0"));
		assertTrue(ipFilter.passes("192.168.13.255"));
	}

	@Test
	public void notPassesWithWildCardFilter() {
		IPAddressFilter ipFilter = new IPAddressFilter("192.168.13.*");
		assertFalse(ipFilter.passes("192.168.14.0"));
	}

	@Test
	public void passesWithRangeFilter() {
		IPAddressFilter ipFilter = new IPAddressFilter("192.168.13.[3-121]");
		assertTrue(ipFilter.passes("192.168.13.3"));
		assertTrue(ipFilter.passes("192.168.13.50"));
		assertTrue(ipFilter.passes("192.168.13.121"));
	}

	@Test
	public void notPassesWithRangeFilter() {
		IPAddressFilter ipFilter = new IPAddressFilter("192.168.13.[3-121]");
		assertFalse(ipFilter.passes("192.168.13.2"));
		assertFalse(ipFilter.passes("192.168.13.122"));
	}

	@Test
	public void passesWithWildCardAndRangeFilter() {
		IPAddressFilter ipFilter = new IPAddressFilter("192.168.*.[12-100]");
		assertTrue(ipFilter.passes("192.168.1.12"));
		assertTrue(ipFilter.passes("192.168.1.50"));
		assertTrue(ipFilter.passes("192.168.1.100"));
		assertTrue(ipFilter.passes("192.168.255.12"));
		assertTrue(ipFilter.passes("192.168.255.50"));
		assertTrue(ipFilter.passes("192.168.255.100"));
	}

	@Test
	public void notPassesWithWildCardAndRangeFilter() {
		IPAddressFilter ipFilter = new IPAddressFilter("192.168.*.[12-100]");
		assertFalse(ipFilter.passes("192.168.1.11"));
		assertFalse(ipFilter.passes("192.168.1.101"));
		assertFalse(ipFilter.passes("192.168.255.11"));
		assertFalse(ipFilter.passes("192.168.255.101"));
	}

	@Test
	public void passesWithRangeAndWildCardFilter() {
		IPAddressFilter ipFilter = new IPAddressFilter("192.168.[1-99].*");
		assertTrue(ipFilter.passes("192.168.1.1"));
		assertTrue(ipFilter.passes("192.168.1.255"));
		assertTrue(ipFilter.passes("192.168.99.1"));
		assertTrue(ipFilter.passes("192.168.99.255"));
	}

	@Test
	public void notPassesWithRangeAndWildCardFilter() {
		IPAddressFilter ipFilter = new IPAddressFilter("192.168.[1-99].*");
		assertFalse(ipFilter.passes("192.168.0.1"));
		assertFalse(ipFilter.passes("192.168.0.255"));
		assertFalse(ipFilter.passes("192.168.100.1"));
		assertFalse(ipFilter.passes("192.168.100.255"));
	}
}
