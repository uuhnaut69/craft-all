/*
 * MIT License
 *
 * Copyright (c) 2024 Tuan Nguyen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.uuhnaut69.ledger_intrastructure.disruptor.account;

import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.uuhnaut69.ledger_domain.account.AccountCommandWrapper;
import com.uuhnaut69.ledger_intrastructure.disruptor.DisruptorExceptionHandler;

public final class AccountDisruptor {

	private AccountDisruptor() {
	}

	public static Disruptor<AccountCommandWrapper> setup(
			AccountCommandJournalHandler journalHandler,
			AccountCommandReplicateHandler replicateHandler,
			AccountBusinessHandler businessHandler,
			DisruptorExceptionHandler exceptionHandler
	) {
		int bufferSize = 1024;
		var disruptor = new Disruptor<>(
				AccountCommandWrapper::new,
				bufferSize,
				DaemonThreadFactory.INSTANCE
		);

		disruptor
				.handleEventsWith(journalHandler, replicateHandler)
				.then(businessHandler);

		disruptor.setDefaultExceptionHandler(exceptionHandler);

		disruptor.start();

		return disruptor;
	}

}
