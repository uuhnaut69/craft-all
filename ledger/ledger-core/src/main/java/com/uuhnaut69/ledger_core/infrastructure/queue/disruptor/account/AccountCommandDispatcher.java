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

package com.uuhnaut69.ledger_core.infrastructure.queue.disruptor.account;

import com.lmax.disruptor.dsl.Disruptor;
import com.uuhnaut69.ledger_core.domain.account.Account;
import com.uuhnaut69.ledger_core.domain.account.AccountCommandWrapper;
import com.uuhnaut69.ledger_core.infrastructure.queue.disruptor.CommandDispatcher;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountCommandDispatcher implements CommandDispatcher<Account> {

	private final Disruptor<AccountCommandWrapper> accountDisruptor;

	@Override
	public CompletableFuture<Account> dispatch(Object command) {
		var asyncResponse = new CompletableFuture<Account>();
		asyncResponse.completeOnTimeout(null, 10, TimeUnit.SECONDS);
		this.accountDisruptor.publishEvent((event, _) -> {
			event.setCommand(command);
			event.setResponse(asyncResponse);
		});
		return asyncResponse;
	}
}
