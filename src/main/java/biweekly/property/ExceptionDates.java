package biweekly.property;

import biweekly.ICalVersion;
import biweekly.Warning;
import biweekly.component.ICalComponent;
import biweekly.util.ICalDate;

import java.util.Date;
import java.util.List;

/*
 Copyright (c) 2013-2016, Michael Angstadt
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met: 

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. 
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution. 

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * <p>
 * Defines a list of exceptions to the dates specified in the
 * {@link RecurrenceRule} property.
 * </p>
 * <p>
 * <b>Code sample:</b>
 * 
 * <pre class="brush:java">
 * VEvent event = new VEvent();
 * 
 * //dates with time components
 * ExceptionDates exdate = new ExceptionDates();
 * Date datetime = ...
 * exdate.addValue(new ICalDate(datetime, true));
 * event.addExceptionDates(exdate);
 * 
 * //dates without time components
 * exdate = new ExceptionDates();
 * Date date = ...
 * exdate.addValue(new ICalDate(date, false));
 * event.addExceptionDates(exdate);
 * </pre>
 * 
 * </p>
 * @author Michael Angstadt
 * @see <a href="http://tools.ietf.org/html/rfc5545#page-118">RFC 5545
 * p.118-20</a>
 * @see <a href="http://tools.ietf.org/html/rfc2445#page-112">RFC 2445
 * p.112-4</a>
 * @see <a href="http://www.imc.org/pdi/vcal-10.doc">vCal 1.0 p.31</a>
 */
public class ExceptionDates extends ListProperty<ICalDate> {
	public ExceptionDates() {
		//empty
		setUid(Uid.random().getValue());
	}

	public String getUid() {
		String uid = getParameter("Uid");
		if (uid == null) {
			setUid(Uid.random().getValue());
			uid = getParameter("Uid");
		}
		return uid;
	}

	public void setUid(String uid) {
		setParameter("Uid", uid);
	}

	/**
	 * Copy constructor.
	 * @param original the property to make a copy of
	 */
	public ExceptionDates(ExceptionDates original) {
		super(original);
		getValues().clear();
		setUid(Uid.random().getValue());
		for (ICalDate date : original.getValues()) {
			addValue(new ICalDate(date));
		}
	}

	/**
	 * Adds a value to this property.
	 * @param value the value to add
	 */
	public void addValue(Date value) {
		addValue(new ICalDate(value));
	}

	@Override
	protected void validate(List<ICalComponent> components, ICalVersion version, List<Warning> warnings) {
		super.validate(components, version, warnings);

		List<ICalDate> dates = getValues();
		if (dates.isEmpty()) {
			return;
		}

		//can't mix date and date-time values
		boolean hasTime = dates.get(0).hasTime();
		for (ICalDate date : dates.subList(1, dates.size())) {
			if (date.hasTime() != hasTime) {
				warnings.add(Warning.validate(50));
				break;
			}
		}
	}

	@Override
	public ExceptionDates copy() {
		return new ExceptionDates(this);
	}
}
