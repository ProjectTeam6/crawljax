/**
 * 
 */
package com.crawljax.condition.invariant;

import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.Logger;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.condition.Condition;

/**
 * @author danny
 * @version $Id: InvariantChecker.java 6234 2009-12-18 13:46:37Z mesbah $
 *          Controller class for the invariants
 */
public class InvariantChecker {

	private static final Logger LOGGER = Logger.getLogger(InvariantChecker.class.getName());

	private List<Invariant> invariants;

	/**
	 * Default constructor.
	 */
	public InvariantChecker() {

	}

	/**
	 * Constructor with invariant list.
	 * @param invariants The invariant list.
	 */
	public InvariantChecker(List<Invariant> invariants) {
		this.invariants = invariants;
	}

	private List<Invariant> failedInvariants = new ArrayList<Invariant>();

	/**
	 * @param browser The browser.
	 * @return true iff browser satisfies ALL the invariants
	 */
	public boolean check(EmbeddedBrowser browser) {
		failedInvariants.clear();
		if (invariants != null) {
			LOGGER.info("Checking " + invariants.size() + " invariants");
			for (Invariant invariant : invariants) {
				boolean conditionsSucceed = true;
				for (Condition condition : invariant.getPreConditions()) {
					boolean check;
					check = condition.check(browser);
					LOGGER.debug("Checking Invariant: " + invariant.getDescription()
					                + " - PreCondition: " + condition.toString() + ": " + check);
					if (!check) {
						conditionsSucceed = false;
						break;
					}
				}
				if (conditionsSucceed) {
					Condition invariantCondition = invariant.getCondition();
					LOGGER.debug("Checking Invariant: " + invariant.getDescription());
					if (!invariantCondition.check(browser)) {
						LOGGER.debug("Invariant '" + invariant.getDescription() + "' failed: "
						                + invariant.getDescription());
						failedInvariants.add(invariant);
					}
				}
			}
		}
		if (failedInvariants.size() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * @return The failedInvariants.
	 */
	public List<Invariant> getFailedInvariants() {
		return failedInvariants;
	}

	/**
	 * @return the invariants
	 */
	public List<Invariant> getInvariants() {
		return invariants;
	}

	/**
	 * @param invariants
	 *            the invariants to set
	 */
	public void setInvariants(List<Invariant> invariants) {
		this.invariants = invariants;
	}

}