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
import com.uuhnaut69.ledger_domain.account.Account;
import com.uuhnaut69.ledger_domain.account.AccountCommandWrapper;
import com.uuhnaut69.ledger_domain.account.AccountRepository;
import com.uuhnaut69.ledger_domain.account.AccountUseCase;
import com.uuhnaut69.ledger_domain.account.AccountUseCaseImpl;
import com.uuhnaut69.ledger_intrastructure.disruptor.CommandDispatcher;
import com.uuhnaut69.ledger_intrastructure.disruptor.DisruptorExceptionHandler;
import com.uuhnaut69.ledger_intrastructure.disruptor.account.AccountBusinessHandler;
import com.uuhnaut69.ledger_intrastructure.disruptor.account.AccountCommandDispatcher;
import com.uuhnaut69.ledger_intrastructure.disruptor.account.AccountCommandJournalHandler;
import com.uuhnaut69.ledger_intrastructure.disruptor.account.AccountCommandReplicateHandler;
import com.uuhnaut69.ledger_intrastructure.disruptor.account.AccountDisruptor;
import com.uuhnaut69.ledger_intrastructure.in_memory.AccountInMemoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@Dependent
public class ApplicationBoostrap {

	// Persistence layer
	@Produces
	@ApplicationScoped
	public AccountRepository accountRepository() {
		return new AccountInMemoryRepository();
	}

	// Domain layer
	@Produces
	@ApplicationScoped
	public AccountUseCase accountUseCase(AccountRepository accountRepository) {
		return new AccountUseCaseImpl(accountRepository);
	}

	// Disruptor Queue
	@Produces
	@ApplicationScoped
	public AccountCommandJournalHandler accountCommandJournalHandler() {
		return new AccountCommandJournalHandler();
	}

	@Produces
	@ApplicationScoped
	public AccountCommandReplicateHandler accountCommandReplicateHandler() {
		return new AccountCommandReplicateHandler();
	}

	@Produces
	@ApplicationScoped
	public AccountBusinessHandler accountBusinessHandler(AccountUseCase accountUseCase) {
		return new AccountBusinessHandler(accountUseCase);
	}

	@Produces
	@ApplicationScoped
	public DisruptorExceptionHandler disruptorExceptionHandler() {
		return new DisruptorExceptionHandler();
	}

	@Produces
	@Singleton
	public Disruptor<AccountCommandWrapper> accountCommandWrapperDisruptor(
			AccountCommandJournalHandler accountCommandJournalHandler,
			AccountCommandReplicateHandler accountCommandReplicateHandler,
			AccountBusinessHandler accountBusinessHandler,
			DisruptorExceptionHandler disruptorExceptionHandler
	) {
		return AccountDisruptor.setup(
				accountCommandJournalHandler,
				accountCommandReplicateHandler,
				accountBusinessHandler,
				disruptorExceptionHandler
		);
	}

	@Produces
	@ApplicationScoped
	public CommandDispatcher<Account> accountCommandDispatcher(
			Disruptor<AccountCommandWrapper> disruptor
	) {
		return new AccountCommandDispatcher(disruptor);
	}
}
