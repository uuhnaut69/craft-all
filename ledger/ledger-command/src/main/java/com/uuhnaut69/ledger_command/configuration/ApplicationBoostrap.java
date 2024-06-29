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

package com.uuhnaut69.ledger_command.configuration;

import com.lmax.disruptor.dsl.Disruptor;
import com.uuhnaut69.ledger_core.domain.account.Account;
import com.uuhnaut69.ledger_core.domain.account.AccountCommandWrapper;
import com.uuhnaut69.ledger_core.domain.account.AccountRepository;
import com.uuhnaut69.ledger_core.domain.account.AccountUseCase;
import com.uuhnaut69.ledger_core.domain.account.AccountUseCaseImpl;
import com.uuhnaut69.ledger_core.infrastructure.persistent.in_memory.AccountInMemoryRepository;
import com.uuhnaut69.ledger_core.infrastructure.queue.disruptor.CommandDispatcher;
import com.uuhnaut69.ledger_core.infrastructure.queue.disruptor.account.AccountBusinessHandler;
import com.uuhnaut69.ledger_core.infrastructure.queue.disruptor.account.AccountCommandDispatcher;
import com.uuhnaut69.ledger_core.infrastructure.queue.disruptor.account.AccountCommandJournalHandler;
import com.uuhnaut69.ledger_core.infrastructure.queue.disruptor.account.AccountCommandReplicateHandler;
import com.uuhnaut69.ledger_core.infrastructure.queue.disruptor.account.AccountDisruptor;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;

@Dependent
public class ApplicationBoostrap {

	// Persistence layer
	@Produces
	@DefaultBean
	public AccountRepository accountRepository() {
		return new AccountInMemoryRepository();
	}

	// Domain layer
	@Produces
	@DefaultBean
	public AccountUseCase accountUseCase(AccountRepository accountRepository) {
		return new AccountUseCaseImpl(accountRepository);
	}

	// Disruptor Queue
	@Produces
	public AccountCommandJournalHandler accountCommandJournalHandler() {
		return new AccountCommandJournalHandler();
	}

	@Produces
	public AccountCommandReplicateHandler accountCommandReplicateHandler() {
		return new AccountCommandReplicateHandler();
	}

	@Produces
	public AccountBusinessHandler accountBusinessHandler(AccountUseCase accountUseCase) {
		return new AccountBusinessHandler(accountUseCase);
	}

	@Produces
	public Disruptor<AccountCommandWrapper> accountCommandWrapperDisruptor(
			AccountCommandJournalHandler accountCommandJournalHandler,
			AccountCommandReplicateHandler accountCommandReplicateHandler,
			AccountBusinessHandler accountBusinessHandler
	) {
		return AccountDisruptor.setup(
				accountCommandJournalHandler,
				accountCommandReplicateHandler,
				accountBusinessHandler
		);
	}

	@Produces
	public CommandDispatcher<Account> accountCommandDispatcher(
			Disruptor<AccountCommandWrapper> disruptor
	) {
		return new AccountCommandDispatcher(disruptor);
	}
}
